package cc.kevinlu.snow.server.processor;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;

import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
@Component
public class SnowflakeLockProcessor {

    /**
     * groupCode cache set
     */
    private volatile Set<String> groupCodeSet    = new ConcurrentHashSet<>();

    public static final Long     DEFAULT_TIMEOUT = 3000L;

    /**
     * release lock
     * 
     * @param groupCode
     */
    public boolean releaseLock(String groupCode) {
        return groupCodeSet.remove(groupCode);
    }

    /**
     * try to lock the group
     * 
     * @param groupCode
     * @param timeout unit: ms
     * @return
     */
    public boolean tryLock(String groupCode, long timeout) {
        if (timeout <= 0L) {
            timeout = DEFAULT_TIMEOUT;
        }
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < timeout) {
            if (groupCodeSet.add(groupCode)) {
                return true;
            }
        }
        return false;
    }

}
