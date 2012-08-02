package semantica

class FileUploadController {

  def contentService

  /**
   * Goes to the file upload page.
   */
  def index() {
    render view: 'upload'
  }

  /**
   * Uploads a file into Semantica and sends back to the upload page.
   */
  def upload() {
    def file = request.getFile('fileToUpload')
    if (file.empty) {
      flash.message = 'File cannot be empty'
    } else {
        String fileName = request.getParameter('fileName')
        contentService.save(file, fileName)
        flash.message = "File \'${fileName}\' uploaded!"
    }
    redirect action: 'index'
  }
}
