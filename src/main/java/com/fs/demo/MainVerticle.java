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
        tracer = Configuration.fromEnv("sampleTracer").getTracer();
        vertx.eventBus().registerDefaultCodec(objOverEventBus.class,new GenericCodec<objOverEventBus>(objOverEventBus.class));
        vertx.deployVerticle(new HelloVerticle());
        Router router =Router.router(vertx);
        router.get("/").handler(this::message);
        vertx.createHttpServer().requestHandler(router).listen(9920);
    }
    public void message(RoutingContext ctx){
        try {Scope scope = (tracer.buildSpan("request Started at handler-1")).startActive(true);
            scope.span().setTag("any tag", "any message");
            objOverEventBus obj = new objOverEventBus(tracer, scope);
            vertx.eventBus().publish("spanTrace", obj);
            vertx.eventBus().request("1","",option,reply -> {
            ctx.request().response().end((String)reply.result().body());
        });
        scope.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
