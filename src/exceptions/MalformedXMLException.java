package exceptions;

/**
 * The MalformedXMLException is an exception-bundle for the various types of exceptions thrown by
 * the XML reader. Each exception thrown by the XML reader will be dealt with in the same way as
 * they are all caused by trying to load a 'bad' file. For this reason, bundling them into this
 * custom exception made the most sense. However, there are some locations in the code where the
 * exception has to be handled directly, as the usage of lambda makes it hard to throw the exception
 * all the way up to the Home page.
 */
public class MalformedXMLException extends Exception {

  /**
   * Constructor for the MalformedXMLException
   *
   * @param message of the error
   */
  public MalformedXMLException(String message) {
    super(message);
  }
}
