package com.knoldus;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.*;

public interface MovieService extends Service {
    /**
     * @param id
     * @return
     */
    ServiceCall<NotUsed, Optional<Movie>> movie(String id);


    ServiceCall<NotUsed, Optional<Movie>> movie2(String id,String name);

    /**
     * @return
     */
    //
    /**
     * 测试数据质量*********************************************************************************************************
     *
     *  ServiceCall<Movie, Done> newMovie();
     */

    ServiceCall<Movie, String> newMovie();

    /**
     * @param id
     * @return
     */
    ServiceCall<Movie, Done> updateMovie(String id);

    /**
     * @param id
     * @return
     */
    ServiceCall<NotUsed, Done> deleteMovie(String id);
    
    /**
     *
     * @return
     */
    ServiceCall<NotUsed, Done> health();

    /**
     * @return
     */
    @Override
    default Descriptor descriptor() {

        return named("movie").withCalls(
                restCall(GET, "/api/movie/:id", this::movie),
                restCall(GET, "/api/movie/:id/:name", this::movie2),
                restCall(GET, "/api/movie/health", this::health),
                restCall(POST, "/api/new-movie", this::newMovie),
                restCall(PUT, "/api/update-movie/:id", this::updateMovie),
                restCall(DELETE, "/api/delete-movie/:id", this::deleteMovie)
                //  restCall(GET, "/api/user/get-all-movie", this::getAllMovie)
        ).withAutoAcl(true);
    }
}
