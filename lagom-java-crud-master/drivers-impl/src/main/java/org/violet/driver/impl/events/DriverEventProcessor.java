package org.violet.driver.impl.events;

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

import org.violet.driver.impl.events.DriverEvent.*;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class DriverEventProcessor extends ReadSideProcessor<DriverEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeDrivers;
    private PreparedStatement deleteDrivers;

    /**
     *
     * @param session
     * @param readSide
     */
    @Inject
    public DriverEventProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    /**
     *
     * @return
     */
    @Override
    public PSequence<AggregateEventTag<DriverEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(DriverEventTag.INSTANCE);
    }

    /**
     *
     * @return
     */
    @Override
    public ReadSideProcessor.ReadSideHandler<DriverEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<DriverEvent>builder("drivers_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteDriver()
                        .thenCombine(prepareDeleteDriver(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(DriverCreated.class, this::processPostAdded)
                .setEventHandler(DriverUpdated.class, this::processPostUpdated)
                .setEventHandler(DriverDeleted.class, this::processPostDeleted)
                .build();
    }

    /**
     *
     * @return
     */
    // Execute only once while application is start
    private CompletionStage<Done> createTable() {
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS drivers ( " +
                        "driverNum text,name text,id text,licenseId text,sex text,carType text,office text,time text,PRIMARY KEY (driverNum))"
        );
    }

    /*
     * START: Prepare statement for insert driver values into Drivers table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareWriteDriver() {
        return session.prepare(
                "INSERT INTO drivers (driverNum,name,id,licenseId,sex,carType,office,time) VALUES (?,?,?,?,?,?,?,?)"
        ).thenApply(ps -> {
            setWriteDrivers(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param statement
     */
    private void setWriteDrivers(PreparedStatement statement) {
        this.writeDrivers = statement;
    }

    // Bind prepare statement while DriverCreate event is executed

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostAdded(DriverCreated event) {
        BoundStatement bindWriteDriver = writeDrivers.bind();
        bindWriteDriver.setString("driverNum", event.getDriver().getDriverNum());
        bindWriteDriver.setString("name", event.getDriver().getName());
        bindWriteDriver.setString("id", event.getDriver().getId());
        bindWriteDriver.setString("licenseId", event.getDriver().getLicenseId());
        bindWriteDriver.setString("sex", event.getDriver().getSex());
        bindWriteDriver.setString("carType", event.getDriver().getCarType());
        bindWriteDriver.setString("office", event.getDriver().getOffice());
        bindWriteDriver.setString("time", event.getDriver().getTime());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteDriver));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for update the data in Drivers table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostUpdated(DriverUpdated event) {
        BoundStatement bindWriteDriver = writeDrivers.bind();
        bindWriteDriver.setString("driverNum", event.getDriver().getDriverNum());
        bindWriteDriver.setString("name", event.getDriver().getName());
        bindWriteDriver.setString("id", event.getDriver().getId());
        bindWriteDriver.setString("licenseId", event.getDriver().getLicenseId());
        bindWriteDriver.setString("sex", event.getDriver().getSex());
        bindWriteDriver.setString("carType", event.getDriver().getCarType());
        bindWriteDriver.setString("office", event.getDriver().getOffice());
        bindWriteDriver.setString("time", event.getDriver().getTime());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteDriver));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for delete the the Driver from table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareDeleteDriver() {
        return session.prepare(
                "DELETE FROM drivers WHERE driverNum=?"
        ).thenApply(ps -> {
            setDeleteDrivers(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param deleteDrivers
     */
    private void setDeleteDrivers(PreparedStatement deleteDrivers) {
        this.deleteDrivers = deleteDrivers;
    }

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostDeleted(DriverDeleted event) {
        BoundStatement bindWriteDriver = deleteDrivers.bind();
        bindWriteDriver.setString("driverNum", event.getDriver().getDriverNum());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteDriver));
    }
    /* ******************* END ****************************/




}
