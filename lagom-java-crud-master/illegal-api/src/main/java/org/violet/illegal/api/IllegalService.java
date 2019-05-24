package org.violet.illegal.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;
import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.*;

public interface IllegalService extends Service {
    /**
     * @param id
     * @return
     */
    ServiceCall<NotUsed, Optional<Illegal>> getIllegal(String id);
    /**
     * @return
     */
    ServiceCall<Illegal, Done>addIllegal();
    /**
     * @param id
     * @return
     */
    ServiceCall<Illegal,Done>updateIllegal(String id);

    /**
     * @param id
     * @return
     */
    ServiceCall<NotUsed,Done>deleteIllegal(String id);

    //get  http://localhost:9000/api/illegal/1234
    //add  http://localhost:9000/api/addIllegal/
    //update http://localhost:9000/api/updateIllegal/1234
    //delete http://localhost:9000/api/deleteIllegal/1234
    @Override
    default Descriptor descriptor(){
        return named("illegal").withCalls(
                restCall(GET, "/api/illegal/:id", this::getIllegal),
                restCall(POST, "/api/addIllegal", this::addIllegal),
                restCall(PUT, "/api/updateIllegal/:id", this::updateIllegal),
                restCall(DELETE, "/api/deleteIllegal/:id", this::deleteIllegal)
        ).withAutoAcl(true);
    }



}
