package semantica

import org.springframework.dao.DataIntegrityViolationException

class ThesaurusTermController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [thesaurusTermInstanceList: ThesaurusTerm.list(params), thesaurusTermInstanceTotal: ThesaurusTerm.count()]
    }

    def create() {
        [thesaurusTermInstance: new ThesaurusTerm(params)]
    }

    def save() {
        def thesaurusTermInstance = new ThesaurusTerm(params)
        if (!thesaurusTermInstance.save(flush: true)) {
            render(view: "create", model: [thesaurusTermInstance: thesaurusTermInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm'), thesaurusTermInstance.id])
        redirect(action: "show", id: thesaurusTermInstance.id)
    }

    def show(Long id) {
        def thesaurusTermInstance = ThesaurusTerm.get(id)
        if (!thesaurusTermInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm'), id])
            redirect(action: "list")
            return
        }

        [thesaurusTermInstance: thesaurusTermInstance]
    }

    def edit(Long id) {
        def thesaurusTermInstance = ThesaurusTerm.get(id)
        if (!thesaurusTermInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm'), id])
            redirect(action: "list")
            return
        }

        [thesaurusTermInstance: thesaurusTermInstance]
    }

    def update(Long id, Long version) {
        def thesaurusTermInstance = ThesaurusTerm.get(id)
        if (!thesaurusTermInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (thesaurusTermInstance.version > version) {
                thesaurusTermInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm')] as Object[],
                          "Another user has updated this ThesaurusTerm while you were editing")
                render(view: "edit", model: [thesaurusTermInstance: thesaurusTermInstance])
                return
            }
        }

        thesaurusTermInstance.properties = params

        if (!thesaurusTermInstance.save(flush: true)) {
            render(view: "edit", model: [thesaurusTermInstance: thesaurusTermInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm'), thesaurusTermInstance.id])
        redirect(action: "show", id: thesaurusTermInstance.id)
    }

    def delete(Long id) {
        def thesaurusTermInstance = ThesaurusTerm.get(id)
        if (!thesaurusTermInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm'), id])
            redirect(action: "list")
            return
        }

        try {
            thesaurusTermInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'thesaurusTerm.label', default: 'ThesaurusTerm'), id])
            redirect(action: "show", id: id)
        }
    }
}
