package cc.kevinlu.snow.server.config;

/**
 * @author chuan
 */
public final class Constants {

    /**
     * 搜索某一时间内的jira数据
     */
    public static final String   JQL_SEARCH_UPDATED_FORMAT = "%s>=%s AND %s<=%s";

    public static final String[] LETTERS                   = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9" };

    /**
     * nacos default group name
     */
    public static final String   NACOS_DEFAULT_GROUP       = "DEFAULT_GROUP";

    /**
     * nacos default timeout
     */
    public static final Long     NACOS_DEFAULT_TIMEOUT     = 10000L;

    public static final String   NACOS_DEFAULT_NAMESPACE   = "public";
}
