/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.violation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.sla.SlaState;
import tmf.org.dsmapi.sla.SlaViolation;

/**
 *
 * @author pierregauthier
 */
@Stateless
@Asynchronous
public class WorkFlowViolation implements WorkFlowViolationLocal {
    
    @EJB
    WorkflowListenerViolationLocal listener;

   
    public void execute(SlaViolation Class)  {
        try {
            System.out.println("Excuting slaViolation");
            listener.update(Class, EventTypeEnum.CreateNotification, "reason" ,SlaState.OPEN_RUNNING);
            Thread.sleep(3000);
            
            listener.update(Class, EventTypeEnum.StatusChangedNotification, "reason", SlaState.CLOSED_COMPLETED);
            System.out.println("slaViolation Completed");
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkFlowViolation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
