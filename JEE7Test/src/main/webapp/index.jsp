<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<c:set var="users" value="${requestScope.users}"/>
<html>
<body>
<h2>Hello World!</h2>
  <%
    request.getRequestDispatcher("/RUsers/all").forward(request, response);
  %>
</body>
</html>
