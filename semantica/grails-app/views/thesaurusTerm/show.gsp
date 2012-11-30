
<%@ page import="semantica.ThesaurusTerm" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm')}" />
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
			
				<g:if test="${thesaurusTermInstance?.label}">
				<li class="fieldcontain">
					<span id="label-label" class="property-label"><g:message code="thesaurusTerm.label.label" default="Label" /></span>
					
						<span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${thesaurusTermInstance}" field="label"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${thesaurusTermInstance?.keywords}">
				<li class="fieldcontain">
					<span id="keywords-label" class="property-label"><g:message code="thesaurusTerm.keywords.label" default="Keywords" /></span>
					
						<span class="property-value" aria-labelledby="keywords-label"><g:fieldValue bean="${thesaurusTermInstance}" field="keywords"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${thesaurusTermInstance?.generalTerm}">
				<li class="fieldcontain">
					<span id="generalTerm-label" class="property-label"><g:message code="thesaurusTerm.generalTerm.label" default="General Term" /></span>
					
						<span class="property-value" aria-labelledby="generalTerm-label"><g:link controller="thesaurusTerm" action="show" id="${thesaurusTermInstance?.generalTerm?.id}">${thesaurusTermInstance?.generalTerm?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${thesaurusTermInstance?.specificTerms}">
				<li class="fieldcontain">
					<span id="specificTerms-label" class="property-label"><g:message code="thesaurusTerm.specificTerms.label" default="Specific Terms" /></span>
					
						<g:each in="${thesaurusTermInstance.specificTerms}" var="s">
						<span class="property-value" aria-labelledby="specificTerms-label"><g:link controller="thesaurusTerm" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${thesaurusTermInstance?.id}" />
					<g:link class="edit" action="edit" id="${thesaurusTermInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
