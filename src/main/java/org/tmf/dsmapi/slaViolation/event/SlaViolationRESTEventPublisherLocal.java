package org.tmf.dsmapi.slaViolation.event;

import javax.ejb.Local;
import org.tmf.dsmapi.hub.Hub;

@Local
public interface SlaViolationRESTEventPublisherLocal {

    public void publish(Hub hub, SlaViolationEvent event);
    
}
