import grails.converters.JSON
import org.apache.commons.io.FileUtils
import semantica.ThesaurusTerm

import java.util.zip.ZipInputStream

class BootStrap {

  static {
    /**
     * Transfers the content of this file to the new specified file.
     * If this file is a ZIP archive, then saves the contained files into the parent directory
     * of the specified file.
     * The closure is called for each entry in the ZIP file or for the created destination file.
     */
    File.metaClass.transferTo = { File dest, Closure closure ->
      //in metaclass added methods, 'delegate' is the object on which
      //the method is called. Here it's the file to transfer to dest
      if (delegate.name.toLowerCase().endsWith('.zip')) {
        def zip = new ZipInputStream(new FileInputStream(delegate))
        dest = dest.parentFile
        if (!dest.exists()) {
          dest.mkdirs();
        }
        zip.withStream {
          def entry
          while (entry = zip.nextEntry) {
            if (!entry.isDirectory()) {
              def entryFile = new File(dest.absolutePath + File.separator + entry.name)
              entryFile.parentFile?.mkdirs()
              def output = new FileOutputStream(entryFile)
              output.withStream {
                int len = 0;
                byte[] buffer = new byte[4096]
                while ((len = zip.read(buffer)) > 0) {
                  output.write(buffer, 0, len);
                }
              }
              closure?.call(entryFile)
            }
            else {
              new File(dest.absolutePath + File.separator + entry.name).mkdir()
            }
          }
        }
      } else {
        if (dest.directory) {
          dest = new File(dest.absolutePath + File.separator + delegate.name)
        }
        FileUtils.copyFile(delegate, dest)
        closure?.call(dest)
      }
    }

    /**
     * Defines a specific behaviour when marshalling ThesaurusTerm objects in JSON.
     */
    JSON.registerObjectMarshaller(ThesaurusTerm) {
      return [
          id: it.id,
          label: it.label,
          keywords: it.keywords,
          generalTerm: it.generalTerm?.id,
          moreGeneral: it.moreGeneral,
          specificTerms: it.specificTerms.collect { it.id }
      ]
    }
  }

  def init = { servletContext ->

  }
  def destroy = {
  }
}
