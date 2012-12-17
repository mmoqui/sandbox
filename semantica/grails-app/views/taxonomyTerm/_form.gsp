<%@ page import="semantica.TaxonomyTerm" %>



<div class="fieldcontain ${hasErrors(bean: taxonomyTermInstance, field: 'label', 'error')} required">
	<label for="label">
		<g:message code="taxonomyTerm.label.label" default="Label" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="label" required="" value="${taxonomyTermInstance?.label}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: taxonomyTermInstance, field: 'subject', 'error')} ">
	<label for="keywords">
		<g:message code="taxonomyTerm.keywords.label" default="Keywords" />
		
	</label>
	<g:textField name="keywords" value="${taxonomyTermInstance?.keywords}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: taxonomyTermInstance, field: 'generalTerm', 'error')} required">
	<label for="generalTerm">
		<g:message code="taxonomyTerm.generalTerm.label" default="General Term" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="generalTerm" name="generalTerm.id" from="${TaxonomyTerm.list()}" optionKey="id" required="" value="${taxonomyTermInstance?.generalTerm?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: taxonomyTermInstance, field: 'specificTerms', 'error')} ">
	<label for="specificTerms">
		<g:message code="taxonomyTerm.specificTerms.label" default="Specific Terms" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${taxonomyTermInstance?.specificTerms?}" var="s">
    <li><g:link controller="taxonomyTerm" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="taxonomyTerm" action="create" params="['thesaurusTerm.id': taxonomyTermInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'taxonomyTerm.label', default: 'ThesaurusTerm')])}</g:link>
</li>
</ul>

</div>

