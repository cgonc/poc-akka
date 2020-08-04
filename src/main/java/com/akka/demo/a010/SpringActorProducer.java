package com.akka.demo.a010;

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

@SuppressWarnings ("all")
public class SpringActorProducer implements IndirectActorProducer {

	private final ApplicationContext applicationContext;
	private final String actorBeanName;
	private final Object[] args;

	public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName, Object... args) {
		this.applicationContext = applicationContext;
		this.actorBeanName = actorBeanName;
		this.args = args;
	}

	public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName) {
		this.applicationContext = applicationContext;
		this.actorBeanName = actorBeanName;
		this.args = null;
	}

	@Override
	public Actor produce() {
		if(args == null){
			return (Actor) applicationContext.getBean(actorBeanName);
		} else {
			return (Actor) applicationContext.getBean(actorBeanName, args);
		}
	}

	@Override
	public Class<? extends Actor> actorClass() {
		return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
	}
}
