/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.plasmo.xml;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class XMLValidatorTest {
    
    public XMLValidatorTest() {
    }

    @Test
    public void testValidateXMLfile() throws Exception {
        System.out.println("validateXMLfile");
        
        File xmlFile = new File(this.getClass().getResource("sbmlL2V3.xml").getFile());
        File schemaFile = new File(this.getClass().getResource("sbmlL2V3.xsd").getFile());
        XMLValidator instance = new XMLValidator();
        instance.validateXMLfile(xmlFile, schemaFile);
        
    }
    
    @Test
    public void testValidateXMLfileComplicated() throws Exception {
        System.out.println("validateXMLfile");
        
        File xmlFile = new File(this.getClass().getResource("PLM_12.xml").getFile());
        File schemaFile = new File(this.getClass().getResource("similexmlv3.xsd").getFile());
        XMLValidator instance = new XMLValidator();
        instance.validateXMLfile(xmlFile, schemaFile);
        
    }
    
    @Test
    public void testValidateXMLfileComplicatedWrong() throws Exception {
        System.out.println("validateXMLfile");
        
        File xmlFile = new File(this.getClass().getResource("PLM_12-wrong.xml").getFile());
        File schemaFile = new File(this.getClass().getResource("similexmlv3.xsd").getFile());
        XMLValidator instance = new XMLValidator();
        try {
        instance.validateXMLfile(xmlFile, schemaFile);
            fail("Expected exception");
        } catch (XMLValidator.NotValidXML e) {
            System.out.println("As expected Math:");
            e.printStackTrace(System.out);
        }
        
    }
    
    @Test
    public void testValidateXMLNofile() throws Exception {
        System.out.println("validateXMLNofile");
        
        File xmlFile = new File(this.getClass().getResource("sbmlL2V3.xml").getFile()+".nothere");
        File schemaFile = new File(this.getClass().getResource("sbmlL2V3.xsd").getFile());
        XMLValidator instance = new XMLValidator();
        try {
            instance.validateXMLfile(xmlFile, schemaFile);
            fail("Expected exception");
        } catch (XMLValidator.NotValidXML e) {
            System.out.println("As expected");
            e.printStackTrace(System.out);
        }
        
    }
    
    @Test
    public void testValidateXMLNoXML() throws Exception {
        System.out.println("validateXMLNoXML");
        
        File xmlFile = new File(this.getClass().getResource("timet-logo-sm.png").getFile());
        File schemaFile = new File(this.getClass().getResource("sbmlL2V3.xsd").getFile());
        XMLValidator instance = new XMLValidator();
        try {
            instance.validateXMLfile(xmlFile, schemaFile);
            fail("Expected exception");
        } catch (XMLValidator.NotValidXML e) {
            System.out.println("As expected");
            e.printStackTrace(System.out);
        }
        
    }
    
    
    @Test
    public void testValidateXMLWrongFormat() throws Exception {
        System.out.println("validateXMLWrongFormat");
        
        File xmlFile = new File(this.getClass().getResource("sbmlL2V3-wrong.xml").getFile());
        File schemaFile = new File(this.getClass().getResource("sbmlL2V3.xsd").getFile());
        XMLValidator instance = new XMLValidator();
        try {
            instance.validateXMLfile(xmlFile, schemaFile);
            fail("Expected exception");
        } catch (XMLValidator.NotValidXML e) {
            System.out.println("As expected");
            e.printStackTrace(System.out);
        }
        
    }
    
    
}