package semantica



import org.junit.*
import grails.test.mixin.*

@TestFor(ThesaurusTermController)
@Mock(ThesaurusTerm)
class ThesaurusTermControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/thesaurusTerm/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.thesaurusTermInstanceList.size() == 0
        assert model.thesaurusTermInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.thesaurusTermInstance != null
    }

    void testSave() {
        controller.save()

        assert model.thesaurusTermInstance != null
        assert view == '/thesaurusTerm/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/thesaurusTerm/show/1'
        assert controller.flash.message != null
        assert ThesaurusTerm.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/thesaurusTerm/list'

        populateValidParams(params)
        def thesaurusTerm = new ThesaurusTerm(params)

        assert thesaurusTerm.save() != null

        params.id = thesaurusTerm.id

        def model = controller.show()

        assert model.thesaurusTermInstance == thesaurusTerm
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/thesaurusTerm/list'

        populateValidParams(params)
        def thesaurusTerm = new ThesaurusTerm(params)

        assert thesaurusTerm.save() != null

        params.id = thesaurusTerm.id

        def model = controller.edit()

        assert model.thesaurusTermInstance == thesaurusTerm
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/thesaurusTerm/list'

        response.reset()

        populateValidParams(params)
        def thesaurusTerm = new ThesaurusTerm(params)

        assert thesaurusTerm.save() != null

        // test invalid parameters in update
        params.id = thesaurusTerm.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/thesaurusTerm/edit"
        assert model.thesaurusTermInstance != null

        thesaurusTerm.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/thesaurusTerm/show/$thesaurusTerm.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        thesaurusTerm.clearErrors()

        populateValidParams(params)
        params.id = thesaurusTerm.id
        params.version = -1
        controller.update()

        assert view == "/thesaurusTerm/edit"
        assert model.thesaurusTermInstance != null
        assert model.thesaurusTermInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/thesaurusTerm/list'

        response.reset()

        populateValidParams(params)
        def thesaurusTerm = new ThesaurusTerm(params)

        assert thesaurusTerm.save() != null
        assert ThesaurusTerm.count() == 1

        params.id = thesaurusTerm.id

        controller.delete()

        assert ThesaurusTerm.count() == 0
        assert ThesaurusTerm.get(thesaurusTerm.id) == null
        assert response.redirectedUrl == '/thesaurusTerm/list'
    }
}
