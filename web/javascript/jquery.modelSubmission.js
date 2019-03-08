(function($) {
	
	$.fn.publicationInput = function(options) {
		
		var options = $.extend({}, $.fn.publicationInput.defaults, options);
		
		return this.each(function(){
			
			var entriesIdx = options.entriesIdx;
			
			$("#"+options.addJournalButtonId).click(function(e) {
				
				e.preventDefault();

				var updateString = "<div id='"+entriesIdx+"_inputFieldsContainer'><table><tbody><tr>" +
				"<td>" +
				"Journal:<span class='errorMessage'>*</span> " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Journal::The name of the Journal in which the reference is published. Can also include volume, issue and page numbers.' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].periodicalName\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Title:<span class='errorMessage'>*</span> " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Title::The title of the journal article' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].title\" size=\"70\" /></td></tr>" +
				"<tr>" +
				"<td>" +
				"Authors:<span class='errorMessage'>*</span> " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Authors::The authors of the publcation' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].authors\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Year:<span class='errorMessage'>*</span> " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Year::The year of publcation' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].year\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"URL: " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='URL::An online citation of the reference associated with the model. E.g. PubMed citation, Digital Object Identifier (DOI), or a URL pointing directly to the reference.' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].url\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Abstract: " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Abstract::The abstract text for the specified reference' />" +
				"</td><td><textarea name=\"submission.publications["+entriesIdx+"].abstract\" cols=\"70\" rows=\"5\" ></textarea></td></tr>" +
				"</tbody></table>" +
				"<input type='hidden' name=\"submission.publications["+entriesIdx+"].referenceType\" value=\"Journal Article\" />" +
				"</div>";
				
				var container = $("<div/>");
				container.attr("id", entriesIdx+"_publicationInputContainer");
				container.addClass("subcontainer");
				
				container.html(updateString);
				
				$("#"+options.injectElement).before(container);
				
				$("."+entriesIdx+"ToolTip").tooltip({
					showURL: false,
					showBody: "::"
				});
				
				entriesIdx++;
				
			});
			
			$("#"+options.addBookRefButtonId).click(function(e) {
				e.preventDefault();
				
				var updateString = "<div id='"+entriesIdx+"_inputFieldsContainer'><table><tbody><tr>" +
				"<td>" +
				"Book Title:<span class='errorMessage'>*</span> " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Book Title::The title of the book' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].title\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Year:<span class='errorMessage'>*</span> " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Year::The year of publcation' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].year\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Book Authors: " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Book Authors::The authors of the book' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].authors\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Chapter Title: " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Chapter Title::The chapter of the book containing the description of the model' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].secondaryTitle\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Chapter Authors: " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Chapter Authors::The authors of the chapter containing the description of the model' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].secondaryAuthors\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Pages: " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Pages::The page numbers containing the description of the model' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].pages\" size=\"70\" /></td></tr>" +
				"<tr><td>" +
				"Publisher: " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='Publisher::The book publisher' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].publisher\" size=\"70\" /></td></tr>" +
				"<tr><td>ISBN:</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].isbn\" size=\"70\" /></td></tr>" +
				"<tr>" +
				"<td>URL: " +
				"<img class='"+entriesIdx+"ToolTip' src='../images/tip.gif' title='URL::An online citation of the reference associated with the model. E.g. Digital Object Identifier (DOI), URL pointing directly to the reference.' />" +
				"</td><td><input type='text' name=\"submission.publications["+entriesIdx+"].url\" size=\"70\" /></td></tr>" +
				"</tbody></table>" +
				"<input type='hidden' name=\"submission.publications["+entriesIdx+"].referenceType\" value=\"Book\" />" +
				"</div>";
				
				var container = $("<div/>");
				container.attr("id", entriesIdx+"_publicationInputContainer");
				container.addClass("subcontainer");
				
				container.html(updateString);
				
				$("#"+options.injectElement).before(container);
				
				$("."+entriesIdx+"ToolTip").tooltip({
					showURL: false,
					showBody: "::"
				});
				
				entriesIdx++;
				
			});
			
			$("#"+options.removeRefButtonId).click(function(e) {
				e.preventDefault();
				
				if(entriesIdx > options.minEntries) {
					entriesIdx --;
					$("#"+entriesIdx+'_publicationInputContainer').remove();
				}
				
			});
			
		});
		
	}


	$.fn.publicationInput.defaults = {
		addJournalButtonId: "addJournalRef",
		addBookRefButtonId: "addBookRef",
		removeRefButtonId: "removePub",
		entriesIdx: 0,
		minEntries: 0,
		insertWhere: "before",
		injectElement:'pubButtonContainer'
	};
	
	$.fn.dataFileInput = function(options) {
		var options = $.extend({}, $.fn.dataFileInput.defaults, options);
		
		return this.each(function(){
			
			var entriesIdx = options.entriesIdx;
			
			$("#"+options.addFileButtonId).click(function(e) {
				e.preventDefault();
				
				var updateString = "<div id='"+entriesIdx+"_dataFileInputFieldsContainer'>" +
				"<p>Data File associated the model: <input type='file' name=\"submission.supplementaryFiles["+entriesIdx+"].dataFile\" size=\"55\" /></p>" +
				"<p>Description of data file : <input type='text' name=\"submission.supplementaryFiles["+entriesIdx+"].description\" size=\"70\" /></p>" +
				"</div>";
				
				var container = $("<div/>");
				
				container.attr("id", entriesIdx+"_dataFileInputContainer");
				container.addClass("subcontainer");
				
				container.html(updateString);
				
				$("#"+options.injectElement).before(container);
				
				entriesIdx++;
				
			});
			
			$("#"+options.removeFileButtonId).click(function(e) {
				e.preventDefault();
				
				if(entriesIdx > options.minEntries) {
					entriesIdx --;
					$("#"+entriesIdx+'_dataFileInputContainer').remove();
				}
			});
			
		});
		
	}
	
	$.fn.dataFileInput.defaults = {
		addFileButtonId: "addDF",
		removeFileButtonId: "removeDF",
		entriesIdx: 1,
		minEntries: 1,
		insertWhere: "before",
		injectElement:'dfButtonContainer'
	};
	
	$.fn.imageFileInput = function(options) {
		var options = $.extend({}, $.fn.imageFileInput.defaults, options);
		
		return this.each(function(){
			
			var entriesIdx = options.entriesIdx;
			
			$("#"+options.addFileButtonId).click(function(e) {
				e.preventDefault();
				
				var updateString = "<div id='"+entriesIdx+"_imageFileInputFieldsContainer'>" +
				"<p>Image/Screen shot of the model: <input type='file' name=\"submission.images["+entriesIdx+"].dataFile\" size=\"55\" /></p>" +
				"<p>Description of image: <input type='text' name=\"submission.images["+entriesIdx+"].description\" size=\"70\" /></p>" +
				"</div>";
				
				var container = $("<div/>");
				
				container.attr("id", entriesIdx+"_imageFileInputContainer");
				container.addClass("subcontainer");
				
				container.html(updateString);
				
				$("#"+options.injectElement).before(container);
				
				entriesIdx++;
		
			});
			
			$("#"+options.removeFileButtonId).click(function(e) {
				e.preventDefault();
				
				if(entriesIdx > options.minEntries) {
					entriesIdx --;
					$("#"+entriesIdx+'_imageFileInputContainer').remove();
				}
			});
		});
	}
	
	$.fn.imageFileInput.defaults = {
		addFileButtonId: "addImg",
		removeFileButtonId: "removeImg",
		entriesIdx: 1,
		minEntries: 1,
		insertWhere: "before",
		injectElement:'imgButtonContainer'
	}
	
	$.fn.newAttributeInput = function(options) {
		var options = $.extend({}, $.fn.newAttributeInput.defaults, options);
		
		return this.each(function(){
			var entriesIdx = options.entriesIdx;
			
			$("#"+options.addAttributeButtonId).click(function(e) {
				e.preventDefault();
				
				var updateString = "<table><tr>" +
				"<td colspan=\"2\">New Attribute</td>" +
				"</tr><tr>" +
				"<td>Name:</td><td><input type='text' name=\"submission.attributes["+entriesIdx+"].name\" size=\"55\" /></td>" +
				"</tr><tr>" +
				"<td>Value:</td><td><textarea name=\"submission.attributes["+entriesIdx+"].value\" id=\"attInput"+entriesIdx+"\" rows=\"6\" cols=\"80\"></textarea></td>" +
				"</tr></table>";
				
				var container = $("<div/>");
				
				container.attr("id", entriesIdx+"_attributeContainer");
				container.addClass("subcontainer");
				
				container.html(updateString);
				
				$("#"+options.injectElement).before(container);
				
				$('#attInput'+entriesIdx).ckeditor(
		function() {},
		{
			toolbar: 'Basic',
		 	height:180, 
		 	width:550,
		 	uiColor: '#CBF5CB',
		 	resize_enabled: false
		} 
	);
				
				entriesIdx++;
			});
			
			$("#"+options.removeAttributeButtonId).click(function(e) {
				e.preventDefault();
				
				if(entriesIdx > options.minEntries) {
					entriesIdx --;
					var editor = $('#attInput'+entriesIdx).ckeditorGet();
					editor.destroy();
					$("#"+entriesIdx+'_attributeContainer').remove();
					
				}
			});
			
		});
	}
	
	$.fn.newAttributeInput.defaults = {
		addAttributeButtonId: "addAttr",
		removeAttributeButtonId: "removeAttr",
		entriesIdx: 0,
		minEntries: 0,
		insertWhere: "before",
		injectElement:"attrButtonContainer"
	}
	
})(jQuery);