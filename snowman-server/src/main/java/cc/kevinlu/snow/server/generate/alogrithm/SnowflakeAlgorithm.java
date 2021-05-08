package cc.kevinlu.snow.server.generate.alogrithm;

import java.util.List;

import cc.kevinlu.snow.server.generate.AbstractAlgorithm;
import cc.kevinlu.snow.server.generate.worker.SnowflakeIdWorker;
import cc.kevinlu.snow.server.processor.AlgorithmProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
public class SnowflakeAlgorithm extends AbstractAlgorithm<Long> {

    public SnowflakeAlgorithm(AlgorithmProcessor algorithmProcessor) {
        super(algorithmProcessor);
    }

    @Override
    protected void generateDistributedId(List<Long> idList, long groupId, long instanceId, long fromValue, int chunk) {
        if (chunk <= 0) {
            throw new IndexOutOfBoundsException("size should not equals zero!");
        }
        SnowflakeIdWorker worker = new SnowflakeIdWorker(groupId, instanceId);
        for (int i = 0; i < chunk; i++) {
            idList.add(worker.nextId());
        }
    }

    @Override
    protected void persistentDB(long instanceId, List<Long> idList) {
        algorithmProcessor.persistentSnowflake(instanceId, idList);
    }
}
