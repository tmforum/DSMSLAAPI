/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.commons.hub.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tmf.org.dsmapi.commons.exceptions.BadUsageException;
import tmf.org.dsmapi.commons.hub.AbstractEvent;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.commons.hub.Hub;
import tmf.org.dsmapi.sla.hub.EventResource;


/**
 *  private String reason;
 *  private Date date;
 * @author pierregauthier
 */
     
public abstract class AbstractPublisher {

    protected abstract HubFacade getHubFacade();

    protected abstract AbstractFacade getEventFacade();

    protected abstract AbstractEventPublisher getRestEventPublisher();

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    //Access Hubs using callbacks and send to http publisher 
    //(pool should be configured around the RESTEventPublisher bean)
    //Loop into array of Hubs
    //Call RestEventPublisher - Need to implement resend policy plus eviction
    //Filtering is done in RestEventPublisher based on query expression
    

    public void publish(AbstractEvent event) {
        System.out.println("Sending Event");

        String id = null;

        if (event instanceof AbstractEvent) {
            event.setId(null);
            try {
                getEventFacade().create(event);
            } catch (BadUsageException ex) {
                // TODO use LOGGER
                Logger.getLogger(AbstractPublisher.class.getName()).log(Level.SEVERE, null, ex);
            }
            id = event.getId();
        }

        List<Hub> hubList = getHubFacade().findAll();
        Iterator<Hub> it = hubList.iterator();
        while (it.hasNext()) {
            Hub hub = it.next();
            getRestEventPublisher().publish(hub, event);
            //Thread.currentThread().sleep(1000);
        }
        System.out.println("Sending Event After, id of event : " + id);
    }

    public void publishCreateNotification(AbstractEvent absEvent) {
        absEvent.setEventType(EventTypeEnum.CreateNotification);
        publish(absEvent);
    }

    public void publishStatusChangedNotification(AbstractEvent absEvent) {
        absEvent.setEventType(EventTypeEnum.StatusChangedNotification);
        publish(absEvent);
    }

    public void publishChangedNotification(AbstractEvent absEvent) {
        absEvent.setEventType(EventTypeEnum.ValueChangeNotification);
        publish(absEvent);
    }

    public void publishRemoveNotification(AbstractEvent absEvent) {
        absEvent.setEventType(EventTypeEnum.RemoveNotification);
        publish(absEvent);
    }

}
