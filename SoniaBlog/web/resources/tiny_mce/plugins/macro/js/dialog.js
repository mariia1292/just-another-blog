tinyMCEPopup.requireLangPack();

var MacroDialog = {
  init : function() {
  },

  insert : function(content) {
    tinyMCEPopup.editor.execCommand('mceInsertContent', false, content);
    tinyMCEPopup.close();
  }

};

tinyMCEPopup.onInit.add(MacroDialog.init, MacroDialog);
