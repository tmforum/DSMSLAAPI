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
public class SlaResource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    private String description;
    private String version;
    @Enumerated(value = EnumType.STRING)
    private SlaState status;
    @Embedded
    private ValidityPeriod validityPeriod;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "uri", column =
                @Column(name = "TEMPLATE_URI")),
        @AttributeOverride(name = "name", column =
                @Column(name = "TEMPLATE_NAME")),
        @AttributeOverride(name = "description", column =
                @Column(name = "TEMPLATE_DESCRIPTION"))
    })
    private Template template;
    @ElementCollection
    private List<InvolvedPartie> involvedParties;
    @AttributeOverride(name = "state", column =
                @Column(name = "SLA_STATE"))
    private String state;
    private String approved;
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalDate;
    @ElementCollection
    private List<Rule> rules;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SlaState getStatus() {
        return status;
    }

    public void setStatus(SlaState status) {
        this.status = status;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(ValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public List<InvolvedPartie> getInvolvedParties() {
        return involvedParties;
    }

    public void setInvolvedParties(List<InvolvedPartie> involvedParties) {
        this.involvedParties = involvedParties;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
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
        if (!(object instanceof SlaResource)) {
            return false;
        }
        SlaResource other = (SlaResource) object;
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
