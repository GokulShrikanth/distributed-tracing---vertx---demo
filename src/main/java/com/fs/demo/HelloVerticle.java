package com.fs.demo;

import io.vertx.core.AbstractVerticle;

public class HelloVerticle extends AbstractVerticle{
    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("1", msg->{
            msg.reply("in eventbus");
        });
        vertx.eventBus().consumer("2",msg ->{
            String name = (String)msg.body();
            msg.reply(String.format("Hello %s", name));
        });
    }
}
