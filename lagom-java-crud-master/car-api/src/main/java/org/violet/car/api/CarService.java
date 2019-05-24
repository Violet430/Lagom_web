package org.violet.car.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;


import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.*;

public interface CarService extends Service {
    /**
     * @param vehicleNumber
     * @return
     */
    ServiceCall<NotUsed, Optional<Car>>getCar(String vehicleNumber);
    /**
     * @return
     */
    ServiceCall<Car, Done>addCar();
    /**
     * @param vehicleNumber
     * @return
     */
    ServiceCall<Car,Done>updateCar(String vehicleNumber);
    /**
     * @param vehicleNumber
     * @return
     */
    ServiceCall<NotUsed,Done>deleteCar(String vehicleNumber);
    //get  http://localhost:9000/api/car/1234
    //add  http://localhost:9000/api/addCar/
    //update http://localhost:9000/api/updateCar/1234
    //delete http://localhost:9000/api/deleteCar/1234
    @Override
    default Descriptor descriptor(){
        return named("car").withCalls(
                restCall(GET, "/api/car/:id", this::getCar),
                restCall(POST, "/api/addCar", this::addCar),
                restCall(PUT, "/api/updateCar/:id", this::updateCar),
                restCall(DELETE, "/api/deleteCar/:id", this::deleteCar)
        ).withAutoAcl(true);
    }

}
