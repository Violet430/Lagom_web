package org.violet.driver.impl.commands;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.driver.api.Driver;

import java.util.Optional;

public interface DriverCommand extends Jsonable {
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CreateDriver implements DriverCommand, PersistentEntity.ReplyType<Done> {
        Driver driver;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class UpdateDriver implements DriverCommand, PersistentEntity.ReplyType<Done> {
        Driver driver;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DeleteDriver implements DriverCommand, PersistentEntity.ReplyType<Done> {
        Driver driver;
    }

    @JsonDeserialize
    final class DriverCurrentState implements DriverCommand, PersistentEntity.ReplyType<Optional<Driver>> {}
}
