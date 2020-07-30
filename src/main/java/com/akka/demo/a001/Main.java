package com.akka.demo.a001;

import akka.actor.typed.ActorSystem;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		ActorSystem<String> firstActorSystem = ActorSystem.create(FirstSimpleBehaviour.create(), "FirstActorSystem");
		firstActorSystem.tell("My first message. Are you there?");
		Thread.sleep(3000);
		firstActorSystem.tell("This is the second message");
	}
}
