package cc.kevinlu.snow.server.generate.alogrithm;

import java.util.List;

import com.alibaba.nacos.common.utils.UuidUtils;

import cc.kevinlu.snow.server.generate.AbstractAlgorithm;
import cc.kevinlu.snow.server.processor.InstanceCacheProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
public class UuidAlgorithm extends AbstractAlgorithm<String> {

    public UuidAlgorithm(InstanceCacheProcessor instanceCacheProcessor) {
        super(instanceCacheProcessor);
    }

    @Override
    protected void generateDistributedId(List<String> idList, long groupId, long instanceId, long fromValue,
                                         int chunk) {
        for (int i = 0; i < chunk; i++) {
            idList.add(UuidUtils.generateUuid().replaceAll("-", ""));
        }
    }
}
