package cc.kevinlu.snow.server.pojo.enums;

/**
 * id status
 * 
 * @author chuan
 */
public enum StatusEnums {

    /**
     */
    USABLE(0),
    USED(1);

    private int status;

    StatusEnums(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
