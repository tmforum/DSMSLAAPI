/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla.hub;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import tmf.org.dsmapi.commons.hub.AbstractEvent;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.sla.SlaResource;

/**
 *
 * @author pierregauthier
 */
@XmlRootElement
@XmlSeeAlso({SlaResource.class, EventTypeEnum.class})
@Entity
@JsonPropertyOrder(value = {"resource", "reason", "date", "eventType"})
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EventResource  
    extends AbstractEvent<SlaResource, EventTypeEnum> 
    implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private String id;
    @OneToOne
    private SlaResource resource;
    private String reason;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEvent;
    @Enumerated(value = EnumType.STRING)
    private EventTypeEnum eventType;

    public EventResource() {
        super(SlaResource.class, EventTypeEnum.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public SlaResource getResource() {
        return resource;
    }

    @Override
    public void setResource(Object resource) {
        this.resource = (SlaResource) resource;
    }


    @Override
    public EventTypeEnum getEventType() {
        return eventType;
    }

    @Override
    public void setEventType(Object eventType) {
        this.eventType = (EventTypeEnum) eventType;
    }


    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Date getDate() {
        return dateEvent;
    }

    @Override
    public void setDate(Date date) {
        this.dateEvent = date;
    }

    @Override
    public String toString() {
        return "EventResource{" + "id=" + getId() + ", slaResource=" + getResource()+ ", eventType=" + getEventType() + ", reason=" + getReason() + ", dateEvent=" + getDate() + '}';
    }
}
