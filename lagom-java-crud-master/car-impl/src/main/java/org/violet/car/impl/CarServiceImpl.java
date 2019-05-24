package org.violet.car.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.violet.car.api.Car;
import org.violet.car.api.CarService;
import org.violet.car.impl.commands.CarCommand;
import org.violet.car.impl.events.CarEventProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CarServiceImpl implements CarService {




    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    /**
     * @param registry
     * @param readSide
     * @param session
     */
    @Inject
    public CarServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;

        persistentEntityRegistry.register(CarEntity.class);
        readSide.register(CarEventProcessor.class);
    }

    /**
     * @param vehicleNumber
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Optional<Car>> getCar(String vehicleNumber) {
        return request -> {
            CompletionStage<Optional<Car>> carFuture =
                    session.selectAll("SELECT * FROM cars WHERE vehicleNumber = ?", vehicleNumber)
                            .thenApply(rows ->
                                    rows.stream()
                                            .map(row -> Car.builder()
                                                    .vehicleNumber(row.getString("vehicleNumber"))
                                                    .carNum(row.getString("carNum"))
                                                    .plateType(row.getString("plateType"))
                                                    .carColor(row.getString("carColor"))
                                                    .frameNum(row.getString("frameNum"))
                                                    .owner(row.getString("owner"))
                                                    .tel(row.getString("tel"))
                                                    .build()
                                            )
                                            .findFirst()
                            );
            return carFuture;
        };
    }


    /**
     * @return
     */
    @Override
    public ServiceCall<Car, Done> addCar() {
        return car -> {
            PersistentEntityRef<CarCommand> ref = carEntityRef(car);
            return ref.ask(CarCommand.CreateCar.builder().car(car).build());
        };
    }

    /**
     * @param vehicleNumber
     * @return
     */
    @Override
    public ServiceCall<Car, Done> updateCar(String vehicleNumber) {
        return car -> {
            PersistentEntityRef<CarCommand> ref = carEntityRef(car);
            return ref.ask(CarCommand.UpdateCar.builder().car(car).build());
        };
    }
    /**
     * @param vehicleNumber
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Done> deleteCar(String vehicleNumber) {
        return request -> {
            Car car = Car.builder().vehicleNumber(vehicleNumber).build();
            PersistentEntityRef<CarCommand> ref = carEntityRef(car);
            return ref.ask(CarCommand.DeleteCar.builder().car(car).build());
        };
    }



    /**
     * @param car
     * @return
     */
    private PersistentEntityRef<CarCommand> carEntityRef(Car car) {
        return persistentEntityRegistry.refFor(CarEntity.class, car.getVehicleNumber());
    }
}
