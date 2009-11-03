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
    messages: function( url, options ){
      this.each( function(){
        new $.Messages(this, url, options);
      });
    }

  });

  $.Messages = function(field, url, options){
    var defaults = {
      infoClass: "info",
      warnClass: "warn",
      errorClass: "error",
      fatalClass: "fatal",
      styleClass: null,
      style: null
    };

    options = $.extend({},defaults, options);

    var $field = $(field);
    var $ul = null;

    $field.empty();

    $.getJSON(url, function(result){
      $.each(result, function(index, item){
        if ( item.summary != "" ){
          if ( item.clientId != "" ){
            append(item);
          } else {
            appendGlobal(item);
          }
        }
      });

      if ( $ul != null ){
        $field.append( $ul );
      }
      
    });

    function append(item){
      var element = $("#message_" + item.clientId);
      if ( element.length > 0 ){
        addText(item, element);
        addClass(item, element);
        element.show();
      }
      else if (console != "undefined") {
        console.debug( "#message_" + item.clientId + " not found" );
      }
    }

    function addText(item, element){
      element.text( item.summary );
      if ( item.detail != "" ){
        element.attr( "title", item.detail );
      }
    }

    function addClass(item, element){
      switch ( item.level ){
        case 0:
          element.addClass( options.infoClass );
          break;
        case 1:
          element.addClass( options.warnClass );
          break;
        case 2:
          element.addClass( options.errorClass );
          break;
        case 3:
          element.addClass( options.fatalClass );
          break;
      }
    }

    function appendGlobal(item){
      if ( $ul == null ){
        $ul = $("<ul />");
        if ( options.styleClass != null ){
          $ul.addClass( options.styleClass );
        }
        if ( options.style != null ){
          $ul.attr( "style", options.style );
        }
      }
      var $li = $("<li />");
      addText( item, $li );
      addClass( item, $li );
      $ul.append( $li );
    }
    
  }

  })(jQuery);