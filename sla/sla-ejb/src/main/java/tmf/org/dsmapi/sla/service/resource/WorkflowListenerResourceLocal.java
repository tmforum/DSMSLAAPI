/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;

import javax.ejb.Local;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.sla.SlaResource;
import tmf.org.dsmapi.sla.SlaState;

/**
 *
 * @author pierregauthier
 */
@Local
public interface WorkflowListenerResourceLocal {
    
  
    void  update(SlaResource eventClass, EventTypeEnum state, String reason, SlaState status);
    
}
