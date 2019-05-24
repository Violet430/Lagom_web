package org.violet.fine.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import org.violet.fine.impl.commands.FineCommand;
import org.violet.fine.impl.events.FineEvent;
import org.violet.fine.impl.states.FineStates;

import java.time.LocalDateTime;
import java.util.Optional;

public class FineEntity extends PersistentEntity<FineCommand, FineEvent, FineStates> {
    /**
     *
     * @param snapshotState
     * @return
     */
    @Override
    public Behavior initialBehavior(Optional<FineStates> snapshotState) {

        // initial behaviour of car
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                FineStates.builder().fine(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(FineCommand.CreateFine.class, (cmd, ctx) ->
                        ctx.thenPersist(FineEvent.FineCreated.builder().fine(cmd.getFine())
                                .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
                /**
                 * 进行数据质量的监控
                 */
        );

        behaviorBuilder.setEventHandler(FineEvent.FineCreated.class, evt ->
                FineStates.builder().fine(Optional.of(evt.getFine()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(FineCommand.UpdateFine.class, (cmd, ctx) ->
                ctx.thenPersist(FineEvent.FineUpdated.builder().fine(cmd.getFine()).entityId(entityId()).build()
                        , evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(FineEvent.FineUpdated.class, evt ->
                FineStates.builder().fine(Optional.of(evt.getFine()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(FineCommand.DeleteFine.class, (cmd, ctx) ->
                ctx.thenPersist(FineEvent.FineDeleted.builder().fine(cmd.getFine()).entityId(entityId()).build(),
                        evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(FineEvent.FineDeleted.class, evt ->
                FineStates.builder().fine(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(FineCommand.FineCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().getFine())
        );

        return behaviorBuilder.build();
    }
}
