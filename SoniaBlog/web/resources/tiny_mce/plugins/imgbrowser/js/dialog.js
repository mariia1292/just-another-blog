tinyMCEPopup.requireLangPack();

var ImageDialog = {
	init : function() {
	},

	insert : function(url) {
		var content = "<img src=\"" + url + "\" />";
		tinyMCEPopup.editor.execCommand('mceInsertContent', false, content);
		tinyMCEPopup.close();
	}
};

tinyMCEPopup.onInit.add(ImageDialog.init, ImageDialog);
