package org.violet.illegal.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.violet.illegal.api.Illegal;
import org.violet.illegal.api.IllegalService;
import org.violet.illegal.impl.commands.IllegalCommand;
import org.violet.illegal.impl.events.IllegalEventProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class IllegalServiceImpl implements IllegalService {
    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    /**
     * @param registry
     * @param readSide
     * @param session
     */
    @Inject
    public IllegalServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;

        persistentEntityRegistry.register(IllegalEntity.class);
        readSide.register(IllegalEventProcessor.class);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Optional<Illegal>> getIllegal(String id) {
        return request -> {
            CompletionStage<Optional<Illegal>> illegalFuture =
                    session.selectAll("SELECT * FROM illegals WHERE id = ?", id)
                            .thenApply(rows ->
                                    rows.stream()
                                            .map(row -> Illegal.builder()
                                                    .id(row.getString("id"))
                                                    .carNum(row.getString("carNum"))
                                                    .plateType(row.getString("plateType"))
                                                    .owner(row.getString("owner"))
                                                    .driver(row.getString("driver"))
                                                    .licenseId(row.getString("licenseId"))
                                                    .time(row.getString("time"))
                                                    .place(row.getString("place"))
                                                    .illegalAct(row.getString("illegalAct"))
                                                    .punish(row.getString("punish"))
                                                    .fine(row.getString("fine"))
                                                    .office(row.getString("office"))
                                                    .police(row.getString("police"))
                                                    .docId(row.getString("docId"))
                                                    .flag(row.getString("flag"))
                                                    .build()
                                            )
                                            .findFirst()
                            );
            return illegalFuture;
        };
    }


    /**
     * @return
     */
    @Override
    public ServiceCall<Illegal, Done> addIllegal() {
        return illegal -> {
            PersistentEntityRef<IllegalCommand> ref = illegalEntityRef(illegal);
            return ref.ask(IllegalCommand.CreateIllegal.builder().illegal(illegal).build());
        };
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<Illegal, Done> updateIllegal(String id) {
        return illegal -> {
            PersistentEntityRef<IllegalCommand> ref = illegalEntityRef(illegal);
            return ref.ask(IllegalCommand.UpdateIllegal.builder().illegal(illegal).build());
        };
    }
    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Done> deleteIllegal(String id) {
        return request -> {
            Illegal illegal = Illegal.builder().id(id).build();
            PersistentEntityRef<IllegalCommand> ref = illegalEntityRef(illegal);
            return ref.ask(IllegalCommand.DeleteIllegal.builder().illegal(illegal).build());
        };
    }



    /**
     * @param illegal
     * @return
     */
    private PersistentEntityRef<IllegalCommand> illegalEntityRef(Illegal illegal) {
        return persistentEntityRegistry.refFor(IllegalEntity.class, illegal.getId());
    }
}
