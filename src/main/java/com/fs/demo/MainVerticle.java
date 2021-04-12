package com.fs.demo;

import io.jaegertracing.Configuration;
import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {
    DeliveryOptions option = new DeliveryOptions().setTracingPolicy(TracingPolicy.ALWAYS);
    public Tracer tracer;
    @Override
    public void start() {
        System.setProperty("JAEGER_REPORTER_LOG_SPANS", "true");
        System.setProperty("JAEGER_SAMPLER_TYPE", "const");
        System.setProperty("JAEGER_SAMPLER_PARAM", "1");
        tracer = Configuration.fromEnv("Demo").getTracer();

        vertx.deployVerticle(new HelloVerticle());
        Router router =Router.router(vertx);
        router.get("/").handler(this::message);
        router.get("/:name").handler(this::messagectm);
        vertx.createHttpServer().requestHandler(router).listen(8090);
    }
    public void message(RoutingContext ctx){
        Scope scope = (tracer.buildSpan("requestStarted to demo listener 1")).startActive(true);
            scope.span().setTag("handler 1", "sample");
        vertx.eventBus().request("1", "",option,reply -> {
            ctx.request().response().end((String)reply.result().body());
        });
        scope.close();
    }
    public void messagectm(RoutingContext ctx){
        Scope scope = (tracer.buildSpan("requestStarted to demo listener 2")).startActive(true);
            scope.span().setTag("handler 2", "sample");
        String name = ctx.pathParam("name");
        vertx.eventBus().request("2",name,option,reply -> {
            ctx.request().response().end((String)reply.result().body());
        });
        scope.close();
    }
}
