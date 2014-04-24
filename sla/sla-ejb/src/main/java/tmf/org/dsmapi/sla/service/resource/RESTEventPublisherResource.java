/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;

import tmf.org.dsmapi.sla.hub.EventFacadeResource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import tmf.org.dsmapi.commons.hub.service.AbstractEventPublisher;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;
import tmf.org.dsmapi.commons.hub.service.PostEventClient;

/**
 *
 * @author pierregauthier
 */
@Stateless
public class RESTEventPublisherResource extends AbstractEventPublisher {

    @PersistenceContext(unitName = "DSSlaPU")

    @EJB
    EventFacadeResource manager;

    @EJB
    PostEventClient client;

    @Override
    protected PostEventClient getPostEventClient() {
        return client;
    }

    @Override
    protected AbstractFacade getManager() {
        return manager;
    }

    
}
