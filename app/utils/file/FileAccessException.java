package utils.file;

/**
 * Exception to be thrown when a file is not accessible.
 *
 */
public class FileAccessException extends Exception {
    /**
     * Constructor to instantiate a FileAccessException.
     *
     */
    public FileAccessException() {
        super();
    }

    /**
     * Constructor to instantiate a FileAccessException with message.
     *
     */
    public FileAccessException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate a FileAccessException with message and cause.
     *
     */
    public FileAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}