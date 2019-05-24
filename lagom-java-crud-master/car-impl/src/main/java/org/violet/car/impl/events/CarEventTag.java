package org.violet.car.impl.events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class CarEventTag {
    public static final AggregateEventTag<CarEvent> INSTANCE = AggregateEventTag.of(CarEvent.class);
}
