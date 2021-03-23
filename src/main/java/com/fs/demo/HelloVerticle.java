package com.fs.demo;

import io.vertx.core.AbstractVerticle;

public class HelloVerticle extends AbstractVerticle{
    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("addr", msg->{
            msg.reply("in eventbus");
        });
        vertx.eventBus().consumer("named addr",msg ->{
            String name = (String)msg.body();
            msg.reply(String.format("Hello %s", name));
        });
    }
}
