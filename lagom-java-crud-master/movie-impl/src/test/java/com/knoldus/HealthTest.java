package com.knoldus;

import akka.Done;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.startServer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HealthTest {
    
    private static ServiceTest.TestServer testServer;
    
    @BeforeClass
    public static void setUp() {
        testServer = startServer(defaultSetup().withCassandra(true).withCluster(true));
    }
    
    @AfterClass
    public static void tearDown() {
        if (testServer != null) {
            testServer.stop();
            testServer = null;
        }
    }
    
    @Test
    public void testHealthService() throws Exception {
        MovieService healthService = testServer.client(MovieService.class);
        Done actualResult = healthService.health().invoke().toCompletableFuture().get(10,
                TimeUnit.SECONDS);
        
        Done expectedResult = Done.getInstance();
        assertEquals(expectedResult,actualResult);
    }
}
