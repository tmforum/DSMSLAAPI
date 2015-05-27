package org.tmf.dsmapi.slaViolation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.tmf.dsmapi.commons.exceptions.BadUsageException;
import org.tmf.dsmapi.commons.exceptions.UnknownResourceException;
import org.tmf.dsmapi.commons.jaxrs.Report;
import org.tmf.dsmapi.sla.model.Attachment;
import org.tmf.dsmapi.sla.model.RelatedParty;
import org.tmf.dsmapi.sla.model.Rule;
import org.tmf.dsmapi.sla.model.RuleRef;
import org.tmf.dsmapi.sla.model.Sla;
import org.tmf.dsmapi.sla.model.SlaRef;
import org.tmf.dsmapi.sla.model.SlaViolation;
import org.tmf.dsmapi.sla.model.Violation;
import org.tmf.dsmapi.slaViolation.event.SlaViolationEvent;
import org.tmf.dsmapi.slaViolation.event.SlaViolationEventFacade;
import org.tmf.dsmapi.slaViolation.event.SlaViolationEventPublisherLocal;

@Stateless
@Path("admin/slaViolation")
public class SlaViolationAdminResource {

    @EJB
    SlaViolationFacade slaFacade;
    @EJB
    SlaViolationEventFacade eventFacade;
//    @EJB
//    SlaViolationEventPublisherLocal publisher;

    @GET
    @Produces({"application/json"})
    public List<SlaViolation> findAll() {
        return slaFacade.findAll();
    }

    /**
     *
     * For test purpose only
     *
     * @param entities
     * @return
     */
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response post(List<SlaViolation> entities, @Context UriInfo info) throws UnknownResourceException {

        if (entities == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        int previousRows = slaFacade.count();
        int affectedRows=0;

        // Try to persist entities
        try {
            for (SlaViolation entitie : entities) {
                slaFacade.checkCreation(entitie);
                slaFacade.create(entitie);
                entitie.setHref(info.getAbsolutePath() + "/" + Long.toString(entitie.getId()));
                slaFacade.edit(entitie);
                affectedRows = affectedRows + 1;
//                publisher.createNotification(entitie, new Date());
            }
        } catch (BadUsageException e) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        Report stat = new Report(slaFacade.count());
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        // 201 OK
        return Response.created(null).
                entity(stat).
                build();
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response update(@PathParam("id") long id, SlaViolation entity) throws UnknownResourceException {
        Response response = null;
        SlaViolation sla = slaFacade.find(id);
        if (sla != null) {
            entity.setId(id);
            slaFacade.edit(entity);
//            publisher.valueChangedNotification(entity, new Date());
            // 200 OK + location
            response = Response.status(Response.Status.OK).entity(entity).build();

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
     * @return
     * @throws org.tmf.dsmapi.commons.exceptions.UnknownResourceException
     */
    @DELETE
    public Report deleteAll() throws UnknownResourceException {

        eventFacade.removeAll();
        int previousRows = slaFacade.count();
        slaFacade.removeAll();
        List<SlaViolation> pis = slaFacade.findAll();
        for (SlaViolation pi : pis) {
            delete(pi.getId());
        }

        int currentRows = slaFacade.count();
        int affectedRows = previousRows - currentRows;

        Report stat = new Report(currentRows);
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        return stat;
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
    public Response delete(@PathParam("id") Long id) throws UnknownResourceException {
        int previousRows = slaFacade.count();
        SlaViolation entity = slaFacade.find(id);

        // Event deletion
//            publisher.deleteNotification(entity, new Date());
        try {
            //Pause for 4 seconds to finish notification
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SlaViolationAdminResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        // remove event(s) binding to the resource
        List<SlaViolationEvent> events = eventFacade.findAll();
        for (SlaViolationEvent event : events) {
            if (event.getResource().getId().equals(id)) {
                eventFacade.remove(event.getId());
            }
        }
        //remove resource
        slaFacade.remove(id);

        int affectedRows = 1;
        Report stat = new Report(slaFacade.count());
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        // 200 
        Response response = Response.ok(stat).build();
        return response;
    }

    @GET
    @Produces({"application/json"})
    @Path("event")
    public List<SlaViolationEvent> findAllEvents() {
        return eventFacade.findAll();
    }

    @DELETE
    @Path("event")
    public Report deleteAllEvent() {

        int previousRows = eventFacade.count();
        eventFacade.removeAll();
        int currentRows = eventFacade.count();
        int affectedRows = previousRows - currentRows;

        Report stat = new Report(currentRows);
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        return stat;
    }

    @DELETE
    @Path("event/{id}")
    public Response deleteEvent(@PathParam("id") String id) throws UnknownResourceException {

        int previousRows = eventFacade.count();
        List<SlaViolationEvent> events = eventFacade.findAll();
        for (SlaViolationEvent event : events) {
            if (event.getResource().getId().equals(id)) {
                eventFacade.remove(event.getId());

            }
        }
        int currentRows = eventFacade.count();
        int affectedRows = previousRows - currentRows;

        Report stat = new Report(currentRows);
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        // 200 
        Response response = Response.ok(stat).build();
        return response;
    }

    /**
     *
     * @return
     */
    @GET
    @Path("count")
    @Produces({"application/json"})
    public Report count() {
        return new Report(slaFacade.count());
    }

    @GET
    @Produces({"application/json"})
    @Path("proto")
    public SlaViolation proto() {
        SlaViolation slaViolation = new SlaViolation();
        slaViolation.setId(new Long(1));
        slaViolation.setHref("http://localhost:8080/DSSlaManagement/api/slaManagement/v2/slaViolation/1");
        
        SlaRef slaRef = new SlaRef();
        slaRef.setHref("http/www.acme.com/slaManagement/sla/123444");
        slaRef.setDescription("sla of premium video");
        slaViolation.setSla(slaRef);
        
        Violation violation = new Violation();
        violation.setUnit("string");
        violation.setReferenceValue("available");
        violation.setOperator(".eq");
        violation.setTolerance(new Float("0.05"));
        violation.setViolationAverage(new Float("0.1"));
        violation.setComment("Availability below agreed level.");
        violation.setConsequence("http://ww.acme.com/contract/clause/42");
        Attachment attachment = new Attachment();
        attachment.setHref("https://foo.bar/screenshot.123");
        attachment.setDescription("availability statistics for August 2013");
        violation.setAttachment(attachment);
        RuleRef rule = new RuleRef();
        rule.setHref("http/www.zak.com/slaManagement/sla/123444/rules/availability");
        rule.setDescription("availability");
        violation.setRule(rule);
        slaViolation.setViolation(violation);
        
        List<RelatedParty> l_rp = new ArrayList<RelatedParty>();
        RelatedParty rp  = new RelatedParty();
        rp.setHref("http://serverlocation:port/partnerManagement/partner/42");
        rp.setRole("SLAProvider");
        l_rp.add(rp);
        rp  = new RelatedParty();
        rp.setHref("http://serverlocation:port/partnerManagement/partner/44");
        rp.setRole("SLAConsumer");
        l_rp.add(rp);
        rp  = new RelatedParty();
        rp.setHref("http://serverlocation:port/partnerManagement/partner/33");
        rp.setRole("SLAAuditor");
        l_rp.add(rp);
        rp  = new RelatedParty();
        rp.setHref("http://serverlocation:port/partnerManagement/partner/57");
        rp.setRole("SLABusinessBroker");
        l_rp.add(rp);
        rp  = new RelatedParty();
        rp.setHref("http://serverlocation:port/partnerManagement/partner/66");
        rp.setRole("SLATechnicalBroker");
        l_rp.add(rp);
        rp  = new RelatedParty();
        rp.setHref("http://serverlocation:port/partnerManagement/partner/48");
        rp.setRole("ThirdPartySLAManager");
        l_rp.add(rp);
        slaViolation.setRelatedParty(l_rp);
        
        return slaViolation;
    }
}
