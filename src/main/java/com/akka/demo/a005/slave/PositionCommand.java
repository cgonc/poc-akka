package com.akka.demo.a005.slave;

import com.akka.demo.a005.master.RaceControllerCommand;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PositionCommand implements RacerCommand {

	private static final long serialVersionUID = 6702265325042151605L;

	@Getter
	private final ActorRef<RaceControllerCommand> master;
}
