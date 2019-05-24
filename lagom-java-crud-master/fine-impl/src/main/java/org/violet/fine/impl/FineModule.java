package org.violet.fine.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import org.violet.fine.api.FineService;

public class FineModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(FineService.class,FineServiceImpl.class));
    }
}
