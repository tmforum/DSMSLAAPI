/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tmf.org.dsmapi.sla;

import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 *     "template": {
        "uri": "http/www.acme.com/slaManagement/slaTemplate/42",
        "name": "DataSLATemplate",
        "description": "basic template for Data SLA"
    },

 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Embeddable
public class Violation implements Serializable {
    
    @AttributeOverrides({
        @AttributeOverride(name = "uri", column =
                @Column(name = "RULE_URI")),
        @AttributeOverride(name = "description", column =
                @Column(name = "RULE_DESCRIPTION"))
    })
    private Description rule;
    private String unit;
    private String referenceValue;
    private String operator;
    private String actualValue;
    private String tolerance;
    private String violationAverage;
    private String comment;
    private String consequence;
    @AttributeOverrides({
        @AttributeOverride(name = "uri", column =
                @Column(name = "ATTACHEMENT_URI")),
        @AttributeOverride(name = "description", column =
                @Column(name = "ATTACHEMENT_DESCRIPTION"))
    })
    private Description attachement;
    
    
    
    

    public Description getRule() {
        return rule;
    }

    public void setRule(Description rule) {
        this.rule = rule;
    }
    
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getTolerance() {
        return tolerance;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }

    public String getViolationAverage() {
        return violationAverage;
    }

    public void setViolationAverage(String violationAverage) {
        this.violationAverage = violationAverage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public Description getAttachement() {
        return attachement;
    }

    public void setAttachement(Description attachement) {
        this.attachement = attachement;
    }
    
}
