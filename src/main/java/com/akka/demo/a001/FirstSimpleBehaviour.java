package com.akka.demo.a001;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirstSimpleBehaviour extends AbstractBehavior<String> {

	private FirstSimpleBehaviour(ActorContext<String> context) {
		super(context);
	}

	public static Behavior<String> create() {
		return Behaviors.setup(FirstSimpleBehaviour::new);
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder().onMessageEquals("say hello", () -> {
			log.info("Hello there");
			return this;
		}).onMessageEquals("Who are you", (() -> {
			log.info("My path is {}", getContext().getSelf().path());
			return this;
		})).onMessageEquals("create a child", (() -> {
			ActorRef<String> secondActor = getContext().spawn(FirstSimpleBehaviour.create(), "secondActorSystem");
			secondActor.tell("Who are you");
			return this;
		})).onAnyMessage(message -> {
			log.info("I received a message : {}", message);
			return this;
		}).build();
	}
}
