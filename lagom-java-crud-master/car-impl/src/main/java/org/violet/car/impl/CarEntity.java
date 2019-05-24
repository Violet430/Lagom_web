package org.violet.car.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import org.violet.car.impl.commands.CarCommand;
import org.violet.car.impl.events.CarEvent;
import org.violet.car.impl.states.CarStates;

import java.time.LocalDateTime;
import java.util.Optional;

public class CarEntity extends PersistentEntity<CarCommand, CarEvent, CarStates> {
    /**
     *
     * @param snapshotState
     * @return
     */
    @Override
    public Behavior initialBehavior(Optional<CarStates> snapshotState) {

        // initial behaviour of car
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                CarStates.builder().car(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(CarCommand.CreateCar.class, (cmd, ctx) ->
                ctx.thenPersist(CarEvent.CarCreated.builder().car(cmd.getCar())
                        .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
                /**
                 * 进行数据质量的监控
                 */
        );

        behaviorBuilder.setEventHandler(CarEvent.CarCreated.class, evt ->
                CarStates.builder().car(Optional.of(evt.getCar()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(CarCommand.UpdateCar.class, (cmd, ctx) ->
                ctx.thenPersist(CarEvent.CarUpdated.builder().car(cmd.getCar()).entityId(entityId()).build()
                        , evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(CarEvent.CarUpdated.class, evt ->
                CarStates.builder().car(Optional.of(evt.getCar()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(CarCommand.DeleteCar.class, (cmd, ctx) ->
                ctx.thenPersist(CarEvent.CarDeleted.builder().car(cmd.getCar()).entityId(entityId()).build(),
                        evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(CarEvent.CarDeleted.class, evt ->
                CarStates.builder().car(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(CarCommand.CarCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().getCar())
        );

        return behaviorBuilder.build();
    }

}
