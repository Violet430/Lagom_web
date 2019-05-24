package org.violet.car.impl.events;

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
import org.violet.car.impl.events.CarEvent.*;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class CarEventProcessor extends ReadSideProcessor<CarEvent>{
    private static final Logger LOGGER = LoggerFactory.getLogger(CarEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeCars;
    private PreparedStatement deleteCars;

    /**
     *
     * @param session
     * @param readSide
     */
    @Inject
    public CarEventProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    /**
     *
     * @return
     */
    @Override
    public PSequence<AggregateEventTag<CarEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(CarEventTag.INSTANCE);
    }

    /**
     *
     * @return
     */
    @Override
    public ReadSideProcessor.ReadSideHandler<CarEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<CarEvent>builder("cars_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteCar()
                        .thenCombine(prepareDeleteCar(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(CarCreated.class, this::processPostAdded)
                .setEventHandler(CarUpdated.class, this::processPostUpdated)
                .setEventHandler(CarDeleted.class, this::processPostDeleted)
                .build();
    }

    /**
     *
     * @return
     */
    // 只应用程序启动时执行一次
    private CompletionStage<Done> createTable() {
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS cars ( " +
                        "vehicleNumber text,carNum text,plateType text,carColor text,frameNum text,owner text,tel text,PRIMARY KEY (vehicleNumber))"
        );
    }

    /*
     * 准备将汽车属性值插入汽车表
     * 这只是创建预备语句，使用事件映射此语句
     */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareWriteCar() {
        return session.prepare(
                "INSERT INTO cars (vehicleNumber, carNum,plateType,carColor,frameNum,owner,tel) VALUES (?,?,?,?,?,?,?)"
        ).thenApply(ps -> {
            setWriteCars(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param statement
     */
    private void setWriteCars(PreparedStatement statement) {
        this.writeCars = statement;
    }

    // Bind prepare statement while CarCreate event is executed

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostAdded(CarCreated event) {
        BoundStatement bindWriteCar = writeCars.bind();
        bindWriteCar.setString("vehicleNumber", event.getCar().getVehicleNumber());
        bindWriteCar.setString("carNum", event.getCar().getCarNum());
        bindWriteCar.setString("plateType", event.getCar().getPlateType());
        bindWriteCar.setString("carColor", event.getCar().getCarColor());
        bindWriteCar.setString("frameNum", event.getCar().getFrameNum());
        bindWriteCar.setString("owner", event.getCar().getOwner());
        bindWriteCar.setString("tel", event.getCar().getTel());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteCar));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for update the data in Cars table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostUpdated(CarUpdated event) {
        BoundStatement bindWriteCar = writeCars.bind();
        bindWriteCar.setString("vehicleNumber", event.getCar().getVehicleNumber());
        bindWriteCar.setString("carNum", event.getCar().getCarNum());
        bindWriteCar.setString("plateType", event.getCar().getPlateType());
        bindWriteCar.setString("carColor", event.getCar().getCarColor());
        bindWriteCar.setString("frameNum", event.getCar().getFrameNum());
        bindWriteCar.setString("owner", event.getCar().getOwner());
        bindWriteCar.setString("tel", event.getCar().getTel());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteCar));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for delete the the Car from table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareDeleteCar() {
        return session.prepare(
                "DELETE FROM cars WHERE vehicleNumber=?"
        ).thenApply(ps -> {
            setDeleteCars(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param deleteCars
     */
    private void setDeleteCars(PreparedStatement deleteCars) {
        this.deleteCars = deleteCars;
    }

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostDeleted(CarDeleted event) {
        BoundStatement bindWriteCar = deleteCars.bind();
        bindWriteCar.setString("vehicleNumber", event.getCar().getVehicleNumber());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteCar));
    }
    /* ******************* END ****************************/
}
