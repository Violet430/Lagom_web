package org.violet.roadMon.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.violet.roadMon.api.RoadMon;
import org.violet.roadMon.api.RoadMonService;
import org.violet.roadMon.impl.commands.RoadMonCommand;
import org.violet.roadMon.impl.events.RoadMonEventProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class RoadMonServiceImpl implements RoadMonService {

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    /**
     * @param registry
     * @param readSide
     * @param session
     */
    @Inject
    public RoadMonServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;

        persistentEntityRegistry.register(RoadMonEntity.class);
        readSide.register(RoadMonEventProcessor.class);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Optional<RoadMon>> getRoadMon(String id) {
        return request -> {
            CompletionStage<Optional<RoadMon>> roadMonFuture =
                    session.selectAll("SELECT * FROM roadmons WHERE id = ?", id)
                            .thenApply(rows ->
                                    rows.stream()
                                            .map(row -> RoadMon.builder()
                                                    .id(row.getString("id"))
                                                    .carNum(row.getString("carNum"))
                                                    .time(row.getString("time"))
                                                    .place(row.getString("place"))
                                                    .illegalAct(row.getString("illegalAct"))
                                                    .docId(row.getString("docId"))
                                                    .punish(row.getString("punish"))
                                                    .fine(row.getString("fine"))
                                                    .pic(row.getString("pic"))
                                                    .build()
                                            )
                                            .findFirst()
                            );
            return roadMonFuture;
        };
    }


    /**
     * @return
     */
    @Override
    public ServiceCall<RoadMon, Done> addRoadMon() {
        return roadMon -> {
            PersistentEntityRef<RoadMonCommand> ref = roadMonEntityRef(roadMon);
            return ref.ask(RoadMonCommand.CreateRoadMon.builder().roadMon(roadMon).build());
        };
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<RoadMon, Done> updateRoadMon(String id) {
        return roadMon -> {
            PersistentEntityRef<RoadMonCommand> ref = roadMonEntityRef(roadMon);
            return ref.ask(RoadMonCommand.UpdateRoadMon.builder().roadMon(roadMon).build());
        };
    }
    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Done> deleteRoadMon(String id) {
        return request -> {
            RoadMon roadMon = RoadMon.builder().id(id).build();
            PersistentEntityRef<RoadMonCommand> ref = roadMonEntityRef(roadMon);
            return ref.ask(RoadMonCommand.DeleteRoadMon.builder().roadMon(roadMon).build());
        };
    }



    /**
     * @param roadMon
     * @return
     */
    private PersistentEntityRef<RoadMonCommand> roadMonEntityRef(RoadMon roadMon) {
        return persistentEntityRegistry.refFor(RoadMonEntity.class, roadMon.getId());
    }
}
