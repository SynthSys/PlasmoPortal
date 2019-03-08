package uk.ac.ed.plasmo.action;

import java.util.List;

import uk.ac.ed.plasmo.assembler.ModelFormatAssembler;

import com.opensymphony.xwork2.ActionSupport;

public class ModelOperations extends ActionSupport {

	private static final long serialVersionUID = 1L;
	String msg;

	private List<String> formats;
	
	
	public List<String> getFormats() {
		ModelFormatAssembler assembler = new ModelFormatAssembler();
		formats = assembler.getModelFormats();
		return formats;
	}

	public void setFormats(List<String> formats) {
		this.formats = formats;
	}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
        
        
}
