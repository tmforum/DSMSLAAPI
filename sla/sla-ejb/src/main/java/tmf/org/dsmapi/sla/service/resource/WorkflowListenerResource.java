/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tmf.org.dsmapi.commons.exceptions.UnknownResourceException;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.sla.SlaResource;
import tmf.org.dsmapi.sla.SlaState;
import tmf.org.dsmapi.sla.hub.EventResource;

/**
 *
 * @author pierregauthier
 */
@Stateless
@Asynchronous
public class WorkflowListenerResource implements WorkflowListenerResourceLocal {

    @EJB
    SlaResourceFacade srFacade;
    @EJB
    PublisherResource publisher;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void update(SlaResource eventClass, EventTypeEnum eventType, String reason, SlaState status) {
        try {
            eventClass.setStatus(status);
            srFacade.edit(eventClass);

            //event publication
            EventResource event = new EventResource();
            event.setResource(eventClass);
            event.setDate(new Date());
            event.setReason(reason);

            switch (eventType) {
                case CreateNotification:
                    publisher.publishCreateNotification(event);
                    break;
                case StatusChangedNotification:
                    publisher.publishStatusChangedNotification(event);
                    break;
            }
        } catch (UnknownResourceException ex) {
            Logger.getLogger(WorkflowListenerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
