package org.violet.car.impl.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.car.api.Car;

public interface CarEvent extends Jsonable, AggregateEvent<CarEvent> {
    @Override
    default AggregateEventTagger<CarEvent> aggregateTag() {
        return CarEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CarCreated implements CarEvent, CompressedJsonable {
        Car car;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CarUpdated implements CarEvent, CompressedJsonable {
        Car car;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CarDeleted implements CarEvent, CompressedJsonable {
        Car car;
        String entityId;
    }
}
