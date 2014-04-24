/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.sla.SlaResource;
import tmf.org.dsmapi.sla.SlaState;

/**
 *
 * @author pierregauthier
 */
@Stateless
@Asynchronous
public class WorkFlowResource implements WorkFlowResourceLocal {
    
    @EJB
    WorkflowListenerResourceLocal listener;

   
    public void execute(SlaResource eventClass)  {
        try {
            System.out.println("Excuting order");
            listener.update(eventClass, EventTypeEnum.CreateNotification, "reason" ,SlaState.OPEN_RUNNING);
            Thread.sleep(3000);
            
            listener.update(eventClass, EventTypeEnum.StatusChangedNotification, "reason", SlaState.CLOSED_COMPLETED);
            System.out.println("Order Completed");
        } catch (InterruptedException ex) {
            Logger.getLogger(WorkFlowResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
