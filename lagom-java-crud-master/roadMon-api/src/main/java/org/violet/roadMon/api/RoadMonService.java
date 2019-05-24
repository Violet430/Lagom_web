package org.violet.roadMon.api;


import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;
import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.*;

public interface RoadMonService extends Service {
    /**
     * @param vehicleNumber
     * @return
     */
    ServiceCall<NotUsed, Optional<RoadMon>> getRoadMon(String vehicleNumber);
    /**
     * @return
     */
    ServiceCall<RoadMon, Done>addRoadMon();
    /**
     * @param vehicleNumber
     * @return
     */
    ServiceCall<RoadMon,Done>updateRoadMon(String vehicleNumber);
    /**
     * @param vehicleNumber
     * @return
     */
    ServiceCall<NotUsed,Done>deleteRoadMon(String vehicleNumber);
    //get  http://localhost:9000/api/roadMon/1234
    //add  http://localhost:9000/api/addRoadMon/
    //update http://localhost:9000/api/updateRoadMon/1234
    //delete http://localhost:9000/api/deleteRoadMon/1234
    @Override
    default Descriptor descriptor(){
        return named("roadMon").withCalls(
                restCall(GET, "/api/roadMon/:id", this::getRoadMon),
                restCall(POST, "/api/addRoadMon", this::addRoadMon),
                restCall(PUT, "/api/updateRoadMon/:id", this::updateRoadMon),
                restCall(DELETE, "/api/deleteRoadMon/:id", this::deleteRoadMon)
        ).withAutoAcl(true);
    }
}
