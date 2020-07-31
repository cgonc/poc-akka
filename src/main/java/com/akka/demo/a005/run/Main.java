package com.akka.demo.a005.run;

import com.akka.demo.a005.master.RaceController;
import com.akka.demo.a005.master.RaceControllerCommand;
import com.akka.demo.a005.master.StartRaceControllerCommand;

import akka.actor.typed.ActorSystem;

public class Main {

	public static void main(String[] args) {
		ActorSystem<RaceControllerCommand> master = ActorSystem.create(RaceController.create(),"RaceSimulation");
		master.tell(new StartRaceControllerCommand());
	}
}
