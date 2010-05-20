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
    autocomplete: function( url, options ){
      return this.each( function(){
        new $.Autocomplete(this, url, options);
      });
    }

  });

  $.Autocomplete = function( field, url, options )
  {
    var defaults = {
      id: 'ja-' + createUUID(),
      maxItems : 10,
      minChars : 3,
      formatter: defaultFormatter,
      queryParam: "query",
      processSelection: processSelection,
      readValue: readValue,
      cache: true
    };

    options = $.extend({},defaults, options);

    var $field = $(field);
    $field.attr( "autocomplete", "off" );
    var $output = $("#" + options.id);
    var selected = null;

    var index = -1;
    var cache = null;
    var timeout = null;

     if ( options.cache ){
       cache = [];
     }

    $field.blur(function(){
      setTimeout( function(){
        $output.hide()
      }, 300);
    });

    $field.keydown(function(event){
      if ( timeout != null ){
        clearTimeout( timeout );
      }
      switch ( event.keyCode )
      {
        // TAB
        case 9:
        // ESC
        case 27:
          $output.hide();
          break;
        // UP
        case 38:
          selectUp();
          break;
        // DOWN
        case 40:
          selectDown();
          break;
        // RETURN
        case 13:
          if ( selected != null ){
            event.preventDefault();
            options.processSelection($field, selected, getCaretPosition(field));
            $output.hide();
          }
          break;
        // END
        case 35:
        // POS1
        case 36:
        // LEFT
        case 37:
        // Right
        case 39:
           // do nothing
           break;
        // ALL OTHER
        default:
          timeout = setTimeout(processKeyPress, 500);
      }
    });

    function selectUp(){
      if ( index > 0 ){
        index--;
        select();
      }
    }

    function selectDown(){
      index++;
      select();
    }

    function select(){
      var $children = $output.children();
      $children.removeClass( "selected" );
      if ( $children.length >= index ){
        var $child = $children.eq(index);
        $child.addClass("selected");
        selected = $child.children().eq(0);
      } else {
        index--;
      }
    }

    function processSelection(field, selection){
      location.href = selection.attr("href");
    }

    function processKeyPress(){
      timeout = null;
      var value = options.readValue( $field, $field.val(), getCaretPosition(field) );
      clear();
      if ( value.length >= options.minChars ){
        if ($output.length == ""){
          createOutputContainer();
        } else {
          setOutputPosition();
        }
        $output.empty().show();

        url = createQueryUrl(value);
        
        processRequest( url );
      }
    }

    function readValue( field, value ){
      return value;
    }

    function processRequest(url){
      if ( cache != null ){
        var result = cache[url];
        if ( result != null ){
          processResult(result);
        } else {
          request(url);
        }
      } else {
        request(url);
      }
    }

    function request(url){
      $.getJSON( url, function(content){
        if ( cache != null ){
          cache[url] = content;
        }
        processResult(content)
      });
    }

    function processResult(result){
      clear();
      $.each(result, function(index, content){
        if ( index < options.maxItems ){
          $output.append( $("<li />").append( options.formatter($field, content, getCaretPosition(field)) ) );
        }
      });
    }

    function clear(){
      index = -1;
      selected = null;
      $output.empty();
    }

    function defaultFormatter(field, content){
      return $("<a />").attr("href", content.url).text(content.value);
    }

    function createQueryUrl(value){
      var param = options.queryParam;
      var index = url.indexOf( "?" + param  + "=" );
      if ( index > 0 ){
        url = url.substring(0, index);
      }
      url += "?" + param + "=" + escape(value);
      return url;
    }

    function createOutputContainer(){
      $output = $("<ul />").attr("id", options.id).addClass( "autocomplete" ).width( $field.width() );
      setOutputPosition();
      $("body").append( $output );
    }

    function setOutputPosition(){
      var p = findPosition();
      $output.css( "top",  p.top + "px").css( "left", p.left + "px" );
    }

    function findPosition(){
      var p = $field.offset();
      p.top += $field.outerHeight() - 1;
      return p;
    }

    function getCaretPosition (ctrl) {
      var CaretPos = 0;
      // IE Support
      if (document.selection) {
        ctrl.focus();
        var Sel = document.selection.createRange ();
        Sel.moveStart ('character', -ctrl.value.length);
        CaretPos = Sel.text.length;
      }
      // Firefox support
      else if (ctrl.selectionStart || ctrl.selectionStart == '0')
        CaretPos = ctrl.selectionStart;

      return (CaretPos);
    }

    function createUUID(){
      var s = [], itoh = '0123456789ABCDEF';
      for (var i = 0; i <36; i++) s[i] = Math.floor(Math.random()*0x10);
      s[14] = 4;
      s[19] = (s[19] & 0x3) | 0x8;
      for (var i = 0; i <36; i++) s[i] = itoh[s[i]];
        s[8] = s[13] = s[18] = s[23] = '-';
      return s.join('');
    }

  }

})(jQuery);