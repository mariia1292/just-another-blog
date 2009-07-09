/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
(function($){

  $.fn.extend({
    autocomplete: function( url, options ){
      this.each( function(){
        new $.Autocomplete(this, url, options);
      });
    }

  });

  $.Autocomplete = function( field, url, options )
  {
    var defaults = {
      id: 'jqueryAutocomplete',
      maxItems : 5,
      minChars : 2
    };

    options = $.extend({},defaults, options);

    var $field = $(field);
    var $output = $("#" + options.id);
    var selected = null;

    var index = -1;

    $field.blur(function(){
      setTimeout( function(){
        $output.hide()
      }, 200);
    });

    $field.keydown(function(event){
      switch ( event.keyCode )
      {
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
          processSelection();
          break;
        // LEFT
        case 37:
           // do nothing
           break;
        // Right
        case 39:
           // do nothing
           break;
        // ALL OTHER
        default:
          processKeyPress();
      }
    });

    function selectUp()
    {
      if ( index > 0 )
      {
              index--;
      select();
      }
    }

    function selectDown()
    {
      index++;
      select();
    }

    function select()
    {
      var $children = $output.children();
      $children.removeClass( "selected" );
      if ( $children.length >= index )
      {
        var $child = $children.eq(index);
        $child.addClass("selected");
        selected = $child.children().eq(0).attr("href");
      }
      else
      {
        index--;
      }
    }

    function processSelection()
    {
      if ( selected != null )
      {
        location.href = selected;
      }
    }

    function processKeyPress()
    {
      var value = $field.val();
      clear();
      if ( value.length >= options.minChars )
      {
        value += "~";
        if ($output.length == "")
        {
          createOutputContainer();
        }
        $output.show();

        url = createQueryUrl(value);

        $.getJSON( url, function(result)
        {
          $.each(result, function(index, content)
          {
            if ( index < options.maxItems )
            {
              $output.append( createOutputElement(content) );
            }

          });
        });
      }
    }

    function clear()
    {
      index = -1;
      selected = null;
      $output.empty();
    }

    function createOutputElement(content)
    {
      return $("<li />").append($("<a />").attr("href", content.url).text(content.value) );
    }

    function createQueryUrl(value)
    {
      var index = url.indexOf( "?query=" );
      if ( index > 0 )
      {
        url = url.substring(0, index);
      }
      url += "?query=" + value;
      return url;
    }

    function createOutputContainer()
    {
      // TODO replace the 4
      var p = findPosition(field, $field.height() +4 );
      $("body").append( $("<ul />").attr("id", options.id).addClass( "autocomplete" ).css( "top",  p.top + "px").css( "left", p.left + "px" ) );
      $output = $("#" + options.id);
      $output.width( $field.width() );
    }

    function findPosition( el, height )
    {
      var top = height;
      var left = 0;
      do {
        left += el.offsetLeft;
        top += el.offsetTop;
        el = el.offsetParent;
      } while(el.tagName.toLowerCase() != 'body');

      return {
        top: top,
        left: left
      }
    }

  }

})(jQuery);