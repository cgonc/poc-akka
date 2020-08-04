package com.akka.demo.a010.config;

import akka.actor.typed.ActorSystem;
import com.akka.demo.a010.ASimpleSpringService;
import com.akka.demo.a010.AkkaSimpleBehaviour;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class ActorInjector {

    @Autowired
    private ASimpleSpringService aSimpleSpringService;

    @Autowired
    private ActorInjector _self;

    public ActorSystem<AkkaSimpleBehaviour.Command> createAkkaSimpleBehaviour(String actorName) {
        return ActorSystem.create(AkkaSimpleBehaviour.create(_self), actorName);
    }

}
