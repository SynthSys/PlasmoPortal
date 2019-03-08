//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.25 at 12:55:53 PM GMT 
//


package uk.ac.ed.plasmo.entity.generated.SimileXMLv3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import uk.ac.ed.plasmo.entity.Model;


/**
 * <p>Java class for submodel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submodel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="submodel" type="{}submodel" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="compartment" type="{}compartmentOrVariable" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="variable" type="{}compartmentOrVariable" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submodel", propOrder = {
    "name",
    "submodel",
    "compartment",
    "variable"
})
public class Submodel
    extends Model
{

    protected String name;
    protected List<Submodel> submodel;
    protected List<CompartmentOrVariable> compartment;
    protected List<CompartmentOrVariable> variable;
    @XmlAttribute
    protected String id;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name!= null);
    }

    /**
     * Gets the value of the submodel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the submodel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubmodel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Submodel }
     * 
     * 
     */
    public List<Submodel> getSubmodel() {
        if (submodel == null) {
            submodel = new ArrayList<Submodel>();
        }
        return this.submodel;
    }

    public boolean isSetSubmodel() {
        return ((this.submodel!= null)&&(!this.submodel.isEmpty()));
    }

    public void unsetSubmodel() {
        this.submodel = null;
    }

    /**
     * Gets the value of the compartment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compartment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompartment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CompartmentOrVariable }
     * 
     * 
     */
    public List<CompartmentOrVariable> getCompartment() {
        if (compartment == null) {
            compartment = new ArrayList<CompartmentOrVariable>();
        }
        return this.compartment;
    }

    public boolean isSetCompartment() {
        return ((this.compartment!= null)&&(!this.compartment.isEmpty()));
    }

    public void unsetCompartment() {
        this.compartment = null;
    }

    /**
     * Gets the value of the variable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the variable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVariable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CompartmentOrVariable }
     * 
     * 
     */
    public List<CompartmentOrVariable> getVariable() {
        if (variable == null) {
            variable = new ArrayList<CompartmentOrVariable>();
        }
        return this.variable;
    }

    public boolean isSetVariable() {
        return ((this.variable!= null)&&(!this.variable.isEmpty()));
    }

    public void unsetVariable() {
        this.variable = null;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id!= null);
    }

}