<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="documents" value="${requestScope.documents}"/>
<c:url var="usersUrl" value="/RUsers/all"/>
<c:url var="docsUrl" value="/RDocument/all"/>
<c:url var="docUrl" value="/RDocument/document"/>
<c:url var="newDocumentUrl" value="/RDocument/import"/>
<html>
<head>
  <title>The documents</title>
</head>
<body>
<h2>The documents</h2>

<div id="menu">
  <a href="${usersUrl}">Manage users</a>&nbsp;&nbsp;
  <a href="${docsUrl}">Manage documents</a>
</div>
<ul>
  <c:forEach var="doc" items="${documents}">
    <li><a href="${docUrl}?id=${doc.id}">${doc.title} (${doc.updateDate})</a></li>
  </c:forEach>
</ul>
<h3>Import a document</h3>

<form action="${newDocumentUrl}" method="POST" enctype="multipart/form-data">
  <div>
    <%--@declare id="title"--%><label for="title">Title of the document: </label>
    <input id="title" name="title" type="text"/>
  </div>
  <div>
    <%--@declare id="file"--%><label for="file">File to upload: </label>
    <input id="file" name="file" type="file"/>
  </div>
  <input type="submit" value="Upload"/>
</form>
<br/>
</body>
</html>