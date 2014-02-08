package org.gytheio.health;

/**
 * Defines an object responsible for listening for the heartbeat.
 * Implementations may utilize a {@link HeartbeatDao} to persist the data.
 * 
 * @author rgauss
 */
public interface HeartbeatMonitor
{

    /**
     * Called when a {@link Heartbeat} message is routed to the
     * monitor.
     * 
     * @param message
     */
    public void onReceive(Object message);
    
}
