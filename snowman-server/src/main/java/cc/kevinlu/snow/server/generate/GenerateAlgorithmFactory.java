package cc.kevinlu.snow.server.generate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.client.enums.IdAlgorithmEnums;
import cc.kevinlu.snow.server.generate.alogrithm.DigitAlgorithm;
import cc.kevinlu.snow.server.generate.alogrithm.SnowflakeAlgorithm;
import cc.kevinlu.snow.server.generate.alogrithm.UuidAlgorithm;
import cc.kevinlu.snow.server.processor.AlgorithmProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
@Component
public class GenerateAlgorithmFactory {

    @Autowired
    private AlgorithmProcessor algorithmProcessor;

    private DigitAlgorithm     digitAlgorithm;
    private SnowflakeAlgorithm snowflakeAlgorithm;
    private UuidAlgorithm      uuidAlgorithm;
    private Object[]           lockObjs = new Object[] { new Object(), new Object(), new Object() };

    public AbstractAlgorithm factory(Integer mode) {
        IdAlgorithmEnums algorithm = IdAlgorithmEnums.getEnumByAlgorithm(mode);
        switch (algorithm) {
            case SNOWFLAKE:
                if (snowflakeAlgorithm == null) {
                    synchronized (lockObjs[0]) {
                        if (snowflakeAlgorithm == null) {
                            snowflakeAlgorithm = new SnowflakeAlgorithm(algorithmProcessor);
                        }
                    }
                }
                return snowflakeAlgorithm;
            case UUID:
                if (uuidAlgorithm == null) {
                    synchronized (lockObjs[1]) {
                        if (uuidAlgorithm == null) {
                            uuidAlgorithm = new UuidAlgorithm(algorithmProcessor);
                        }
                    }
                }
                return uuidAlgorithm;
            default:
                if (digitAlgorithm == null) {
                    synchronized (lockObjs[2]) {
                        if (digitAlgorithm == null) {
                            digitAlgorithm = new DigitAlgorithm(algorithmProcessor);
                        }
                    }
                }
                return digitAlgorithm;
        }
    }

}
