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

    @Override
    public void create(Sla entity) throws BadUsageException {
        if (entity.getId() != null) {
            throw new BadUsageException(ExceptionType.BAD_USAGE_GENERIC, "While creating Sla, id must be null");
        }

        super.create(entity);
    }

    public Sla updateAttributs(long id, Sla partialSla) throws UnknownResourceException, BadUsageException {
        Sla currentSla = this.find(id);

        if (currentSla == null) {
            throw new UnknownResourceException(ExceptionType.UNKNOWN_RESOURCE);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.convertValue(partialSla, JsonNode.class);


        partialSla.setId(id);
        if (BeanUtils.patch(currentSla, partialSla, node)) {
            publisher.valueChangedNotification(currentSla, new Date());
        }

        return currentSla;
    }

}
