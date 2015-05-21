package org.tmf.dsmapi.sla;

import java.util.Date;
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
import org.tmf.dsmapi.sla.event.SlaEventPublisherLocal;
import org.tmf.dsmapi.sla.model.Sla;

/**
 *
 * @author pierregauthier
 */
@Stateless
public class SlaFacade extends AbstractFacade<Sla> {

    @PersistenceContext(unitName = "DSSlaPU")
    private EntityManager em;
    @EJB
    SlaEventPublisherLocal publisher;

    public SlaFacade() {
        super(Sla.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void checkCreation(Sla newSla) throws BadUsageException, UnknownResourceException {

        if (newSla.getId() != null) {
            if (this.find(newSla.getId()) != null) {
                throw new BadUsageException(ExceptionType.BAD_USAGE_GENERIC,
                        "Duplicate Exception, Product with same id :" + newSla.getId() + " alreay exists");
            }
        }

        if (null == newSla.getName()
                || newSla.getName().isEmpty()) {
            throw new BadUsageException(ExceptionType.BAD_USAGE_MANDATORY_FIELDS, "name is mandatory");
        }
    }
    
    

    public Sla patchAttributs(long id, Sla partialSla) throws UnknownResourceException, BadUsageException {
        Sla currentSla = this.find(id);

        if (currentSla == null) {
            throw new UnknownResourceException(ExceptionType.UNKNOWN_RESOURCE);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.convertValue(partialSla, JsonNode.class);


        partialSla.setId(id);
        if (BeanUtils.patch(currentSla, partialSla, node)) {
//            publisher.valueChangedNotification(currentSla, new Date());
        }

        return currentSla;
    }

}
