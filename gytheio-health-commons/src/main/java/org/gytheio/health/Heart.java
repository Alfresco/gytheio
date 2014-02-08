package org.gytheio.health;

/**
 * Defines the object responsible for producing {@link Heartbeat}s
 * which let components monitor health of other components in the system.
 * 
 * @author rgauss
 */
public interface Heart
{
    /**
     * Produces a {@link Heartbeat} for health monitoring
     */
    public void beat();
    
}
