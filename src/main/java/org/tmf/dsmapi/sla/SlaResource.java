/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmf.dsmapi.sla;

//import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.tmf.dsmapi.commons.exceptions.BadUsageException;
import org.tmf.dsmapi.commons.exceptions.UnknownResourceException;
import org.tmf.dsmapi.commons.jaxrs.PATCH;
import org.tmf.dsmapi.commons.utils.BeanUtils;
import org.tmf.dsmapi.commons.utils.Jackson;
import org.tmf.dsmapi.commons.utils.URIParser;
import org.tmf.dsmapi.sla.model.Sla;
import org.tmf.dsmapi.sla.event.SlaEventPublisherLocal;
import org.tmf.dsmapi.sla.event.SlaEvent;
import org.tmf.dsmapi.sla.event.SlaEventFacade;

@Stateless
@Path("/slaManagement/v2/sla")
public class SlaResource {

    @EJB
    SlaFacade slaFacade;
    @EJB
    SlaEventFacade eventFacade;
    @EJB
    SlaEventPublisherLocal publisher;

    public SlaResource() {
    }

    /**
     * Test purpose only
     */
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response create(Sla entity, @Context UriInfo info) throws BadUsageException, UnknownResourceException {
        slaFacade.checkCreation(entity);
        slaFacade.create(entity);
        entity.setHref(info.getAbsolutePath()+ "/" + Long.toString(entity.getId()));
        slaFacade.edit(entity);
//        publisher.createNotification(entity, new Date());
        Sla entityCreated = null;
        try {
           entityCreated  =   slaFacade.find(entity.getId());
        } catch (UnknownResourceException ex) {
            Logger.getLogger(SlaResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        // 201
        Response response = Response.status(Response.Status.CREATED).entity(entityCreated).build();
        return response;
    }

    @GET
    @Produces({"application/json"})
    public Response find(@Context UriInfo info) throws BadUsageException {

        // search queryParameters
        MultivaluedMap<String, String> queryParameters = info.getQueryParameters();

        Map<String, List<String>> mutableMap = new HashMap();
        for (Map.Entry<String, List<String>> e : queryParameters.entrySet()) {
            mutableMap.put(e.getKey(), e.getValue());
        }

        // fields to filter view
        Set<String> fieldSet = URIParser.getFieldsSelection(mutableMap);

        Set<Sla> resultList = findByCriteria(mutableMap);

        Response response;
        if (fieldSet.isEmpty() || fieldSet.contains(URIParser.ALL_FIELDS)) {
            response = Response.ok(resultList).build();
        } else {
            fieldSet.add(URIParser.ID_FIELD);
            List<ObjectNode> nodeList = Jackson.createNodes(resultList, fieldSet);
            response = Response.ok(nodeList).build();
        }
        return response;
    }

    // return Set of unique elements to avoid List with same elements in case of join
    private Set<Sla> findByCriteria(Map<String, List<String>> criteria) throws BadUsageException {

        List<Sla> resultList = null;
        if (criteria != null && !criteria.isEmpty()) {
            resultList = slaFacade.findByCriteria(criteria, Sla.class);
        } else {
            resultList = slaFacade.findAll();
        }
        if (resultList == null) {
            return new LinkedHashSet<Sla>();
        } else {
            return new LinkedHashSet<Sla>(resultList);
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Response get(@PathParam("id") long id, @Context UriInfo info) throws UnknownResourceException {

        // search queryParameters
        MultivaluedMap<String, String> queryParameters = info.getQueryParameters();

        Map<String, List<String>> mutableMap = new HashMap();
        for (Map.Entry<String, List<String>> e : queryParameters.entrySet()) {
            mutableMap.put(e.getKey(), e.getValue());
        }

        // fields to filter view
        Set<String> fieldSet = URIParser.getFieldsSelection(mutableMap);

        Sla sla = slaFacade.find(id);
        Response response;

        // If the result list (list of bills) is not empty, it conains only 1 unique bill
        if (sla != null) {
            // 200
            if (fieldSet.isEmpty() || fieldSet.contains(URIParser.ALL_FIELDS)) {
                response = Response.ok(sla).build();
            } else {
                fieldSet.add(URIParser.ID_FIELD);
                ObjectNode node = Jackson.createNode(sla, fieldSet);
                response = Response.ok(node).build();
            }
        } else {
            // 404 not found
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response update(@PathParam("id") long id, Sla entity) throws UnknownResourceException {
        Response response = null;
        Sla sla = slaFacade.find(id);
        if (sla != null) {
            entity.setId(id);
            slaFacade.edit(entity);
//            publisher.valueChangedNotification(entity, new Date());
            // 201 OK + location
            response = Response.status(Response.Status.CREATED).entity(entity).build();

        } else {
            // 404 not found
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    /**
     *
     * For test purpose only
     *
     * @param id
     * @return
     * @throws UnknownResourceException
     */
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) throws UnknownResourceException {
            Sla entity = slaFacade.find(id);

            // Event deletion
//            publisher.deleteNotification(entity, new Date());
            try {
                //Pause for 4 seconds to finish notification
                Thread.sleep(4000);
            } catch (InterruptedException ex) {
                // Log someting to the console (should never happen)
            }
            // remove event(s) binding to the resource
            List<SlaEvent> events = eventFacade.findAll();
            for (SlaEvent event : events) {
                if (event.getResource().getId().equals(id)) {
                    eventFacade.remove(event.getId());
                }
            }
            //remove resource
            slaFacade.remove(id);

            // 200 
            Response response = Response.ok(entity).build();
            return response;
    }

    @PATCH
    @Path("{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response patch(@PathParam("id") long id, Sla partialsla) throws BadUsageException, UnknownResourceException {
        Response response = null;
        
        Sla currentSla = slaFacade.updateAttributs(id, partialsla);

        // 201 OK + location
        response = Response.status(Response.Status.CREATED).entity(currentSla).build();

        return response;
    }
}
