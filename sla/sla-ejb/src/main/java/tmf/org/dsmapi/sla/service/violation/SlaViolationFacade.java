/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.violation;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;
import tmf.org.dsmapi.sla.SlaViolation;
import tmf.org.dsmapi.sla.hub.EventFacadeViolation;

/**
 *
 * @author pierregauthier
 */
@Stateless
public class SlaViolationFacade extends AbstractFacade<SlaViolation> {

    @PersistenceContext(unitName = "DSSlaPU")
    private EntityManager em;
    @EJB
    EventFacadeViolation eventManager;

    public SlaViolationFacade() {
        super(SlaViolation.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
