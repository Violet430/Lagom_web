package org.violet.fine.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.violet.fine.api.Fine;
import org.violet.fine.api.FineService;
import org.violet.fine.impl.commands.FineCommand;
import org.violet.fine.impl.events.FineEventProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class FineServiceImpl  implements FineService {

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    /**
     * @param registry
     * @param readSide
     * @param session
     */
    @Inject
    public FineServiceImpl(final PersistentEntityRegistry registry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = registry;
        this.session = session;

        persistentEntityRegistry.register(FineEntity.class);
        readSide.register(FineEventProcessor.class);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Optional<Fine>> getFine(String id) {
        return request -> {
            CompletionStage<Optional<Fine>> carFuture =
                    session.selectAll("SELECT * FROM fines WHERE id = ?", id)
                            .thenApply(rows ->
                                    rows.stream()
                                            .map(row -> Fine.builder()
                                                    .id(row.getString("id"))
                                                    .driver(row.getString("driver"))
                                                    .carNum(row.getString("carNum"))
                                                    .illegalId(row.getString("illegalId"))
                                                    .docID(row.getString("docID"))
                                                    .fine(row.getString("fine"))
                                                    .forfeit(row.getString("forfeit"))
                                                    .time(row.getString("time"))
                                                    .office(row.getString("office"))
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
    public ServiceCall<Fine, Done> addFine() {
        return fine -> {
            PersistentEntityRef<FineCommand> ref = fineEntityRef(fine);
            return ref.ask(FineCommand.CreateFine.builder().fine(fine).build());
        };
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<Fine, Done> updateFine(String id) {
        return fine -> {
            PersistentEntityRef<FineCommand> ref = fineEntityRef(fine);
            return ref.ask(FineCommand.UpdateFine.builder().fine(fine).build());
        };
    }
    /**
     * @param id
     * @return
     */
    @Override
    public ServiceCall<NotUsed, Done> deleteFine(String id) {
        return request -> {
            Fine fine = Fine.builder().id(id).build();
            PersistentEntityRef<FineCommand> ref = fineEntityRef(fine);
            return ref.ask(FineCommand.DeleteFine.builder().fine(fine).build());
        };
    }



    /**
     * @param fine
     * @return
     */
    private PersistentEntityRef<FineCommand> fineEntityRef(Fine fine) {
        return persistentEntityRegistry.refFor(FineEntity.class, fine.getId());
    }
}
