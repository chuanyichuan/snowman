package cc.kevinlu.snow.server.generate;

import cc.kevinlu.snow.client.enums.IdAlgorithmEnums;
import cc.kevinlu.snow.client.exceptions.ParamIllegalException;
import cc.kevinlu.snow.client.exceptions.ValueTooBigException;
import cc.kevinlu.snow.server.data.model.GroupDO;
import cc.kevinlu.snow.server.processor.AlgorithmProcessor;

import java.util.List;

/**
 * <p>abs class for generate.</p>
 * <p>1. get thr last value of the group</p>
 * <p>2. compute from value and</p>
 * <p>3. get instance id with groupId and instanceCode</p>
 * <p>4. generate</p>
 *
 * @author chuan
 * @see
 */
public abstract class AbstractAlgorithm<T> {

    protected AlgorithmProcessor algorithmProcessor;

    public AbstractAlgorithm(AlgorithmProcessor algorithmProcessor) {
        this.algorithmProcessor = algorithmProcessor;
    }

    /**
     * get from value
     *
     * @param group
     * @return
     */
    private Long fromValue(GroupDO group) {
        if (IdAlgorithmEnums.DIGIT.getAlgorithm() == group.getMode()) {
            long lastValue = Long.parseLong(group.getLastValue());
            if (lastValue >= Long.MAX_VALUE - group.getChunk()) {
                throw new ValueTooBigException("ID has been used up!");
            }
            return lastValue + 1L;
        }
        return 0L;
    }

    /**
     * get instance id
     *
     * @param groupId
     * @param instanceCode
     * @return
     */
    private Long instanceId(Long groupId, String instanceCode) {
        Long instanceId = algorithmProcessor.instanceId(groupId, instanceCode);
        if (null == instanceId) {
            throw new ParamIllegalException("instance not exists!");
        }
        return instanceId;
    }

    /**
     * client can get records by call it
     *
     * @param group
     * @param instanceCode
     * @return
     */
    public List<T> generate(GroupDO group, String instanceCode) {
        long fromValue = fromValue(group);
        long instanceId = instanceId(group.getId(), instanceCode);
        int chunk = group.getChunk();
        List<T> idList = generateDistributedId(group.getId(), instanceId, fromValue, chunk);
        persistentDB(instanceId, idList);
        recordSnowTimes(instanceId);
        recordLastValue(group.getId(), idList.get(idList.size() - 1));
        return idList;
    }

    /**
     * Implementation
     *
     * @param groupId
     * @param instanceId
     * @param fromValue
     * @param chunk
     */
    protected abstract List<T> generateDistributedId(long groupId, long instanceId, long fromValue,
                                                     int chunk);

    protected abstract void persistentDB(long instanceId, List<T> idList);

    /**
     * record last value
     *
     * @param id
     * @param value
     */
    protected void recordLastValue(Long id, T value) {
        algorithmProcessor.recordGroupLastValue(id, String.valueOf(value));
    }

    /**
     * record times
     *
     * @param instanceId
     */
    private void recordSnowTimes(long instanceId) {
        algorithmProcessor.recordSnowTimes(instanceId);
    }

}
