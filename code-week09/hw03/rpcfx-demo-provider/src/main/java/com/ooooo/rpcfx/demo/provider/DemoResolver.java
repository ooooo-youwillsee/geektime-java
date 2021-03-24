package com.ooooo.rpcfx.demo.provider;

import com.ooooo.rpcfx.api.RpcfxResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DemoResolver implements RpcfxResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    
    @Override
    public <T> T resolve(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
