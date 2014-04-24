/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.commons.hub.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tmf.org.dsmapi.commons.hub.Hub;

/**
 *
 * @author pierregauthier
 */
@Stateless
public class HubFacade extends AbstractFacade<Hub>{
    
    @PersistenceContext(unitName = "DSSlaPU")
    private EntityManager em;
   

    
    /**
     *
     */
    public HubFacade() {
        super(Hub.class);
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
