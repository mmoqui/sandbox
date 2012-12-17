
<%@ page import="semantica.TaxonomyTerm" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'taxonomyTerm.label', default: 'ThesaurusTerm')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-thesaurusTerm" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-thesaurusTerm" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list thesaurusTerm">
			
				<g:if test="${taxonomyTermInstance?.label}">
				<li class="fieldcontain">
					<span id="label-label" class="property-label"><g:message code="taxonomyTerm.label.label" default="Label" /></span>
					
						<span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${taxonomyTermInstance}" field="label"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${taxonomyTermInstance?.subject}">
				<li class="fieldcontain">
					<span id="keywords-label" class="property-label"><g:message code="taxonomyTerm.keywords.label" default="Keywords" /></span>
					
						<span class="property-value" aria-labelledby="keywords-label"><g:fieldValue bean="${taxonomyTermInstance}" field="subject"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${taxonomyTermInstance?.generalTerm}">
				<li class="fieldcontain">
					<span id="generalTerm-label" class="property-label"><g:message code="taxonomyTerm.generalTerm.label" default="General Term" /></span>
					
						<span class="property-value" aria-labelledby="generalTerm-label"><g:link controller="taxonomyTerm" action="show" id="${taxonomyTermInstance?.generalTerm?.id}">${thesaurusTermInstance?.generalTerm?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${taxonomyTermInstance?.specificTerms}">
				<li class="fieldcontain">
					<span id="specificTerms-label" class="property-label"><g:message code="taxonomyTerm.specificTerms.label" default="Specific Terms" /></span>
					
						<g:each in="${taxonomyTermInstance.specificTerms}" var="s">
						<span class="property-value" aria-labelledby="specificTerms-label"><g:link controller="taxonomyTerm" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${taxonomyTermInstance?.id}" />
					<g:link class="edit" action="edit" id="${taxonomyTermInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
