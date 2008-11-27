<%-- 
    Document   : forward
    Created on : Sep 24, 2008, 3:10:20 PM
    Author     : sdorra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="sonia.blog.api.app.*" %>
<%
    String uri = BlogContext.getInstance().getLinkBuilder().buildLink(
            new BlogRequest(request), "/list/index.jab");
    response.sendRedirect(uri);
%>