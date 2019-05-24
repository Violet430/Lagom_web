package org.violet.roadMon.impl.events;

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
import org.violet.roadMon.impl.events.RoadMonEvent.*;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class RoadMonEventProcessor extends ReadSideProcessor<RoadMonEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadMonEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeRoadMons;
    private PreparedStatement deleteRoadMons;

    /**
     *
     * @param session
     * @param readSide
     */
    @Inject
    public RoadMonEventProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    /**
     *
     * @return
     */
    @Override
    public PSequence<AggregateEventTag<RoadMonEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(RoadMonEventTag.INSTANCE);
    }

    /**
     *
     * @return
     */
    @Override
    public ReadSideProcessor.ReadSideHandler<RoadMonEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<RoadMonEvent>builder("roadmons_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteRoadMon()
                        .thenCombine(prepareDeleteRoadMon(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(RoadMonCreated.class, this::processPostAdded)
                .setEventHandler(RoadMonUpdated.class, this::processPostUpdated)
                .setEventHandler(RoadMonDeleted.class, this::processPostDeleted)
                .build();
    }

    /**
     *
     * @return
     */
    // Execute only once while application is start
    private CompletionStage<Done> createTable() {
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS roadmons ( " +
                        "id text,carNum text,time text,place text,illegalAct text,docId text,punish text,fine text,pic text,PRIMARY KEY (id))"
        );
    }

    /*
     * START: Prepare statement for insert car values into Cars table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareWriteRoadMon() {
        return session.prepare(
                "INSERT INTO roadmons (id,carNum,time,place,illegalAct,docId,punish,fine,pic) VALUES (?,?,?,?,?,?,?,?,?)"
        ).thenApply(ps -> {
            setWriteRoadMons(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param statement
     */
    private void setWriteRoadMons(PreparedStatement statement) {
        this.writeRoadMons = statement;
    }

    // Bind prepare statement while CarCreate event is executed

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostAdded(RoadMonCreated event) {
        BoundStatement bindWriteRoadMon = writeRoadMons.bind();
        bindWriteRoadMon.setString("id", event.getRoadMon().getId());
        bindWriteRoadMon.setString("carNum", event.getRoadMon().getCarNum());
        bindWriteRoadMon.setString("time", event.getRoadMon().getTime());
        bindWriteRoadMon.setString("place", event.getRoadMon().getPlace());
        bindWriteRoadMon.setString("illegalAct", event.getRoadMon().getIllegalAct());
        bindWriteRoadMon.setString("docId", event.getRoadMon().getDocId());
        bindWriteRoadMon.setString("punish", event.getRoadMon().getPunish());
        bindWriteRoadMon.setString("fine", event.getRoadMon().getFine());
        bindWriteRoadMon.setString("pic", event.getRoadMon().getPic());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteRoadMon));
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
    private CompletionStage<List<BoundStatement>> processPostUpdated(RoadMonUpdated event) {
        BoundStatement bindWriteRoadMon = writeRoadMons.bind();
        bindWriteRoadMon.setString("id", event.getRoadMon().getId());
        bindWriteRoadMon.setString("carNum", event.getRoadMon().getCarNum());
        bindWriteRoadMon.setString("time", event.getRoadMon().getTime());
        bindWriteRoadMon.setString("place", event.getRoadMon().getPlace());
        bindWriteRoadMon.setString("illegalAct", event.getRoadMon().getIllegalAct());
        bindWriteRoadMon.setString("docId", event.getRoadMon().getDocId());
        bindWriteRoadMon.setString("punish", event.getRoadMon().getPunish());
        bindWriteRoadMon.setString("fine", event.getRoadMon().getFine());
        bindWriteRoadMon.setString("pic", event.getRoadMon().getPic());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteRoadMon));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for delete the the Car from table.
     * This is just creation of prepared statement, we will map this statement with our event
     */

    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareDeleteRoadMon() {
        return session.prepare(
                "DELETE FROM roadmons WHERE id=?"
        ).thenApply(ps -> {
            setDeleteRoadMons(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param deleteRoadMons
     */
    private void setDeleteRoadMons(PreparedStatement deleteRoadMons) {
        this.deleteRoadMons = deleteRoadMons;
    }

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostDeleted(RoadMonDeleted event) {
        BoundStatement bindWriteRoadMon = deleteRoadMons.bind();
        bindWriteRoadMon.setString("id", event.getRoadMon().getId());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteRoadMon));
    }
    /* ******************* END ****************************/
}
