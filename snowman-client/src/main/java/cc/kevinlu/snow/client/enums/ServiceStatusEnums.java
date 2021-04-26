package cc.kevinlu.snow.client.enums;

/**
 * the status of service instance
 * 
 * @author chuan
 */
public enum ServiceStatusEnums {
    /**
     */
    ONLINE(1),
    OFFLINE(2),
    UNKNOWN(3);

    private int status;

    ServiceStatusEnums(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
