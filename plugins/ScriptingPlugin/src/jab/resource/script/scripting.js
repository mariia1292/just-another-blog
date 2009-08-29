/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
(function($){

  $.fn.extend({
    scripting: function( url, options ){
      this.each( function(){
        new $.Scripting(this, url, options);
      });
    }

  });

  $.Scripting = function( field, url, options ){

    var defaults = {
      loadingImage : null,
      listViewCallback: function(){},
      detailViewCallback: function(){},
      editViewCallback: function(){},
      excecuteCallback: function(){}
    };

    options = $.extend({},defaults, options);

    var $field = $(field);
    var parameters = {};

    listScripts();

    function listScripts(){
      loadScreen();
      $.getJSON(url + "?action=list", function(result){
        clearLoadScreen();
        var $list = $("<ul/>").addClass("scList");
        $.each(result, function(index, content){
          $list.append( createListItem( content ) );
        });
        $field.append(
          $list
        ).append(
          $("<button />").text( "new script" ).click(function(){
            editView({title: "", description: "", name: ""},"");
          })
        );
        options.listViewCallback();
      });
    }

    function createListItem(content){
      var $item = $("<li />").append(
        $("<a />").attr("href", "#").attr("title", content.description).text( content.title ).click(function(){
          detailView(content);
        })
      );
      return $item;
    }

    function detailView(content){
      loadScreen();
      $.post( url, { action: "view", script: content.name }, function(result){
        clearLoadScreen();
        $field.append(
          $("<pre />").addClass( "brush: js" ).text( result )
        ).append(
          $("<button />").text("edit").click(function(){
            editView( content, result );
          })
        ).append(
          " "
        ).append(
          $("<button />").text("excecute").click(function(){
            excecute(content);
          })
        ).append(
          " "
        ).append(
          $("<button />").text("back").click(function(){
            listScripts();
          })
        ).append(
          " "
        ).append(
          $("<button />").text("remove").click(function(){
            removeScript( content.name );
          })
        );
        options.detailViewCallback();
      });
    }

    function removeScript(script){
      loadScreen();
      $.post(url, {action: "remove", script: script}, function(){
        clearLoadScreen();
        listScripts();
      });
    }

    function editView( content, script ){
      $field.empty();
      $field.append(
        $("<div />").addClass("scEdit").append(
          $("<table />").append(
            $("<tr />").append(
              $("<td />").text("Title:")
            ).append(
              $("<td />").append(
                $("<input />").attr("name", "title").attr("type", "text").val(content.title)
              )
            )
          ).append(
            $("<tr />").append(
              $("<td />").text("Description:")
            ).append(
              $("<td />").append(
                $("<textarea />").attr("name", "description").addClass("scDescription").val(content.description)
              )
            )
          ).append(
            $("<tr />").append(
              $("<td />").text("Script:")
            ).append(
              $("<td />").append(
                $("<textarea />").attr("name", "script").addClass("scScript").val(script)
              )
            )
          )
        ).append(
          $("<button />").text("store").click(function(){
            store( content.name )
          })
        )
      );
      options.editViewCallback();
    }

    function readParameters(){
      parameters.title = $("div.scEdit [name=title]").val();
      parameters.description = $("div.scEdit [name=description]").val();
      parameters.content = $("div.scEdit [name=script]").val();
    }

    function store(name){
      readParameters();
      loadScreen();
      parameters.action = "store";
      if ( name != "" && name != "undefined" ){
        parameters.script = name;
      }
      $.post(url, parameters, function(result){
        var content = parameters;
        content.name = result;
        parameters = {};
        detailView(content);
      });
    }

    function excecute(content){
      loadScreen();
      $.post(url, { action: "excecute", script: content.name }, function(result){
        clearLoadScreen();
        $field.append(
          $("<div />").addClass("scResult").html(result)
        ).append(
          $("<button />").text("back").click(function(){
            detailView(content);
          })
        );
        options.excecuteCallback();
      });
    }

    function loadScreen(){
      $field.empty();
      if ( options.loadingImage != null ){
        $field.append(
          $("<div />").addClass("load").append(
            $("<img />").attr("src", options.loadingImage)
          )
        );
      }
    }

    function clearLoadScreen(){
      $("div.load").remove();
    }

  }


})(jQuery);