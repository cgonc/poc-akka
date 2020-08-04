package com.akka.demo.a001;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirstSimpleBehaviour extends AbstractBehavior<FirstSimpleBehaviour.Command> {

	private final List<String> messages = new ArrayList<>();
	private ActorRef<List<String>> sender;

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

	private FirstSimpleBehaviour(ActorContext<Command> context) {
		super(context);
	}

	public static Behavior<Command> create() {
		return Behaviors.setup(FirstSimpleBehaviour::new);
	}

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder().onMessage(TellMeSomething.class, message -> {
			messages.add(message.getMessage());
			return Behaviors.same();
		}).onMessage(CollectTheResults.class, message -> {
			this.sender = message.getSender();
			if(messages.size() == 10){
				this.sender.tell(messages);
			}
			return Behaviors.same();
		}).build();
	}
}
