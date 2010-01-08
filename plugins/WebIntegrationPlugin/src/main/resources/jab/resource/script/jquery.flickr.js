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
    flickr: function( url, options ){
      this.each( function(){
        new $.flickr(this, url, options);
      });
    }

  });

  $.flickr = function(field, url, options){
    var defaults = {
      nsid: null,
      username: null,
      email: null,
      perPage: 25,
      method: "user",
      id: "flickr-" + new Date().getTime(),
      loadingImage: null
    };

    options = $.extend({},defaults, options);
    var $field = $(field);

    if ( ! url.match("/$") ){
      url += "/";
    }

    doMethod();

    function doMethod(){
      loadScreen();
      var gatewayUrl = getURL();

      $.getJSON(gatewayUrl, function(data){
        clearLoadScreen();
        $.each( data, function(index, image){
          $field.append(
            $("<a />").attr("href",image.picture).attr("title",image.title).attr("rel", "flickr["+options.id+"]").css("margin", "2px").append(
              $("<img />").attr("src",image.thumbnail ).attr("alt",image.title)
            )
          );
        });

        $("a", $field).fancybox({
          type: "image",
          overlayShow: true,
          overlayOpacity: 0.5
        });
        
      });
    }

    function getURL(){
      var gatewayUrl = url + options.method + "?perPage=" + options.perPage;
      if ( options.nsid != null ){
        gatewayUrl += "&nsid=" + options.nsid;
      } else if ( options.username != null ){
        gatewayUrl += "&username=" + options.username;
      } else if ( options.email != null ){
        gatewayUrl += "&email=" + options.email;
      }
      return gatewayUrl;
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
      $("div.load", $field).remove();
    }

  }

  })(jQuery);