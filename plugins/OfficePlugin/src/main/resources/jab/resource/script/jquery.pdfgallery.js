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
    pdfgallery: function( url, options ){
      this.each( function(){
        new $.pdfgallery(this, url, options);
      });
    }
  });

  $.pdfgallery = function(field, url, options){
    var defaults = {
      id: "pdfgallery-" + new Date().getTime(),
      type: "thumb",
      loadingImage: null,
      fancybox: {
        type: "image",
        overlayShow: true,
        overlayOpacity: 0.5,
        padding: 0
      }
    };

    options = $.extend({},defaults, options);

    var $field = $(field);
    var baseUrl = getBaseUrl();

    var body = null;
    if ( options.type == "hidden" ){
      body = $field.html();
    }

    loadData();

    function getBaseUrl(){
      return url.substring(0, url.lastIndexOf("/")+1);
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

    function loadData(){
      loadScreen();
      $.getJSON(url, function(data){
        clearLoadScreen();
        $.each( data, function(index, content){
          var title = content.title;
          var size = content.size;
          if ( options.type == "thumb" ){
            createThumbGallery(title, size, content.pages);
          } else if ( options.type == "hidden" ){
            createHiddenGallery(title, size, content.pages);
          } else if ( options.type == "linklist" ){
            createLinkListGallery(title, size, content.pages);
          }
        });
      });
    }

    function createLinkListGallery(title, size, pages){
      var $ul = $("<ul />");
      $field.append($ul);
      $.each(pages, function(index, page){
        var name = title + " " + page.nr + "/" + size;
        $ul.append(
          $("<li />").append(
            $("<a />").attr("href", baseUrl + page.name).attr("title", name).attr("rel", "pdfgallery[group_" + options.id + "]").text( name )
            )
          );
      });
      $("a", $ul).fancybox(options.fancybox);
    }

    function createHiddenGallery(title, size, pages){
      var images = [];
      $.each( pages, function(index, page){
        images.push( {
          title: title + " " + page.nr + "/" + size,
          href: baseUrl + page.name
        });
      });

      var $button = null;
      if ( body == null || body.length <= 0 ){
        $button = $("<a />").css("cursor", "pointer").text(title);
      } else {
        $button = $("<a />").css("cursor", "pointer").html( body );
      }

      $field.append(
        $button
        );

      $button.click(function(){
        $.fancybox( images, options.fancybox);
      });
    }

    function createThumbGallery(title, size, pages){
      $.each( pages, function(index, page){
        var name =  title + " " + page.nr + "/" + size;
        var pageUrl = baseUrl + page.name;
        $field.append(
          $("<a />").attr("href", pageUrl).attr("title", name).attr("rel", "pdfgallery[group_" + options.id + "]").append(
            $("<img />").attr("border", "0").attr("src", pageUrl + "?thumb").attr("alt", name)
            )
          );
      });
      $("a", $field).fancybox(options.fancybox);
    }
  }

})(jQuery);