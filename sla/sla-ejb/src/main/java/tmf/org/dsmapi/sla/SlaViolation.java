/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author ecus6396
 */
@Entity
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SlaViolation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Enumerated(value = EnumType.STRING)
    private SlaState status;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "uri", column =
                @Column(name = "SLA_URI")),
        @AttributeOverride(name = "description", column =
                @Column(name = "SLA_DESCRIPTION"))
    })
    private Description sla;
    @ElementCollection
    private List<InvolvedPartie> involvedParties;
    @Embedded
    @ElementCollection
    private List<Violation> violations;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SlaState getStatus() {
        return status;
    }

    public void setStatus(SlaState status) {
        this.status = status;
    }

    public Description getSla() {
        return sla;
    }

    public void setSla(Description sla) {
        this.sla = sla;
    }

    public List<InvolvedPartie> getInvolvedParties() {
        return involvedParties;
    }

    public void setInvolvedParties(List<InvolvedPartie> involvedParties) {
        this.involvedParties = involvedParties;
    }

    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SlaViolation)) {
            return false;
        }
        SlaViolation other = (SlaViolation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tmf.org.slaapi.ordering.SlaResource[ id=" + id + " ]";
    }
    
}
