package org.alfresco.messaging.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests routes defined via Spring with package scan
 *
 * @author Ray Gauss II
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-messaging-context.xml")
public class RouteTest
{
    @EndpointInject(uri = "mock:result1")
    protected MockEndpoint resultEndpoint1;
    
    @EndpointInject(uri = "mock:result2")
    protected MockEndpoint resultEndpoint2;
    
    @EndpointInject(uri = "mock:dlq")
    protected MockEndpoint dlqEndpoint;
    
    @Autowired
    protected CamelContext camelContext;
    
    @Produce(uri = "direct:alfresco.test.1")
    protected ProducerTemplate template1;
    
    @Produce(uri = "direct:alfresco.test.2")
    protected ProducerTemplate template2;
    
    @Produce(uri = "direct:alfresco.default")
    protected ProducerTemplate template3;

    @Test
    public void testMessageRouteXmlDefined() throws Exception {
        String expectedBody = "<matched.>";
 
        resultEndpoint1.expectedBodiesReceived(expectedBody);
 
        template1.sendBody(expectedBody);
 
        resultEndpoint1.assertIsSatisfied();
    }
    
    @Test
    public void testMessageRoutePackageDefined() throws Exception {
        String expectedBody = "<matched.>";
 
        resultEndpoint2.expectedBodiesReceived(expectedBody);
 
        template2.sendBody(expectedBody);
 
        resultEndpoint2.assertIsSatisfied();
    }
    
    @Test
    public void testMessageRouteXmlOverride() throws Exception {
        String expectedBody = "<matched.>";
 
        dlqEndpoint.expectedBodiesReceived(expectedBody);
 
        template3.sendBody(expectedBody);
 
        dlqEndpoint.assertIsSatisfied();
    }
}
