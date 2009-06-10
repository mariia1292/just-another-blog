tinyMCEPopup.requireLangPack();

var MacroDialog = {
  init : function() {
    alert( "init" );
  },

  message : function() {
    alert( "message" );
  }

};

tinyMCEPopup.onInit.add(MacroDialog.init, MacroDialog);
