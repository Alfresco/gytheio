package org.gytheio.content.dropwizard.health;

import org.gytheio.content.hash.BaseContentHashComponent;
import org.gytheio.messaging.amqp.AmqpDirectEndpoint;

import com.codahale.metrics.health.HealthCheck;

/**
 * Health check for a hash component
 *
 */
public class HashHealthCheck extends HealthCheck
{
    private BaseContentHashComponent component;
    private AmqpDirectEndpoint endpoint;

    public HashHealthCheck(BaseContentHashComponent component, AmqpDirectEndpoint endpoint)
    {
        this.component = component;
        this.endpoint = endpoint;
    }
    
    @Override
    protected Result check() throws Exception
    {
        if (endpoint == null || !endpoint.isInitialized())
        {
            return Result.unhealthy("AMQP endpoint could not be initialized, "
                    + "please check the logs for additional information");
        }
        return Result.healthy();
    }

}
