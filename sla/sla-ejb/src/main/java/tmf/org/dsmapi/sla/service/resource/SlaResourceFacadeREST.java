/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import tmf.org.dsmapi.commons.utils.JSONMarshaller;
import tmf.org.dsmapi.sla.InvolvedPartie;
import tmf.org.dsmapi.sla.Rule;
import tmf.org.dsmapi.sla.SlaResource;
import tmf.org.dsmapi.sla.SlaState;
import tmf.org.dsmapi.sla.Template;
import tmf.org.dsmapi.sla.ValidityPeriod;
import tmf.org.dsmapi.commons.utils.URIParser;
import tmf.org.dsmapi.sla.Report;
import tmf.org.dsmapi.sla.hub.EventResource;

/**
 *
 * @author pierregauthier
 */
@Stateless
@Path("slaResource")
public class SlaResourceFacadeREST {

    @EJB
    WorkFlowResourceLocal workflow;
    @EJB
    PublisherResource publisher;
    @EJB
    SlaResourceFacade manager;

    public SlaResourceFacadeREST() {
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response create(SlaResource entity) {
        try {
            entity.setId(null);
            entity.setStatus(SlaState.OPEN_RUNNING);
            manager.create(entity);

            //event publication
            EventResource event = new EventResource();
            event.setResource(entity);
            event.setDate(new Date());
            event.setReason("New SLAResource");
            publisher.publishCreateNotification(event);


            Response response = Response.ok(entity).build();
            return response;
        } catch (BadUsageException ex) {
            Logger.getLogger(SlaResourceFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        }
    }

    @PUT
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response edit(SlaResource entity) {
        try {
            Response response = null;
            SlaResource order = manager.find(entity.getId());
            if (order != null) {
                // 200
                //entity.setId(id);
                if (!order.getState().equalsIgnoreCase(entity.getState())) {
                    //event publication
                    EventResource event = new EventResource();
                    event.setResource(entity);
                    event.setDate(new Date());
                    event.setReason("SLAResource Changed status");
                    publisher.publishStatusChangedNotification(event);
                }
                manager.edit(entity);
                response = Response.ok(entity).build();
            } else {
                // 404 not found
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
            return response;
        } catch (UnknownResourceException ex) {
            Logger.getLogger(SlaResourceFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        }
    }

    @GET
    @Produces({"application/json"})
    public Response findByCriteriaWithFields(@Context UriInfo info) {

        Response response;
        Set<String> fieldsSelection = URIParser.getFieldsSelection(info.getQueryParameters());
        MultivaluedMap<String, String> map = info.getQueryParameters();
        Set<SlaResource> resultList = findByCriteria(map);

        // 200
        if (fieldsSelection.isEmpty() || fieldsSelection.contains(URIParser.ALL_FIELDS)) {
            response = Response.ok(resultList).build();
        } else {
            fieldsSelection.add(URIParser.ID_FIELD);
            List<ObjectNode> nodeList = new ArrayList<ObjectNode>();
            ObjectNode node;
            node = JSONMarshaller.createNode(resultList, fieldsSelection);
            nodeList.add(node);
            response = Response.ok(nodeList).build();
        }

        return response;
    }

    // return Set of unique elements to avoid List with same elements in case of join
    private Set<SlaResource> findByCriteria(MultivaluedMap<String, String> criteria) {
        List<SlaResource> resultList = null;
        if (criteria != null && !criteria.isEmpty()) {
            resultList = manager.findByCriteria(criteria, SlaResource.class);
        } else {
            resultList = manager.findAll();
        }
        if (resultList == null) {
            return new LinkedHashSet<SlaResource>();
        } else {
            return new LinkedHashSet<SlaResource>(resultList);
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Response findById(@PathParam("id") String id, @Context UriInfo info) {
        try {
            // fields to filter view
            Set<String> fieldSet = URIParser.getFieldsSelection(info.getQueryParameters());

            SlaResource p = manager.find(id);
            Response response;
            if (p != null) {
                // 200
                if (fieldSet.isEmpty() || fieldSet.contains(URIParser.ALL_FIELDS)) {
                    response = Response.ok(p).build();
                } else {
                    fieldSet.add(URIParser.ID_FIELD);
                    List<ObjectNode> nodeList = new ArrayList<ObjectNode>();
                    ObjectNode node = JSONMarshaller.createNode(p, fieldSet);
                    nodeList.add(node);
                    response = Response.ok(nodeList).build();
                }
            } else {
                // 404 not found
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
            return response;
        } catch (UnknownResourceException ex) {
            Logger.getLogger(SlaResourceFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        }
    }

    @DELETE
    @Path("admin/{id}")
    public void remove(@PathParam("id") String id) {
        try {
            manager.remove(manager.find(id));
        } catch (UnknownResourceException ex) {
            Logger.getLogger(SlaResourceFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("admin/count")
    @Produces({"application/json"})
    public Report count() {
        return new Report(manager.count());
    }

    @POST
    @Path("admin")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response createList(List<SlaResource> entities) {
        try {
            if (entities == null) {
                return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
            }

            int previousRows = manager.count();
            int affectedRows;

            affectedRows = manager.create(entities);

            Report stat = new Report(manager.count());
            stat.setAffectedRows(affectedRows);
            stat.setPreviousRows(previousRows);

            // 201 OK
            return Response.created(null).
                    entity(stat).
                    build();
        } catch (BadUsageException ex) {
            Logger.getLogger(SlaResourceFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        }
    }

    @DELETE
    @Path("admin")
    public Report deleteAll() {

        int previousRows = manager.count();
        manager.removeAll();
        int currentRows = manager.count();
        int affectedRows = previousRows - currentRows;

        Report stat = new Report(currentRows);
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        return stat;
    }

    @GET
    @Path("mock")
    @Produces({"application/json"})
    public SlaResource mock() {
        SlaResource sr = new SlaResource();
        sr.setId("123444");
        sr.setName("HighSpeedDataSLA");
        sr.setDescription("SLA for high speed data.");
        sr.setVersion("0.1");
        sr.setStatus(SlaState.OPEN_RUNNING);

        ValidityPeriod vp = new ValidityPeriod();
        vp.setStartTime(new Date());
        vp.setEndTime(new Date());
        sr.setValidityPeriod(vp);

        Template template = new Template();
        template.setUri("http://sdfk");
        template.setName("DataSLATemplate");
        template.setDescription("basic template for Data SLA");
        sr.setTemplate(template);

        InvolvedPartie ip1 = new InvolvedPartie();
        ip1.setRole("SLAProvider");
        ip1.setReference("http://....");
        InvolvedPartie ip2 = new InvolvedPartie();
        ip2.setRole("SLAConsumer");
        ip2.setReference("http://....");
        InvolvedPartie ip3 = new InvolvedPartie();
        ip3.setRole("SLAAuditor");
        ip3.setReference("http://....");
        InvolvedPartie ip4 = new InvolvedPartie();
        ip4.setRole("EndUser");
        ip4.setReference("http://....");
        List<InvolvedPartie> ips = new ArrayList<InvolvedPartie>();
        ips.add(ip1);
        ips.add(ip2);
        ips.add(ip3);
        ips.add(ip4);
        sr.setInvolvedParties(ips);

        sr.setState("observed");
        sr.setApproved("true");
        sr.setApprovalDate(new Date());

        Rule r1 = new Rule();
        r1.setId("availability");
        r1.setMetric("http://...");
        r1.setUnit("string");
        r1.setReferenceValue("available");
        r1.setOperator(".eq");
        r1.setTolerance("0.05");
        r1.setConsequence("http://...");
        Rule r2 = new Rule();
        r2.setId("downstream_GBR");
        r2.setMetric("http://...");
        r2.setUnit("kbps");
        r2.setReferenceValue("1024");
        r2.setOperator(".ge");
        r2.setTolerance("0.25");
        r2.setConsequence("http://...");
        List<Rule> rules = new ArrayList<Rule>();
        rules.add(r1);
        rules.add(r2);
        sr.setRules(rules);

        return sr;
    }
}
