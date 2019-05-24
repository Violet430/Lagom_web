package org.violet.car.impl.commands;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.car.api.Car;

import java.util.Optional;

public interface CarCommand extends Jsonable {
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CreateCar implements CarCommand, PersistentEntity.ReplyType<Done> {
        Car car;
    }
    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class UpdateCar implements CarCommand, PersistentEntity.ReplyType<Done> {
        Car car;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DeleteCar implements CarCommand, PersistentEntity.ReplyType<Done> {
        Car car;
    }

    @JsonDeserialize
    final class CarCurrentState implements CarCommand, PersistentEntity.ReplyType<Optional<Car>> {}
}
