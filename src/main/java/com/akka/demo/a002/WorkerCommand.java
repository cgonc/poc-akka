package com.akka.demo.a002;

import java.io.Serializable;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class WorkerCommand implements Serializable {

	private static final long serialVersionUID = 5492061315316991422L;
	@Getter
	private final String message;
	@Getter
	private final ActorRef<ManagerCommand> managerBehaviourRef;
}
