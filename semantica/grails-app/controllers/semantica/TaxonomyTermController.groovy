package semantica

import org.springframework.dao.DataIntegrityViolationException

class TaxonomyTermController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [taxonomyTermInstanceList: TaxonomyTerm.list(params), taxonomyTermInstanceTotal: TaxonomyTerm.count()]
    }

    def create() {
        [taxonomyTermInstance: new TaxonomyTerm(params)]
    }

    def save() {
        def taxonomyTermInstance = new TaxonomyTerm(params)
        if (!taxonomyTermInstance.save(flush: true)) {
            render(view: "create", model: [taxonomyTermInstance: taxonomyTermInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm'), taxonomyTermInstance.id])
        redirect(action: "show", id: taxonomyTermInstance.id)
    }

    def show(Long id) {
        def taxonomyTermInstance = TaxonomyTerm.get(id)
        if (!taxonomyTermInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm'), id])
            redirect(action: "list")
            return
        }

        [taxonomyTermInstance: taxonomyTermInstance]
    }

    def edit(Long id) {
        def taxonomyTermInstance = TaxonomyTerm.get(id)
        if (!taxonomyTermInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm'), id])
            redirect(action: "list")
            return
        }

        [taxonomyTermInstance: taxonomyTermInstance]
    }

    def update(Long id, Long version) {
        def taxonomyTermInstance = TaxonomyTerm.get(id)
        if (!taxonomyTermInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (taxonomyTermInstance.version > version) {
                taxonomyTermInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm')] as Object[],
                          "Another user has updated this TaxonomyTerm while you were editing")
                render(view: "edit", model: [taxonomyTermInstance: taxonomyTermInstance])
                return
            }
        }

        taxonomyTermInstance.properties = params

        if (!taxonomyTermInstance.save(flush: true)) {
            render(view: "edit", model: [TaxonomyTermInstance: taxonomyTermInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm'), taxonomyTermInstance.id])
        redirect(action: "show", id: taxonomyTermInstance.id)
    }

    def delete(Long id) {
        def taxonomyTermInstance = TaxonomyTerm.get(id)
        if (!taxonomyTermInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm'), id])
            redirect(action: "list")
            return
        }

        try {
            taxonomyTermInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'taxonomyTerm.label', default: 'TaxonomyTerm'), id])
            redirect(action: "show", id: id)
        }
    }
}
