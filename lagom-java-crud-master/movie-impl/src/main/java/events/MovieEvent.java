package events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knoldus.Movie;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Created by knoldus on 30/1/17.
 */
public interface MovieEvent extends Jsonable, AggregateEvent<MovieEvent> {

    @Override
    default AggregateEventTagger<MovieEvent> aggregateTag() {
        return MovieEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class MovieCreated implements MovieEvent, CompressedJsonable {
        Movie movie;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class MovieUpdated implements MovieEvent, CompressedJsonable {
        Movie movie;
        String entityId;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class MovieDeleted implements MovieEvent, CompressedJsonable {
        Movie movie;
        String entityId;
    }
}
