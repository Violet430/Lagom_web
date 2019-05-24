package org.violet.roadMon.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import org.violet.roadMon.impl.commands.RoadMonCommand;
import org.violet.roadMon.impl.events.RoadMonEvent;
import org.violet.roadMon.impl.states.RoadMonStates;

import java.time.LocalDateTime;
import java.util.Optional;

public class RoadMonEntity extends PersistentEntity<RoadMonCommand, RoadMonEvent, RoadMonStates> {
    /**
     *
     * @param snapshotState
     * @return
     */
    @Override
    public PersistentEntity.Behavior initialBehavior(Optional<RoadMonStates> snapshotState) {

        // initial behaviour of roadMon
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                RoadMonStates.builder().roadMon(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(RoadMonCommand.CreateRoadMon.class, (cmd, ctx) ->
                        ctx.thenPersist(RoadMonEvent.RoadMonCreated.builder().roadMon(cmd.getRoadMon())
                                .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
                /**
                 * 进行数据质量的监控
                 */
        );

        behaviorBuilder.setEventHandler(RoadMonEvent.RoadMonCreated.class, evt ->
                RoadMonStates.builder().roadMon(Optional.of(evt.getRoadMon()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(RoadMonCommand.UpdateRoadMon.class, (cmd, ctx) ->
                ctx.thenPersist(RoadMonEvent.RoadMonUpdated.builder().roadMon(cmd.getRoadMon()).entityId(entityId()).build()
                        , evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(RoadMonEvent.RoadMonUpdated.class, evt ->
                RoadMonStates.builder().roadMon(Optional.of(evt.getRoadMon()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(RoadMonCommand.DeleteRoadMon.class, (cmd, ctx) ->
                ctx.thenPersist(RoadMonEvent.RoadMonDeleted.builder().roadMon(cmd.getRoadMon()).entityId(entityId()).build(),
                        evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(RoadMonEvent.RoadMonDeleted.class, evt ->
                RoadMonStates.builder().roadMon(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(RoadMonCommand.RoadMonCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().getRoadMon())
        );

        return behaviorBuilder.build();
    }
}
