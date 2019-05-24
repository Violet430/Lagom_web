package org.violet.illegal.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import org.violet.illegal.impl.commands.IllegalCommand;
import org.violet.illegal.impl.events.IllegalEvent;
import org.violet.illegal.impl.states.IllegalStates;

import java.time.LocalDateTime;
import java.util.Optional;

public class IllegalEntity extends PersistentEntity<IllegalCommand, IllegalEvent, IllegalStates>{
    /**
     *
     * @param snapshotState
     * @return
     */
    @Override
    public PersistentEntity.Behavior initialBehavior(Optional<IllegalStates> snapshotState) {

        // initial behaviour of driver
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                IllegalStates.builder().illegal(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(IllegalCommand.CreateIllegal.class, (cmd, ctx) ->
                        ctx.thenPersist(IllegalEvent.IllegalCreated.builder().illegal(cmd.getIllegal())
                                .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
                /**
                 * 进行数据质量的监控
                 */
        );

        behaviorBuilder.setEventHandler(IllegalEvent.IllegalCreated.class, evt ->
                IllegalStates.builder().illegal(Optional.of(evt.getIllegal()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(IllegalCommand.UpdateIllegal.class, (cmd, ctx) ->
                ctx.thenPersist(IllegalEvent.IllegalUpdated.builder().illegal(cmd.getIllegal()).entityId(entityId()).build()
                        , evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(IllegalEvent.IllegalUpdated.class, evt ->
                IllegalStates.builder().illegal(Optional.of(evt.getIllegal()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(IllegalCommand.DeleteIllegal.class, (cmd, ctx) ->
                ctx.thenPersist(IllegalEvent.IllegalDeleted.builder().illegal(cmd.getIllegal()).entityId(entityId()).build(),
                        evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(IllegalEvent.IllegalDeleted.class, evt ->
                IllegalStates.builder().illegal(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(IllegalCommand.IllegalCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().getIllegal())
        );

        return behaviorBuilder.build();
    }
}
