package com.fs.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.deployVerticle(new HelloVerticle());
        //vertx.createHttpServer().requestHandler(req -> 
        //req.response().end("HTTP server")).listen(8080);
        Router router =Router.router(vertx);
        router.get("/home").handler(this::message);
        router.get("/home/:name").handler(this::messagectm);
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }
    public void message(RoutingContext ctx){
        //ctx.request().response().end("Hello from a route");
        vertx.eventBus().request("addr", "", reply -> {
            ctx.request().response().end((String)reply.result().body());
        });
    }
    public void messagectm(RoutingContext ctx){
        String name = ctx.pathParam("name");
        vertx.eventBus().request("named addr",name, reply -> {
            ctx.request().response().end((String)reply.result().body());
        });
    }
}
