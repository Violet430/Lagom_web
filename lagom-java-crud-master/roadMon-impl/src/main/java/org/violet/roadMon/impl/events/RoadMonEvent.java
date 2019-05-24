package org.violet.roadMon.impl.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.roadMon.api.RoadMon;

public interface RoadMonEvent extends Jsonable, AggregateEvent<RoadMonEvent> {
    @Override
    default AggregateEventTagger<RoadMonEvent> aggregateTag() {
        return RoadMonEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class RoadMonCreated implements RoadMonEvent, CompressedJsonable {
        RoadMon roadMon;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class RoadMonUpdated implements RoadMonEvent, CompressedJsonable {
        RoadMon roadMon;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class RoadMonDeleted implements RoadMonEvent, CompressedJsonable {
        RoadMon roadMon;
        String entityId;
    }
}
