/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;
import tmf.org.dsmapi.sla.hub.EventFacadeResource;
import tmf.org.dsmapi.sla.SlaResource;

/**
 *
 * @author pierregauthier
 */
@Stateless
public class SlaResourceFacade extends AbstractFacade<SlaResource> {

    @PersistenceContext(unitName = "DSSlaPU")
    private EntityManager em;
    @EJB
    EventFacadeResource eventManager;

    public SlaResourceFacade() {
        super(SlaResource.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventFacadeResource getEventManager() {
        return eventManager;
    }

}
