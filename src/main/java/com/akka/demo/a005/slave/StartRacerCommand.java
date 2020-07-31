package com.akka.demo.a005.slave;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StartRacerCommand implements RacerCommand {

	private static final long serialVersionUID = 6702265325042151605L;

	@Getter
	private final int raceLength;
}
