package org.djodjo.tarator;

/**
 * An checked {@link Exception} indicating that event injection failed with a
 * {@link SecurityException}.
 */
public final class InjectEventSecurityException extends Exception implements TaratorException {

  public InjectEventSecurityException(String message) {
    super(message);
  }

  public InjectEventSecurityException(Throwable cause) {
    super(cause);
  }

  public InjectEventSecurityException(String message, Throwable cause) {
    super(message, cause);
  }
}
