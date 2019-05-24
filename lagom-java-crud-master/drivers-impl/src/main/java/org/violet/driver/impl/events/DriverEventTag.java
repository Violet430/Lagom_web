package org.violet.driver.impl.events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class DriverEventTag {
    public static final AggregateEventTag<DriverEvent> INSTANCE = AggregateEventTag.of(DriverEvent.class);
}
