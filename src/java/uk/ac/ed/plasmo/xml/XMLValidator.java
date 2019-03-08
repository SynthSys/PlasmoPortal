package uk.ac.ed.plasmo.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * class used to validate XML documents in various input formats against a specific XML Schema
 * @author ctindal
 *
 */
public class XMLValidator {
	
        final static Map<File,Schema> schemas = new ConcurrentHashMap<>();
	static SchemaFactory factory;

	public void validateXML(String model, File schemaFile) throws NotValidXML, NotValidSchema {
            
            Source source = new StreamSource(new StringReader(model));
            validateXML(source,schemaFile);
        }        
        
        
	public void validateXMLfile(File xmlFile, File schemaFile) throws NotValidXML, NotValidSchema {

            Source source = new StreamSource(xmlFile);
            validateXML(source,schemaFile);
            
	}
        
	public void validateXML(Source source, File schemaFile) throws NotValidXML, NotValidSchema {

            
            Schema schema = getSchemaFromFile(schemaFile);
            Validator validator = schema.newValidator();
            
            try {
            validator.validate(source);
            } catch (SAXException e) {
                throw new NotValidXML(e.getMessage(),e);
            } catch (IOException e) {
                throw new NotValidXML("Could not read the xml: "+e.getMessage(),e);
            }
            
	}
        

    protected Schema getSchemaFromFile(File schemaFile) throws NotValidSchema {
        
        Schema schema = schemas.get(schemaFile);
        if (schema != null) return schema;
        
        return createNewSchema(schemaFile);
        
    }

    protected Schema createNewSchema(File schemaFile) throws NotValidSchema {
        try {
        Schema schema = getSchemaFactory().newSchema(schemaFile);
        schemas.put(schemaFile, schema);
        return schema;
        } catch (SAXException e) {
            throw new NotValidSchema(e.getMessage(), e);
        }
    }
    
    protected SchemaFactory getSchemaFactory() {
        if (factory == null) {
            factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        }
        return factory;
    }
        
        
        public static class NotValidXML extends Exception {
            private static final long serialVersionUID = 1L;

            public NotValidXML(String msg) {
                super(msg);
            }
            public NotValidXML(String msg,Throwable e) {
                super(msg,e);
            }

        }
        
        public static class NotValidSchema extends Exception {
            private static final long serialVersionUID = 1L;

            public NotValidSchema(String msg) {
                super(msg);
            }
            public NotValidSchema(String msg,Throwable e) {
                super(msg,e);
            }

        }
        
}

