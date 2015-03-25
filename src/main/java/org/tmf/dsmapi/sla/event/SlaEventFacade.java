package org.tmf.dsmapi.sla.event;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.tmf.dsmapi.commons.facade.AbstractFacade;

@Stateless
public class SlaEventFacade extends AbstractFacade<SlaEvent>{
    
    @PersistenceContext(unitName = "DSSlaPU")
    private EntityManager em;
   

    
    /**
     *
     */
    public SlaEventFacade() {
        super(SlaEvent.class);
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
