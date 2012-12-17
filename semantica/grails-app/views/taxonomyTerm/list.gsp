<%@ page import="semantica.TaxonomyTerm" %>
<!doctype html>
<html>
<head>
  <meta name="layout" content="main">
  <g:set var="entityName"
         value="${message(code: 'taxonomyTerm.label', default: 'ThesaurusTerm')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-thesaurusTerm" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                    default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
      <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                            args="[entityName]"/></g:link></li>
    </ul>
  </div>
</div>

<div id="list-thesaurusTerm" class="content scaffold-list" role="main">
  <h1><g:message code="default.list.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
  </g:if>
  <table>
    <thead>
    <tr>

      <g:sortableColumn property="label"
                        title="${message(code: 'taxonomyTerm.label.label', default: 'Label')}"/>

      <g:sortableColumn property="keywords"
                        title="${message(code: 'taxonomyTerm.keywords.label', default: 'Keywords')}"/>

      <th><g:message code="taxonomyTerm.generalTerm.label" default="General Term"/></th>

    </tr>
    </thead>
    <tbody>
    <g:each in="${taxonomyTermInstanceList}" status="i" var="thesaurusTermInstance">
      <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

        <td><g:link action="show"
                    id="${thesaurusTermInstance.id}">${fieldValue(bean: thesaurusTermInstance, field: "label")}</g:link></td>

        <td>${fieldValue(bean: thesaurusTermInstance, field: "subject")}</td>

        <td>${fieldValue(bean: thesaurusTermInstance, field: "generalTerm")}</td>

      </tr>
    </g:each>
    </tbody>
  </table>

  <div class="pagination">
    <g:paginate total="${taxonomyTermInstanceTotal}"/>
  </div>
</div>
</body>
</html>
