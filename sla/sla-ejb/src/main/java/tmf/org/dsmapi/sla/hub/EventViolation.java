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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import tmf.org.dsmapi.commons.hub.AbstractEvent;
import tmf.org.dsmapi.commons.hub.EventTypeEnum;
import tmf.org.dsmapi.sla.SlaViolation;

/**
 *
 * @author pierregauthier
 */
@XmlRootElement
@Entity
@JsonPropertyOrder(value = {"resource", "reason", "date", "eventType"})
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EventViolation     
    extends AbstractEvent<SlaViolation, EventTypeEnum> 
    implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private String id;
    @OneToOne
    private SlaViolation resource;
    private String reason;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEvent;
    @Enumerated(value = EnumType.STRING)
    private EventTypeEnum eventType;

    public EventViolation() {
        super(SlaViolation.class, EventTypeEnum.class);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public SlaViolation getResource() {
        return resource;
    }

    @Override
    public final void setResource(Object resource) {
        this.resource = (SlaViolation) resource;
    }

    @Override
    public EventTypeEnum getEventType() {
        return eventType;
    }

    @Override
    public void setEventType(Object eventType) {
        this.eventType = (EventTypeEnum)eventType;
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
        return "EventViolation{" + "id=" + getId() + ", slaViolation=" + getResource()+ ", eventType=" + getEventType() + ", reason=" + getReason() + ", dateEvent=" + getDate() + '}';
    }

}
