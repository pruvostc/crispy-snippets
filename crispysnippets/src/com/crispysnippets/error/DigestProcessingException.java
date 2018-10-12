package com.crispysnippets.error;

/** Digest Processing Exception Declaration. **/
public class DigestProcessingException extends Exception {
  public static final long serialVersionUID = 42L;
  
  public DigestProcessingException() {
    super();
  }
  
  public DigestProcessingException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public DigestProcessingException(String message) {
    super(message);
  }

  public DigestProcessingException(Throwable throwable) {
    super(throwable);
  }
}