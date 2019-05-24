package org.violet.roadMon.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import org.violet.roadMon.api.RoadMonService;

public class RoadMonModule extends AbstractModule implements ServiceGuiceSupport {

    @Override
    protected void configure() {
        bindServices(serviceBinding(RoadMonService.class,RoadMonServiceImpl.class));
    }
}
