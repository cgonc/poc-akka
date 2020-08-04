package com.akka.demo.a010;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedAbstractActor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component ("simpleSpringActor")
@Scope ("prototype")
public class SimpleSpringActor extends UntypedAbstractActor {

	@Autowired
	private ASimpleSpringService aSimpleSpringService;

	@Override
	public void onReceive(Object message) {
		if(message instanceof String){
			aSimpleSpringService.logSomething(message.toString());
		}
	}
}
