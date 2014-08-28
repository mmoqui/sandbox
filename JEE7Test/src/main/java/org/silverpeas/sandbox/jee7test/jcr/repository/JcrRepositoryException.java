package org.silverpeas.sandbox.jee7test.jcr.repository;

/**
 * @author mmoquillon
 */
public class JcrRepositoryException extends RuntimeException {

  public JcrRepositoryException() {
  }

  public JcrRepositoryException(final String message) {
    super(message);
  }

  public JcrRepositoryException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public JcrRepositoryException(final Throwable cause) {
    super(cause);
  }

  public JcrRepositoryException(final String message, final Throwable cause,
      final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
