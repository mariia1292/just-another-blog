/**
 * $Id: editor_plugin_src.js 201 2007-02-12 15:56:56Z spocke $
 *
 * @author Moxiecode
 * @copyright Copyright � 2004-2008, Moxiecode Systems AB, All rights reserved.
 */

(function() {

  tinymce.create('tinymce.plugins.ImgResizePlugin', {

    /**
		 * Initializes the plugin, this will be executed after the plugin has been created.
		 * This call is done before the editor instance has finished it's initialization so use the onInit event
		 * of the editor instance to intercept that event.
		 *
		 * @param {tinymce.Editor} ed Editor instance that the plugin is initialized in.
		 * @param {string} url Absolute URL to where the plugin is located.
		 */
    init : function(ed, url) {
      var handlerPattern = ed.getParam("imageHandlerPattern");
      ed.onMouseUp.add( function(ed, e){
        var el = tinyMCE.activeEditor.selection.getNode();
        if ( el.nodeName == "IMG" && ed.dom.getAttrib(el, "src").indexOf(handlerPattern) >= 0)
        {
          setTimeout(function(){
            fixSize(ed, el);
          }, 100);
        }
      }
      );
    },

    /**
		 * Creates control instances based in the incomming name. This method is normally not
		 * needed since the addButton method of the tinymce.Editor class is a more easy way of adding buttons
		 * but you sometimes need to create more complex controls like listboxes, split buttons etc then this
		 * method can be used to create those.
		 *
		 * @param {String} n Name of the control to create.
		 * @param {tinymce.ControlManager} cm Control manager to use inorder to create new control.
		 * @return {tinymce.ui.Control} New control instance or null if no control was created.
		 */
    createControl : function(n, cm) {
      return null;
    },

    /**
		 * Returns information about the plugin as a name/value array.
		 * The current keys are longname, author, authorurl, infourl and version.
		 *
		 * @return {Object} Name/value array containing information about the plugin.
		 */
    getInfo : function() {
      return {
        longname : 'ImageResize Plugin',
        author : 'Sebastian Sdorra',
        authorurl : 'http://kenai.com/projects/jab',
        infourl : 'http://kenai.com/projects/jab',
        version : "0.1"
      };
    }
  });

  function fixSize(ed, el)
  {
    var src = ed.dom.getAttrib(el, "src");
    var paramIndex = src.indexOf( "?" );
    if ( paramIndex > 0 )
    {
      src = src.substring(0, paramIndex);
    }
    var width = ed.dom.getAttrib(el, "width");
    var height = ed.dom.getAttrib( el, "height" );

    if ( width != "" && height != "" )
    {
      src += "?type=fix&width=" + width + "&height=" + height;
    }
    console.debug( src );
    ed.dom.setAttrib( el, "_mce_src", src );
  }

  // Register plugin
  tinymce.PluginManager.add('imgresize', tinymce.plugins.ImgResizePlugin);
})();