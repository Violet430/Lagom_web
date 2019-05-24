package org.violet.fine.impl.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.fine.api.Fine;

public interface FineEvent extends Jsonable, AggregateEvent<FineEvent> {
    @Override
    default AggregateEventTagger<FineEvent> aggregateTag() {
        return FineEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class FineCreated implements FineEvent, CompressedJsonable {
        Fine fine;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class FineUpdated implements FineEvent, CompressedJsonable {
        Fine fine;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class FineDeleted implements FineEvent, CompressedJsonable {
        Fine fine;
        String entityId;
    }
}
