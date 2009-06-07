<%-- 
    Document   : forward
    Created on : Sep 24, 2008, 3:10:20 PM
    Author     : sdorra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="sonia.blog.api.app.*,sonia.blog.entity.Blog,sonia.blog.util.BlogUtil,sonia.blog.api.link.LinkBuilder,sonia.util.Util" %>
<%
    String uri = null;
    BlogRequest blogRequest = BlogUtil.getBlogRequest(request);
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();

    if (BlogContext.getInstance().isInstalled()) {
      Blog blog = blogRequest.getCurrentBlog();
      if (blog != null && Util.hasContent(blog.getStartPage()))
      {
        uri = blog.getStartPage();
      }
      else
      {
        uri = linkBuilder.buildLink(blogRequest, "/list/index.jab");
      }

    }
    else
    {
      uri = linkBuilder.buildLink(blogRequest, "/install/step1.jab");
    }
    response.sendRedirect(uri);
%>