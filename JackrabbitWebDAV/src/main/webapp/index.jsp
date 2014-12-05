<%@ page import="com.stratelia.webactiv.publication.control.PublicationService" %>
<%@ page import="org.silverpeas.sandbox.service.MyService" %>
<%@ page import="org.silverpeas.util.ServiceProvider" %>
<html>
<body>
<h2>Hello World!</h2>
<%
  PublicationService service = ServiceProvider.getService(PublicationService.class);
%>
</body>
</html>
