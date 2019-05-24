package commands;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knoldus.Movie;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Created by knoldus on 30/1/17.
 */
public interface MovieCommand extends Jsonable {

    /**
     * 测试数据质量*********************************************************************************************************
     *      @Value
     *     @Builder
     *     @JsonDeserialize
     *     @AllArgsConstructor
     *     final class CreateMovie implements MovieCommand, PersistentEntity.ReplyType<Done> {
     *         Movie movie;
     *     }
     */


    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class CreateMovie implements MovieCommand, PersistentEntity.ReplyType<String> {
        Movie movie;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class UpdateMovie implements MovieCommand, PersistentEntity.ReplyType<Done> {
        Movie movie;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class DeleteMovie implements MovieCommand, PersistentEntity.ReplyType<Done> {
        Movie movie;
    }

    @JsonDeserialize
    final class MovieCurrentState implements MovieCommand, PersistentEntity.ReplyType<Optional<Movie>> {}
}
