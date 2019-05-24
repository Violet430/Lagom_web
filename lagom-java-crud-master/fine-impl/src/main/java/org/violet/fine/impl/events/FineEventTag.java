package org.violet.fine.impl.events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class FineEventTag {
    public static final AggregateEventTag<FineEvent> INSTANCE = AggregateEventTag.of(FineEvent.class);
}
