package org.violet.driver.impl.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.driver.api.Driver;

public interface DriverEvent  extends Jsonable, AggregateEvent<DriverEvent> {
    @Override
    default AggregateEventTagger<DriverEvent> aggregateTag() {
        return DriverEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DriverCreated implements DriverEvent, CompressedJsonable {
        Driver driver;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DriverUpdated implements DriverEvent, CompressedJsonable {
        Driver driver;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DriverDeleted implements DriverEvent, CompressedJsonable {
        Driver driver;
        String entityId;
    }
}
