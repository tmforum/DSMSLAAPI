/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.violation;

import javax.ejb.Local;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.sla.SlaState;
import tmf.org.dsmapi.sla.SlaViolation;

/**
 *
 * @author pierregauthier
 */
@Local
public interface WorkflowListenerViolationLocal {
    
  
    void update(SlaViolation event, EventTypeEnum state, String reason, SlaState status);
    
}
