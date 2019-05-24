import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import commands.MovieCommand;
import events.MovieEvent;
import events.MovieEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import states.MovieStates;

import java.util.regex.*;

import java.time.LocalDateTime;
import java.util.Optional;

public class MovieEntity  extends PersistentEntity<MovieCommand, MovieEvent, MovieStates> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieEntity.class);


    /**
     *
     * @param snapshotState
     * @return
     */
    @Override
    public Behavior initialBehavior(Optional<MovieStates> snapshotState) {

        // initial behaviour of movie
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                MovieStates.builder().movie(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );


        /**
         * 注释的内容与下面的代码功能相同
         */
        /*behaviorBuilder.setCommandHandler(MovieCommand.CreateMovie.class, (cmd, ctx) ->

                ctx.thenPersist(MovieEvent.MovieCreated.builder().movie(cmd.getMovie())
                        .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
        );*/
        behaviorBuilder.setCommandHandler(MovieCommand.CreateMovie.class, (cmd, ctx) ->{
                    MovieEvent event = MovieEvent.MovieCreated.builder().movie(cmd.getMovie()).entityId(entityId()).build();
                    System.out.println(cmd.getMovie().getName());

                    String content1 = cmd.getMovie().getName();
                    //String content2 = cmd.getMovie().getGenre();

                    String pattern="^[\\u4e00-\\u9fa5]*$";

                    Matcher m = Pattern.compile(pattern).matcher(content1);

                    if (m==null){
                        System.out.println(cmd.getMovie().getName());

                        MyUtil.sendInfo(cmd.getMovie().getName());

                        return ctx.thenPersist(event,evt->ctx.reply("warning"));
                    }

                    return ctx.thenPersist(event,evt->ctx.reply("ok"));

            /**
             * 测试数据质量***************
             */



                    //return ctx.thenPersist(event,evt->ctx.reply(Done.getInstance()));
                }
        );
        /**
         * 上面是例子！！！！！！！！
         */

        behaviorBuilder.setEventHandler(MovieEvent.MovieCreated.class, evt ->
                MovieStates.builder().movie(Optional.of(evt.getMovie()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );


        behaviorBuilder.setCommandHandler(MovieCommand.UpdateMovie.class, (cmd, ctx) ->
                ctx.thenPersist(MovieEvent.MovieUpdated.builder().movie(cmd.getMovie()).entityId(entityId()).build()
                        , evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(MovieEvent.MovieUpdated.class, evt ->
                MovieStates.builder().movie(Optional.of(evt.getMovie()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setCommandHandler(MovieCommand.DeleteMovie.class, (cmd, ctx) ->
                ctx.thenPersist(MovieEvent.MovieDeleted.builder().movie(cmd.getMovie()).entityId(entityId()).build(),
                        evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(MovieEvent.MovieDeleted.class, evt ->
                MovieStates.builder().movie(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(MovieCommand.MovieCurrentState.class, (cmd, ctx) ->
                ctx.reply(state().getMovie())
        );

        return behaviorBuilder.build();
    }
}
