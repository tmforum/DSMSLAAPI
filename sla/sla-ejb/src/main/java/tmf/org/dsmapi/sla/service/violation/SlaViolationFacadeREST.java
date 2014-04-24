/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.violation;

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
import tmf.org.dsmapi.commons.utils.URIParser;
import tmf.org.dsmapi.sla.InvolvedPartie;
import tmf.org.dsmapi.sla.Description;
import tmf.org.dsmapi.sla.SlaState;
import tmf.org.dsmapi.sla.SlaViolation;
import tmf.org.dsmapi.sla.Violation;
import tmf.org.dsmapi.sla.Report;
import tmf.org.dsmapi.sla.SlaResource;

/**
 *
 * @author pierregauthier
 */
@Stateless
@Path("slaViolation")
public class SlaViolationFacadeREST {

    @EJB
    WorkFlowViolationLocal workflow;
    @EJB
    PublisherViolation publisher;
    @EJB
    SlaViolationFacade manager;

    public SlaViolationFacadeREST() {
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response create(SlaViolation entity) {
        try {
            entity.setId(null);
            entity.setStatus(SlaState.OPEN_RUNNING);
            manager.create(entity);
            workflow.execute(entity);
            Response response = Response.ok(entity).build();
            return response;
        } catch (BadUsageException ex) {
            Logger.getLogger(SlaViolationFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        }
    }

    @PUT
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response edit(SlaViolation entity) {
        try {
            Response response = null;
            SlaViolation slaV = manager.find(entity.getId());
            if (slaV != null) {
                // 200
                //entity.setId(id);
                manager.edit(entity);
                response = Response.ok(entity).build();
            } else {
                // 404 not found
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
            return response;
        } catch (UnknownResourceException ex) {
            Logger.getLogger(SlaViolationFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        }
    }

    @GET
    @Produces({"application/json"})
    public Response findByCriteriaWithFields(@Context UriInfo info) {

        Response response;
        // search criteria
        Set<String> fieldsSelection = URIParser.getFieldsSelection(info.getQueryParameters());
        // fields to filter view
        MultivaluedMap<String, String> map = info.getQueryParameters();
        Set<SlaViolation> resultList = findByCriteria(map);

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
    private Set<SlaViolation> findByCriteria(MultivaluedMap<String, String> criteria) {
        List<SlaViolation> resultList = null;
        if (criteria != null && !criteria.isEmpty()) {
            resultList = manager.findByCriteria(criteria, SlaViolation.class);
        } else {
            resultList = manager.findAll();
        }
        if (resultList == null) {
            return new LinkedHashSet<SlaViolation>();
        } else {
            return new LinkedHashSet<SlaViolation>(resultList);
        }
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Response findById(@PathParam("id") String id, @Context UriInfo info) {
        try {
            // fields to filter view
            Set<String> fieldSet = URIParser.getFieldsSelection(info.getQueryParameters());

            SlaViolation p = manager.find(id);
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
            Logger.getLogger(SlaViolationFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SlaViolationFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
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
    public Response createList(List<SlaViolation> entities) {
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
            Logger.getLogger(SlaViolationFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
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
    public SlaViolation mock() {
        SlaViolation sv = new SlaViolation();
        sv.setId("444");
        sv.setDate(new Date());
        sv.setStatus(SlaState.OPEN_RUNNING);

        Description sla = new Description();
        sla.setDescription("sla of premium video");
        sla.setUri("http://......");
        sv.setSla(sla);
        
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
        sv.setInvolvedParties(ips);
        
        List<Violation> vs = new ArrayList<Violation>();
        Violation v = new Violation();
        Description rule = new Description();
        rule.setDescription("availability");
        rule.setUri("http://......");
        v.setRule(rule);
        
        v.setUnit("string");
        v.setReferenceValue("available");
        v.setOperator(".eq");
        v.setActualValue("available");
        v.setTolerance("0.0.5");
        v.setViolationAverage("0.1");
        v.setComment("Availability below agreed level.");
        v.setConsequence("http://.....");
        
        Description attachement = new Description();
        attachement.setDescription("availability statistics for August 2013");
        attachement.setUri("http://....");
        v.setAttachement(attachement);
        vs.add(v);
        
        sv.setViolations(vs);
        
        return sv;
    }
}
