tinyMCEPopup.requireLangPack();

var AttachmentDialog = {
  init : function() {
  },

  insertLink : function(url,name) {
    url = tinyMCEPopup.editor.documentBaseURI.toAbsolute(url);
    var c = tinyMCEPopup.editor.selection.getContent();
    if ( c != "" )
    {
      name = c;
    }
    var newwindow = document.getElementById("newwindow")
    var target = "_self";
    if ( newwindow.checked )
    {
      target = "_blank";
    }

    var content = "<a target=\"" + target + "\" href=\"" + url + "\">" + name + "</a>";

    tinyMCEPopup.editor.execCommand('mceInsertContent', false, content);
    tinyMCEPopup.close();
  },

  insert : function(value) {
    tinyMCEPopup.editor.execCommand('mceInsertContent', false, value);
    tinyMCEPopup.close();
  }


};

tinyMCEPopup.onInit.add(AttachmentDialog.init, AttachmentDialog);
