package org.violet.illegal.impl.commands;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.illegal.api.Illegal;

import java.util.Optional;

public interface IllegalCommand extends Jsonable {
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CreateIllegal implements IllegalCommand, PersistentEntity.ReplyType<Done> {
        Illegal illegal;
    }
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class UpdateIllegal implements IllegalCommand, PersistentEntity.ReplyType<Done> {
        Illegal illegal;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DeleteIllegal implements IllegalCommand, PersistentEntity.ReplyType<Done> {
        Illegal illegal;
    }

    @JsonDeserialize
    final class IllegalCurrentState implements IllegalCommand, PersistentEntity.ReplyType<Optional<Illegal>> {}
}
