package cc.kevinlu.snow.server.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cc.kevinlu.snow.server.generate.worker.SnowflakeIdWorker;

/**
 * @author chuan
 */
public class CollectionUtils {

    /**
     * fill list with data
     * 
     * @param list
     * @param from
     * @param to
     * @return
     */
    public static List<Long> fillList(List<Long> list, long from, long to) {
        if (to - from > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("size too long!");
        } else if (to >= from) {
            throw new IndexOutOfBoundsException("size should not equals zero!");
        }
        if (list == null) {
            list = new ArrayList((int) (to - from + 1));
        }
        for (long i = from; i <= to; i++) {
            list.add(i);
        }
        return list;
    }

    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Collection coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * fill list with data
     * 
     * @param result
     * @param chunk the num of 
     * @param dcId datacenter id
     * @param instanceId
     */
    public static void fillListBySnowflake(List<Long> result, int chunk, Long dcId, long instanceId) {
        if (chunk <= 0) {
            throw new IndexOutOfBoundsException("size should not equals zero!");
        }
        SnowflakeIdWorker worker = new SnowflakeIdWorker(dcId, instanceId);
        for (int i = 0; i < chunk; i++) {
            result.add(worker.nextId());
        }
    }
}
