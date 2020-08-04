package com.akka.demo.a010;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@RestController
public class MyRestController {

	@Autowired
	private ActorSystem actorSystem;

	@Autowired
	private SpringExtension springExtension;

	@GetMapping("/hello-akka")
	public void greetings(){
		ActorRef workerActor = actorSystem.actorOf(springExtension.props("simpleSpringActor"), "worker-actor");
		workerActor.tell("testme", null);
		workerActor.tell("testme", null);
		workerActor.tell("testme", null);
		workerActor.tell("testme", null);
		workerActor.tell("testme", null);
		workerActor.tell("testme", null);
	}

}
