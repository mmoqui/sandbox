<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set var="groups" value="${requestScope.groups}"/>
<c:url var="newGroupUrl" value="/RGroups/newGroup"/>
<html>
<head>
  <title>The groups</title>
</head>
<body>
<h2>The groups</h2>
<t:menu></t:menu>
<ul>
  <c:forEach var="group" items="${groups}">
    <li>${group.name}</li>
  </c:forEach>
</ul>
<h3>New group</h3>
<form action="${newGroupUrl}" method="POST">
  <%--@declare id="name"--%><label for="name">name: </label>
  <input id="name" name="name" type="text"/>
  <input type="submit" value="Create"/>
</form>
</body>
</html>
