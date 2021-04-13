package com.fs.demo;

import io.opentracing.Scope;
import io.opentracing.Tracer;

public class objOverEventBus {
    Tracer tracer;
    Scope scope;
    
    objOverEventBus(Tracer t, Scope s ){
        this.scope=s;
        this.tracer=t;

    }
    public Scope getScope(){
        return scope;
        
    }
    public Tracer getTracer(){
        return tracer;
    }
}
