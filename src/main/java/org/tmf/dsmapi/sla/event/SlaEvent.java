/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmf.dsmapi.sla.event;

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
import org.tmf.dsmapi.sla.model.Sla;

@XmlRootElement
@Entity
@Table(name="Event_Sla")
@JsonPropertyOrder(value = {"id", "eventTime", "eventType", "event"})
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SlaEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
@JsonIgnore
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date eventTime;

    @Enumerated(value = EnumType.STRING)
    private SlaEventTypeEnum eventType;

     @JsonIgnore
    private Sla resource; //check for object

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

    public SlaEventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(SlaEventTypeEnum eventType) {
        this.eventType = eventType;
    }
    
    @JsonIgnore
    public Sla getResource() {
        
        
        return resource;
    }

    public void setResource(Sla resource) {
        this.resource = resource;
    }


     
    @JsonAutoDetect(fieldVisibility = ANY)
    class EventBody {
        private Sla sla;
        public Sla getSla() {
            return sla;
        }
        public EventBody(Sla sla) { 
        this.sla = sla;
    }
    
       
    }
   @JsonProperty("event")
   public EventBody getEvent() {
       
       return new EventBody(getResource() );
   }

    @Override
    public String toString() {
        return "SlaEvent{" + "id=" + id + ", eventTime=" + eventTime + ", eventType=" + eventType + ", resource=" + resource + '}';
    }

   

}
