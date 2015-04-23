package org.tmf.dsmapi.sla.event;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.tmf.dsmapi.commons.exceptions.BadUsageException;
import org.tmf.dsmapi.sla.model.Sla;
import org.tmf.dsmapi.hub.Hub;
import org.tmf.dsmapi.hub.HubFacade;

/**
 *
 * Should be async or called with MDB
 */
@Stateless
@Asynchronous
public class SlaEventPublisher implements SlaEventPublisherLocal {

    @EJB
    HubFacade hubFacade;
    @EJB
    SlaEventFacade eventFacade;
    @EJB
    SlaRESTEventPublisherLocal restEventPublisherLocal;

    /** 
     * Add business logic below. (Right-click in editor and choose
     * "Insert Code > Add Business Method")
     * Access Hubs using callbacks and send to http publisher 
     *(pool should be configured around the RESTEventPublisher bean)
     * Loop into array of Hubs
     * Call RestEventPublisher - Need to implement resend policy plus eviction
     * Filtering is done in RestEventPublisher based on query expression
    */ 
    @Override
    public void publish(SlaEvent event) {
        try {
            eventFacade.create(event);
        } catch (BadUsageException ex) {
            Logger.getLogger(SlaEventPublisher.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Hub> hubList = hubFacade.findAll();
        Iterator<Hub> it = hubList.iterator();
        while (it.hasNext()) {
            Hub hub = it.next();
            restEventPublisherLocal.publish(hub, event);
        }
    }

    @Override
    public void createNotification(Sla bean, Date date) {
        SlaEvent event = new SlaEvent();
        event.setEventTime(date);
        event.setEventType(SlaEventTypeEnum.SlaCreationNotification);
        event.setResource(bean);
        publish(event);

    }

    @Override
    public void deletionNotification(Sla bean, Date date) {
        SlaEvent event = new SlaEvent();
        event.setEventTime(date);
        event.setEventType(SlaEventTypeEnum.SlaDeletionNotification);
        event.setResource(bean);
        publish(event);
    }
	
    @Override
    public void updateNotification(Sla bean, Date date) {
        SlaEvent event = new SlaEvent();
        event.setEventTime(date);
        event.setEventType(SlaEventTypeEnum.SlaUpdateNotification);
        event.setResource(bean);
        publish(event);
    }

    @Override
    public void valueChangedNotification(Sla bean, Date date) {
        SlaEvent event = new SlaEvent();
        event.setEventTime(date);
        event.setEventType(SlaEventTypeEnum.SlaValueChangeNotification);
        event.setResource(bean);
        publish(event);
    }
}
