var PublicationInputUpdater = new Class({
	Implements:[Options, Events],
	options: {
		injectElement:'addPubButtonContainer',
		injectWhere: '',
		index: 0,
		minInputs: 0
	},
	initialize:function(options){
		this.setOptions(options);
	},
	removeContent:function() {
		if(this.options.index > this.options.minInputs) {
			this.options.index --;
			$(this.options.index+'_publicationInputContainer').destroy();
		}	
		return false;
	},
	addJournalRef: function() {
		var updateString = "";//"<p>ID of Publication:<span class='errorMessage'>*</span> <input type='text' name='publications["+this.options.index+"].id'/>";
	
		var publicationIdTypes = new Array('PUBMED','DOI','URL','OFFLINE');
	
		/*for(var i=0;i<publicationIdTypes.length;i++){
			updateString += " <input type='radio' name='publications["+this.options.index+"].idType' id='"+this.options.index+"_pubType"+publicationIdTypes[i]+"' value='"+publicationIdTypes[i]+"'/>" +
					"<label for="+this.options.index+"_pubType"+publicationIdTypes[i]+"'>"+publicationIdTypes[i]+"</label>";
		}
		updateString +="</p>";*/
		
		//<img class='toolTip' src='' title='Journal::The name of the Journal in which the reference is published. Can also include volume, issue and page numbers.' />
		
		updateString += "<div id='"+this.options.index+"_inputFieldsContainer'><table><tbody><tr>" +
				"<td>" +
				"Journal:<span class='errorMessage'>*</span> " +
				"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Journal::The name of the Journal in which the reference is published. Can also include volume, issue and page numbers.' />" +
				"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].periodicalName\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Title:<span class='errorMessage'>*</span> " +
				"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Title::The title of the journal article' />" +
				"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].title\" size=\"70\" /></td></tr>" +
				"<tr>" +
				"<td>" +
				"Authors:<span class='errorMessage'>*</span> " +
				"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Authors::The authors of the publcation' />" +
				"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].authors\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Year:<span class='errorMessage'>*</span> " +
				"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Year::The year of publcation' />" +
				"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].year\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"URL: " +
				"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='URL::An online citation of the reference associated with the model. E.g. PubMed citation, Digital Object Identifier (DOI), or a URL pointing directly to the reference.' />" +
				"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].url\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Abstract: " +
				"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Abstract::The abstract text for the specified reference' />" +
				"</td><td><textarea name=\"submission.publications["+this.options.index+"].abstract\" cols=\"70\" rows=\"5\" ></textarea></td></tr>" +
				"</tbody></table>" +
				"<input type='hidden' name=\"submission.publications["+this.options.index+"].referenceType\" value=\"Journal Article\" />" +
				"</div>";

		var publicationInputContainer = new Element ('div', 
		{
			'id':this.options.index+'_publicationInputContainer',
			'class': 'subcontainer'
		});
	
		publicationInputContainer.set('html',updateString);
		publicationInputContainer.inject(this.options.injectElement,this.options.injectWhere);
		
		$$('.'+this.options.index+'ToolTip').each(function(element, index) {
            var content = element.get('title').split('::');
            element.store('tip:title',content[0]);
            element.store('tip:text', content[1]);
        });
		
		tips.attach($$('.'+this.options.index+'ToolTip'))
		
		this.options.index ++;
		return false;
	},
	addBookRef: function() {
		var updateString = "<div id='"+this.options.index+"_inputFieldsContainer'><table><tbody><tr>" +
		"<td>" +
		"Book Title:<span class='errorMessage'>*</span> " +
		"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Book Title::The title of the book' />" +
		"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].title\" size=\"70\" /></td></tr>" +
		"<tr><td>" +
		"Year:<span class='errorMessage'>*</span> " +
		"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Year::The year of publcation' />" +
		"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].year\" size=\"70\" /></td></tr>" +
		"<tr><td>" +
		"Book Authors: " +
		"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Book Authors::The authors of the book' />" +
		"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].authors\" size=\"70\" /></td></tr>" +
		"<tr><td>" +
		"Chapter Title: " +
		"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Chapter Title::The chapter of the book containing the description of the model' />" +
		"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].secondaryTitle\" size=\"70\" /></td></tr>" +
		"<tr><td>" +
		"Chapter Authors: " +
		"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Chapter Authors::The authors of the chapter containing the description of the model' />" +
		"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].secondaryAuthors\" size=\"70\" /></td></tr>" +
		"<tr><td>" +
		"Pages: " +
		"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Pages::The page numbers containing the description of the model' />" +
		"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].pages\" size=\"70\" /></td></tr>" +
		"<tr><td>" +
		"Publisher: " +
		"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='Publisher::The book publisher' />" +
		"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].publisher\" size=\"70\" /></td></tr>" +
		"<tr><td>ISBN:</td><td><input type='text' name=\"submission.publications["+this.options.index+"].isbn\" size=\"70\" /></td></tr>" +
		"<tr>" +
		"<td>URL: " +
		"<img class='"+this.options.index+"ToolTip' src='../images/tip.gif' title='URL::An online citation of the reference associated with the model. E.g. Digital Object Identifier (DOI), URL pointing directly to the reference.' />" +
		"</td><td><input type='text' name=\"submission.publications["+this.options.index+"].url\" size=\"70\" /></td></tr>" +
		"</tbody></table>" +
		"<input type='hidden' name=\"submission.publications["+this.options.index+"].referenceType\" value=\"Book\" />" +
		"</div>";
		
		var publicationInputContainer = new Element ('div', 
		{
			'id':this.options.index+'_publicationInputContainer',
			'class': 'subcontainer'
		});
			
		publicationInputContainer.set('html',updateString);
		publicationInputContainer.inject(this.options.injectElement,this.options.injectWhere);
		
		$$('.'+this.options.index+'ToolTip').each(function(element, index) {
            var content = element.get('title').split('::');
            element.store('tip:title',content[0]);
            element.store('tip:text', content[1]);
        });
		
		tips.attach($$('.'+this.options.index+'ToolTip'))
		
		this.options.index ++;
		return false;
	}
});

var DataFileInputUpdater = new Class({
	Implements:[Options, Events],
	options: {
		injectElement:'',
		injectWhere:'',
		index:0,
		minInputs: 0
	},
	initialize: function(options){
		this.setOptions(options);
	},
	addContent: function() {
		var updateString = "<div id='"+this.options.index+"_dataFileInputFieldsContainer'>" +
				"<p>Data File associated the model: <input type='file' name=\"submission.supplementaryFiles["+this.options.index+"].dataFile\" size=\"55\" /></p>" +
				"<p>Description of data file : <input type='text' name=\"submission.supplementaryFiles["+this.options.index+"].description\" size=\"70\" /></p>" +
				"</div></div>";
        
        var dataFileInputContainer = new Element ('div', 
		{
			'id':this.options.index+'_dataFileInputContainer',
			'class': 'subcontainer'
		});  
          
          
        dataFileInputContainer.set('html',updateString);
		dataFileInputContainer.inject(this.options.injectElement,this.options.injectWhere);
		this.options.index ++;
		return false;
		
	},
	removeContent: function() {
		if(this.options.index > this.options.minInputs) {
			this.options.index --;
			$(this.options.index+'_dataFileInputContainer').destroy();
		}	
		return false;
	}
});

var ImageFileInputUpdater = new Class({
	Implements:[Options, Events],
	options: {
		injectElement:'',
		injectWhere:'',
		index:0,
		minInputs: 0
	},
	initialize: function(options){
		this.setOptions(options);
	},
	addContent: function() {
		var updateString = "<div id='"+this.options.index+"_imageFileInputFieldsContainer'>" +
		"<p>Image/Screen shot of the model: <input type='file' name=\"submission.images["+this.options.index+"].dataFile\" size=\"55\" /></p>" +
		"<p>Description of image: <input type='text' name=\"submission.images["+this.options.index+"].description\" size=\"70\" /></p>" +
		"</div></div>";
		
		var imageFileInputContainer = new Element ('div', 
		{
			'id':this.options.index+'_imageFileInputContainer',
			'class': 'subcontainer'
		});  
          
		imageFileInputContainer.set('html',updateString);
		imageFileInputContainer.inject(this.options.injectElement,this.options.injectWhere);
		this.options.index ++;
		return false;
	},
	removeContent: function() {
		if(this.options.index > this.options.minInputs) {
			this.options.index --;
			$(this.options.index+'_imageFileInputContainer').destroy();
		}	
		return false;
	}
});

var NewAttributeInput = new Class({
	Implements:[Options, Events],
	options: {
		injectElement:'',
		injectWhere:'',
		index:0
	},
	initialize: function(options){
		this.setOptions(options);
	},
	addAttributeInput: function() {
		var updateString = "<table><tr>" +
				"<td colspan=\"2\">New Attribute</td>" +
				"</tr><tr>" +
				"<td>Name:</td><td><input type='text' name=\"submission.attributes["+this.options.index+"].name\" size=\"55\" /></td>" +
				"</tr><tr>" +
				"<td>Value:</td><td><textarea name=\"submission.attributes["+this.options.index+"].value\" rows=\"6\" cols=\"80\"></textarea></td>" +
				"</tr></table>";
		
		var attributeContainer = new Element ('div', 
		{
			'id':this.options.index+'_attributeContainer',
			'class': 'subcontainer'
		});
		
		attributeContainer.set('html', updateString);
		attributeContainer.inject(this.options.injectElement, this.options.injectWhere);
		this.options.index++;
		return false;
	},
	removeAttributeInput: function() {
		if(this.options.index > 0) {
			this.options.index --;
			$(this.options.index+'_attributeContainer').destroy();
		}	
		return false;
	}
});