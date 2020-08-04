package com.akka.demo.a010;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import com.akka.demo.a010.config.ActorInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class MyRestController {

    @Autowired
    private ActorInjector actorInjector;

    @GetMapping("/hello-akka")
    public void greetings() {
        ActorSystem<AkkaSimpleBehaviour.Command> exampleActor = actorInjector.createAkkaSimpleBehaviour("anActor");
        exampleActor.tell(new AkkaSimpleBehaviour.TellMeSomething("hello"));
        exampleActor.tell(new AkkaSimpleBehaviour.TellMeSomething("Who are you"));
        exampleActor.tell(new AkkaSimpleBehaviour.TellMeSomething("create a child"));
        exampleActor.tell(new AkkaSimpleBehaviour.TellMeSomething("Here is some message"));

        CompletionStage<List<String>> result = AskPattern.ask(exampleActor, AkkaSimpleBehaviour.CollectTheResults::new, Duration.ofSeconds(10), exampleActor.scheduler());
        result.whenComplete((reply, failure) -> {
            if(reply != null){
                log.info("The system responds in time");
            } else {
                log.error("The system does not respond in time");
                exampleActor.terminate();
                throw new RuntimeException("The system does not respond in time");
            }
            exampleActor.terminate();
        });
        try{
            List<String> messages = result.toCompletableFuture().get();
            messages.forEach(log::info);
        } catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }

}
