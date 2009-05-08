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

function addScript( src, type )
{
  if ( srcArray.indexOf( src) < 0 )
  {
    var element = document.createElement( "script" );
    if ( type && type.length > 0 )
    {
      element.type = type;
    }
    else
    {
      element.type = "text/javascript";
    }
    element.src = src;
    $("head:first").append( element );
    srcArray.push(src);
  }
}