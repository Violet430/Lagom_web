package org.violet.illegal.impl.events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class IllegalEventTag {
    public static final AggregateEventTag<IllegalEvent> INSTANCE = AggregateEventTag.of(IllegalEvent.class);
}
