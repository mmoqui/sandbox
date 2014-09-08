<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set var="components" value="${requestScope.components}"/>
<c:url var="newComponentUrl" value="/RComponents/newComponent"/>
<html>
<head>
  <title>The Components</title>
</head>
<body>
<h2>The Components</h2>
<t:menu></t:menu>
<ul>
  <c:forEach var="component" items="${components}">
    <li>${component.name} of type ${component.type}</li>
  </c:forEach>
</ul>
<h3>New Component</h3>
<form action="${newComponentUrl}" method="POST">
  <%--@declare id="name"--%><label for="name">name: </label>
  <input id="name" name="name" type="text"/>
  <%--@declare id="type"--%><label for="type">type: </label>
  <input id="type" name="type" type="text"/>
  <input type="submit" value="Create"/>
</form>
</body>
</html>
