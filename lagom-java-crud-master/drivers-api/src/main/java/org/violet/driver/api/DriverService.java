package org.violet.driver.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.*;

public interface DriverService extends Service {
    /**
     * @param driverNum
     * @return
     */
    ServiceCall<NotUsed, Optional<Driver>> getDriver(String driverNum);
    /**
     * @return
     */
    ServiceCall<Driver, Done>addDriver();
    /**
     * @param driverNum
     * @return
     */
    ServiceCall<Driver,Done>updateDriver(String driverNum);
    /**
     * @param driverNum
     * @return
     */
    ServiceCall<NotUsed,Done>deleteDriver(String driverNum);


    //get  http://localhost:9000/api/driver/1234
    //add  http://localhost:9000/api/addDriver/
    //update http://localhost:9000/api/updateDriver/1234
    //delete http://localhost:9000/api/deleteDriver/1234
    @Override
    default Descriptor descriptor(){
        return named("driver").withCalls(
                restCall(GET, "/api/driver/:id", this::getDriver),
                restCall(POST, "/api/addDriver", this::addDriver),
                restCall(PUT, "/api/updateDriver/:id", this::updateDriver),
                restCall(DELETE, "/api/deleteDriver/:id", this::deleteDriver)
        ).withAutoAcl(true);
    }
}
