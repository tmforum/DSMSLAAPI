/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.violation;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import tmf.org.dsmapi.commons.hub.service.AbstractEventPublisher;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;
import tmf.org.dsmapi.commons.hub.service.PostEventClient;
import tmf.org.dsmapi.sla.hub.EventFacadeViolation;

/**
 *
 * @author pierregauthier
 */
@Stateless
public class RESTEventPublisherViolation extends AbstractEventPublisher {

    @EJB
    EventFacadeViolation manager;

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
