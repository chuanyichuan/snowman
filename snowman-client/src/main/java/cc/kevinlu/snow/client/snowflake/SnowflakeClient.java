package cc.kevinlu.snow.client.snowflake;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * The client that generates the snowflake ID<br/>
 * <p>It only has one function called generate, client can use it to get some records.</p>
 * <p>When the client calls it, they should provide the code of the group they belong to.</p>
 * <p>For Example:
 * <br>
 * <pre>
 *    {@code @Autowired}
 *     private SnowflakeClient client;
 *     
 *     public void method(String groupCode) {
 *         List<Long> idList = client.generate(groupCode);
 *     }
 * </pre>
 * </p>
 * 
 * @author chuan
 */
@FeignClient(name = "snowman", contextId = "snowflake")
public interface SnowflakeClient {

    /**
     * generate function
     * 
     * @param groupCode
     * @return
     */
    @GetMapping(value = "/generate/{code}/{instance}", produces = MediaType.APPLICATION_JSON_VALUE)
    default List<Object> generate(@PathVariable(name = "code") String groupCode,
                                  @PathVariable(name = "instance") String instanceCode) {
        return new ArrayList<>();
    }

}
