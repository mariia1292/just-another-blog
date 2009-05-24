/**
 * $Id: editor_plugin_src.js 201 2007-02-12 15:56:56Z spocke $
 *
 * @author Moxiecode
 * @copyright Copyright � 2004-2008, Moxiecode Systems AB, All rights reserved.
 */
(function(){tinymce.PluginManager.requireLangPack('imgbrowser');tinymce.create('tinymce.plugins.ImgBrowserPlugin',{init:function(ed,url){var id=ed.getParam("entry_id");var address=url+'/../../../../personal/author/editor/images.jab';if(id!=null&&id!=""){address+="?id="+id}ed.addCommand('mceImgBrowser',function(){ed.windowManager.open({file:address,width:720+parseInt(ed.getLang('imgbrowser.delta_width',0)),height:576+parseInt(ed.getLang('imgbrowser.delta_height',0)),inline:1,scrollbars:"yes"},{plugin_url:url})});ed.addButton('imgbrowser',{title:'imgbrowser.title',cmd:'mceImgBrowser',image:url+'/img/image.gif'})},createControl:function(n,cm){return null},getInfo:function(){return{longname:'ImageBrowser Plugin',author:'Sebastian Sdorra',authorurl:'http://kenai.com/projects/jab',infourl:'http://kenai.com/projects/jab',version:"0.1"}}});tinymce.PluginManager.add('imgbrowser',tinymce.plugins.ImgBrowserPlugin)})();