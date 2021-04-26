package cc.kevinlu.snow.client.exceptions;

/**
 * @author chuan
 */
public class ParamIllegalException extends RuntimeException {

    private String code;

    public ParamIllegalException() {
    }

    public ParamIllegalException(String code) {
        this.code = code;
    }

    public ParamIllegalException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ParamIllegalException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }
}
