package com.akka.demo.a004.master;

import com.akka.demo.a004.slave.RacerCommand;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RacerUpdateRaceControllerCommand implements RaceControllerCommand {

	private static final long serialVersionUID = -1882635508963435099L;

	@Getter
	private ActorRef<RacerCommand> slave;
	@Getter
	private int position;
}
