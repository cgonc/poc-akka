package com.akka.demo.a001;

import akka.actor.typed.ActorSystem;

public class Main {

	public static void main(String[] args) {
		ActorSystem<String> firstActorSystem = ActorSystem.create(FirstSimpleBehaviour.create(), "firstActorSystem");
		firstActorSystem.tell("say hello");
		firstActorSystem.tell("Who are you");
		firstActorSystem.tell("create a child");
		firstActorSystem.tell("Here is some message");
	}
}
