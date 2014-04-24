/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.commons.hub;

import java.util.Date;

/**
 *
 * @author pierregauthier
 */
public abstract class AbstractEvent<U, V> {

    private Class<U> resource;
    private Class<V> eventType;

    private String id;
    private String reason;
    private Date dateEvent;

    public AbstractEvent() {
        this.resource = resource;
        this.eventType = eventType;
    }

    public AbstractEvent(Class<U> resource, Class<V> eventType) {
        this.resource = resource;
        this.eventType = eventType;
    }

    public abstract String getId();

    public abstract void setId(String id);

    public abstract Object getResource();

    public abstract void setResource(Object resource);

    public abstract Object getEventType();

    public abstract void setEventType(Object eventType);

    public abstract String getReason();

    public abstract void setReason(String reason);

    public abstract Date getDate() ;

    public abstract void setDate(Date date) ;

    @Override
    public String toString() {
        return "EventGeneric{" + "resource=" + getResource()+ ", eventType=" + getEventType() + ", reason=" + getReason() + ", dateEvent=" + getDate() + '}';
    }
}
