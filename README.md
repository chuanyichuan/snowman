# snowman
snowflake（分布式ID生成器），基于Twitter的Snowflake理论

支持客户端分组按块（chunk）生成算法，当前支持的算法包括UUID、DIGIT递增顺序号、SNOWFLAKE（雪花算法）

## 项目说明

- ***JDK 版本要求: `JDK8+`*** 

## 核心概念
* 服务名称name- 服务的名称
* 区块chunk- 一个区块对应一个客户端集群，集群内节点共享，代表每次生成的ID数量
* 服务组group- 服务独有唯一标识
* 服务实例ID- 服务组中每一个运行实例的唯一标识(集群)
* ID策略mode- 可选值，可选项为digit、snowflake、uuid，默认为digit


## 使用方法

客户端引入依赖
```xml
<dependency>
    <groupId>cc.kevinlu</groupId>
    <artifactId>snowman-spring-boot-starter</artifactId>
    <version>最新版本</version>
</dependency>
```
开启配置(Nacos)
```yaml
snowman:
  prop:
    name: springcloud-nacos
    chunk: 25
    mode: uuid
    group-id: springcloud-nacos
    server-id: nacos-1
```
开启配置(spring-boot)
```properties
snowman.prop.name=springboot
snowman.prop.group-id=springboot
snowman.prop.server-id=1
server.port=8081
```
使用
```java
import cc.kevinlu.snow.autoconfigure.SnowmanClient;

@RestController
public class TestController {

    @Resource
    private SnowmanClient snowmanClient;

    @RequestMapping(value = "/index")
    public List<Object> query() {
        return snowmanClient.generateSnowId();
    }
}
```
> 服务端请查看[snowman-server](https://github.com/chuanyichuan/snowman-server)
> `maven`依赖等详细配置请查看[examples](https://github.com/chuanyichuan/snowman-example)目录下的演示项目
