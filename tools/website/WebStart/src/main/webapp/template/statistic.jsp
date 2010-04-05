<%-- 
    Document   : statistic
    Created on : Apr 5, 2010, 8:51:27 PM
    Author     : Sebastian Sdorra
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=MacRoman">
    <title>WebStart Statistics</title>
  </head>
  <body>
    <h1>WebStart Statistics</h1>

    <ul>
      <c:forEach items="${statistics}" var="stat">
        <li>${stat.name} - (${stat.counter})</li>
      </c:forEach>
    </ul>
  </body>
</html>
