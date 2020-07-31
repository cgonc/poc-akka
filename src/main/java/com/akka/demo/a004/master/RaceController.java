package com.akka.demo.a004.master;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.akka.demo.a004.slave.PositionCommand;
import com.akka.demo.a004.slave.Racer;
import com.akka.demo.a004.slave.RacerCommand;
import com.akka.demo.a004.slave.StartRacerCommand;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class RaceController extends AbstractBehavior<RaceControllerCommand> {

	private Object TIMER_KEY;

	private Map<ActorRef<RacerCommand>, Integer> currentPositions;
	private Long start;
	private int raceLength = 100;

	private RaceController(ActorContext<RaceControllerCommand> context) {
		super(context);
	}

	public static Behavior<RaceControllerCommand> create() {
		return Behaviors.setup(RaceController::new);
	}

	private void displayRace() {
		int displayLength = 160;
		for (int i = 0; i < 50; ++i) System.out.println();
		System.out.println("Race has been running for " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
		System.out.println("    " + new String (new char[displayLength]).replace('\0', '='));

		int i = 0;
		for(ActorRef<RacerCommand> racerCommandActorRef : currentPositions.keySet()){
			System.out.println(i + " : "  + new String (new char[currentPositions.get(racerCommandActorRef) * displayLength / 100]).replace('\0', '*'));
			i++;
		}
	}

	@Override
	public Receive<RaceControllerCommand> createReceive() {
		return newReceiveBuilder().onMessage(StartRaceControllerCommand.class, message -> {
			start = System.currentTimeMillis();
			currentPositions = new HashMap<>();
			for(int i = 0; i < 10; i++){
				ActorRef<RacerCommand> slave = getContext().spawn(Racer.create(), "racer" + i);
				currentPositions.put(slave, 0);
				slave.tell(new StartRacerCommand(raceLength));
			}
			return Behaviors.withTimers(timer -> {
				timer.startTimerAtFixedRate(TIMER_KEY, new GetPositionsRaceControllerCommand(), Duration.ofSeconds(1));
				return this;
			});
		}).onMessage(RacerUpdateRaceControllerCommand.class, message -> {
			currentPositions.put(message.getSlave(), message.getPosition());
			return this;
		}).onMessage(GetPositionsRaceControllerCommand.class, message -> {
			for(ActorRef<RacerCommand> racerCommandActorRef : currentPositions.keySet()){
				racerCommandActorRef.tell(new PositionCommand(getContext().getSelf()));
				displayRace();
			}
			return this;
		}).build();
	}
}
