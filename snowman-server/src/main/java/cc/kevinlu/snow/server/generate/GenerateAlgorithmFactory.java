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

    public AbstractAlgorithm factory(Integer mode) {
        IdAlgorithmEnums algorithm = IdAlgorithmEnums.getEnumByAlgorithm(mode);
        switch (algorithm) {
            case SNOWFLAKE:
                return new SnowflakeAlgorithm(algorithmProcessor);
            case UUID:
                return new UuidAlgorithm(algorithmProcessor);
            default:
                return new DigitAlgorithm(algorithmProcessor);
        }
    }

}
