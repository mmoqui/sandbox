package semantica

/**
 * Exception thrown when an error is encountered while performing business tasks in the application.
 */
class ApplicationException extends RuntimeException {

  ApplicationException(String message) {
    super(message);
  }

  ApplicationException(String message, Throwable cause) {
    super(message, cause);
  }
}
