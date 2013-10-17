package org.alfresco.messaging.camel;

import java.util.ArrayList;

import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Injects a specified route context into a specified Camel context
 * 
 * @author Ray Gauss II
 */
public class RouteLoader implements ApplicationContextAware, InitializingBean
{

    private ApplicationContext applicationContext;
    private String camelContextId;
    private String routeContextId;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    public void setCamelContextId(String camelContextId)
    {
        this.camelContextId = camelContextId;
    }

    public void setRouteContextId(String routeContextId)
    {
        this.routeContextId = routeContextId;
    }

    @SuppressWarnings("unchecked")
    public void addRoutesToCamelContext() throws Exception
    {
        ModelCamelContext modelCamelContext = (ModelCamelContext) applicationContext.getBean(camelContextId);
        ArrayList<RouteDefinition> routeDefinitions = (ArrayList<RouteDefinition>) applicationContext.getBean(routeContextId);
        modelCamelContext.addRouteDefinitions(routeDefinitions);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        addRoutesToCamelContext();
    }


}
