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
   * The file can be either a ZIP archive with one or several contents to upload or just a single
   * content to upload.
   */
  def upload() {
    def file = request.getFile('fileToUpload')
    String fileName = file.originalFilename
    if (file.empty) {
      flash.message = 'File cannot be empty'
    } else {
      def content
      if (fileName.toLowerCase().endsWith('.zip')) {
        int suffixIndex = fileName.lastIndexOf(".")
        content = File.createTempFile(fileName.substring(0, suffixIndex), fileName.substring(suffixIndex))
        file.transferTo(content)
      } else {
        content = [transferTo: { dest, Closure c ->
          file.transferTo(dest)
          c.call(dest)
        }]
      }
      contentService.save(content, fileName)
      flash.message = "File \'${fileName}\' uploaded!"
    }
    redirect action: 'index'
  }
}
