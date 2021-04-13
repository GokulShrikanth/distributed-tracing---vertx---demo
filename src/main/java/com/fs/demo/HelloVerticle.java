package com.fs.demo;

import io.vertx.core.AbstractVerticle;
import io.opentracing.Scope;
import io.opentracing.Span;

public class HelloVerticle extends AbstractVerticle{
    public objOverEventBus obj;
    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("spanTrace", context -> {
            objOverEventBus msg = (objOverEventBus) context.body();
            Span parent=msg.scope.span();
            Scope child=(msg.tracer.buildSpan("inside HelloVerticle")).asChildOf(parent).startActive(true);
            child.span().setTag("any tag", "any message");
            //any operations

            child.close();
       });
        vertx.eventBus().consumer("1", context->{
            context.reply("succesfull");
        });
    }
}
