package org.violet.illegal.impl.events;

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
import org.violet.illegal.impl.events.IllegalEvent.*;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class IllegalEventProcessor extends ReadSideProcessor<IllegalEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IllegalEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement writeIllegals;
    private PreparedStatement deleteIllegals;

    /**
     *
     * @param session
     * @param readSide
     */
    @Inject
    public IllegalEventProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }
    /**
     *
     * @return
     */
    @Override
    public PSequence<AggregateEventTag<IllegalEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(IllegalEventTag.INSTANCE);
    }
    /**
     *
     * @return
     */
    @Override
    public ReadSideProcessor.ReadSideHandler<IllegalEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<IllegalEvent>builder("illegals_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteIllegal()
                        .thenCombine(prepareDeleteIllegal(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(IllegalCreated.class, this::processPostAdded)
                .setEventHandler(IllegalUpdated.class, this::processPostUpdated)
                .setEventHandler(IllegalDeleted.class, this::processPostDeleted)
                .build();
    }

    /**
     *
     * @return
     */
    // Execute only once while application is start
    private CompletionStage<Done> createTable() {
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS illegals ( " +
                        "id text,carNum text,plateType text,owner text,driver text,licenseId text,"+
                        "time text,place text,illegalAct text,punish text,fine text,office text,police text,docId text,flag text,PRIMARY KEY (id))"
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
    private CompletionStage<Done> prepareWriteIllegal() {
        return session.prepare(
                "INSERT INTO illegals (id,carNum,plateType,owner,driver,licenseId,time,place,illegalAct,punish,fine,office,police,docId,flag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        ).thenApply(ps -> {
            setWriteIllegals(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param statement
     */
    private void setWriteIllegals(PreparedStatement statement) {
        this.writeIllegals = statement;
    }

    // Bind prepare statement while CarCreate event is executed
    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostAdded(IllegalCreated event) {
        BoundStatement bindWriteIllegal = writeIllegals.bind();
        bindWriteIllegal.setString("id", event.getIllegal().getId());
        bindWriteIllegal.setString("carNum", event.getIllegal().getCarNum());
        bindWriteIllegal.setString("plateType", event.getIllegal().getPlateType());
        bindWriteIllegal.setString("owner", event.getIllegal().getOwner());
        bindWriteIllegal.setString("driver", event.getIllegal().getDriver());
        bindWriteIllegal.setString("licenseId", event.getIllegal().getLicenseId());
        bindWriteIllegal.setString("time", event.getIllegal().getTime());
        bindWriteIllegal.setString("place", event.getIllegal().getPlace());
        bindWriteIllegal.setString("illegalAct", event.getIllegal().getIllegalAct());
        bindWriteIllegal.setString("punish", event.getIllegal().getPunish());
        bindWriteIllegal.setString("fine", event.getIllegal().getFine());
        bindWriteIllegal.setString("office", event.getIllegal().getOffice());
        bindWriteIllegal.setString("police", event.getIllegal().getPolice());
        bindWriteIllegal.setString("docId", event.getIllegal().getDocId());
        bindWriteIllegal.setString("flag", event.getIllegal().getFlag());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteIllegal));
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
    private CompletionStage<List<BoundStatement>> processPostUpdated(IllegalUpdated event) {
        BoundStatement bindWriteIllegal = writeIllegals.bind();
        bindWriteIllegal.setString("id", event.getIllegal().getId());
        bindWriteIllegal.setString("carNum", event.getIllegal().getCarNum());
        bindWriteIllegal.setString("plateType", event.getIllegal().getPlateType());
        bindWriteIllegal.setString("owner", event.getIllegal().getOwner());
        bindWriteIllegal.setString("driver", event.getIllegal().getDriver());
        bindWriteIllegal.setString("licenseId", event.getIllegal().getLicenseId());
        bindWriteIllegal.setString("time", event.getIllegal().getTime());
        bindWriteIllegal.setString("place", event.getIllegal().getPlace());
        bindWriteIllegal.setString("illegalAct", event.getIllegal().getIllegalAct());
        bindWriteIllegal.setString("punish", event.getIllegal().getPunish());
        bindWriteIllegal.setString("fine", event.getIllegal().getFine());
        bindWriteIllegal.setString("office", event.getIllegal().getOffice());
        bindWriteIllegal.setString("police", event.getIllegal().getPolice());
        bindWriteIllegal.setString("docId", event.getIllegal().getDocId());
        bindWriteIllegal.setString("flag", event.getIllegal().getFlag());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteIllegal));
    }
    /* ******************* END ****************************/

    /* START: Prepare statement for delete the the Car from table.
     * This is just creation of prepared statement, we will map this statement with our event
     */
    /**
     *
     * @return
     */
    private CompletionStage<Done> prepareDeleteIllegal() {
        return session.prepare(
                "DELETE FROM illegals WHERE id=?"
        ).thenApply(ps -> {
            setDeleteIllegals(ps);
            return Done.getInstance();
        });
    }

    /**
     *
     * @param deleteIllegals
     */
    private void setDeleteIllegals(PreparedStatement deleteIllegals) {
        this.deleteIllegals = deleteIllegals;
    }

    /**
     *
     * @param event
     * @return
     */
    private CompletionStage<List<BoundStatement>> processPostDeleted(IllegalDeleted event) {
        BoundStatement bindWriteIllegal = deleteIllegals.bind();
        bindWriteIllegal.setString("id", event.getIllegal().getId());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteIllegal));
    }
    /* ******************* END ****************************/
}
