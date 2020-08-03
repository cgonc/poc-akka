package com.akka.demo.a009;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Duration;
import java.util.SortedSet;
import java.util.TreeSet;

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
public class ManagerBehavior extends AbstractBehavior<ManagerBehavior.Command> {

	private ActorRef<SortedSet<BigInteger>> sender;

	public interface Command extends Serializable {

	}

	@AllArgsConstructor
	public static class InstructionCommand implements Command {

		private static final long serialVersionUID = 5958389409949174538L;
		@Getter
		private final String message;
		@Getter
		private final ActorRef<SortedSet<BigInteger>> sender;
	}

	@AllArgsConstructor
	public static class ResultCommand implements Command {

		private static final long serialVersionUID = 4178018484174128753L;
		@Getter
		private final BigInteger prime;

	}

	@AllArgsConstructor
	private static class NoResponseRecievedCommand implements Command {

		private static final long serialVersionUID = -6651905187259273950L;
		@Getter
		private final ActorRef<WorkerBehavior.Command> worker;
	}

	private ManagerBehavior(ActorContext<Command> context) {
		super(context);
	}

	public static Behavior<Command> create() {
		return Behaviors.setup(ManagerBehavior::new);
	}

	private final SortedSet<BigInteger> primes = new TreeSet<>();

	private void askWorkerForPrime(ActorRef<WorkerBehavior.Command> worker) {
		getContext().ask(Command.class, worker, Duration.ofSeconds(5), WorkerBehavior.Command::new, (response, throwable) -> {
			if(response != null){
				return response;
			}
			log.info("Worker {} failed to respond within 5 seconds", worker.path());
			return new NoResponseRecievedCommand(worker);
		});
	}

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder().onMessage(InstructionCommand.class, command -> {
			if(command.getMessage().equals("start")){
				this.sender = command.sender;
				for(int i = 0; i < 20; i++){
					ActorRef<WorkerBehavior.Command> worker = getContext().spawn(WorkerBehavior.create(), "worker" + i);
					askWorkerForPrime(worker);
				}
			}
			return this;
		}).onMessage(ResultCommand.class, command -> {
			primes.add(command.getPrime());
			log.info("I have received " + primes.size() + " prime numbers");
			if(primes.size() == 20){
				this.sender.tell(primes);
			}
			return this;
		}).onMessage(NoResponseRecievedCommand.class, command -> {
			log.info("Retrying with worker {}", command.getWorker().path());
			askWorkerForPrime(command.getWorker());
			return Behaviors.same();
		}).build();
	}

}
