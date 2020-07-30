package com.akka.demo.a002;

import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagerBehaviour extends AbstractBehavior<ManagerCommand> {

	@Getter
	private final SortedSet<BigInteger> primes;

	private ManagerBehaviour(ActorContext<ManagerCommand> context) {
		super(context);
		primes = new TreeSet<>();
	}

	public static Behavior<ManagerCommand> create() {
		return Behaviors.setup(ManagerBehaviour::new);
	}

	@Override
	public Receive<ManagerCommand> createReceive() {
		return newReceiveBuilder().onMessage(InstructionManagerCommand.class, command -> {
			if("start".equals(command.getMessage())){
				for(int i = 0; i < 5000000; i++){
					ActorRef<WorkerCommand> workerBehaviour = getContext().spawn(WorkerBehaviour.create(), "workerBehaviour" + i);
					workerBehaviour.tell(new WorkerCommand("start", getContext().getSelf()));
				}
			}
			return this;
		}).onMessage(ResultManagerCommand.class, command -> {
			primes.add(command.getPrime());
			log.info("I have received {} primes so for", primes.size());
			if(primes.size() == 5000000){
				log.info("My work is done...");
			}
			return this;
		}).build();
	}
}
