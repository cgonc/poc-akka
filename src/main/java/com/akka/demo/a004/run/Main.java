package com.akka.demo.a004.run;

import com.akka.demo.a004.master.RaceController;
import com.akka.demo.a004.master.RaceControllerCommand;
import com.akka.demo.a004.master.StartRaceControllerCommand;

import akka.actor.typed.ActorSystem;

public class Main {

	public static void main(String[] args) {
		ActorSystem<RaceControllerCommand> master = ActorSystem.create(RaceController.create(),"RaceSimulation");
		master.tell(new StartRaceControllerCommand());
	}
}
