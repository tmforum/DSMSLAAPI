/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.service.resource;


import javax.ejb.Local;
import tmf.org.dsmapi.sla.SlaResource;

/**
 *
 * @author pierregauthier
 */
@Local
public interface WorkFlowResourceLocal {
    
    public void execute(SlaResource eventClass);
    
}
