/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.commons.hub.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jackson.node.ObjectNode;
import tmf.org.dsmapi.commons.exceptions.BadUsageException;
import tmf.org.dsmapi.commons.exceptions.UnknownResourceException;
import tmf.org.dsmapi.commons.hub.AbstractEvent;
import tmf.org.dsmapi.commons.hub.Hub;
import tmf.org.dsmapi.commons.utils.JSONMarshaller;
import tmf.org.dsmapi.commons.utils.URIParser;

/**
 *
 * @author pierregauthier
 */
public abstract class HubFacadeREST {

    protected abstract HubFacade getHubManager();

    protected abstract AbstractFacade getEventManager();

    public HubFacadeREST() {
    }
    
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response create(Hub entity) throws BadUsageException {
        entity.setId(null);
        getHubManager().create(entity);
        Response response = Response.ok(entity).build();
        return response;
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) throws UnknownResourceException {
        getHubManager().remove(getHubManager().find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Response findById(@PathParam("id") String id, @Context UriInfo info) throws UnknownResourceException {
        Response response;
        Hub entity = this.getHubManager().find(id);

        // fields to filter view
        Set<String> fieldSet = URIParser.getFieldsSelection(info.getQueryParameters());


        if (fieldSet.isEmpty() || fieldSet.contains(URIParser.ALL_FIELDS)) {
            response = Response.ok(entity).build();
        } else {
            fieldSet.add(URIParser.ID_FIELD);
            ObjectNode node = JSONMarshaller.createNode(entity, fieldSet);
            response = Response.ok(node).build();
        }

        return response;
    }

    @GET
    @Produces({"application/json"})
    public List<Hub> findAll() {
        return getHubManager().findAll();
    }

    @POST
    @Path("listener")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public void publishEvent(AbstractEvent event) {

        System.out.println("Event =" + event);
        System.out.println("Event type = " + event.getEventType());
        System.out.println("Event Resource= " + event.getResource());

    }

    @GET
    @Path("listener")
    @Produces({"application/json"})
    public Response findEvents(@Context UriInfo info) {

        // search criteria
        MultivaluedMap<String, String> parameters = URIParser.getParameters(info);
        // fields to filter view
        Set<String> fieldsSelection = URIParser.getFieldsSelection(parameters);

        Set<AbstractEvent> resultList = findByCriteria(parameters);

        Response response;
        if (fieldsSelection.isEmpty() || fieldsSelection.contains(URIParser.ALL_FIELDS)) {
            response = Response.ok(resultList).build();
        } else {
            fieldsSelection.add(URIParser.ID_FIELD);
            List<ObjectNode> nodeList = JSONMarshaller.createNodes(resultList, fieldsSelection);
            response = Response.ok(nodeList).build();
        }
        return response;
    }

    // return Set of unique elements to avoid List with same elements in case of join
    private Set<AbstractEvent> findByCriteria(MultivaluedMap<String, String> criteria) {
        List<AbstractEvent> resultList = null;
        if (criteria != null && !criteria.isEmpty()) {
            resultList = getEventManager().findByCriteria(criteria, AbstractEvent.class);
        } else {
            resultList = getEventManager().findAll();
        }
        if (resultList == null) {
            return new LinkedHashSet<AbstractEvent>();
        } else {
            return new LinkedHashSet<AbstractEvent>(resultList);
        }
    }

    @GET
    @Path("mock")
    @Produces({"application/json"})
    public Hub hubMock() {
        Hub hub = new Hub();
        hub.setCallback("callback");
        hub.setQuery("queryString");
        hub.setId("id");
        return hub;
    }
}
