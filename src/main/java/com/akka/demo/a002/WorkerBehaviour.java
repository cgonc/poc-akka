package com.akka.demo.a002;

import java.math.BigInteger;
import java.util.Random;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkerBehaviour extends AbstractBehavior<WorkerCommand> {

	private WorkerBehaviour(ActorContext<WorkerCommand> context) {
		super(context);
	}

	public static Behavior<WorkerCommand> create() {
		return Behaviors.setup(WorkerBehaviour::new);
	}

	@Override
	public Receive<WorkerCommand> createReceive() {
		return newReceiveBuilder().onAnyMessage(workerCommand -> {
			if("start".equals(workerCommand.getMessage())){
				BigInteger bigInteger = new BigInteger(200, new Random());
				workerCommand.getManagerBehaviourRef().tell(new ResultManagerCommand(bigInteger));
				log.info("Worker node calculates one");
			}
			return this;
		}).build();
	}
}
