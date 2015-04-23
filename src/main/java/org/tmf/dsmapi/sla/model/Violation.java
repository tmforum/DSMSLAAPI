//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2015.03.19 à 08:45:33 AM CET 
//


package org.tmf.dsmapi.sla.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * <p>Classe Java pour Violation complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Violation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referenceValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="actualValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tolerance" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="violationAverage" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consequence" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attachment" type="{http://orange.com/api/sla/tmf/v2/model/business}Attachment" minOccurs="0"/>
 *         &lt;element name="rule" type="{http://orange.com/api/sla/tmf/v2/model/business}RuleRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Violation", propOrder = {
    "unit",
    "referenceValue",
    "operator",
    "actualValue",
    "tolerance",
    "violationAverage",
    "comment",
    "consequence",
    "attachment",
    "rule"
})
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Entity(name = "Violation")
@Table(name = "VIOLATION")
@Inheritance(strategy = InheritanceType.JOINED)
public class Violation
    implements Serializable
{

    private final static long serialVersionUID = 11L;
    protected String unit;
    protected String referenceValue;
    protected String operator;
    protected String actualValue;
    protected Float tolerance;
    protected Float violationAverage;
    protected String comment;
    protected String consequence;
    protected Attachment attachment;
    protected RuleRef rule;
   
    @JsonIgnore
    protected Long hjid;

    /**
     * Obtient la valeur de la propriété unit.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "UNIT", length = 255)
    public String getUnit() {
        return unit;
    }

    /**
     * Définit la valeur de la propriété unit.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnit(String value) {
        this.unit = value;
    }

    /**
     * Obtient la valeur de la propriété referenceValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "REFERENCE_VALUE", length = 255)
    public String getReferenceValue() {
        return referenceValue;
    }

    /**
     * Définit la valeur de la propriété referenceValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceValue(String value) {
        this.referenceValue = value;
    }

    /**
     * Obtient la valeur de la propriété operator.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "OPERATOR_", length = 255)
    public String getOperator() {
        return operator;
    }

    /**
     * Définit la valeur de la propriété operator.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperator(String value) {
        this.operator = value;
    }

    /**
     * Obtient la valeur de la propriété actualValue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ACTUAL_VALUE", length = 255)
    public String getActualValue() {
        return actualValue;
    }

    /**
     * Définit la valeur de la propriété actualValue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActualValue(String value) {
        this.actualValue = value;
    }

    /**
     * Obtient la valeur de la propriété tolerance.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    @Basic
    @Column(name = "TOLERANCE", precision = 20, scale = 10)
    public Float getTolerance() {
        return tolerance;
    }

    /**
     * Définit la valeur de la propriété tolerance.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setTolerance(Float value) {
        this.tolerance = value;
    }

    /**
     * Obtient la valeur de la propriété violationAverage.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    @Basic
    @Column(name = "VIOLATION_AVERAGE", precision = 20, scale = 10)
    public Float getViolationAverage() {
        return violationAverage;
    }

    /**
     * Définit la valeur de la propriété violationAverage.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setViolationAverage(Float value) {
        this.violationAverage = value;
    }

    /**
     * Obtient la valeur de la propriété comment.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "COMMENT_", length = 255)
    public String getComment() {
        return comment;
    }

    /**
     * Définit la valeur de la propriété comment.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Obtient la valeur de la propriété consequence.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "CONSEQUENCE", length = 255)
    public String getConsequence() {
        return consequence;
    }

    /**
     * Définit la valeur de la propriété consequence.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsequence(String value) {
        this.consequence = value;
    }

    /**
     * Obtient la valeur de la propriété attachment.
     * 
     * @return
     *     possible object is
     *     {@link Attachment }
     *     
     */
    @ManyToOne(targetEntity = Attachment.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ATTACHMENT_VIOLATION_HJID")
    public Attachment getAttachment() {
        return attachment;
    }

    /**
     * Définit la valeur de la propriété attachment.
     * 
     * @param value
     *     allowed object is
     *     {@link Attachment }
     *     
     */
    public void setAttachment(Attachment value) {
        this.attachment = value;
    }

    /**
     * Obtient la valeur de la propriété rule.
     * 
     * @return
     *     possible object is
     *     {@link RuleRef }
     *     
     */
    @ManyToOne(targetEntity = RuleRef.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "RULE__VIOLATION_HJID")
    public RuleRef getRule() {
        return rule;
    }

    /**
     * Définit la valeur de la propriété rule.
     * 
     * @param value
     *     allowed object is
     *     {@link RuleRef }
     *     
     */
    public void setRule(RuleRef value) {
        this.rule = value;
    }

    /**
     * Obtient la valeur de la propriété hjid.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
    public Long getHjid() {
        return hjid;
    }

    /**
     * Définit la valeur de la propriété hjid.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHjid(Long value) {
        this.hjid = value;
    }

}
