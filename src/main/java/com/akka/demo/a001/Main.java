package com.akka.demo.a001;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

	public static void main(String[] args) {
		ActorSystem<FirstSimpleBehaviour.Command> firstActorSystem = ActorSystem.create(FirstSimpleBehaviour.create(), "firstActorSystem");
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("hello"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("Who are you"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("create a child"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));
		firstActorSystem.tell(new FirstSimpleBehaviour.TellMeSomething("Here is some message"));

		CompletionStage<List<String>> result = AskPattern.ask(firstActorSystem, FirstSimpleBehaviour.CollectTheResults::new, Duration.ofSeconds(10), firstActorSystem.scheduler());
		AtomicReference<Boolean> complete = new AtomicReference<>(false);
		result.whenComplete((reply, failure) -> {
			if(reply != null){
				reply.forEach(log::info);
				complete.set(true);
			} else {
				log.error("The system does not respond in time");
				complete.set(false);
			}
			firstActorSystem.terminate();
		}).whenComplete((reply, failure) -> {
			log.info("The execution result {}", complete.get().toString());
		});

	}
}
