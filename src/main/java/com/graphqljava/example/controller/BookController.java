package com.graphqljava.example.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@Slf4j
public class BookController {

    @Autowired
    private GraphQL graphql;

    @RequestMapping("/sub")
    public String getSubscribe() {

        log.info("=============>>1>=======");

        ExecutionResult result = graphql.execute("subscription bookById {\n" +
                "  bookById(id: \"book-1\") {\n" +
                "    id\n" +
                "    name\n" +
                "  }\n" +
                "}");

        log.info("=============>>2>=======");

        Publisher<ExecutionResult> publisher = result.getData();

        log.info("=============>>3>=======");
        AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();
        log.info("=============>>4>=======");

        publisher.subscribe(new Subscriber<ExecutionResult>() {


            @Override
            public void onSubscribe(Subscription s) {
                log.info("=============>>5>=======");
                subscriptionRef.set(s);
                s.request(1);
            }

            @Override
            public void onNext(ExecutionResult executionResult) {
                log.info("=============>>>======="+executionResult.getData());
                Map<String,String> map = executionResult.getData();

                log.info("=============>>>======="+map);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

                log.info("================Complete=================");
            }
        });

        return "Subscription Complete";

    }

    @RequestMapping("/")
    public String getMessage() {
        log.info("==>>"+ graphql);

        ExecutionResult result = graphql.execute( "{\n" +
                "  bookById(id: \"book-1\") {\n" +
                "    id\n" +
                "    name\n" +
                "  }\n" +
                "}");

        Map<String, String> map = result.getData();

        log.info("Map>>"+map);

        return "Message sent";
    }

}
