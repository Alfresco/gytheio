package org.alfresco.messaging.camel.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Route builder for test messages
 * 
 * @author Ray Gauss II
 */
@Component
public class TestingRouteBuilder extends SpringRouteBuilder
{

    @Override
    public void configure() throws Exception
    {
        from("direct:alfresco.test.2").to("mock:result2");
    }

}
