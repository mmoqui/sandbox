<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="users" value="${requestScope.users}"/>
<c:url var="newUserUrl" value="/RUsers/newUser"/>
<c:url var="bundleUrl" value="/RBundle/view"/>
<html>
<head>
  <title>The users</title>
</head>
<body>
<h2>The users</h2>
<ul>
  <c:forEach var="user" items="${users}">
    <li>${user.firstName} ${user.lastName}</li>
  </c:forEach>
</ul>
<h3>New user</h3>
<form action="${newUserUrl}" method="POST">
  <%--@declare id="firstname"--%><label for="firstName">First name: </label>
  <input id="firstName" name="firstName" type="text"/>
    <%--@declare id="lastname"--%><label for="lastName">Last name: </label>
  <input id="lastName" name="lastName" type="text"/>
  <input type="submit" value="Create"/>
</form>
<br/>
<h3>Bundles</h3>
<form action="${bundleUrl}" method="GET">
  <%--@declare id="bundle"--%><label for="bundle">Bundle path: </label>
  <input id="bundle" name="bundle" type="text"/>
  <input type="submit" value="View it"/>
</form>
</body>
</html>
