package org.violet.fine.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.*;

public interface FineService extends Service {
    /**
     * @param id
     * @return
     */
    ServiceCall<NotUsed, Optional<Fine>> getFine(String id);
    /**
     * @return
     */
    ServiceCall<Fine, Done>addFine();
    /**
     * @param id
     * @return
     */
    ServiceCall<Fine,Done>updateFine(String id);
    /**
     * @param id
     * @return
     */
    ServiceCall<NotUsed,Done>deleteFine(String id);
    //get  http://localhost:9000/api/fine/1234
    //add  http://localhost:9000/api/addFine/
    //update http://localhost:9000/api/updateFine/1234
    //delete http://localhost:9000/api/deleteFine/1234
    @Override
    default Descriptor descriptor(){
        return named("fine").withCalls(
                restCall(GET, "/api/fine/:id", this::getFine),
                restCall(POST, "/api/addFine", this::addFine),
                restCall(PUT, "/api/updateFine/:id", this::updateFine),
                restCall(DELETE, "/api/deleteFine/:id", this::deleteFine)
        ).withAutoAcl(true);
    }
}
