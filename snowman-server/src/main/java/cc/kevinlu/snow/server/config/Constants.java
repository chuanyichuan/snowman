package cc.kevinlu.snow.server.config;

/**
 * @author chuan
 */
public final class Constants {

    public static final int    DEFAULT_TIMEOUT                     = 3000;

    public static final int    BATCH_INSERT_SIZE                   = 500;

    public static final String CHECK_CHUNK_TOPIC                   = "check_chunk_queue";

    public static final String CHECK_CHUNK_LOCK_PATTERN            = "check_chunk_lock_%s";

    public static final String GROUP_RECENT_MAX_VALUE_QUEUE        = "group_recent_max_value_queue";
    public static final String GROUP_RECENT_MAX_VALUE_ITEM_PATTERN = "item_%d";

    /**
     * fill with groupId and instanceId and mode
     */
    public static final String CACHE_ID_LOCK_PATTERN               = "cache_id_lock_%d_%d_%d";

    /**
     * fill with groupId
     */
    public static final String CACHE_GENERATE_LOCK_PATTERN         = "cache_generate_lock_%s";

}
