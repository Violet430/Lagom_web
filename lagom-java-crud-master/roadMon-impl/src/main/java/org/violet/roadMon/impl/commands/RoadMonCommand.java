package org.violet.roadMon.impl.commands;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.roadMon.api.RoadMon;

import java.util.Optional;

public interface RoadMonCommand extends Jsonable {
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CreateRoadMon implements RoadMonCommand, PersistentEntity.ReplyType<Done> {
        RoadMon roadMon;
    }
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class UpdateRoadMon implements RoadMonCommand, PersistentEntity.ReplyType<Done> {
        RoadMon roadMon;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DeleteRoadMon implements RoadMonCommand, PersistentEntity.ReplyType<Done> {
        RoadMon roadMon;
    }

    @JsonDeserialize
    final class RoadMonCurrentState implements RoadMonCommand, PersistentEntity.ReplyType<Optional<RoadMon>> {}
}
