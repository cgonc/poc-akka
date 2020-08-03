package com.akka.demo.a005;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class RaceController extends AbstractBehavior<RaceController.Command> {

	public interface Command extends Serializable { }

	public static class StartCommand implements Command {
		private static final long serialVersionUID = -8775807999675952049L;
	}

	@AllArgsConstructor
	public static class RacerUpdateCommand implements Command {
		private static final long serialVersionUID = 1991780396587728039L;
		@Getter
		private final ActorRef<Racer.Command> racer;
		@Getter
		private final int position;
	}

	private static class GetPositionsCommand implements Command {
		private static final long serialVersionUID = 1L;
	}

	private RaceController(ActorContext<Command> context) {
		super(context);
	}

	public static Behavior<Command> create() {
		return Behaviors.setup(RaceController::new);
	}

	private Map<ActorRef<Racer.Command>, Integer> currentPositions;
	private long start;
	private final int raceLength = 100;
	private final String TIMER_KEY = "racerTimeKey";

	private void displayRace() {
		int displayLength = 160;
		for(int i = 0; i < 50; ++i) System.out.println();
		System.out.println("Race has been running for " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
		System.out.println("    " + new String(new char[displayLength]).replace('\0', '='));
		int i = 0;
		for(ActorRef<Racer.Command> racer : currentPositions.keySet()){
			System.out.println(i + " : " + new String(new char[currentPositions.get(racer) * displayLength / 100]).replace('\0', '*'));
			i++;
		}
	}

	public Receive<Command> createReceive() {
		return newReceiveBuilder().onMessage(StartCommand.class, message -> {
			start = System.currentTimeMillis();
			currentPositions = new HashMap<>();
			for(int i = 0; i < 10; i++){
				ActorRef<Racer.Command> racer = getContext().spawn(Racer.create(), "racer" + i);
				currentPositions.put(racer, 0);
				racer.tell(new Racer.StartCommand(raceLength));
			}
			return Behaviors.withTimers(timer -> {
				timer.startTimerAtFixedRate(TIMER_KEY, new GetPositionsCommand(), Duration.ofSeconds(1));
				return this;
			});
		}).onMessage(GetPositionsCommand.class, message -> {
			for(ActorRef<Racer.Command> racer : currentPositions.keySet()){
				racer.tell(new Racer.PositionCommand(getContext().getSelf()));
				displayRace();
			}
			return this;
		}).onMessage(RacerUpdateCommand.class, message -> {
			currentPositions.put(message.getRacer(), message.getPosition());
			return this;
		}).build();
	}

}
