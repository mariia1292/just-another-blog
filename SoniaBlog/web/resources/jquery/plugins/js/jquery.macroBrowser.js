/* *
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */


(function($){

  $.fn.extend({
    macroBrowser: function( url, options ){
      this.each( function(){
        new $.MacroBrowser(this, url, options);
      });
    }

  });

  $.MacroBrowser = function(field, url, options){
    var defaults = {
      defaultIcon : "#",
      insertLabel : "Insert",
      listLabel : "Back",
      detailLabel : "Back",
      previewLabel : "Preview",
      loadingImage : null,
      insertCallback : function(){},
      listViewCallback : function(){},
      detailViewCallback : function(){},
      previewCallback : function(){}
    };

    options = $.extend({},defaults, options);

    var $field = $(field);
    var detailMacro = null;
    var formParameters = null;

    listView();

    function listView(){
      loadScreen();
      $.getJSON( url + "?action=list", function(result){
        clearLoadScreen();
        $.each( result, function(index, content){
          $field.append(createMacro(content));
        });
        options.listViewCallback();
      });
    }

    function clearLoadScreen(){
      $("div.load").remove();
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

    function getIcon(content){
      var icon = content.icon;
      if ( content.icon == "" )
      {
        icon = options.defaultIcon;
      }
      return icon;
    }

    function createMacro(content){
      var icon = getIcon(content);
      return $("<div />").addClass("mbOverview").append(
        $("<a />").attr("href", "#").click(function(){
            detailView( content.name )
          }).append(
            $("<img />").attr( "src", icon )
        )).append(
          $("<div />").addClass("mbOverviewText").append(
            $("<strong />").text(content.label)
          ).append(
            $("<p />").text( content.description )
          )
        );
    }

    function detailView(name){
      loadScreen();
      detailMacro = name;
      $.getJSON(url + "?action=detail&name=" + name, function(result){
        clearLoadScreen();
        $.each(result, function(index, content){
          $field.append( createDetailMacro( content ) );
        });
        options.detailViewCallback();
        writeFormParameters();
      });
    }

    function createDetailMacro(content){
      var $form = $("<form />");
      appendParameters($form, content.parameters);
      var body = content.body;
      if ( body != null ){
        $form.append( convertField( body ) );
      }
      $field.append(
        $("<h4 />").text( content.label )
      ).append(
        $("<p />").text( content.description )
      ).append(
        $form
      );

      appendInsertButton();
      if ( content.preview ){
        appendPreviewButton();
      }
      appendListButton();

      var js = content.js;
      if ( js != null && js != "" ){
        eval(convertField(js));
      }
    }

    function appendPreviewButton(){
      $field.append( $("<button />").text(options.previewLabel).click(function(){
        preview();
      }));
    }

    function appendListButton(){
      $field.append( $("<button />").text(options.listLabel).click(function(){
        listView();
      }));
    }

    function appendInsertButton(){
      $field.append( $("<button />").text(options.insertLabel).click(function(){
        insert();
      }));
    }

    function appendDetailButton(){
      $field.append( $("<button />").text(options.detailLabel).click(function(){
        detailView(detailMacro)
      }));
    }

    function convertField(field){
      while ( field.indexOf( "&quot;" ) >= 0 ){
        field = field.replace( "&quot;", "\"" );
      }
      return field;
    }

    function appendParameters( $form, parameters ){
      var $tbody = $("<tbody />")
      $.each( parameters, function(index, parameter){
        $tbody.append(
          $("<tr />").append(
            $("<td />").text(parameter.label + ":")
          ).append(
            $("<td />").html( convertField( parameter.field ) )
          )
        );
        var js = parameter.js;
        if ( js != "" ){
          eval(convertField(js));
        }
      });
      $form.append(
        $("<table />").append( $tbody )
      )
    }

    function extendParameters(parameters){
      if ( detailMacro != null ){
        parameters["name"] = detailMacro;
      }
      $("form [name]").each(function(i){
        $this = $(this);
        var value = $this.val();
        if ( $this.is(":checkbox" )){
          if ( $this.is(":checked") ){
            value = true;
          }
          else{
            value = false;
          }
        }
        // TODO support multi value
        parameters[$this.attr("name")] = value;
      });
      return parameters;
    }

    function readFormParameters(){
      // TODO use one method for reading form
      formParameters = [];
      $("form [name]").each(function(i){
        $this = $(this);
        var value = $this.val();
        if ( $this.is(":checkbox" )){
          if ( $this.is(":checked") ){
            value = true;
          }
          else{
            value = false;
          }
        }
        formParameters.push({
          name: $this.attr("name"),
          value: value
        });
      });
    }

    function writeFormParameters(){
      if ( formParameters != null ){
        $.each(formParameters, function(i, parameter){
          var $input = $("form [name=" + parameter.name + "]");
          if ( $input.is(":checkbox")){
            if ( parameter.value == true ){
              $input.attr("checked", "checked");
            } else {
              $input.remove(":checked");
            }
          } else {
            $input.val( parameter.value );
          }
        });
      }
      formParameters = null;
    }

    function preview(){
      readFormParameters();
      var parameters = { "action": "getPreview" };
      parameters = extendParameters(parameters);
      loadScreen();
      $.post(url, parameters, function(data){
        clearLoadScreen();
        $field.append(
          $("<div />").addClass("mbPreview").append(
            $("<iframe />").attr("src",url + "?action=preview&key="+data)
          )
        );
        appendInsertButton();
        appendDetailButton();
        options.previewCallback();
      });
    }

    function insert(){
      var parameters = { "action": "result" };
      if (formParameters != null){
        parameters["name"] = detailMacro;
        $.each(formParameters, function(i,parameter){
          parameters[parameter.name] = parameter.value;
        });
      } else {
        parameters = extendParameters(parameters);
      }
      $.post(url, parameters, function(data){
        options.insertCallback(data);
      });
    }

  }

})(jQuery);