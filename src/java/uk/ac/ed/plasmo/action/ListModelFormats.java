package uk.ac.ed.plasmo.action;

import java.util.List;

import uk.ac.ed.plasmo.assembler.ModelFormatAssembler;

import com.opensymphony.xwork2.ActionSupport;
/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve all the model formats currently stored in the database</p> 
 * @author ctindal
 *
 */
public class ListModelFormats extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	private List<String> formats;
	
	public ListModelFormats() {
		ModelFormatAssembler assembler = new ModelFormatAssembler();
		formats = assembler.getModelFormats();
	}
	
	public List<String> getFormats() {
		return formats;
	}

	public void setFormats(List<String> formats) {
		this.formats = formats;
	}
	
}
