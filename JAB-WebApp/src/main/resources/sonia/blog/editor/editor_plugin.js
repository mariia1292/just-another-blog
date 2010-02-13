(function() {

  tinymce.create('tinymce.plugins.${plugin.name}', {
    
    init : function(ed, url) {


      ed.addCommand('mce${plugin.name}', function() {
        var address = "${plugin.url}";
        var content = ed.selection.getContent();
        if ( content != null && content != "" )
        {
          address += "?selection=" + encodeURIComponent(content);
        }

        ed.windowManager.open({
          file : address,
          width : 720,
          height : 576,
          inline : 1,
          scrollbars: "yes"
        }, {
          plugin_url : url
        });
      });

      ed.addButton("${plugin.name}", {
        title : "${plugin.displayName}",
        cmd : 'mce${plugin.name}',
        image : "${plugin.icon}"
      });
    },

    createControl : function(n, cm) {
      return null;
    },

    getInfo : function() {
      return {
        longname : '${plugin.displayName}',
        author : "${plugin.author}",
        authorurl : "${plugin.authorUrl}",
        infourl : "${plugin.infoUrl}",
        version : "${plugin.version}"
      };
    }
  });

  tinymce.PluginManager.add('${plugin.name}', tinymce.plugins.${plugin.name});
})();