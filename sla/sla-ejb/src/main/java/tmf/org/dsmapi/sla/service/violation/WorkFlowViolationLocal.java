/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.violation;

import javax.ejb.Local;
import tmf.org.dsmapi.sla.SlaViolation;

/**
 *
 * @author pierregauthier
 */
@Local
public interface WorkFlowViolationLocal {
    
    void execute(SlaViolation eventClass);
    
}
