/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.hub;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;

/**
 *
 * @author pierregauthier
 */
@Stateless
public class EventFacadeResource extends AbstractFacade<EventResource> {
    
    @PersistenceContext(unitName = "DSSlaPU")
    private EntityManager em;

    public EventFacadeResource() {
        super(EventResource.class);
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }



}
