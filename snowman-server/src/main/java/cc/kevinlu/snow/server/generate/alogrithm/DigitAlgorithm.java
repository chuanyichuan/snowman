package cc.kevinlu.snow.server.generate.alogrithm;

import java.util.List;

import cc.kevinlu.snow.server.generate.AbstractAlgorithm;
import cc.kevinlu.snow.server.pojo.PersistentBO;
import cc.kevinlu.snow.server.processor.AlgorithmProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
public class DigitAlgorithm extends AbstractAlgorithm<Long> {

    public DigitAlgorithm(AlgorithmProcessor algorithmProcessor) {
        super(algorithmProcessor);
    }

    @Override
    protected void generateDistributedId(List<Long> idList, long groupId, long instanceId, long fromValue, int chunk) {
        for (long i = fromValue; i < fromValue + chunk; i++) {
            idList.add(i);
        }
    }

    @Override
    protected void persistentDB(PersistentBO<Long> persistent) {
        algorithmProcessor.persistentDigit(persistent);
    }

}
