/**
 * $Id: editor_plugin_src.js 201 2007-02-12 15:56:56Z spocke $
 *
 * @author Moxiecode
 * @copyright Copyright � 2004-2008, Moxiecode Systems AB, All rights reserved.
 */
(function(){tinymce.PluginManager.requireLangPack('attachment');tinymce.create('tinymce.plugins.AttachmentPlugin',{init:function(ed,url){ed.addCommand('mceAttachment',function(){var address=url+'/../../../../personal/author/editor/attachments.jab';var content=ed.selection.getContent();if(content!=null&&content!=""){address+="?selection="+encodeURIComponent(content)}ed.windowManager.open({file:address,width:720+parseInt(ed.getLang('attachment.delta_width',0)),height:576+parseInt(ed.getLang('attachment.delta_height',0)),inline:1,scrollbars:"yes"},{plugin_url:url})});ed.addButton('attachment',{title:'attachment.title',cmd:'mceAttachment',image:url+'/img/attachment.gif'})},createControl:function(n,cm){return null},getInfo:function(){return{longname:'Attachment Plugin',author:'Sebastian Sdorra',authorurl:'http://kenai.com/projects/jab',infourl:'http://kenai.com/projects/jab',version:"0.1"}}});tinymce.PluginManager.add('attachment',tinymce.plugins.AttachmentPlugin)})();