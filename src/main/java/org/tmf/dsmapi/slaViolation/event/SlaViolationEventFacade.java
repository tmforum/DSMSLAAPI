package org.tmf.dsmapi.slaViolation.event;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.tmf.dsmapi.commons.facade.AbstractFacade;

@Stateless
public class SlaViolationEventFacade extends AbstractFacade<SlaViolationEvent>{
    
    @PersistenceContext(unitName = "DSSlaPU")
    private EntityManager em;
   

    
    /**
     *
     */
    public SlaViolationEventFacade() {
        super(SlaViolationEvent.class);
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
