package com.akka.demo.a009;

import java.math.BigInteger;
import java.time.Duration;
import java.util.SortedSet;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ActorSystem<ManagerBehavior.Command> bigPrimes = ActorSystem.create(ManagerBehavior.create(), "BigPrimes");
		CompletionStage<SortedSet<BigInteger>> result = AskPattern.ask(bigPrimes, (me) -> new ManagerBehavior.InstructionCommand("start", me), Duration.ofSeconds(10), bigPrimes.scheduler());
		result.whenComplete((reply, failure) -> {
			if(reply != null){
				reply.forEach(s -> log.info(s.toString()));
			} else {
				log.error("The system does not respond in time");
				bigPrimes.terminate();
				throw new RuntimeException("The system does not respond in time");
			}
			bigPrimes.terminate();
		});
		SortedSet<BigInteger> bigIntegers = result.toCompletableFuture().get();
		log.info(bigIntegers.toString());
	}
}
