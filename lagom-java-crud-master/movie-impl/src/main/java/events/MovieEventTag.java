package events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class MovieEventTag {

    public static final AggregateEventTag<MovieEvent> INSTANCE = AggregateEventTag.of(MovieEvent.class);
}
