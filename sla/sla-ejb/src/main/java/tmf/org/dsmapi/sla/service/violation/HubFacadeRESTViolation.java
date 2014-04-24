/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.violation;

import tmf.org.dsmapi.sla.hub.EventFacadeViolation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import tmf.org.dsmapi.sla.hub.EventViolation;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.commons.hub.service.AbstractFacade;
import tmf.org.dsmapi.sla.Description;
import tmf.org.dsmapi.sla.InvolvedPartie;
import tmf.org.dsmapi.sla.SlaState;
import tmf.org.dsmapi.sla.SlaViolation;
import tmf.org.dsmapi.sla.Violation;
import tmf.org.dsmapi.commons.hub.service.HubFacade;
import tmf.org.dsmapi.commons.hub.service.HubFacadeREST;

/**
 *
 * @author pierregauthier
 */
@Stateless
@Path("hubViolation")
public class HubFacadeRESTViolation extends HubFacadeREST {

    @EJB
    HubFacade hubManager;
    @EJB
    EventFacadeViolation eventManager;

    public HubFacadeRESTViolation() {
    }

    @Override
    protected HubFacade getHubManager() {
        return hubManager;
    }

    @Override
    protected AbstractFacade getEventManager() {
        return eventManager;
    }

    @GET
    @Path("eventProto")
    @Produces({"application/json"})
    public EventViolation eventProto() {
        EventViolation event = new EventViolation();
        event.setResource(proto());
        event.setEventType(EventTypeEnum.CreateNotification);
        System.out.println("Event = " + event.getResource());
        System.out.println("Event type = " + event.getEventType());
        return event;
    }

    public SlaViolation proto() {
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
