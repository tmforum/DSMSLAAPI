package org.tmf.dsmapi.sla;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.tmf.dsmapi.commons.utils.TMFDate;
import org.tmf.dsmapi.sla.model.Sla;
import org.tmf.dsmapi.sla.event.SlaEvent;
import org.tmf.dsmapi.sla.event.SlaEventFacade;
import org.tmf.dsmapi.sla.event.SlaEventPublisherLocal;
import org.tmf.dsmapi.sla.model.RelatedParty;
import org.tmf.dsmapi.sla.model.Rule;
import org.tmf.dsmapi.sla.model.Template;
import org.tmf.dsmapi.sla.model.ValidFor;

@Stateless
@Path("admin/sla")
public class SlaAdminResource {

    @EJB
    SlaFacade slaFacade;
    @EJB
    SlaEventFacade eventFacade;
//    @EJB
//    SlaEventPublisherLocal publisher;

    @GET
    @Produces({"application/json"})
    public List<Sla> findAll() {
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
    public Response post(List<Sla> entities, @Context UriInfo info) throws UnknownResourceException {

        if (entities == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
        }

        int previousRows = slaFacade.count();
        int affectedRows = 0;

        // Try to persist entities
        try {
            for (Sla entitie : entities) {
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
    public Response update(@PathParam("id") long id, Sla entity) throws UnknownResourceException {
        Response response = null;
        Sla sla = slaFacade.find(id);
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
        List<Sla> pis = slaFacade.findAll();
        for (Sla pi : pis) {
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
        Sla entity = slaFacade.find(id);

        // Event deletion
//            publisher.deleteNotification(entity, new Date());
        try {
            //Pause for 4 seconds to finish notification
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SlaAdminResource.class.getName()).log(Level.SEVERE, null, ex);
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
    public List<SlaEvent> findAllEvents() {
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
        List<SlaEvent> events = eventFacade.findAll();
        for (SlaEvent event : events) {
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
    public Sla proto() {
        Sla sla = new Sla();
        sla.setId(new Long(1));
        sla.setHref("http://localhost:8080/DSSlaManagement/api/slaManagement/v2/sla/1");
        sla.setName("HighSpeedDataSLA");
        sla.setDescription("SLA for high speed data.");
        sla.setVersion("0.1");

        GregorianCalendar gc = new GregorianCalendar();
        gc.set(2015, 05, 15);
        ValidFor validFor = new ValidFor();
        validFor.setStartDateTime(TMFDate.parse("2013-04-19T18:42:23+02:00"));
        validFor.setEndDateTime(TMFDate.parse("2013-04-21T11:43:54+02:00"));
        sla.setValidFor(validFor);
        
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
        sla.setRelatedParty(l_rp);

        List<Rule> l_rules = new ArrayList<Rule>();
        Rule rule  = new Rule();
        rule.setId("availability");
        rule.setMetric("http://IEEE99.5/Availability");
        rule.setUnit("string");
        rule.setReferenceValue("available");
        rule.setOperator(".eq");
        rule.setTolerance("0.05");
        rule.setConsequence("http://ww.acme.com/contract/clause/42");
        l_rules.add(rule);
        rule  = new Rule();
        rule.setId("downstream_GBR");
        rule.setMetric("http://IEEE99.5/Data/bitrates/GBR/down");
        rule.setUnit("kbps");
        rule.setReferenceValue("1024");
        rule.setOperator(".ge");
        rule.setTolerance("0.20");
        rule.setConsequence("http://ww.acme.com/contract/clause/45");
        l_rules.add(rule);
        sla.setRule(l_rules);
        
        Template template = new Template();
        template.setHref("http/www.acme.com/slaManagement/slaTemplate/42");
        template.setName("DataSLATemplate");
        template.setDescription("basic template for Data SLA");
        sla.setTemplate(template);
        
        return sla;
    }
}
