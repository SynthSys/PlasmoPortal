/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.plasmo.xml;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Utility class that apllies given xslt tranformation to the xml
 * @author tzielins
 */
public class XSLTTransformer {
    
    protected static TransformerFactory transformerFactory;
    
    /**
     * Performs xslt transformation
     * @param xml input xml document
     * @param xsltTemplate file with xslt template
     * @param params map of transformation parameters (xslt variables) that will be passed to transformer to customiz the xslt template. If null or empty than ignored.
     * @return transforemd document
     * @throws uk.ac.ed.plasmo.xml.XSLTTransformer.XMLException if there was xml-error
     */
    public static String transform(String xml,File xsltTemplate,Map<String,String> params) throws XMLException {
        
           Source source = new StreamSource(new StringReader(xml));
           StringWriter writer = new StringWriter();
           Result result = new StreamResult(writer);
           transform(source, xsltTemplate, result, params);
           return writer.toString();
    };

    public static void transform(String xml, File xsltTemplate, Result result, Map<String,String> params) throws XMLException {
        Source source = new StreamSource(new StringReader(xml));
        transform(source,xsltTemplate,result,params);
    }
    
    public static void transform(Source source, File xsltTemplate, Result result, Map<String,String> params) throws XMLException {
        try {
           Transformer trans = getTransformerFromFile(xsltTemplate);
           if (params != null) {
               for (Map.Entry<String,String> entry : params.entrySet())
                trans.setParameter(entry.getKey(), entry.getValue());
           }
           
           trans.transform(source, result);
        } catch (TransformerException e) {
            throw new XMLException(e);
        }        
    }
    private static Transformer getTransformerFromFile(File xsltTemplate) throws TransformerConfigurationException {
        
            StreamSource stylesource = new StreamSource(xsltTemplate);
            Transformer trans = getTransformerFactory().newTransformer(stylesource);
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            return trans;
    }

    protected synchronized static TransformerFactory getTransformerFactory() {
            if (transformerFactory == null)
            {
                    transformerFactory = TransformerFactory.newInstance();
            }
            return transformerFactory;
    }

    
    
    public static class XMLException extends Exception {
        private static final long serialVersionUID = 1L;
        
        public XMLException(Exception e) {
            super(e.getMessage(),e);
        }
    }
    
    
}
