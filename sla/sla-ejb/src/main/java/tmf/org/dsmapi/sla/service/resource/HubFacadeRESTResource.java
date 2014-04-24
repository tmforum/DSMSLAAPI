/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;

import tmf.org.dsmapi.sla.hub.EventFacadeResource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import tmf.org.dsmapi.sla.hub.EventResource;

import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;
import tmf.org.dsmapi.sla.InvolvedPartie;
import tmf.org.dsmapi.sla.Rule;
import tmf.org.dsmapi.sla.SlaResource;
import tmf.org.dsmapi.sla.SlaState;
import tmf.org.dsmapi.sla.Template;
import tmf.org.dsmapi.sla.ValidityPeriod;
import tmf.org.dsmapi.commons.hub.service.HubFacade;
import tmf.org.dsmapi.commons.hub.service.HubFacadeREST;

/**
 *
 * @author pierregauthier
 */
@Stateless
@Path("hubResource")
public class HubFacadeRESTResource extends HubFacadeREST {

    @EJB
    HubFacade hubManager;
    @EJB
    EventFacadeResource hubEventManager;

    public HubFacadeRESTResource() {
    }

    @Override
    protected HubFacade getHubManager() {
        return hubManager;
    }

    @Override
    protected AbstractFacade getEventManager() {
        return hubEventManager;
    }

    @GET
    @Path("eventProto")
    @Produces({"application/json"})
    public EventResource eventProto() {
        EventResource event = new EventResource();
        event.setResource(proto());
        event.setEventType(EventTypeEnum.CreateNotification);
        System.out.println("Event = " + event.getResource());
        System.out.println("Event type = " + event.getEventType());
        return event;
    }

    public SlaResource proto() {
        SlaResource sr = new SlaResource();
        sr.setId("123");
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
