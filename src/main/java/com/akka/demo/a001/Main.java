package com.akka.demo.a001;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

	public static void main(String[] args) {
		ActorSystem<FirstSimpleBehaviour.Command> exampleActor = ActorSystem.create(FirstSimpleBehaviour.create(), "firstActorSystem");
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("hello"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("Who are you"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("create a child"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		exampleActor.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));

		CompletionStage<List<String>> result = AskPattern.ask(exampleActor, FirstSimpleBehaviour.CollectTheResults::new, Duration.ofSeconds(10), exampleActor.scheduler());
		result.whenComplete((reply, failure) -> {
			if(reply != null){
				log.info("The system responds in time");
			} else {
				log.error("The system does not respond in time");
				exampleActor.terminate();
				throw new RuntimeException("The system does not respond in time");
			}
			exampleActor.terminate();
		});
		try{
			List<String> messages = result.toCompletableFuture().get();
			messages.forEach(log::info);
		} catch (InterruptedException | ExecutionException e){
			e.printStackTrace();
		}
	}
}
