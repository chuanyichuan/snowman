package cc.kevinlu.snow.client.enums;

/**
 * the algorithm of generate
 * 
 * @author chuan
 */
public enum IdAlgorithmEnums {
    /**
     */
    DIGIT(1),
    SNOWFLAKE(2),
    UUID(3),
    TIMESTAMP(4);

    private int algorithm;

    IdAlgorithmEnums(int algorithm) {
        this.algorithm = algorithm;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public static IdAlgorithmEnums getEnumByAlgorithm(int algorithm) {
        IdAlgorithmEnums[] values = IdAlgorithmEnums.values();
        for (IdAlgorithmEnums value : values) {
            if (value.getAlgorithm() == algorithm) {
                return value;
            }
        }
        return DIGIT;
    }

}
