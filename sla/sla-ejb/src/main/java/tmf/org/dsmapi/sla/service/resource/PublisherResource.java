/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;

import java.util.Date;
import tmf.org.dsmapi.sla.hub.EventFacadeResource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import tmf.org.dsmapi.sla.hub.EventResource;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.commons.hub.service.AbstractEventPublisher;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;
import tmf.org.dsmapi.commons.hub.service.AbstractPublisher;
import tmf.org.dsmapi.commons.hub.service.HubFacade;

/**
 *
 * @author pierregauthier should be async or called with MDB
 */
//@Asynchronous bug in 7.3
@Asynchronous
@Stateless
//public class PublisherResource extends PublisherLocal {
public class PublisherResource extends AbstractPublisher {

    @PersistenceContext(unitName = "DSSlaPU")
    @EJB
    HubFacade hubFacade;
    @EJB
    EventFacadeResource eventFacade;
    @EJB
    RESTEventPublisherResource restEventPublisher;

    @Override
    protected HubFacade getHubFacade() {
        return hubFacade;
    }

    @Override
    protected AbstractFacade getEventFacade() {
        return eventFacade;
    }

    @Override
    protected AbstractEventPublisher getRestEventPublisher() {
        return restEventPublisher;
    }

}
