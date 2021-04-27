package cc.kevinlu.snow.client.exceptions;

/**
 * @author chuan
 */
public class ValueTooBigException extends RuntimeException {

    private String code;

    public ValueTooBigException() {
    }

    public ValueTooBigException(String code) {
        this.code = code;
    }

    public ValueTooBigException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ValueTooBigException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
