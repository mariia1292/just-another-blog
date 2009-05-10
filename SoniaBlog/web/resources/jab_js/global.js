/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function addCSS( href, media )
{
  if ( $("link[href="+href+"]").length == 0  )
  {
    var element = document.createElement( "link" );
    element.type = "text/css"
    element.rel = "stylesheet";
    element.href = href;
    if ( media && media.length > 0 )
    {
      element.media = media;
    }
    $("head:first").append( element );
  }
}

var srcArray = new Array();

function addScript( src )
{
  if ( srcArray.indexOf( src) < 0 )
  {
    $.ajaxSetup({ async: false });
    $.getScript( src, function(){ console.log( src ) } );
    $.ajaxSetup({ async: true });
    /*var element = document.createElement( "script" );
    if ( type && type.length > 0 )
    {
      element.type = type;
    }
    else
    {
      element.type = "text/javascript";
    }
    element.src = src;
    document.getElementsByTagName("head")[0].appendChild(element);
    $("head:first").append( element );*/
    srcArray.push(src);
  }
}