package org.tmf.dsmapi.slaViolation.event;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.tmf.dsmapi.commons.exceptions.BadUsageException;
import org.tmf.dsmapi.sla.model.SlaViolation;
import org.tmf.dsmapi.hub.Hub;
import org.tmf.dsmapi.hub.HubFacade;

/**
 *
 * Should be async or called with MDB
 */
@Stateless
@Asynchronous
public class SlaViolationEventPublisher implements SlaViolationEventPublisherLocal {

    @EJB
    HubFacade hubFacade;
    @EJB
    SlaViolationEventFacade eventFacade;
    @EJB
    SlaViolationRESTEventPublisherLocal restEventPublisherLocal;

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
    public void publish(SlaViolationEvent event) {
        try {
            eventFacade.create(event);
        } catch (BadUsageException ex) {
            Logger.getLogger(SlaViolationEventPublisher.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Hub> hubList = hubFacade.findAll();
        Iterator<Hub> it = hubList.iterator();
        while (it.hasNext()) {
            Hub hub = it.next();
            restEventPublisherLocal.publish(hub, event);
        }
    }

    @Override
    public void createNotification(SlaViolation bean, Date date) {
        SlaViolationEvent event = new SlaViolationEvent();
        event.setEventTime(date);
        event.setEventType(SlaViolationEventTypeEnum.SlaViolationCreationNotification);
        event.setEvent(bean);
        publish(event);

    }

    @Override
    public void deletionNotification(SlaViolation bean, Date date) {
        SlaViolationEvent event = new SlaViolationEvent();
        event.setEventTime(date);
        event.setEventType(SlaViolationEventTypeEnum.SlaViolationDeletionNotification);
        event.setEvent(bean);
        publish(event);
    }
	
    @Override
    public void updateNotification(SlaViolation bean, Date date) {
        SlaViolationEvent event = new SlaViolationEvent();
        event.setEventTime(date);
        event.setEventType(SlaViolationEventTypeEnum.SlaViolationUpdateNotification);
        event.setEvent(bean);
        publish(event);
    }

    @Override
    public void valueChangedNotification(SlaViolation bean, Date date) {
        SlaViolationEvent event = new SlaViolationEvent();
        event.setEventTime(date);
        event.setEventType(SlaViolationEventTypeEnum.SlaViolationValueChangeNotification);
        event.setEvent(bean);
        publish(event);
    }
}
