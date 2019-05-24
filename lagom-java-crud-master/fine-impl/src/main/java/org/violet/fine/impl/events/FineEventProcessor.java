package org.violet.fine.impl.events;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.violet.fine.impl.events.FineEvent.*;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class FineEventProcessor extends ReadSideProcessor<FineEvent>{
    private static final Logger LOGGER = LoggerFactory.getLogger(FineEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeFines;
    private PreparedStatement deleteFines;

    /**
     *
     * @param session
     * @param readSide
     */
    @Inject
    public FineEventProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    /**
     *
     * @return
     */
    @Override
    public PSequence<AggregateEventTag<FineEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(FineEventTag.INSTANCE);
    }

    /**
     *
     * @return
     */
    @Override
    public ReadSideProcessor.ReadSideHandler<FineEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<FineEvent>builder("fines_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteFine()
                        .thenCombine(prepareDeleteFine(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(FineCreated.class, this::processPostAdded)
                .setEventHandler(FineUpdated.class, this::processPostUpdated)
                .setEventHandler(FineDeleted.class, this::processPostDeleted)
                .build();
    }

    /**
     *
     * @return
     */
    // Execute only once while application is start
    private CompletionStage<Done> createTable() {
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS fines ( " +
                        "id text,driver text,carNum text,illegalId text,docID text,fine text,forfeit text,time text,office text,PRIMARY KEY (id))"
        );
    }

    /*
     * START: Prepare statement for insert car values into Cars table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareWriteFine() {
        return session.prepare(
                "INSERT INTO fines (id, driver,carNum,illegalId,docID,fine,forfeit,time,office) VALUES (?,?,?,?,?,?,?,?,?)"
        ).thenApply(ps -> {
            setWriteFines(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param statement
     */
    private void setWriteFines(PreparedStatement statement) {
        this.writeFines = statement;
    }

    // Bind prepare statement while CarCreate event is executed

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostAdded(FineCreated event) {
        BoundStatement bindWriteFine = writeFines.bind();
        bindWriteFine.setString("id", event.getFine().getId());
        bindWriteFine.setString("driver", event.getFine().getDriver());
        bindWriteFine.setString("carNum", event.getFine().getCarNum());
        bindWriteFine.setString("illegalId", event.getFine().getIllegalId());
        bindWriteFine.setString("docID", event.getFine().getDocID());
        bindWriteFine.setString("fine", event.getFine().getFine());
        bindWriteFine.setString("forfeit", event.getFine().getForfeit());
        bindWriteFine.setString("time", event.getFine().getTime());
        bindWriteFine.setString("office", event.getFine().getOffice());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteFine));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for update the data in Cars table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostUpdated(FineUpdated event) {
        BoundStatement bindWriteFine = writeFines.bind();
        bindWriteFine.setString("id", event.getFine().getId());
        bindWriteFine.setString("driver", event.getFine().getDriver());
        bindWriteFine.setString("carNum", event.getFine().getCarNum());
        bindWriteFine.setString("illegalId", event.getFine().getIllegalId());
        bindWriteFine.setString("docID", event.getFine().getDocID());
        bindWriteFine.setString("fine", event.getFine().getFine());
        bindWriteFine.setString("forfeit", event.getFine().getForfeit());
        bindWriteFine.setString("time", event.getFine().getTime());
        bindWriteFine.setString("office", event.getFine().getOffice());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteFine));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for delete the the Car from table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareDeleteFine() {
        return session.prepare(
                "DELETE FROM fines WHERE id=?"
        ).thenApply(ps -> {
            setDeleteFines(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param deleteFines
     */
    private void setDeleteFines(PreparedStatement deleteFines) {
        this.deleteFines = deleteFines;
    }

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostDeleted(FineDeleted event) {
        BoundStatement bindWriteFine = deleteFines.bind();
        bindWriteFine.setString("id", event.getFine().getId());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteFine));
    }
    /* ******************* END ****************************/
}
