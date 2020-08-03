package com.akka.demo.a009;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class WorkerBehavior extends AbstractBehavior<WorkerBehavior.Command> {

	@AllArgsConstructor
	public static class Command implements Serializable {

		private static final long serialVersionUID = -4866861576200261714L;
		@Getter
		private final ActorRef<ManagerBehavior.Command> sender;
	}

	private WorkerBehavior(ActorContext<Command> context) {
		super(context);
	}

	public static Behavior<Command> create() {
		return Behaviors.setup(WorkerBehavior::new);
	}

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder().onMessage(Command.class, command -> {
			BigInteger bigInteger = new BigInteger(2000, new Random());
			BigInteger prime = bigInteger.nextProbablePrime();
			Random r = new Random();
			if(r.nextInt(5) < 2){
				command.getSender().tell(new ManagerBehavior.ResultCommand(prime));
			}
			return Behaviors.same();
		}).build();
	}

}