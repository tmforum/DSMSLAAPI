/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.violation;

import tmf.org.dsmapi.sla.hub.EventFacadeViolation;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tmf.org.dsmapi.commons.hub.service.AbstractEventPublisher;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;
import tmf.org.dsmapi.commons.hub.service.AbstractPublisher;
import tmf.org.dsmapi.commons.hub.service.HubFacade;

/**
 *
 * @author pierregauthier should be async or called with MDB
 */
@Stateless
//@Asynchronous bug in 7.3
@Asynchronous
public class PublisherViolation extends AbstractPublisher {

    @EJB
    HubFacade hubFacade;
    @EJB
    EventFacadeViolation hubEventFacade;
    @EJB
    RESTEventPublisherViolation restEventPublisher;

    @Override
    protected HubFacade getHubFacade() {
        return hubFacade;
    }

    @Override
    protected AbstractFacade getEventFacade() {
        return hubEventFacade;
    }

    @Override
    protected AbstractEventPublisher getRestEventPublisher() {
        return restEventPublisher;
    }

}
