package org.violet.driver.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import org.violet.driver.impl.commands.DriverCommand;
import org.violet.driver.impl.events.DriverEvent;
import org.violet.driver.impl.states.DriverStates;

import java.time.LocalDateTime;
import java.util.Optional;

public class DriverEntity extends PersistentEntity<DriverCommand, DriverEvent, DriverStates> {
    /**
     *
     * @param snapshotState
     * @return
     */
    @Override
    public Behavior initialBehavior(Optional<DriverStates> snapshotState) {

        // initial behaviour of driver
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                DriverStates.builder().driver(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(DriverCommand.CreateDriver.class, (cmd, ctx) ->
                        ctx.thenPersist(DriverEvent.DriverCreated.builder().driver(cmd.getDriver())
                                .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
                /**
                 * 进行数据质量的监控
                 */
        );

        behaviorBuilder.setEventHandler(DriverEvent.DriverCreated.class, evt ->
                DriverStates.builder().driver(Optional.of(evt.getDriver()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(DriverCommand.UpdateDriver.class, (cmd, ctx) ->
                ctx.thenPersist(DriverEvent.DriverUpdated.builder().driver(cmd.getDriver()).entityId(entityId()).build()
                        , evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(DriverEvent.DriverUpdated.class, evt ->
                DriverStates.builder().driver(Optional.of(evt.getDriver()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(DriverCommand.DeleteDriver.class, (cmd, ctx) ->
                ctx.thenPersist(DriverEvent.DriverDeleted.builder().driver(cmd.getDriver()).entityId(entityId()).build(),
                        evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(DriverEvent.DriverDeleted.class, evt ->
                DriverStates.builder().driver(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(DriverCommand.DriverCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().getDriver())
        );

        return behaviorBuilder.build();
    }
}
