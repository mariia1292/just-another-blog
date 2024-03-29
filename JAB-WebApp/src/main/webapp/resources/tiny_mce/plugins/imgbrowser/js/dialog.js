tinyMCEPopup.requireLangPack();

var ImageDialog = {
  init : function() {
  },

  insert : function(url) {
    url = tinyMCEPopup.editor.documentBaseURI.toAbsolute(url);
    var r = document.getElementsByName("size:type");
    for ( i in r )
    {
      if ( r[i].checked && r[i].value != "" )
      {
        url += "?type=" + r[i].value;
      }
    }

    var content = "<img src=\"" + url + "\" />";
    tinyMCEPopup.editor.execCommand('mceInsertContent', false, content);
    document.getElementById("size").submit();
    tinyMCEPopup.close();
  },

  insertGallery : function() {
    tinyMCEPopup.editor.execCommand('mceInsertContent', false, "{gallery}{/gallery}");
    tinyMCEPopup.close();
  }

};

tinyMCEPopup.onInit.add(ImageDialog.init, ImageDialog);
