/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmf.dsmapi.slaViolation.event;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import static org.codehaus.jackson.annotate.JsonAutoDetect.Visibility.ANY;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tmf.dsmapi.commons.utils.CustomJsonDateSerializer;
import org.tmf.dsmapi.sla.model.SlaViolation;

@XmlRootElement
@Entity
@Table(name = "Event_SlaViolation")
@JsonPropertyOrder(value = {"id", "eventTime", "eventType", "event"})
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SlaViolationEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("eventId")
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date eventTime;

    @Enumerated(value = EnumType.STRING)
    private SlaViolationEventTypeEnum eventType;
    @JsonIgnore
    private SlaViolation resource; //check for object

    @JsonProperty("eventId")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public SlaViolationEventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(SlaViolationEventTypeEnum eventType) {
        this.eventType = eventType;
    }

    @JsonAutoDetect(fieldVisibility = ANY)
    class EventBody {

        private SlaViolation slaViolation;

        public SlaViolation getSlaViolation() {
            return slaViolation;
        }

        public EventBody(SlaViolation slaViolation) {
            this.slaViolation = slaViolation;
        }

    }

    @JsonProperty("event")
    public EventBody getEvent() {

        return new EventBody(getResource());
    }

    @JsonIgnore
    public SlaViolation getResource() {

        return resource;
    }

    public void setResource(SlaViolation resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "SlaViolationEvent{" + "id=" + id + ", eventTime=" + eventTime + ", eventType=" + eventType + ", resource=" + resource + '}';
    }

}
