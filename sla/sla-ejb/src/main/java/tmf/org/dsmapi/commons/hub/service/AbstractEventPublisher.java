/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.commons.hub.service;

import java.util.List;
import java.util.Set;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jackson.node.ObjectNode;
import tmf.org.dsmapi.commons.hub.AbstractEvent;
import tmf.org.dsmapi.commons.hub.Hub;
import tmf.org.dsmapi.commons.utils.JSONMarshaller;
import tmf.org.dsmapi.commons.utils.URIParser;

/**
 *
 * @author pierregauthier
 */
public abstract class AbstractEventPublisher {

    protected abstract PostEventClient getPostEventClient();

    protected abstract AbstractFacade getManager();

    public void publish(Hub hub, AbstractEvent event) {

        String query = hub.getQuery();
        if (query != null && query.length() > 0) {
            MultivaluedMap<String, String> queryMap = URIParser.getParameters(query);
            Set<String> fieldSet = URIParser.getFieldsSelection(queryMap);
            if (!fieldSet.isEmpty()) {
                fieldSet.add(URIParser.ID_FIELD);
            }
            queryMap.putSingle("id", event.getId());
            List results = getManager().findByCriteria(queryMap, event.getClass());
            if (results != null && !results.isEmpty()) {
                if (!fieldSet.isEmpty()) {
                    fieldSet.add("reason");
                    fieldSet.add("date");
                    fieldSet.add("eventType");
                    ObjectNode node = JSONMarshaller.createNode(event, fieldSet);
                    getPostEventClient().publishEvent(hub.getCallback(), node);
                } else {
                    getPostEventClient().publishEvent(hub.getCallback(), event);
                }
            }
        } else {
            getPostEventClient().publishEvent(hub.getCallback(), event);
        }

        System.out.println("Sending Event");
    }
}
