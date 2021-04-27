package cc.kevinlu.snow.server.generate.alogrithm;

import java.util.List;

import cc.kevinlu.snow.server.generate.AbstractAlgorithm;
import cc.kevinlu.snow.server.processor.InstanceCacheProcessor;
import cc.kevinlu.snow.server.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
public class SnowflakeAlgorithm extends AbstractAlgorithm<Long> {

    public SnowflakeAlgorithm(InstanceCacheProcessor instanceCacheProcessor) {
        super(instanceCacheProcessor);
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
}
