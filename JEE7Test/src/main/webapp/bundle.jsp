<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="bundleContent" value="${requestScope.bundleContent}"/>
<c:set var="bundleName" value="${requestScope.bundleName}"/>
<c:url var="backUrl" value="/RUsers/all"/>
<html>
<head>
  <title>Bundle content</title>
</head>
<body>
  <h2>${bundleName}</h2>
  <pre>
    ${bundleContent}
  </pre>

  <a href="${backUrl}">Go back</a>
</body>
</html>
