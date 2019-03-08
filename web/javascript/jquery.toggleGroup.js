(function($) {
	
	$.fn.toggleGroup = function(options) {
		
		var options = $.extend({}, $.fn.toggleGroup.defaults, options);
		options.headers = this.find(options.header) ;
		return this.each(function() {
			options.headers.each(function(index) {
				
				var obj = $(this);
				obj.css("cursor", "pointer");
				var openEl = isOpen(obj, options.openTabs, index);
				
				if(options.toggleIcons != undefined) {
					var holder = $("."+options.toggleIcons.holderClass, obj);
					var icon = $("<img/>");
					icon.addClass(options.toggleIcons.iconClass);
					if(openEl) {
						icon.attr("src", options.toggleIcons.openIcon);
					}
					else {
						icon.attr("src", options.toggleIcons.closedIcon);
					}
					
					holder.append(icon);
					
				}
			
				obj.click(function() {
					
					openEl = !openEl;
					if(options.toggleIcons != undefined) {
						var icon = $("img", obj.children("."+options.toggleIcons.holderClass));
						if(openEl) {
							icon.attr("src", options.toggleIcons.openIcon);
						}
						else {
							icon.attr("src", options.toggleIcons.closedIcon);
						}
					}
					obj.next().slideToggle(options.toggleSpeed);
				});
			});
		});
	}
	
	function isOpen(header, openTabs, index) {
		if(openTabs != undefined) {
			if(typeof openTabs == "number") {
				if(openTabs != index) {
					header.next().hide();
					return false;
				}
				else {
					return true;
				}
			}
			else if(typeof openTabs == "string") {
				if(openTabs == "none") {
					header.next().hide();
					return false;
				}
				else {
					return true;
				}
			}
			return true;
		}
		return true;
	}

	$.fn.toggleGroup.defaults = {
		selectedClass : "selected",
		header: "a",
		openTabs: "all", //can be a number (index), 'none' or 'all'
		toggleSpeed: "normal"
	};
	
})(jQuery);