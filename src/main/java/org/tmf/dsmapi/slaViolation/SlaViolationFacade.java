package org.tmf.dsmapi.slaViolation;

import org.tmf.dsmapi.commons.facade.AbstractFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.tmf.dsmapi.commons.exceptions.BadUsageException;
import org.tmf.dsmapi.commons.exceptions.ExceptionType;
import org.tmf.dsmapi.commons.exceptions.UnknownResourceException;
import org.tmf.dsmapi.commons.utils.BeanUtils;
import org.tmf.dsmapi.sla.model.SlaViolation;
import org.tmf.dsmapi.slaViolation.event.SlaViolationEventPublisherLocal;

/**
 *
 * @author pierregauthier
 */
@Stateless
public class SlaViolationFacade extends AbstractFacade<SlaViolation> {

    @PersistenceContext(unitName = "DSSlaPU")
    private EntityManager em;
    @EJB
    SlaViolationEventPublisherLocal publisher;

    public SlaViolationFacade() {
        super(SlaViolation.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void checkCreation(SlaViolation newSlaViolation) throws BadUsageException, UnknownResourceException {

        if (newSlaViolation.getId() != null) {
            if (this.find(newSlaViolation.getId()) != null) {
                throw new BadUsageException(ExceptionType.BAD_USAGE_GENERIC,
                        "Duplicate Exception, SlaViolation with same id :" + newSlaViolation.getId() + " alreay exists");
            }
        }

    }
    
    public SlaViolation patchAttributs(long id, SlaViolation partialSlaViolation) throws UnknownResourceException, BadUsageException {
        SlaViolation currentSlaViolation = this.find(id);

        if (currentSlaViolation == null) {
            throw new UnknownResourceException(ExceptionType.UNKNOWN_RESOURCE);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.convertValue(partialSlaViolation, JsonNode.class);


        partialSlaViolation.setId(id);
        if (BeanUtils.patch(currentSlaViolation, partialSlaViolation, node)) {
//            publisher.valueChangedNotification(currentSlaViolation, new Date());
        }

        return currentSlaViolation;
    }

}
