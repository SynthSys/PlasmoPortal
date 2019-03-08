package uk.ac.ed.plasmo.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;

import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.assembler.TransformModelAssembler;

import com.opensymphony.xwork2.ActionSupport;
import uk.ac.ed.plasmo.xml.XSLTTransformer;
/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve a model (as an input stream) and the file system location of 
 * the xsl file used to transform the model into an alternative form.</p>
 * <p>The class is responsible for transforming the model xml into another format 
 * (e.g. xhtml to display the content of the model in the browser.</p>  
 * @author ctindal
 *
 */
public class TransformModel extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private String tf; //numerical id of xslt file
	private String accession; //the accession id of the model to be transformed
	private String version; //the version no. of the model to be transformed
	private String xsltFileLocation; //the filesystem location of the xslt file
	private Document model;
	
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getXsltFileLocation() {
		return xsltFileLocation;
	}
	public void setXsltFileLocation(String xsltFileLocation) {
		this.xsltFileLocation = xsltFileLocation;
	}
	public Document getModel() {
		return model;
	}
	public void setModel(Document model) {
		this.model = model;
	}
	public String getTf() {
		return tf;
	}
	public void setTf(String tf) {
		this.tf = tf;
	}
	
        @Override
	public String execute() {
		
            
		TransformModelAssembler tfmAssembler = new TransformModelAssembler();
		
		xsltFileLocation = tfmAssembler.getXSLTFileLocForModelVsn(tf, accession, version);
		
		if(xsltFileLocation==null) {
			return INPUT;
		}
		File stylesheet = new File(xsltFileLocation);
                if (!stylesheet.exists()) return INPUT;
		
		ModelAssembler assembler = new ModelAssembler();
		
		//InputStream modelIs = assembler.getModelAsInputStream(accession, version, DOWNLOAD_TYPE.ORIGINAL);
                String modelTXT = assembler.getModelAsString(accession, version);
		if (modelTXT == null) return INPUT;
                
                HttpServletResponse response = ServletActionContext.getResponse();
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");
                
                try (PrintWriter out = response.getWriter()) {
                    StreamResult result = new StreamResult(out);

                    XSLTTransformer.transform(modelTXT, stylesheet,result, null);
                
                    out.flush();
                    return null;
                } catch(IOException| XSLTTransformer.XMLException e) {
                    System.out.println("Error in model transformation: "+e.getMessage());
                    if (e.getCause() != null) System.out.println("Caused by: "+e.getCause().getMessage()+"\n"+e.toString());
                    return INPUT;
                }
                
		
	}
}
