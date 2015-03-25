package org.tmf.dsmapi.sla.event;

import javax.ejb.Local;
import org.tmf.dsmapi.hub.Hub;

@Local
public interface SlaRESTEventPublisherLocal {

    public void publish(Hub hub, SlaEvent event);
    
}
