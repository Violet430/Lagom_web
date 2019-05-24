package org.violet.illegal.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import org.violet.illegal.api.IllegalService;

public class IllegalModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(IllegalService.class,IllegalServiceImpl.class));
    }
}
