/**
 * $Id: editor_plugin_src.js 201 2007-02-12 15:56:56Z spocke $
 *
 * @author Moxiecode
 * @copyright Copyright � 2004-2008, Moxiecode Systems AB, All rights reserved.
 */
(function(){tinymce.PluginManager.requireLangPack('links');tinymce.create('tinymce.plugins.LinksPlugin',{init:function(ed,url){var address=url+'/../../../../personal/author/editor/links.jab';ed.addCommand('mceLinks',function(){ed.windowManager.open({file:address,width:320+parseInt(ed.getLang('links.delta_width',0)),height:576+parseInt(ed.getLang('links.delta_height',0)),inline:1,scrollbars:"yes"},{plugin_url:url})});ed.addButton('links',{title:'links.title',cmd:'mceLinks',image:url+'/img/links.gif'})},createControl:function(n,cm){return null},getInfo:function(){return{longname:'Links Plugin',author:'Sebastian Sdorra',authorurl:'http://kenai.com/projects/jab',infourl:'http://kenai.com/projects/jab',version:"0.1"}}});tinymce.PluginManager.add('links',tinymce.plugins.LinksPlugin)})();