package org.violet.driver.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.violet.driver.api.Driver;
import org.violet.driver.api.DriverService;
import org.violet.driver.impl.commands.DriverCommand;
import org.violet.driver.impl.events.DriverEventProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class DriverServiceImpl implements DriverService {
    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    /**
     * @param registry
     * @param readSide
     * @param session
     */
    @Inject
    public DriverServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;

        persistentEntityRegistry.register(DriverEntity.class);
        readSide.register(DriverEventProcessor.class);
    }
    /**
     * @param driverNum
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Optional<Driver>> getDriver(String driverNum) {
        return request -> {
            CompletionStage<Optional<Driver>> driverFuture =
                    session.selectAll("SELECT * FROM drivers WHERE driverNum = ?", driverNum)
                            .thenApply(rows ->
                                    rows.stream()
                                            .map(row -> Driver.builder()
                                                    .driverNum(row.getString("driverNum"))
                                                    .name(row.getString("name"))
                                                    .id(row.getString("id"))
                                                    .licenseId(row.getString("licenseId"))
                                                    .sex(row.getString("sex"))
                                                    .carType(row.getString("carType"))
                                                    .office(row.getString("office"))
                                                    .time(row.getString("time"))
                                                    .build()
                                            )
                                            .findFirst()
                            );
            return driverFuture;
        };
    }

    /**
     * @return
     */
    @Override
    public ServiceCall<Driver, Done> addDriver() {
        return driver -> {
            PersistentEntityRef<DriverCommand> ref = driverEntityRef(driver);
            return ref.ask(DriverCommand.CreateDriver.builder().driver(driver).build());
        };
    }



    /**
     * @param driverNum
     * @return
     */
    @Override
    public ServiceCall<Driver, Done> updateDriver(String driverNum) {
        return driver -> {
            PersistentEntityRef<DriverCommand> ref = driverEntityRef(driver);
            return ref.ask(DriverCommand.UpdateDriver.builder().driver(driver).build());
        };
    }
    /**
     * @param driverNum
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Done> deleteDriver(String driverNum) {
        return request -> {
            Driver driver = Driver.builder().driverNum(driverNum).build();
            PersistentEntityRef<DriverCommand> ref = driverEntityRef(driver);
            return ref.ask(DriverCommand.DeleteDriver.builder().driver(driver).build());
        };
    }

    /**
     * @param driver
     * @return
     */
    private PersistentEntityRef<DriverCommand> driverEntityRef(Driver driver) {
        return persistentEntityRegistry.refFor(DriverEntity.class, driver.getDriverNum());
    }
}
