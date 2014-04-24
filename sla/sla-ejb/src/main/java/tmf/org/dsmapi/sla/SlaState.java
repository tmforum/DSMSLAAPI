/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla;

/**
 *
 * @author pierregauthier
 */
public enum SlaState {
    
    OPEN_RUNNING, CLOSED_COMPLETED, CLOSED_ABORTED_BYSERVER;
    
     public static SlaState fromString(String text) {
        if (text != null) {
            for (SlaState b : SlaState.values()) {
                if (text.equalsIgnoreCase(b.toString())) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "OrderItemStateEnum{" + '}';
    }
}

