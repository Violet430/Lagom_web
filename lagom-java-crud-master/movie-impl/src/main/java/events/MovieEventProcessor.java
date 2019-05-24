package events;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import events.MovieEvent.*;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;


public class MovieEventProcessor extends ReadSideProcessor<MovieEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeMovies;
    private PreparedStatement deleteMovies;

    /**
     *
     * @param session
     * @param readSide
     */
    @Inject
    public MovieEventProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    /**
     *
     * @return
     */
    @Override
    public PSequence<AggregateEventTag<MovieEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(MovieEventTag.INSTANCE);
    }

    /**
     *
     * @return
     */
    @Override
    public ReadSideHandler<MovieEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<MovieEvent>builder("Movies_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteMovie()
                        .thenCombine(prepareDeleteMovie(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(MovieCreated.class, this::processPostAdded)
                .setEventHandler(MovieUpdated.class, this::processPostUpdated)
                .setEventHandler(MovieDeleted.class, this::processPostDeleted)
                .build();
    }

    /**
     *
     * @return
     */
    // Execute only once while application is start
    private CompletionStage<Done> createTable() {
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS Movies ( " +
                        "id TEXT, name TEXT, genre TEXT, PRIMARY KEY(id,name))"
        );
    }

    /*
    * START: Prepare statement for insert Movie values into Movies table.
    * This is just creation of prepared statement, we will map this statement with our event
    */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareWriteMovie() {
        return session.prepare(
                "INSERT INTO Movies (id, name, genre) VALUES (?, ?, ?)"
        ).thenApply(ps -> {
            setWriteMovies(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param statement
     */
    private void setWriteMovies(PreparedStatement statement) {
        this.writeMovies = statement;
    }

    // Bind prepare statement while MovieCreate event is executed

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostAdded(MovieCreated event) {
        BoundStatement bindWriteMovie = writeMovies.bind();
        bindWriteMovie.setString("id", event.getMovie().getId());
        bindWriteMovie.setString("name", event.getMovie().getName());
        bindWriteMovie.setString("genre", event.getMovie().getGenre());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteMovie));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for update the data in Movies table.
    * This is just creation of prepared statement, we will map this statement with our event
    */

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostUpdated(MovieUpdated event) {
        BoundStatement bindWriteMovie = writeMovies.bind();
        bindWriteMovie.setString("id", event.getMovie().getId());
        bindWriteMovie.setString("name", event.getMovie().getName());
        bindWriteMovie.setString("genre", event.getMovie().getGenre());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteMovie));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for delete the the Movie from table.
    * This is just creation of prepared statement, we will map this statement with our event
    */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareDeleteMovie() {
        return session.prepare(
                "DELETE FROM Movies WHERE id=?"
        ).thenApply(ps -> {
            setDeleteMovies(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param deleteMovies
     */
    private void setDeleteMovies(PreparedStatement deleteMovies) {
        this.deleteMovies = deleteMovies;
    }

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostDeleted(MovieDeleted event) {
        BoundStatement bindWriteMovie = deleteMovies.bind();
        bindWriteMovie.setString("id", event.getMovie().getId());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteMovie));
    }
    /* ******************* END ****************************/


}
