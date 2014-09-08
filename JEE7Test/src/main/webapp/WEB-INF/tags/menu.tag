<%@tag description="Menu Template" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="usersUrl" value="/RUsers/all"/>
<c:url var="groupsUrl" value="/RGroups/all"/>
<c:url var="docsUrl" value="/RDocument/all"/>
<c:url var="componentsUrl" value="/RComponents/all"/>
<div id="menu">
  <a href="${usersUrl}">Manage users</a>&nbsp;&nbsp;|&nbsp;&nbsp;
  <a href="${groupsUrl}">Manage user groups</a>&nbsp;&nbsp;|&nbsp;&nbsp;
  <a href="${docsUrl}">Manage documents</a>&nbsp;&nbsp;|&nbsp;&nbsp;
  <a href="${componentsUrl}">Manage components</a>
</div>
