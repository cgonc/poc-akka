package com.akka.demo.a002.run;

import com.akka.demo.a002.InstructionManagerCommand;
import com.akka.demo.a002.ManagerBehaviour;
import com.akka.demo.a002.ManagerCommand;

import akka.actor.typed.ActorSystem;

public class Main {

	public static void main(String[] args) {
		ActorSystem<ManagerCommand> managerBehaviour = ActorSystem.create(ManagerBehaviour.create(), "managerBehaviour");
		managerBehaviour.tell(new InstructionManagerCommand("start"));
	}
}
