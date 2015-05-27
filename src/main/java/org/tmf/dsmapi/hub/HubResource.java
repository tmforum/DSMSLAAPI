package org.tmf.dsmapi.hub;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.tmf.dsmapi.commons.exceptions.BadUsageException;
import org.tmf.dsmapi.commons.exceptions.UnknownResourceException;
import org.tmf.dsmapi.commons.jaxrs.Report;
import org.tmf.dsmapi.commons.utils.TMFDate;
import org.tmf.dsmapi.sla.event.SlaEvent;
import org.tmf.dsmapi.sla.event.SlaEventTypeEnum;
import org.tmf.dsmapi.sla.model.Attachment;
import org.tmf.dsmapi.sla.model.RelatedParty;
import org.tmf.dsmapi.sla.model.Rule;
import org.tmf.dsmapi.sla.model.RuleRef;
import org.tmf.dsmapi.sla.model.Sla;
import org.tmf.dsmapi.sla.model.SlaRef;
import org.tmf.dsmapi.sla.model.SlaViolation;
import org.tmf.dsmapi.sla.model.Template;
import org.tmf.dsmapi.sla.model.ValidFor;
import org.tmf.dsmapi.sla.model.Violation;
import org.tmf.dsmapi.slaViolation.event.SlaViolationEvent;
import org.tmf.dsmapi.slaViolation.event.SlaViolationEventTypeEnum;

@Stateless
@Path("/slaManagement/v2/hub")
public class HubResource {

    @EJB
    HubFacade hubFacade;

    public HubResource() {
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response create(Hub entity) throws BadUsageException {
        entity.setId(null);
        hubFacade.create(entity);
        //201
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @DELETE
    public Report deleteAllHub() {

        int previousRows = hubFacade.count();
        hubFacade.removeAll();
        int currentRows = hubFacade.count();
        int affectedRows = previousRows - currentRows;

        Report stat = new Report(currentRows);
        stat.setAffectedRows(affectedRows);
        stat.setPreviousRows(previousRows);

        return stat;
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") String id) throws UnknownResourceException {
        Hub hub = hubFacade.find(id);
        if (null == hub) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            hubFacade.remove(id);
            // 204
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    @GET
    @Produces({"application/json"})
    public List<Hub> findAll() {
        return hubFacade.findAll();
    }

    @GET
    @Produces({"application/json"})
    @Path("proto/sla/event")
    public SlaEvent protoslaevent() {
        SlaEvent event = new SlaEvent();
        SlaEventTypeEnum x = SlaEventTypeEnum.SlaCreateNotification;
        event.setEventType(x);
        event.setEventTime(new Date());
        event.setId("42");
        
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
        
        event.setResource(sla);
        
        return event;
    
    }

    @GET
    @Produces({"application/json"})
    @Path("proto/slaviolation/event")
    public SlaViolationEvent protoslaviolationevent() {
        SlaViolationEvent event = new SlaViolationEvent();
        SlaViolationEventTypeEnum x = SlaViolationEventTypeEnum.SlaViolationCreateNotification;
        event.setEventType(x);
        event.setEventTime(new Date());
        event.setId("42");
        
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
        
        event.setResource(slaViolation);
        
        return event;
    }        
}
