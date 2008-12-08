tinyMCEPopup.requireLangPack();

var AttachmentDialog = {
  init : function() {
  },

  insert : function(url,name) {
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
  }

};

tinyMCEPopup.onInit.add(AttachmentDialog.init, AttachmentDialog);
