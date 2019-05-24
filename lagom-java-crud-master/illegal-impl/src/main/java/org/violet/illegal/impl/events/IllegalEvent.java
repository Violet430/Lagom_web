package org.violet.illegal.impl.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.illegal.api.Illegal;

public interface IllegalEvent extends Jsonable, AggregateEvent<IllegalEvent> {
    @Override
    default AggregateEventTagger<IllegalEvent> aggregateTag() {
        return IllegalEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class IllegalCreated implements IllegalEvent, CompressedJsonable {
        Illegal illegal;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class IllegalUpdated implements IllegalEvent, CompressedJsonable {
        Illegal illegal;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class IllegalDeleted implements IllegalEvent, CompressedJsonable {
        Illegal illegal;
        String entityId;
    }


}
