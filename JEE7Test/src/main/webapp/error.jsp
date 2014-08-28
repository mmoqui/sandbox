<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:url var="usersUrl" value="/RUsers/all"/>
<c:url var="docsUrl" value="/RDocument/all"/>
<html>
<body>
<h2>Oups!</h2>
<div id="menu">
  <a href="${usersUrl}">Manage users</a>&nbsp;&nbsp;
  <a href="${docsUrl}">Manage documents</a>
</div>
<p>An error has occurred while processing your request.</p>
<p>${requestScope.ErrorMessage}</p>
</body>
</html>
