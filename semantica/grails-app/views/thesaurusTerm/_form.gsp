<%@ page import="semantica.ThesaurusTerm" %>



<div class="fieldcontain ${hasErrors(bean: thesaurusTermInstance, field: 'label', 'error')} required">
	<label for="label">
		<g:message code="thesaurusTerm.label.label" default="Label" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="label" required="" value="${thesaurusTermInstance?.label}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: thesaurusTermInstance, field: 'keywords', 'error')} ">
	<label for="keywords">
		<g:message code="thesaurusTerm.keywords.label" default="Keywords" />
		
	</label>
	<g:textField name="keywords" value="${thesaurusTermInstance?.keywords}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: thesaurusTermInstance, field: 'generalTerm', 'error')} required">
	<label for="generalTerm">
		<g:message code="thesaurusTerm.generalTerm.label" default="General Term" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="generalTerm" name="generalTerm.id" from="${semantica.ThesaurusTerm.list()}" optionKey="id" required="" value="${thesaurusTermInstance?.generalTerm?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: thesaurusTermInstance, field: 'specificTerms', 'error')} ">
	<label for="specificTerms">
		<g:message code="thesaurusTerm.specificTerms.label" default="Specific Terms" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${thesaurusTermInstance?.specificTerms?}" var="s">
    <li><g:link controller="thesaurusTerm" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="thesaurusTerm" action="create" params="['thesaurusTerm.id': thesaurusTermInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm')])}</g:link>
</li>
</ul>

</div>

