//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2015.03.19 à 08:45:33 AM CET 
//


package org.tmf.dsmapi.sla.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tmf.dsmapi.sla.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ValidFor_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "ValidFor");
    private final static QName _Violation_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "Violation");
    private final static QName _Rule_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "Rule");
    private final static QName _RelatedParty_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "RelatedParty");
    private final static QName _Attachment_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "Attachment");
    private final static QName _SlaViolation_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "SlaViolation");
    private final static QName _SlaRef_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "SlaRef");
    private final static QName _RuleRef_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "RuleRef");
    private final static QName _Template_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "Template");
    private final static QName _Sla_QNAME = new QName("http://orange.com/api/sla/tmf/v2/model/business", "Sla");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tmf.dsmapi.sla.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RelatedParty }
     * 
     */
    public RelatedParty createRelatedParty() {
        return new RelatedParty();
    }

    /**
     * Create an instance of {@link RuleRef }
     * 
     */
    public RuleRef createRuleRef() {
        return new RuleRef();
    }

    /**
     * Create an instance of {@link SlaRef }
     * 
     */
    public SlaRef createSlaRef() {
        return new SlaRef();
    }

    /**
     * Create an instance of {@link SlaViolation }
     * 
     */
    public SlaViolation createSlaViolation() {
        return new SlaViolation();
    }

    /**
     * Create an instance of {@link Attachment }
     * 
     */
    public Attachment createAttachment() {
        return new Attachment();
    }

    /**
     * Create an instance of {@link Template }
     * 
     */
    public Template createTemplate() {
        return new Template();
    }

    /**
     * Create an instance of {@link Sla }
     * 
     */
    public Sla createSla() {
        return new Sla();
    }

    /**
     * Create an instance of {@link Violation }
     * 
     */
    public Violation createViolation() {
        return new Violation();
    }

    /**
     * Create an instance of {@link ValidFor }
     * 
     */
    public ValidFor createValidFor() {
        return new ValidFor();
    }

    /**
     * Create an instance of {@link Rule }
     * 
     */
    public Rule createRule() {
        return new Rule();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidFor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "ValidFor")
    public JAXBElement<ValidFor> createValidFor(ValidFor value) {
        return new JAXBElement<ValidFor>(_ValidFor_QNAME, ValidFor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Violation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "Violation")
    public JAXBElement<Violation> createViolation(Violation value) {
        return new JAXBElement<Violation>(_Violation_QNAME, Violation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "Rule")
    public JAXBElement<Rule> createRule(Rule value) {
        return new JAXBElement<Rule>(_Rule_QNAME, Rule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RelatedParty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "RelatedParty")
    public JAXBElement<RelatedParty> createRelatedParty(RelatedParty value) {
        return new JAXBElement<RelatedParty>(_RelatedParty_QNAME, RelatedParty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Attachment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "Attachment")
    public JAXBElement<Attachment> createAttachment(Attachment value) {
        return new JAXBElement<Attachment>(_Attachment_QNAME, Attachment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SlaViolation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "SlaViolation")
    public JAXBElement<SlaViolation> createSlaViolation(SlaViolation value) {
        return new JAXBElement<SlaViolation>(_SlaViolation_QNAME, SlaViolation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SlaRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "SlaRef")
    public JAXBElement<SlaRef> createSlaRef(SlaRef value) {
        return new JAXBElement<SlaRef>(_SlaRef_QNAME, SlaRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RuleRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "RuleRef")
    public JAXBElement<RuleRef> createRuleRef(RuleRef value) {
        return new JAXBElement<RuleRef>(_RuleRef_QNAME, RuleRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Template }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "Template")
    public JAXBElement<Template> createTemplate(Template value) {
        return new JAXBElement<Template>(_Template_QNAME, Template.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Sla }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://orange.com/api/sla/tmf/v2/model/business", name = "Sla")
    public JAXBElement<Sla> createSla(Sla value) {
        return new JAXBElement<Sla>(_Sla_QNAME, Sla.class, null, value);
    }

}
