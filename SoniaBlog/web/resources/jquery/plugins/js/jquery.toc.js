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
    toc: function( options ){
      this.each( function(){
        new $.Toc(this, options);
      });
    }

  });

  $.Toc = function(field, options){
    var defaults = {
      id: null,
      exclude: "h1",
      container: null,
      orderedList : false
    };

    options = $.extend({},defaults, options);

    var $field = $(field);
    var id = null;
    var container = null;
    var listItem = options.orderedList ? "<ol />" : "<ul />";

    if ( options.container == null ){
      container = $field.parent();
    } else {
      container = $(options.container);
    }

    if ( options.id == null ){
      id = new Date().getTime();
    } else {
      id = options.id;
    }

    var current = null;
    var last = -1;

    container.children(":header").not(options.exclude).each(function(index, content){

      var header = $(content);
      var name = content.localName;
      var nr = name.charAt(1);
      var i;

      if ( last == -1 ){
        
        current = $(listItem);
        $field.append(current);

      } else {

        if ( last > nr ){

          for ( i=0;i<(last-nr); i++ ){
            current = current.parent();
          }
          
        } else if ( last < nr ){

          for ( i=0;i<(nr-last); i++ ){
            var n = $(listItem);
            current.append(n);
            current = n;
          }

        }
        
      }

      header.before( "<a name=\"toc_" + id + "_" + index + "\"></a>" );
      current.append(
        $("<li />").append(
          $("<a />").attr("href", "#toc_" + id + "_" + index).text( header.text() )
        )
      );
      last = nr;
    });

  }

  })(jQuery);