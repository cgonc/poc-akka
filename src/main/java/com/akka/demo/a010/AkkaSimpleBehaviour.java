package com.akka.demo.a010;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.akka.demo.a010.config.ActorInjector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AkkaSimpleBehaviour extends AbstractBehavior<AkkaSimpleBehaviour.Command> {

    private final List<String> messages = new ArrayList<>();
    private ActorRef<List<String>> sender;
    private ActorInjector actorInjector;

    public interface Command extends Serializable {

    }

    @AllArgsConstructor
    public static class TellMeSomething implements Command {

        private static final long serialVersionUID = -7796709831949054890L;
        @Getter
        private final String message;
    }

    @AllArgsConstructor
    public static class CollectTheResults implements Command {

        private static final long serialVersionUID = 1643210899551075153L;
        @Getter
        private final ActorRef<List<String>> sender;
    }

    private AkkaSimpleBehaviour(ActorContext<Command> context, ActorInjector actorInjector) {
        super(context);
        this.actorInjector = actorInjector;
    }

    public static Behavior<Command> create(ActorInjector actorInjector) {
        return Behaviors.setup(ctx -> new AkkaSimpleBehaviour(ctx,actorInjector));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessage(TellMeSomething.class, message -> {
            messages.add(message.getMessage());
            actorInjector.getASimpleSpringService().logSomething("*-*-*-*-*-*-*-*-*-*");
            return Behaviors.same();
        }).onMessage(CollectTheResults.class, message -> {
            this.sender = message.getSender();
            if (messages.size() == 4) {
                this.sender.tell(messages);
            }
            return Behaviors.same();
        }).build();
    }
}
