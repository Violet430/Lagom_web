import akka.Done;
import akka.NotUsed;
import com.knoldus.Movie;
import com.knoldus.MovieService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import commands.MovieCommand;
import events.MovieEventProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MovieServiceImpl implements MovieService {
    
    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;
    
    /**
     * @param registry
     * @param readSide
     * @param session
     */
    @Inject
    public MovieServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;
        
        persistentEntityRegistry.register(MovieEntity.class);
        readSide.register(MovieEventProcessor.class);
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Optional<Movie>> movie2(String id,String name ){
        return request -> {
            CompletionStage<Optional<Movie>> movieFuture =
                    session.selectAll("SELECT * FROM movies WHERE id = ? and name=?", id,name)
                            .thenApply(rows ->
                                    rows.stream()
                                            .map(row -> Movie.builder().id(row.getString("id"))
                                                    .name(row.getString("name")).genre(row.getString("genre"))
                                                    .build()
                                            )
                                            .findFirst()
                            );
            return movieFuture;
        };
    }

    @Override
    public ServiceCall<NotUsed, Optional<Movie>> movie(String id){
        return request -> {
            CompletionStage<Optional<Movie>> movieFuture =
                    session.selectAll("SELECT * FROM movies WHERE id = ?", id)
                            .thenApply(rows ->
                                    rows.stream()
                                            .map(row -> Movie.builder().id(row.getString("id"))
                                                    .name(row.getString("name")).genre(row.getString("genre"))
                                                    .build()
                                            )
                                            .findFirst()
                            );
            return movieFuture;
        };
    }
    
    /**
     * @return
     */
    /**
     * 测试数据质量******************
     * @@Override
     *     public ServiceCall<Movie, Done> newMovie() {
     *         return movie -> {
     *             PersistentEntityRef<MovieCommand> ref = movieEntityRef(movie);
     *             return ref.ask(MovieCommand.CreateMovie.builder().movie(movie).build());
     *         };
     *     }
     */

    @Override
    public ServiceCall<Movie, String> newMovie() {
        return movie -> {
            PersistentEntityRef<MovieCommand> ref = movieEntityRef(movie);
            return ref.ask(MovieCommand.CreateMovie.builder().movie(movie).build());
        };
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<Movie, Done> updateMovie(String id) {
        return movie -> {
            PersistentEntityRef<MovieCommand> ref = movieEntityRef(movie);
            return ref.ask(MovieCommand.UpdateMovie.builder().movie(movie).build());
        };
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Done> deleteMovie(String id) {
        return request -> {
            Movie movie = Movie.builder().id(id).build();
            PersistentEntityRef<MovieCommand> ref = movieEntityRef(movie);
            return ref.ask(MovieCommand.DeleteMovie.builder().movie(movie).build());
        };
    }
    
    @Override
    public ServiceCall<NotUsed, Done> health() {
        return request -> CompletableFuture.completedFuture(Done.getInstance());
    }
    
    /**
     * @param movie
     * @return
     */
    private PersistentEntityRef<MovieCommand> movieEntityRef(Movie movie) {
        return persistentEntityRegistry.refFor(MovieEntity.class, movie.getId());
    }
    
}

