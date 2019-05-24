package org.violet.driver.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import org.violet.driver.api.DriverService;

public class DriverModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(DriverService.class,DriverServiceImpl.class));
    }
}
