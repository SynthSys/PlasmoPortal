package uk.ac.ed.plasmo.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * The {@code ErrorHandler} class picks up errors found when validating XML
 * against a prticular XML schema. Errors are stored as a list of Strings 
 * so they can later be retrieved by the application
 * @author ctindal
 *
 */
public class ErrorHandler extends DefaultHandler {
	
private List<String> errorList; //contains the list of validation errors
	List<Location> locations;
        
	public ErrorHandler() {
		errorList = new ArrayList<>();
                locations = new ArrayList<>();
	}
	
	public List<String> getErrorList() {
		return errorList;
	}

	@Override
	public void error(SAXParseException ex) throws SAXException {
                Location loc = new Location(ex.getLineNumber(), ex.getColumnNumber());
                if (locations.contains(loc)) throw ex;
                else {
                    locations.add(loc);
                    errorList.add("Validation Error: Location "+ ex.getLineNumber()+":"+ex.getColumnNumber()+". Message: "+ex.getMessage());
                }
	}

	@Override
	public void fatalError(SAXParseException ex) throws SAXException {
		errorList.add("Fatal error: Location "+ ex.getLineNumber()+":"+ex.getColumnNumber()+". Message: "+ex.getMessage());
                throw ex;
	}

	@Override
	public void warning(SAXParseException ex) throws SAXException {
		errorList.add("Validation warning: Location "+ ex.getLineNumber()+":"+ex.getColumnNumber()+". Message: "+ex.getMessage());
	}
        
        static class Location {
            int line;
            int column;
            Location(int line,int column) {
                this.line = line;
                this.column = column;
            }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + this.line;
            hash = 73 * hash + this.column;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Location other = (Location) obj;
            if (this.line != other.line) {
                return false;
            }
            if (this.column != other.column) {
                return false;
            }
            return true;
        }
            
            
        }
}
