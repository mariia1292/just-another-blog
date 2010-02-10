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
    fancyplayer: function( options ){
      return this.each( function(){
        new $.FancyPlayer(this, options);
      });
    }

  });

  $.FancyPlayer = function( field, options ){

    var defaults = {
      id: "fc_" + new Date().getTime(),
      width: 480,
      height: 360,
      player: "flowplayer.swf",
      video: null
    };

    options = $.extend({},defaults, options);

    var $field = $(field);

    var height = options.height;
    try {
      height = parseInt(height) + 20;
    } catch(err){};

    height += "px";
    options.height += "px";
    options.width += "px";

    var output = $("<div />").hide().css("width", options.width).css("height", options.height).attr("id", options.id);
    $field.after( output );

    $field.css("cursor", "pointer").click(function(){

      $.fancybox(output, {

        overlayShow: true,
        overlayOpacity: 0.7,
        overlayColor: '#000',
        padding: 0,

        hideOnContentClick: false,
        autoScale: false,
        scrolling: "no",
        width: options.width,
        height: height,

        onStart: function(){
          output.show();
          $f(options.id, options.player, {
            clip: {
              url: options.video,
              autoPlay: true,
              autoBuffering: false,
              onFinish: function(){
                $.fancybox.close();
              }
            }
          });
        },

        onCleanup: function(){
          output.hide();
        }

      });

    });

}

})(jQuery);