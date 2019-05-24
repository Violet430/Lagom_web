package org.violet.fine.impl.commands;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.fine.api.Fine;

import java.util.Optional;

public interface FineCommand extends Jsonable {
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CreateFine implements FineCommand, PersistentEntity.ReplyType<Done> {
        Fine fine;
    }
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class UpdateFine implements FineCommand, PersistentEntity.ReplyType<Done> {
        Fine fine;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DeleteFine implements FineCommand, PersistentEntity.ReplyType<Done> {
        Fine fine;
    }

    @JsonDeserialize
    final class FineCurrentState implements FineCommand, PersistentEntity.ReplyType<Optional<Fine>> {}
}
