package org.violet.roadMon.impl.events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class RoadMonEventTag {
    public static final AggregateEventTag<RoadMonEvent> INSTANCE = AggregateEventTag.of(RoadMonEvent.class);
}
