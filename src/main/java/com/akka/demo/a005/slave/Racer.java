package com.akka.demo.a005.slave;

import java.util.Random;

import com.akka.demo.a005.master.RacerUpdateRaceControllerCommand;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Racer extends AbstractBehavior<RacerCommand> {

	private int raceLength;
	private final double defaultAverageSpeed = 48.2;
	private int averageSpeedAdjustmentFactor;
	private Random random;

	private double currentSpeed = 0;
	private int currentPosition = 0;


	private double getMaxSpeed() {
		return defaultAverageSpeed * (1 + ((double) averageSpeedAdjustmentFactor / 100));
	}

	private double getDistanceMovedPerSecond() {
		return currentSpeed * 1000 / 3600;
	}

	private void determineNextSpeed() {
		if(currentPosition < (raceLength / 4)){
			currentSpeed = currentSpeed + (((getMaxSpeed() - currentSpeed) / 10) * random.nextDouble());
		} else {
			currentSpeed = currentSpeed * (0.5 + random.nextDouble());
		}

		if(currentSpeed > getMaxSpeed()){
			currentSpeed = getMaxSpeed();
		}

		if(currentSpeed < 5){
			currentSpeed = 5;
		}

		if(currentPosition > (raceLength / 2) && currentSpeed < getMaxSpeed() / 2){
			currentSpeed = getMaxSpeed() / 2;
		}
	}

	private Racer(ActorContext<RacerCommand> context) {
		super(context);
	}

	public static Behavior<RacerCommand> create() {
		return Behaviors.setup(Racer::new);
	}

	@Override
	public Receive<RacerCommand> createReceive() {

		return newReceiveBuilder().onMessage(StartRacerCommand.class, message -> {
			raceLength = message.getRaceLength();
			random = new Random();
			averageSpeedAdjustmentFactor = random.nextInt(30) - 10;
			return this;
		}).onMessage(PositionCommand.class, message -> {
			determineNextSpeed();
			currentPosition += getDistanceMovedPerSecond();
			if(currentPosition > raceLength){
				currentPosition = raceLength;
			}
			message.getMaster().tell(new RacerUpdateRaceControllerCommand(getContext().getSelf(), currentPosition));
			return this;
		}).build();
	}
}
