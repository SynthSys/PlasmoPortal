package uk.ac.ed.plasmo.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * The {@code SchemaValidator} class validates xml schema.
 * @author ctindal
 *
 */
public class SchemaValidator {
	
	/**
	 * validate a schema file. This simply involves creating a JAXP-based schema object from a specified file.
	 * If the content of the file is valid XML Schema there will be no errors.
	 * @param schemaFile
	 * @return
	 */
	public static synchronized List<String> validateSchema(File schemaFile){
		
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		ErrorHandler errorHandler = new ErrorHandler();
		factory.setErrorHandler(errorHandler);
		try {
			Schema schema = factory.newSchema(schemaFile);
		}
		catch (SAXException e) {
                        List<String> errorList = new ArrayList<>();
                        errorList.add("Not valid schea: "+e.getMessage());
                        return errorList;
		}
		
		return errorHandler.getErrorList();
		
	}

}
