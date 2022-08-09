![](https://files.mdnice.com/user/5408/d2bf2d3a-27b6-483c-9f4b-4fdf1fa40179.png)

大家好，我是一航

后端程序员，不管是出去面试，还是当面试官，Redis几乎是100%会问到的技术点；究其原因，主要是因为他实在过于强大、使用率太高了；导致项目中几乎无处不在。

那Redis部分，不出意外，第一个问题就是：**你做的项目，用Redis干了些啥？**大部分人的回答都会是：**缓存**；当问到是否还有其他场景中使用时，部分用的少的朋友就会微微摇头；

其实也没错，Redis绝不部分使用场景就是用来做缓存；但是，由于Redis 支持比较丰富的数据结构，因此他能实现的功能并不仅限于缓存，而是可以运用到各种业务场景中，开发出既简洁、又高效的系统;

下面整理了20种 Redis 的妙用场景，每个方案都用一个实际的业务需求并结合数据结构的API来讲解，希望大家能够理解其底层的实现方式，学会举一反三，并运用到项目的方方面面：

![](https://files.mdnice.com/user/5408/09f7057e-ae3c-476d-9ecb-0319b92bdedf.png)


> 本文稍微有点点长，如果时间不够，建议看一下目录，收藏起来，用的时候再翻出来看看。

测试源码：https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-011-redis 

## 缓存

本文假定你已经了解过Redis，并知晓Redis最基础的一些使用，如果你对Redis的基础API还不了解，可以先看一下菜鸟教程：https://www.runoob.com/redis，那么缓存部分及基础API的演示，就不过多来讲解了；

但是，基本的数据结构，在这里再列举一下，方便后续方案的理解：

| 结构类型         | 结构存储的值                               | 结构的读写能力                                               |
| ---------------- | ------------------------------------------ | ------------------------------------------------------------ |
| **String字符串** | 可以是字符串、整数或浮点数                 | 对整个字符串或字符串的一部分进行操作；对整数或浮点数进行自增或自减操作； |
| **List列表**     | 一个链表，链表上的每个节点都包含一个字符串 | 对链表的两端进行push和pop操作，读取单个或多个元素；根据值查找或删除元素； |
| **Set集合**      | 包含字符串的无序集合                       | 字符串的集合，包含基础的方法有：看是否存在、添加、获取、删除；还包含计算交集、并集、差集等 |
| **Hash散列**     | 包含键值对的无序散列表                     | 包含方法有：添加、获取、删除单个元素                         |
| **Zset有序集合** | 和散列一样，用于存储键值对                 | 字符串成员与浮点数分数之间的有序映射；元素的排列顺序由分数的大小决定；包含方法有：添加、获取、删除单个元素以及根据分值范围或成员来获取元素 |

- 依赖

  以下所有通过SpringBoot测试的用例，都需要引入 Redis 的依赖

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
  </dependency>
  ```



## 抽奖

曾几何时，抽奖是互联网APP热衷的一种推广、拉新的方式，节假日没有好的策划，那就抽个奖吧！一堆用户参与进来，然后随机抽取几个幸运用户给予实物/虚拟的奖品；此时，开发人员就需要写上一个抽奖的算法，来实现幸运用户的抽取；其实我们完全可以利用Redis的集合（Set），就能轻松实现抽奖的功能；

功能实现需要的API

- **SADD** key member1 [member2]：添加一个或者多个参与用户；
- **SRANDMEMBER** KEY [count]：随机返回一个或者多个用户；
- **SPOP** key：随机返回一个或者多个用户，并删除返回的用户；

SRANDMEMBER 和 SPOP  主要用于两种不同的抽奖模式，SRANDMEMBER 适用于一个用户可中奖多次的场景（就是中奖之后，不从用户池中移除，继续参与其他奖项的抽取）；而 SPOP 就适用于仅能中一次的场景（一旦中奖，就将用户从用户池中移除，后续的抽奖，就不可能再抽到该用户）； 通常 SPOP 会用的会比较多。

### Redis-cli 操作

```java
127.0.0.1:6379> SADD raffle user1
(integer) 1
127.0.0.1:6379> SADD raffle user2 user3 user4 user5 user6 user7 user8 user9 user10
(integer) 9
127.0.0.1:6379> SRANDMEMBER raffle 2
1) "user5"
2) "user2"
127.0.0.1:6379> SPOP raffle 2
1) "user3"
2) "user4"
127.0.0.1:6379> SPOP raffle 2
1) "user10"
2) "user9"
```

### SpringBoot 实现

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author 一行Java
 * @title: RaffleMain
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/18 15:17
 */
@Slf4j
@SpringBootTest
public class RaffleMain {
    private final String KEY_RAFFLE_PROFIX = "raffle:";
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void test() {
        Integer raffleId = 1;
        join(raffleId, 1000, 1001, 2233, 7890, 44556, 74512);
        List lucky = lucky(raffleId, 2);
        log.info("活动：{} 的幸运中奖用户是：{}", raffleId, lucky);
    }

    public void join(Integer raffleId, Integer... userIds) {
        String key = KEY_RAFFLE_PROFIX + raffleId;
        redisTemplate.opsForSet().add(key, userIds);
    }

    public List lucky(Integer raffleId, long num) {
        String key = KEY_RAFFLE_PROFIX + raffleId;
        // 随机抽取 抽完之后将用户移除奖池
        List list = redisTemplate.opsForSet().pop(key, num);
        // 随机抽取 抽完之后用户保留在池子里
        //List list = redisTemplate.opsForSet().randomMembers(key, num);
        return list;
    }

}
```



## Set实现点赞/收藏功能

有互动属性APP一般都会有点赞/收藏/喜欢等功能，来提升用户之间的互动。

**传统的实现**：用户点赞之后，在数据库中记录一条数据，同时一般都会在主题库中记录一个点赞/收藏汇总数，来方便显示；

**Redis方案**：基于Redis的集合（Set），记录每个帖子/文章对应的收藏、点赞的用户数据，同时set还提供了检查集合中是否存在指定用户，用户快速判断用户是否已经点赞过

功能实现需要的API

- **SADD** key member1 [member2]：添加一个或者多个成员（点赞）
- **SCARD** key：获取所有成员的数量（点赞数量）
- **SISMEMBER** key member：判断成员是否存在（是否点赞）
- **SREM** key member1 [member2] ：移除一个或者多个成员（点赞数量）

### Redis-cli API操作

```java
127.0.0.1:6379> sadd like:article:1 user1
(integer) 1
127.0.0.1:6379> sadd like:article:1 user2
(integer) 1
# 获取成员数量（点赞数量）
127.0.0.1:6379> SCARD like:article:1
(integer) 2
# 判断成员是否存在（是否点在）
127.0.0.1:6379> SISMEMBER like:article:1 user1
(integer) 1
127.0.0.1:6379> SISMEMBER like:article:1 user3
(integer) 0
# 移除一个或者多个成员（取消点赞）
127.0.0.1:6379> SREM like:article:1 user1
(integer) 1
127.0.0.1:6379> SCARD like:article:1
(integer) 1
```

### SpringBoot 操作

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 一行Java
 * @title: LikeMain
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/18 15:38
 */
@Slf4j
@SpringBootTest
public class LikeMain {
    private final String KEY_LIKE_ARTICLE_PROFIX = "like:article:";

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void test() {
        long articleId = 100;
        Long likeNum = like(articleId, 1001, 1002, 2001, 3005, 4003);
        unLike(articleId, 2001);
        likeNum = likeNum(articleId);
        boolean b2001 = isLike(articleId, 2001);
        boolean b3005 = isLike(articleId, 3005);
        log.info("文章：{} 点赞数量：{} 用户2001的点赞状态：{} 用户3005的点赞状态：{}", articleId, likeNum, b2001, b3005);
    }

    /**
     * 点赞
     *
     * @param articleId 文章ID
     * @return 点赞数量
     */
    public Long like(Long articleId, Integer... userIds) {
        String key = KEY_LIKE_ARTICLE_PROFIX + articleId;
        Long add = redisTemplate.opsForSet().add(key, userIds);
        return add;
    }

    public Long unLike(Long articleId, Integer... userIds) {
        String key = KEY_LIKE_ARTICLE_PROFIX + articleId;
        Long remove = redisTemplate.opsForSet().remove(key, userIds);
        return remove;
    }

    public Long likeNum(Long articleId) {
        String key = KEY_LIKE_ARTICLE_PROFIX + articleId;
        Long size = redisTemplate.opsForSet().size(key);
        return size;
    }

    public Boolean isLike(Long articleId, Integer userId) {
        String key = KEY_LIKE_ARTICLE_PROFIX + articleId;
        return redisTemplate.opsForSet().isMember(key, userId);
    }

}
```





## 排行榜

排名、排行榜、热搜榜是很多APP、游戏都有的功能，常用于用户活动推广、竞技排名、热门信息展示等功能；

![](https://files.mdnice.com/user/5408/0cc0caee-d5b4-46be-b847-9bb8edc03ba5.png)

比如上面的热搜榜，热度数据来源于全网用户的贡献，但用户只关心热度最高的前50条。

**常规的做法**：就是将用户的名次、分数等用于排名的数据更新到数据库，然后查询的时候通过Order by + limit 取出前50名显示，如果是参与用户不多，更新不频繁的数据，采用数据库的方式也没有啥问题，但是一旦出现爆炸性热点资讯（比如：大陆收复湾湾，xxx某些绿了等等），短时间会出现爆炸式的流量，瞬间的压力可能让数据库扛不住；

**Redis方案**：将热点资讯全页缓存，采用Redis的**有序队列**（Sorted Set）来缓存热度（SCORES），即可瞬间缓解数据库的压力，同时轻松筛选出热度最高的50条；

功能实现需要的命令

- **ZADD** key score1 member1 [score2 member2]：添加并设置SCORES，支持一次性添加多个；
- **ZREVRANGE** key start stop [WITHSCORES] ：根据SCORES降序排列；
- **ZRANGE** key start stop [WITHSCORES] ：根据SCORES降序排列；

### Redis-cli操作

```java
# 单个插入
127.0.0.1:6379> ZADD ranking 1 user1  
(integer) 1
# 批量插入
127.0.0.1:6379> ZADD ranking 10 user2 50 user3 3 user4 25 user5
(integer) 4
# 降序排列 不带SCORES
127.0.0.1:6379> ZREVRANGE ranking 0 -1 
1) "user3"
2) "user5"
3) "user2"
4) "user4"
5) "user1"
# 降序排列 带SCORES
127.0.0.1:6379> ZREVRANGE ranking 0 -1 WITHSCORES
 1) "user3"
 2) "50"
 3) "user5"
 4) "25"
 5) "user2"
 6) "10"
 7) "user4"
 8) "3"
 9) "user1"
10) "1"
# 升序
127.0.0.1:6379> ZRANGE ranking 0 -1 WITHSCORES
 1) "user1"
 2) "1"
 3) "user4"
 4) "3"
 5) "user2"
 6) "10"
 7) "user5"
 8) "25"
 9) "user3"
10) "50"
```



### SpringBoot操作

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * @author 一行Java
 * @title: RankingTest
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/18 15:54
 */
@SpringBootTest
@Slf4j
public class RankingTest {
    private final String KEY_RANKING = "ranking";
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void test() {
        add(1001, (double) 60);
        add(1002, (double) 80);
        add(1003, (double) 100);
        add(1004, (double) 90);
        add(1005, (double) 70);

        // 取所有
        Set<DefaultTypedTuple> range = range(0, -1);
        log.info("所有用户排序：{}", range);

        // 前三名
        range = range(0, 2);
        log.info("前三名排序：{}", range);
    }

    public Boolean add(Integer userId, Double score) {
        Boolean add = redisTemplate.opsForZSet().add(KEY_RANKING, userId, score);
        return add;
    }

    public Set<DefaultTypedTuple> range(long min, long max) {
        // 降序
        Set<DefaultTypedTuple> set = redisTemplate.opsForZSet().reverseRangeWithScores(KEY_RANKING, min, max);
        // 升序
        //Set<DefaultTypedTuple> set = redisTemplate.opsForZSet().rangeWithScores(KEY_RANKING, min, max);
        return set;
    }
}
```

输出

```java
所有用户排序：[DefaultTypedTuple [score=100.0, value=1003], DefaultTypedTuple [score=90.0, value=1004], DefaultTypedTuple [score=80.0, value=1002], DefaultTypedTuple [score=70.0, value=1005], DefaultTypedTuple [score=60.0, value=1001]]
前三名排序：[DefaultTypedTuple [score=100.0, value=1003], DefaultTypedTuple [score=90.0, value=1004], DefaultTypedTuple [score=80.0, value=1002]]
```



## PV统计（incr自增计数）

Page View（PV）指的是页面浏览量，是用来衡量流量的一个重要标准，也是数据分析很重要的一个依据；通常统计规则是页面被展示一次，就加一

功能所需命令

- **INCR**：将 key 中储存的数字值增一

### Redis-cli 操作

```shell
127.0.0.1:6379> INCR pv:article:1
(integer) 1
127.0.0.1:6379> INCR pv:article:1
(integer) 2
```



## UV统计（HeyperLogLog）

前面，介绍了通过（INCR）方式来实现页面的PV；除了PV之外，UV（独立访客）也是一个很重要的统计数据；

但是如果要想通过计数（INCR）的方式来实现UV计数，就非常的麻烦，增加之前，需要判断这个用户是否访问过；那判断依据就需要额外的方式再进行记录。

你可能会说，不是还有Set嘛！一个页面弄个集合，来一个用户塞（SADD）一个用户进去，要统计UV的时候，再通过SCARD汇总一下数量，就能轻松搞定了；此方案确实能实现UV的统计效果，但是忽略了成本；如果是普通页面，几百、几千的访问，可能造成的影响微乎其微，如果一旦遇到爆款页面，动辄上千万、上亿用户访问时，就一个页面UV将会带来非常大的内存开销，对于如此珍贵的内存来说，这显然是不划算的。

此时，HeyperLogLog数据结构，就能完美的解决这一问题，它提供了一种**不精准的去重计数**方案，注意！这里强调一下，是不精准的，会存在误差，不过误差也不会很大，**标准的误差率是0.81%**，这个误差率对于统计UV计数，是能够容忍的；所以，不要将这个数据结构拿去做精准的去重计数。

另外，HeyperLogLog 是会**占用12KB的存储空间**，虽然说，Redis 对 HeyperLogLog 进行了优化，在存储数据比较少的时候，采用了稀疏矩阵存储，只有在数据量变大，稀疏矩阵空间占用超过阈值时，才会转为空间为12KB的稠密矩阵；相比于成千、上亿的数据量，这小小的12KB，简直是太划算了；但是还是建议，不要将其用于数据量少，且频繁创建 HeyperLogLog 的场景，避免使用不当，造成资源消耗没减反增的不良效果。

功能所需命令：

- **PFADD** key element [element ...]：增加计数（统计UV）
- **PFCOUNT** key [key ...]：获取计数（货物UV）
- **PFMERGE** destkey sourcekey [sourcekey ...]：将多个 HyperLogLog 合并为一个 HyperLogLog（多个合起来统计）

### Redis-cli 操作

```nginx
# 添加三个用户的访问
127.0.0.1:6379> PFADD uv:page:1 user1 user2 user3
(integer) 1
# 获取UV数量
127.0.0.1:6379> PFCOUNT uv:page:1
(integer) 3
# 再添加三个用户的访问  user3是重复用户
127.0.0.1:6379> PFADD uv:page:1 user3 user4 user5
(integer) 1
# 获取UV数量 user3是重复用户 所以这里返回的是5
127.0.0.1:6379> PFCOUNT uv:page:1
(integer) 5
```



### SpringBoot操作HeyperLogLog

模拟测试10000个用户访问id为2的页面

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 一行Java
 * @title: HeyperLogLog 统计UV
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/19 16:13
 */
@SpringBootTest
@Slf4j
public class UVTest {
    private final String KEY_UV_PAGE_PROFIX = "uv:page:";

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void uvTest() {
        Integer pageId = 2;
        for (int i = 0; i < 10000; i++) {
            uv(pageId, i);
        }
        for (int i = 0; i < 10000; i++) {
            uv(pageId, i);
        }

        Long uv = getUv(pageId);
        log.info("pageId:{} uv:{}", pageId, uv);
    }

    /**
     * 用户访问页面
     * @param pageId
     * @param userId
     * @return
     */
    private Long uv(Integer pageId, Integer userId) {
        String key = KEY_UV_PAGE_PROFIX + pageId;
        return redisTemplate.opsForHyperLogLog().add(key, userId);
    }

    /**
     * 统计页面的UV
     * @param pageId
     * @return
     */
    private Long getUv(Integer pageId) {
        String key = KEY_UV_PAGE_PROFIX + pageId;
        return redisTemplate.opsForHyperLogLog().size(key);
    }
}
```

日志输出

```
pageId:2 uv:10023
```

由于存在误差，这里访问的实际访问的数量是1万，统计出来的多了23个，在标准的误差（0.81%）范围内，加上UV数据不是必须要求准确，因此这个误差是可以接受的。



## 去重（BloomFiler）

通过上面HeyperLogLog的学习，我们掌握了一种**不精准的去重计数**方案，但是有没有发现，他没办法获取某个用户是否访问过；理想中，我们是希望有一个`PFEXISTS`的命令，来判断某个key是否存在，然而HeyperLogLog并没有；要想实现这一需求，就得 BloomFiler 上场了。

- 什么是Bloom Filter？

  Bloom Filter是由Bloom在1970年提出的一种多哈希函数映射的快速查找算法。
  通常应用在一些需要快速判断某个元素是否属于集合，但是**并不严格要求100%正确的场合**。
  基于一种概率数据结构来实现，是一个有趣且强大的算法。

**举个例子**：假如你写了一个爬虫，用于爬取网络中的所有页面，当你拿到一个新的页面时，如何判断这个页面是否爬取过？

**普通做法**：每爬取一个页面，往数据库插入一行数据，记录一下URL，每次拿到一个新的页面，就去数据库里面查询一下，存在就说明爬取过；

**普通做法的缺点**：少量数据，用传统方案没啥问题，如果是海量数据，每次爬取前的检索，将会越来越慢；如果你的爬虫只关心内容，对来源数据不太关心的话，这部分数据的存储，也将消耗你很大的物理资源；

此时通过 BloomFiler 就能以很小的内存空间作为代价，即可轻松判断某个值是否存在。

同样，BloomFiler 也不那么精准，在默认参数情况下，是存在1%左右的误差；但是 BloomFiler 是允许通过error_rate（误差率）以及initial_size（预计大小）来设置他的误差比例

- **error_rate**：误差率，越低，需要的空间就越大；
- **initial_size**：预计放入值的数量，当实际放入的数量大于设置的值时，误差率就会逐渐升高；所以为了避免误差率，可以提前做好估值，避免再次大的误差；

### BloomFiler 安装

为了方便测试，这里使用 Docker 快速安装

```shell
docker run -d -p 6379:6379 redislabs/rebloom
```

功能所需的命令

-  **bf.add** 添加单个元素
-  **bf.madd** 批量田间
-  **bf.exists** 检测元素是否存在
-  **bf.mexists** 批量检测

### Redis-cli操作

```java
127.0.0.1:6379> bf.add web:crawler baidu
(integer) 1
127.0.0.1:6379> bf.madd web:crawler tencent bing
1) (integer) 1
2) (integer) 1
127.0.0.1:6379> bf.exists web:crawler baidu
(integer) 1
127.0.0.1:6379> bf.mexists web:crawler baidu 163
1) (integer) 1
2) (integer) 0
```

### SpringBoot整合

- 工具类 RedisBloom

  ```java
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.data.redis.core.StringRedisTemplate;
  import org.springframework.data.redis.core.script.DefaultRedisScript;
  import org.springframework.data.redis.core.script.RedisScript;
  import org.springframework.stereotype.Component;
  
  import java.util.Arrays;
  import java.util.List;
  
  /**
   * redis布隆过滤器
   *
   * @author 一行Java
   * @title: RedisBloom
   * @projectName ehang-spring-boot
   * @description: TODO
   * @date 2022/7/19 17:03
   */
  @Component
  public class RedisBloom {
  
      private static RedisScript<Boolean> bfreserveScript = new DefaultRedisScript<>("return redis.call('bf.reserve', KEYS[1], ARGV[1], ARGV[2])", Boolean.class);
      private static RedisScript<Boolean> bfaddScript = new DefaultRedisScript<>("return redis.call('bf.add', KEYS[1], ARGV[1])", Boolean.class);
      private static RedisScript<Boolean> bfexistsScript = new DefaultRedisScript<>("return redis.call('bf.exists', KEYS[1], ARGV[1])", Boolean.class);
      private static String bfmaddScript = "return redis.call('bf.madd', KEYS[1], %s)";
      private static String bfmexistsScript = "return redis.call('bf.mexists', KEYS[1], %s)";
      @Autowired
      private StringRedisTemplate redisTemplate;
  
      /**
       * 设置错误率和大小（需要在添加元素前调用，若已存在元素，则会报错）
       * 错误率越低，需要的空间越大
       *
       * @param key
       * @param errorRate   错误率，默认0.01
       * @param initialSize 默认100，预计放入的元素数量，当实际数量超出这个数值时，误判率会上升，尽量估计一个准确数值再加上一定的冗余空间
       * @return
       */
      public Boolean bfreserve(String key, double errorRate, int initialSize) {
          return redisTemplate.execute(bfreserveScript, Arrays.asList(key), String.valueOf(errorRate), String.valueOf(initialSize));
      }
  
      /**
       * 添加元素
       *
       * @param key
       * @param value
       * @return true表示添加成功，false表示添加失败（存在时会返回false）
       */
      public Boolean bfadd(String key, String value) {
          return redisTemplate.execute(bfaddScript, Arrays.asList(key), value);
      }
  
      /**
       * 查看元素是否存在（判断为存在时有可能是误判，不存在是一定不存在）
       *
       * @param key
       * @param value
       * @return true表示存在，false表示不存在
       */
      public Boolean bfexists(String key, String value) {
          return redisTemplate.execute(bfexistsScript, Arrays.asList(key), value);
      }
  
      /**
       * 批量添加元素
       *
       * @param key
       * @param values
       * @return 按序 1表示添加成功，0表示添加失败
       */
      public List<Integer> bfmadd(String key, String... values) {
          return (List<Integer>) redisTemplate.execute(this.generateScript(bfmaddScript, values), Arrays.asList(key), values);
      }
  
      /**
       * 批量检查元素是否存在（判断为存在时有可能是误判，不存在是一定不存在）
       *
       * @param key
       * @param values
       * @return 按序 1表示存在，0表示不存在
       */
      public List<Integer> bfmexists(String key, String... values) {
          return (List<Integer>) redisTemplate.execute(this.generateScript(bfmexistsScript, values), Arrays.asList(key), values);
      }
  
      private RedisScript<List> generateScript(String script, String[] values) {
          StringBuilder sb = new StringBuilder();
          for (int i = 1; i <= values.length; i++) {
              if (i != 1) {
                  sb.append(",");
              }
              sb.append("ARGV[").append(i).append("]");
          }
          return new DefaultRedisScript<>(String.format(script, sb.toString()), List.class);
      }
  
  }
  ```

- 测试

  ```java
  import lombok.extern.slf4j.Slf4j;
  import org.junit.jupiter.api.Test;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.boot.test.context.SpringBootTest;
  import org.springframework.data.redis.core.RedisTemplate;
  
  import java.util.List;
  
  /**
   * @author 一行Java
   * @title: BFTest
   * @projectName ehang-spring-boot
   * @description: TODO
   * @date 2022/7/19 17:04
   */
  @SpringBootTest
  @Slf4j
  public class BFTest {
  
      private final String KEY_WEB_CRAWLER = "web:crawler1";
  
      @Autowired
      RedisBloom bloom;
  
      @Autowired
      RedisTemplate redisTemplate;
  
  
      @Test
      public void test() {
          Boolean hasKey = redisTemplate.hasKey(KEY_WEB_CRAWLER);
          log.info("bloom hasKey:{}", hasKey);
          if (!hasKey) {
              // 不存在的时候  再去初始化
              Boolean bfreserve = bloom.bfreserve(KEY_WEB_CRAWLER, 0.0001, 10000);
              log.info("bloom bfreserve:{}", bfreserve);
          }
          List<Integer> madd = bloom.bfmadd(KEY_WEB_CRAWLER, "baidu", "google");
          log.info("bloom bfmadd:{}", madd);
  
          Boolean baidu = bloom.bfexists(KEY_WEB_CRAWLER, "baidu");
          log.info("bloom bfexists baidu:{}", baidu);
  
          Boolean bing = bloom.bfexists(KEY_WEB_CRAWLER, "bing");
          log.info("bloom bfexists bing:{}", bing);
      }
  }
  ```

  日志输出

  ```java
  com.ehang.redis.bloom_filter.BFTest      : bloom hasKey:false
  com.ehang.redis.bloom_filter.BFTest      : bloom bfreserve:true
  com.ehang.redis.bloom_filter.BFTest      : bloom bfmadd:[1, 1]
  com.ehang.redis.bloom_filter.BFTest      : bloom bfexists baidu:true
  com.ehang.redis.bloom_filter.BFTest      : bloom bfexists bing:false
  ```

  

## 用户签到（BitMap）

很多APP为了拉动用户活跃度，往往都会做一些活动，比如连续签到领积分/礼包等等

![](https://files.mdnice.com/user/5408/0afd3a7f-37cc-437f-b430-41b51a22fa16.png)

**传统做法**：用户每次签到时，往是数据库插入一条签到数据，展示的时候，把本月（或者指定周期）的签到数据获取出来，用于判断用户是否签到、以及连续签到情况；此方式，简单，理解容易；

**Redis做法**：由于签到数据的关注点就2个：是否签到（0/1）、连续性，因此就完全可以利用BitMap（位图）来实现；

![一个月的签到情况，4个字节就记录了（图源：网络）](https://files.mdnice.com/user/5408/6e6c9dfc-0b3d-4803-aa93-eff49ea4e945.png)

如上图所示，将一个月的31天，用31个位（4个字节）来表示，偏移量（offset）代表当前是第几天，0/1表示当前是否签到，连续签到只需从右往左校验连续为1的位数；

由于String类型的最大上限是512M，转换为bit则是2^32个bit位。

所需命令：

- **SETBIT** key offset value：向指定位置offset存入一个0或1

- **GETBIT** key offset：获取指定位置offset的bit值

- **BITCOUNT** key [start] [end]：统计BitMap中值为1的bit位的数量

- **BITFIELD**: 操作（查询，修改，自增）BitMap中bit 数组中的指定位置offset的值

  这里最不容易理解的就是：BITFIELD，详情可参考：https://deepinout.com/redis-cmd/redis-bitmap-cmd/redis-cmd-bitfield.html  而且这部分还必须理解了，否则，该需求的核心部分就没办法理解了；

需求：假如当前为8月4号，检测本月的签到情况，用户分别于1、3、4号签到过

### Redis-cli 操作：

```java
# 8月1号的签到
127.0.0.1:6379> SETBIT RangeId:Sign:1:8899 0 1
(integer) 1

# 8月3号的签到
127.0.0.1:6379> SETBIT RangeId:Sign:1:8899 2 1
(integer) 1

# 8月4号的签到
127.0.0.1:6379> SETBIT RangeId:Sign:1:8899 3 1
(integer) 1

# 查询各天的签到情况
# 查询1号
127.0.0.1:6379> GETBIT RangeId:Sign:1:8899 0
(integer) 1
# 查询2号
127.0.0.1:6379> GETBIT RangeId:Sign:1:8899 1
(integer) 0
# 查询3号
127.0.0.1:6379> GETBIT RangeId:Sign:1:8899 2
(integer) 1
# 查询4号
127.0.0.1:6379> GETBIT RangeId:Sign:1:8899 3
(integer) 1

# 查询指定区间的签到情况
127.0.0.1:6379> BITFIELD RangeId:Sign:1:8899 get u4 0
1) (integer) 11
```

> 1-4号的签到情况为：1011（二进制） ==> 11（十进制）



### 是否签到、连续签到判断

签到功能中，最不好理解的就是是否签到、连续签到的判断，在下面SpringBoot代码中，就是通过这样的：`signFlag >> 1 << 1 != signFlag`来判断的，稍微有一点不好理解，在这里提前讲述一下；

上面测试了1-4号的签到情况，通过`BITFIELD`获取出来signFlag = 11（十进制） = 1011（二进制）；

连续签到的判断依据就是：**从右往左计算连续为1的BIT个数**，二进制 1011 表示连续签到的天数就是2天，2天的计算过程如下：

- 第一步，获取signFlag 

- 第二步，循环天数，以上测试用例是4天的签到情况，for循环也就是4次

- 第三步，从右往左循环判断

  **连续签到**：遇到第一个false的时候，终止并得到连续天数

  **签到详情**：循环所有天数，true就表示当前签到了，false表示当天未签到；

  第一次循环

  ```java
  signFlag = 1011
  signFlag >> 1   结果： 101
  signFlag << 1   结果：1010
  1010 != signFlag（1011） 结果：true  //4号已签到，说明连续签到1天
  signFlag >>= 1  结果： 101   // 此时signFlag = 101
  ```

  第二次循环

  ```java
  signFlag = 101  // 前一次循环计算的结果
  signFlag >> 1   结果： 10
  signFlag << 1   结果：100
  100 != signFlag（101） 结果：true  //3号已签到，说明连续签到2天
  signFlag >>= 1  结果： 10   // 此时signFlag = 10
  ```

  第三次循环

  ```java
  signFlag = 10  // 前一次循环计算的结果
  signFlag >> 1   结果： 1
  signFlag << 1   结果：10
  10 != signFlag（10） 结果：false  //2号未签到，说明连续签到从这里就断了 
  signFlag >>= 1  结果： 1   // 此时signFlag = 1
  ```

  到这一步，遇到第一个false，说明连续签到中断；

  第四次循环：

  ```java
  signFlag = 1  // 前一次循环计算的结果
  signFlag >> 1   结果： 0
  signFlag << 1   结果： 0
  0 != signFlag（1） 结果：true  //1号已签到
  ```

  到此，根据`BITFIELD`获取出来11（十进制），就能得到1、3、4号已签到，2号未签到；连续签到2天；

理解上面的逻辑之后，再来看下面的SpringBoot代码，就会容易很多了；

### SpringBoot实现签到

签到的方式一般就两种，按月（周）/ 自定义周期，下面将两种方式的签到全部列举出来，以供大家参考：

#### 按月签到

签到工具类：

```java
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 一行Java
 * @title: 按月签到
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/18 18:28
 */
@Slf4j
@Service
public class SignByMonthServiceImpl {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private int dayOfMonth() {
        DateTime dateTime = new DateTime();
        return dateTime.dayOfMonth().get();
    }

    /**
     * 按照月份和用户ID生成用户签到标识 UserId:Sign:560:2021-08
     *
     * @param userId 用户id
     * @return
     */
    private String signKeyWitMouth(String userId) {
        DateTime dateTime = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM");

        StringBuilder builder = new StringBuilder("UserId:Sign:");
        builder.append(userId).append(":")
                .append(dateTime.toString(fmt));
        return builder.toString();
    }

    /**
     * 设置标记位
     * 标记是否签到
     *
     * @param key
     * @param offset
     * @param tag
     * @return
     */
    public Boolean sign(String key, long offset, boolean tag) {
        return this.stringRedisTemplate.opsForValue().setBit(key, offset, tag);
    }


    /**
     * 统计计数
     *
     * @param key 用户标识
     * @return
     */
    public long bitCount(String key) {
        return stringRedisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount(key.getBytes()));
    }

    /**
     * 获取多字节位域
     */
    public List<Long> bitfield(String buildSignKey, int limit, long offset) {
        return this.stringRedisTemplate
                .opsForValue()
                .bitField(buildSignKey, BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(limit)).valueAt(offset));
    }


    /**
     * 判断是否被标记
     *
     * @param key
     * @param offest
     * @return
     */
    public Boolean container(String key, long offest) {
        return this.stringRedisTemplate.opsForValue().getBit(key, offest);
    }


    /**
     * 用户今天是否签到
     *
     * @param userId
     * @return
     */
    public int checkSign(String userId) {
        DateTime dateTime = new DateTime();

        String signKey = this.signKeyWitMouth(userId);
        int offset = dateTime.getDayOfMonth() - 1;
        int value = this.container(signKey, offset) ? 1 : 0;
        return value;
    }


    /**
     * 查询用户当月签到日历
     *
     * @param userId
     * @return
     */
    public Map<String, Boolean> querySignedInMonth(String userId) {
        DateTime dateTime = new DateTime();
        int lengthOfMonth = dateTime.dayOfMonth().getMaximumValue();
        Map<String, Boolean> signedInMap = new HashMap<>(dateTime.getDayOfMonth());

        String signKey = this.signKeyWitMouth(userId);
        List<Long> bitfield = this.bitfield(signKey, lengthOfMonth, 0);
        if (!CollectionUtils.isEmpty(bitfield)) {
            long signFlag = bitfield.get(0) == null ? 0 : bitfield.get(0);

            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            for (int i = lengthOfMonth; i > 0; i--) {

                DateTime dateTime1 = dateTime.withDayOfMonth(i);
                signedInMap.put(dateTime1.toString(fmt), signFlag >> 1 << 1 != signFlag);
                signFlag >>= 1;
            }
        }
        return signedInMap;
    }


    /**
     * 用户签到
     *
     * @param userId
     * @return
     */
    public boolean signWithUserId(String userId) {
        int dayOfMonth = this.dayOfMonth();
        String signKey = this.signKeyWitMouth(userId);
        long offset = (long) dayOfMonth - 1;
        boolean re = false;
        if (Boolean.TRUE.equals(this.sign(signKey, offset, Boolean.TRUE))) {
            re = true;
        }

        // 查询用户连续签到次数,最大连续次数为7天
        long continuousSignCount = this.queryContinuousSignCount(userId, 7);
        return re;
    }

    /**
     * 统计当前月份一共签到天数
     *
     * @param userId
     */
    public long countSignedInDayOfMonth(String userId) {
        String signKey = this.signKeyWitMouth(userId);
        return this.bitCount(signKey);
    }


    /**
     * 查询用户当月连续签到次数
     *
     * @param userId
     * @return
     */
    public long queryContinuousSignCountOfMonth(String userId) {
        int signCount = 0;
        String signKey = this.signKeyWitMouth(userId);
        int dayOfMonth = this.dayOfMonth();
        List<Long> bitfield = this.bitfield(signKey, dayOfMonth, 0);

        if (!CollectionUtils.isEmpty(bitfield)) {
            long signFlag = bitfield.get(0) == null ? 0 : bitfield.get(0);
            DateTime dateTime = new DateTime();
            // 连续不为0即为连续签到次数，当天未签到情况下
            for (int i = 0; i < dateTime.getDayOfMonth(); i++) {
                if (signFlag >> 1 << 1 == signFlag) {
                    if (i > 0) break;
                } else {
                    signCount += 1;
                }
                signFlag >>= 1;
            }
        }
        return signCount;
    }


    /**
     * 以7天一个周期连续签到次数
     *
     * @param period 周期
     * @return
     */
    public long queryContinuousSignCount(String userId, Integer period) {
        //查询目前连续签到次数
        long count = this.queryContinuousSignCountOfMonth(userId);
        //按最大连续签到取余
        if (period != null && period < count) {
            long num = count % period;
            if (num == 0) {
                count = period;
            } else {
                count = num;
            }
        }
        return count;
    }
}
```

测试类：

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * @author 一行Java
 * @title: SignTest2
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/19 12:06
 */
@SpringBootTest
@Slf4j
public class SignTest2 {
    @Autowired
    private SignByMonthServiceImpl signByMonthService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 测试用户按月签到
     */
    @Test
    public void querySignDay() {
        //模拟用户签到
        //for(int i=5;i<19;i++){
        redisTemplate.opsForValue().setBit("UserId:Sign:560:2022-08", 0, true);
        //}

        System.out.println("560用户今日是否已签到:" + this.signByMonthService.checkSign("560"));
        Map<String, Boolean> stringBooleanMap = this.signByMonthService.querySignedInMonth("560");
        System.out.println("本月签到情况:");
        for (Map.Entry<String, Boolean> entry : stringBooleanMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + (entry.getValue() ? "√" : "-"));
        }
        long countSignedInDayOfMonth = this.signByMonthService.countSignedInDayOfMonth("560");
        System.out.println("本月一共签到:" + countSignedInDayOfMonth + "天");
        System.out.println("目前连续签到:" + this.signByMonthService.queryContinuousSignCount("560", 7) + "天");
    }
}
```

执行日志：

```java
c.e.r.bitmap_sign_by_month.SignTest2     : 560用户今日是否已签到:0
c.e.r.bitmap_sign_by_month.SignTest2     : 本月签到情况:
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-12: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-11: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-10: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-31: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-30: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-19: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-18: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-17: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-16: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-15: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-14: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-13: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-23: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-01: √
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-22: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-21: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-20: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-09: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-08: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-29: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-07: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-28: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-06: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-27: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-05: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-26: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-04: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-25: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-03: √
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-24: -
c.e.r.bitmap_sign_by_month.SignTest2     : 2022-08-02: -
c.e.r.bitmap_sign_by_month.SignTest2     : 本月一共签到:2天
c.e.r.bitmap_sign_by_month.SignTest2     : 目前连续签到:1天
```



#### 指定时间签到

签到工具类：

```java
package com.ehang.redis.bitmap_sign_by_range;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 一行Java
 * @title: SignByRangeServiceImpl
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/19 12:27
 */
@Slf4j
@Service
public class SignByRangeServiceImpl {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 根据区间的id 以及用户id 拼接key
     *
     * @param rangeId 区间ID 一般是指定活动的ID等
     * @param userId  用户的ID
     * @return
     */
    private String signKey(Integer rangeId, Integer userId) {
        StringBuilder builder = new StringBuilder("RangeId:Sign:");
        builder.append(rangeId).append(":")
                .append(userId);
        return builder.toString();
    }

    /**
     * 获取当前时间与起始时间的间隔天数
     *
     * @param start 起始时间
     * @return
     */
    private int intervalTime(LocalDateTime start) {
        return (int) (LocalDateTime.now().toLocalDate().toEpochDay() - start.toLocalDate().toEpochDay());
    }

    /**
     * 设置标记位
     * 标记是否签到
     *
     * @param key    签到的key
     * @param offset 偏移量 一般是指当前时间离起始时间（活动开始）的天数
     * @param tag    是否签到  true:签到  false:未签到
     * @return
     */
    private Boolean setBit(String key, long offset, boolean tag) {
        return this.stringRedisTemplate.opsForValue().setBit(key, offset, tag);
    }

    /**
     * 统计计数
     *
     * @param key 统计的key
     * @return
     */
    private long bitCount(String key) {
        return stringRedisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount(key.getBytes()));
    }

    /**
     * 获取多字节位域
     *
     * @param key    缓存的key
     * @param limit  获取多少
     * @param offset 偏移量是多少
     * @return
     */
    private List<Long> bitfield(String key, int limit, long offset) {
        return this.stringRedisTemplate
                .opsForValue()
                .bitField(key, BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(limit)).valueAt(offset));
    }

    /**
     * 判断是否签到
     *
     * @param key    缓存的key
     * @param offest 偏移量 指当前时间距离起始时间的天数
     * @return
     */
    private Boolean container(String key, long offest) {
        return this.stringRedisTemplate.opsForValue().getBit(key, offest);
    }

    /**
     * 根据起始时间进行签到
     *
     * @param rangeId
     * @param userId
     * @param start
     * @return
     */
    public Boolean sign(Integer rangeId, Integer userId, LocalDateTime start) {
        int offset = intervalTime(start);
        String key = signKey(rangeId, userId);
        return setBit(key, offset, true);
    }

    /**
     * 根据偏移量签到
     *
     * @param rangeId
     * @param userId
     * @param offset
     * @return
     */
    public Boolean sign(Integer rangeId, Integer userId, long offset) {
        String key = signKey(rangeId, userId);
        return setBit(key, offset, true);
    }

    /**
     * 用户今天是否签到
     *
     * @param userId
     * @return
     */
    public Boolean checkSign(Integer rangeId, Integer userId, LocalDateTime start) {
        long offset = intervalTime(start);
        String key = this.signKey(rangeId, userId);
        return this.container(key, offset);
    }

    /**
     * 统计当前月份一共签到天数
     *
     * @param userId
     */
    public long countSigned(Integer rangeId, Integer userId) {
        String signKey = this.signKey(rangeId, userId);
        return this.bitCount(signKey);
    }

    public Map<String, Boolean> querySigned(Integer rangeId, Integer userId, LocalDateTime start) {
        int days = intervalTime(start);
        Map<String, Boolean> signedInMap = new HashMap<>(days);

        String signKey = this.signKey(rangeId, userId);
        List<Long> bitfield = this.bitfield(signKey, days + 1, 0);
        if (!CollectionUtils.isEmpty(bitfield)) {
            long signFlag = bitfield.get(0) == null ? 0 : bitfield.get(0);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i = days; i >= 0; i--) {
                LocalDateTime localDateTime = start.plusDays(i);
                signedInMap.put(localDateTime.format(fmt), signFlag >> 1 << 1 != signFlag);
                signFlag >>= 1;
            }
        }
        return signedInMap;
    }

    /**
     * 查询用户当月连续签到次数
     *
     * @param userId
     * @return
     */
    public long queryContinuousSignCount(Integer rangeId, Integer userId, LocalDateTime start) {
        int signCount = 0;
        String signKey = this.signKey(rangeId, userId);
        int days = this.intervalTime(start);
        List<Long> bitfield = this.bitfield(signKey, days + 1, 0);

        if (!CollectionUtils.isEmpty(bitfield)) {
            long signFlag = bitfield.get(0) == null ? 0 : bitfield.get(0);
            DateTime dateTime = new DateTime();
            // 连续不为0即为连续签到次数，当天未签到情况下
            for (int i = 0; i < dateTime.getDayOfMonth(); i++) {
                if (signFlag >> 1 << 1 == signFlag) {
                    if (i > 0) break;
                } else {
                    signCount += 1;
                }
                signFlag >>= 1;
            }
        }
        return signCount;
    }
}
```

测试工具类：

```java
package com.ehang.redis.bitmap_sign_by_range;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author 一行Java
 * @title: SignTest
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/18 16:11
 */
@SpringBootTest
@Slf4j
public class SignTest {
    @Autowired
    SignByRangeServiceImpl signByRangeService;


    @Test
    void test() {
        DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_DATE_TIME;
        // 活动开始时间
        LocalDateTime start = LocalDateTime.of(2022, 8, 1, 1, 0, 0);
        Integer rangeId = 1;
        Integer userId = 8899;
        log.info("签到开始时间: {}", start.format(isoDateTime));
        log.info("活动ID: {} 用户ID: {}", rangeId, userId);

        // 手动指定偏移量签到
        signByRangeService.sign(rangeId, userId, 0);

        // 判断是否签到
        Boolean signed = signByRangeService.checkSign(rangeId, userId, start);
        log.info("今日是否签到: {}", signed ? "√" : "-");

        // 签到
        Boolean sign = signByRangeService.sign(rangeId, userId, start);
        log.info("签到操作之前的签到状态：{} （-：表示今日第一次签到，√：表示今天已经签到过了）", sign ? "√" : "-");

        // 签到总数
        long countSigned = signByRangeService.countSigned(rangeId, userId);
        log.info("总共签到: {} 天", countSigned);

        // 连续签到的次数
        long continuousSignCount = signByRangeService.queryContinuousSignCount(rangeId, userId, start);
        log.info("连续签到: {} 天", continuousSignCount);

        // 签到的详情
        Map<String, Boolean> stringBooleanMap = signByRangeService.querySigned(rangeId, userId, start);
        for (Map.Entry<String, Boolean> entry : stringBooleanMap.entrySet()) {
            log.info("签到详情> {} : {}", entry.getKey(), (entry.getValue() ? "√" : "-"));
        }
    }
}
```

输出日志：

```java
签到开始时间: 2022-08-01T01:00:00
活动ID: 1 用户ID: 8899
今日是否签到: √
签到操作之前的签到状态：√ （-：表示今日第一次签到，√：表示今天已经签到过了）
总共签到: 3 天
连续签到: 2 天
签到详情> 2022-08-01 : √
签到详情> 2022-08-04 : √
签到详情> 2022-08-03 : √
签到详情> 2022-08-02 : -
```







## GEO搜附近

很多生活类的APP都具备一个搜索附近的功能，比如美团搜索附近的商家；

![网图](https://files.mdnice.com/user/5408/35ce6d15-ae38-4c13-b695-979358ec6247.png)

如果自己想要根据经纬度来实现一个搜索附近的功能，是非常麻烦的；但是Redis 在3.2的版本新增了Redis GEO，用于存储地址位置信息，并对支持范围搜索；基于GEO就能轻松且快速的开发一个搜索附近的功能；

### GEO API 及Redis-cli 操作：

- **geoadd**：新增位置坐标。

  ```java
  127.0.0.1:6379> GEOADD drinks 116.62445 39.86206 starbucks 117.3514785 38.7501247 yidiandian 116.538542 39.75412 xicha
  (integer) 3
  ```

- **geopos**：获取位置坐标。

  ```java
  127.0.0.1:6379> GEOPOS drinks starbucks
  1) 1) "116.62445157766342163"
     2) "39.86206038535793539"
  127.0.0.1:6379> GEOPOS drinks starbucks yidiandian mxbc
  1) 1) "116.62445157766342163"
     2) "39.86206038535793539"
  2) 1) "117.35148042440414429"
     2) "38.75012383773680114"
  3) (nil)
  ```

- **geodist**：计算两个位置之间的距离。

  单位参数：

  - m ：米，默认单位。
  - km ：千米。
  - mi ：英里。
  - ft ：英尺。

  ```java
  127.0.0.1:6379> GEODIST drinks starbucks yidiandian
  "138602.4133"
  127.0.0.1:6379> GEODIST drinks starbucks xicha
  "14072.1255"
  127.0.0.1:6379> GEODIST drinks starbucks xicha m
  "14072.1255"
  127.0.0.1:6379> GEODIST drinks starbucks xicha km
  "14.0721"
  ```

- **georadius**：根据用户给定的经纬度坐标来获取指定范围内的地理位置集合。

  参数说明

  - m ：米，默认单位。
  - km ：千米。
  - mi ：英里。
  - ft ：英尺。
  - WITHDIST: 在返回位置元素的同时， 将位置元素与中心之间的距离也一并返回。
  - WITHCOORD: 将位置元素的经度和纬度也一并返回。
  - WITHHASH: 以 52 位有符号整数的形式， 返回位置元素经过原始 geohash 编码的有序集合分值。 这个选项主要用于底层应用或者调试， 实际中的作用并不大。
  - COUNT 限定返回的记录数。
  - ASC: 查找结果根据距离从近到远排序。
  - DESC: 查找结果根据从远到近排序。

  ```java
  127.0.0.1:6379> GEORADIUS drinks 116 39 100 km WITHDIST
  1) 1) "xicha"
     2) "95.8085"
  127.0.0.1:6379> GEORADIUS drinks 116 39 100 km WITHDIST WITHCOORD
  1) 1) "xicha"
     2) "95.8085"
     3) 1) "116.53854042291641235"
        2) "39.75411928478748536"
  127.0.0.1:6379> GEORADIUS drinks 116 39 100 km WITHDIST WITHCOORD WITHHASH
  1) 1) "xicha"
     2) "95.8085"
     3) (integer) 4069151800882301
     4) 1) "116.53854042291641235"
        2) "39.75411928478748536"
  
  127.0.0.1:6379> GEORADIUS drinks 116 39 120 km WITHDIST WITHCOORD  COUNT 1
  1) 1) "xicha"
     2) "95.8085"
     3) 1) "116.53854042291641235"
        2) "39.75411928478748536"
  
  127.0.0.1:6379> GEORADIUS drinks 116 39 120 km WITHDIST WITHCOORD  COUNT 1 ASC
  1) 1) "xicha"
     2) "95.8085"
     3) 1) "116.53854042291641235"
        2) "39.75411928478748536"
  
  127.0.0.1:6379> GEORADIUS drinks 116 39 120 km WITHDIST WITHCOORD  COUNT 1 DESC
  1) 1) "starbucks"
     2) "109.8703"
     3) 1) "116.62445157766342163"
        2) "39.86206038535793539"
  ```

- **georadiusbymember**：根据储存在位置集合里面的某个地点获取指定范围内的地理位置集合。

  功能和上面的georadius类似，只是georadius是以经纬度坐标为中心，这个是以某个地点为中心；

- **geohash**：返回一个或多个位置对象的 geohash 值。

  ```java
  127.0.0.1:6379> GEOHASH drinks starbucks xicha
  1) "wx4fvbem6d0"
  2) "wx4f5vhb8b0"
  ```

  

### SpringBoot 操作

通过SpringBoot操作GEO的示例如下

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author 一行Java
 * @title: GEOTest
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/7/28 17:29
 */
@SpringBootTest
@Slf4j
public class GEOTest {
    private final String KEY = "geo:drinks";

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() {
        add("starbucks", new Point(116.62445, 39.86206));
        add("yidiandian", new Point(117.3514785, 38.7501247));
        add("xicha", new Point(116.538542, 39.75412));

        get("starbucks", "yidiandian", "xicha");

        GeoResults nearByXY = getNearByXY(new Point(116, 39), new Distance(120, Metrics.KILOMETERS));
        List<GeoResult> content = nearByXY.getContent();
        for (GeoResult geoResult : content) {
            log.info("{}", geoResult.getContent());
        }

        GeoResults nearByPlace = getNearByPlace("starbucks", new Distance(120, Metrics.KILOMETERS));
        content = nearByPlace.getContent();
        for (GeoResult geoResult : content) {
            log.info("{}", geoResult.getContent());
        }

        getGeoHash("starbucks", "yidiandian", "xicha");

        del("yidiandian", "xicha");
    }

    private void add(String name, Point point) {
        Long add = redisTemplate.opsForGeo().add(KEY, point, name);
        log.info("成功添加名称：{} 的坐标信息信息：{}", name, point);
    }


    private void get(String... names) {
        List<Point> position = redisTemplate.opsForGeo().position(KEY, names);
        log.info("获取名称为：{} 的坐标信息：{}", names, position);
    }

    private void del(String... names) {
        Long remove = redisTemplate.opsForGeo().remove(KEY, names);
        log.info("删除名称为：{} 的坐标信息数量：{}", names, remove);
    }

    /**
     * 根据坐标 获取指定范围的位置
     *
     * @param point
     * @param distance
     * @return
     */
    private GeoResults getNearByXY(Point point, Distance distance) {
        Circle circle = new Circle(point, distance);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.
                newGeoRadiusArgs().
                includeDistance(). // 包含距离
                includeCoordinates(). // 包含坐标
                sortAscending(). // 排序 还可选sortDescending()
                limit(5); // 获取前多少个
        GeoResults geoResults = redisTemplate.opsForGeo().radius(KEY, circle, args);
        log.info("根据坐标获取：{} {} 范围的数据：{}", point, distance, geoResults);
        return geoResults;
    }

    /**
     * 根据一个位置，获取指定范围内的其他位置
     *
     * @param name
     * @param distance
     * @return
     */
    private GeoResults getNearByPlace(String name, Distance distance) {
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.
                newGeoRadiusArgs().
                includeDistance(). // 包含距离
                includeCoordinates(). // 包含坐标
                sortAscending(). // 排序 还可选sortDescending()
                limit(5); // 获取前多少个
        GeoResults geoResults = redisTemplate.opsForGeo()
                .radius(KEY, name, distance, args);
        log.info("根据位置：{} 获取： {} 范围的数据：{}", name, distance, geoResults);
        return geoResults;
    }

    /**
     * 获取GEO HASH
     *
     * @param names
     * @return
     */
    private List<String> getGeoHash(String... names) {
        List<String> hash = redisTemplate.opsForGeo().hash(KEY, names);
        log.info("names：{} 对应的hash：{}", names, hash);
        return hash;
    }
}
```

执行日志：

```java
成功添加名称：starbucks 的坐标信息信息：Point [x=116.624450, y=39.862060]
成功添加名称：yidiandian 的坐标信息信息：Point [x=117.351479, y=38.750125]
成功添加名称：xicha 的坐标信息信息：Point [x=116.538542, y=39.754120]

获取名称为：[starbucks, yidiandian, xicha] 的坐标信息：[Point [x=116.624452, y=39.862060], Point [x=117.351480, y=38.750124], Point [x=116.538540, y=39.754119]]

根据坐标获取：Point [x=116.000000, y=39.000000] 120.0 KILOMETERS 范围的数据：GeoResults: [averageDistance: 102.8394 KILOMETERS, results: GeoResult [content: RedisGeoCommands.GeoLocation(name=xicha, point=Point [x=116.538540, y=39.754119]), distance: 95.8085 KILOMETERS, ],GeoResult [content: RedisGeoCommands.GeoLocation(name=starbucks, point=Point [x=116.624452, y=39.862060]), distance: 109.8703 KILOMETERS, ]]
RedisGeoCommands.GeoLocation(name=xicha, point=Point [x=116.538540, y=39.754119])
RedisGeoCommands.GeoLocation(name=starbucks, point=Point [x=116.624452, y=39.862060])

根据位置：starbucks 获取： 120.0 KILOMETERS 范围的数据：GeoResults: [averageDistance: 7.03605 KILOMETERS, results: GeoResult [content: RedisGeoCommands.GeoLocation(name=starbucks, point=Point [x=116.624452, y=39.862060]), distance: 0.0 KILOMETERS, ],GeoResult [content: RedisGeoCommands.GeoLocation(name=xicha, point=Point [x=116.538540, y=39.754119]), distance: 14.0721 KILOMETERS, ]]
RedisGeoCommands.GeoLocation(name=starbucks, point=Point [x=116.624452, y=39.862060])
RedisGeoCommands.GeoLocation(name=xicha, point=Point [x=116.538540, y=39.754119])

names：[starbucks, yidiandian, xicha] 对应的hash：[wx4fvbem6d0, wwgkqqhxzd0, wx4f5vhb8b0]

删除名称为：[yidiandian, xicha] 的坐标信息数量：2
```



## 简单限流

为了保证项目的安全稳定运行，防止被恶意的用户或者异常的流量打垮整个系统，一般都会加上限流，比如常见的`sential`、`hystrix`，都是实现限流控制；如果项目用到了Redis，也可以利用Redis，来实现一个简单的限流功能；

功能所需命令

- **INCR**：将 key 中储存的数字值增一
- **Expire**：设置key的有效期

### Redis-cli操作

```
127.0.0.1:6379> INCR r:f:user1
(integer) 1
# 第一次 设置一个过期时间
127.0.0.1:6379> EXPIRE r:f:user1 5
(integer) 1
127.0.0.1:6379> INCR r:f:user1
(integer) 2
# 等待5s 再次增加 发现已经重置了
127.0.0.1:6379> INCR r:f:user1
(integer) 1
```

### SpringBoot示例：

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author 一行Java
 * @title: 基于Redis的简单限流
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/8/2 9:43
 */
@SpringBootTest
@Slf4j
public class FreqTest {
    // 单位时间（秒）
    private static final Integer TIME = 5;
    // 允许访问上限次数
    private static final Integer MAX = 100;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        String userName = "user1";

        int tag = 1;

        boolean frequency = frequency(userName);
        log.info("第{}次是否放行：{}", tag, frequency);
        for (int i = 0; i < 100; i++) {
            tag += 1;
            frequency(userName);
        }
        frequency = frequency(userName);
        log.info("第{}次是否放行：{}", tag, frequency);

        Thread.sleep(5000);
        frequency = frequency(userName);
        log.info("模拟等待5s后，第{}次是否放行：{}", tag, frequency);
    }

    /**
     * 校验访问频率
     *
     * @param uniqueId 用于限流的唯一ID 可以是用户ID、或者客户端IP等
     * @return true：放行  false：拦截
     */
    private boolean frequency(String uniqueId) {
        String key = "r:q:" + uniqueId;
        Long increment = redisTemplate.opsForValue().increment(key);
        if (increment == 1) {
            redisTemplate.expire(key, TIME, TimeUnit.SECONDS);
        }

        if (increment <= MAX) {
            return true;
        }

        return false;
    }
}
```

运行日志：

```java
user1 第1次请求是否放行：true
user1 第101次请求是否放行：false
模拟等待5s后，user1 第101次请求是否放行：true
```



## 全局ID

在分布式系统中，很多场景下需要全局的唯一ID，由于Redis是独立于业务服务的其他应用，就可以利用`Incr`的原子性操作来生成全局的唯一递增ID

功能所需命令

- **INCR**：将 key 中储存的数字值增一

### Redis-cli 客户端测试

```java
127.0.0.1:6379> incr g:uid
(integer) 1
127.0.0.1:6379> incr g:uid
(integer) 2
127.0.0.1:6379> incr g:uid
(integer) 3
```





## 简单分布式锁

在分布式系统中，很多操作是需要用到分布式锁，防止并发操作带来一些问题；因为redis是独立于分布式系统外的其他服务，因此就可以利用redis，来实现一个简单的**不完美**分布式锁；

功能所需命令

- **SETNX** key不存在，设置；key存在，不设置

  ```java
  # 加锁
  127.0.0.1:6379> SETNX try_lock 1
  (integer) 1
  # 释放锁
  127.0.0.1:6379> del try_lock
  (integer) 1
  ```

  ![](https://files.mdnice.com/user/5408/e803587c-f42a-420a-98fc-7cb91690d206.png)

- set  key  value [ex seconds] [nx | xx]

  上面的方式，虽然能够加锁，但是不难发现，很容易出现死锁的情况；比如，a用户在加锁之后，突然系统挂了，此时a就永远不会释放他持有的锁了，从而导致死锁；为此，我们可以利用redis的过期时间来防止死锁问题

  ```
  set try_lock 1 ex 5 nx
  ```

  ![](https://files.mdnice.com/user/5408/35b22fa8-9f80-4d1d-a547-5af48fb10a4c.png)



**不完美的锁**

上面的方案，虽然解决了死锁的问题，但是又带来了一个新的问题，执行时间如果长于自动释放的时间（比如自动释放是5秒，但是业务执行耗时了8秒），那么在第5秒的时候，锁就自动释放了，此时其他的线程就能正常拿到锁，简单流程如下：

![](https://files.mdnice.com/user/5408/26835563-2142-49f1-bb70-ccb5ec20e5a0.png)

此时相同颜色部分的时间区间是由多线程同时在执行。而且此问题在此方案下并**没有完美的解决方案，只能做到尽可能的避免**：

- 方式一，*value设置为随机数（如：1234），在程序释放锁的时候，检测一下是不是自己加的锁*；比如，A线程在第8s释放的锁就是线程B加的，此时在释放的时候，就可以检验一下value是不是自己当初设置的值（1234），是的就释放，不是的就不管了；
- 方式二，*只在时间消耗比较小的业务上选用此方案*，尽可能的避免执行时间超过锁的自动释放时间



## 认识的人/好友推荐

在支付宝、抖音、QQ等应用中，都会看到好友推荐；

![](https://files.mdnice.com/user/5408/5788262d-e954-4dff-affe-8342d1b37365.png)

好友推荐往往都是基于你的好友关系网来推荐，将你可能认识的人推荐给你，让你去添加好友，如果随意在系统找个人推荐给你，那你认识的可能性是非常小的，此时就失去了推荐的目的；

比如，A和B是好友，B和C是好友，此时A和C认识的概率是比较大的，就可以在A和C之间的好友推荐；

基于这个逻辑，就可以利用 Redis 的 Set 集合，缓存各个用户的好友列表，然后以差集的方式，来实现好友推荐；

功能所需的命令

- **SADD** key member [member …]：集合中添加元素，缓存好友列表
- **SDIFF** key [key …]：取两个集合间的差集，找出可以推荐的用户

### Redis-cli 客户端测试

```java
# 记录 用户1 的好友列表
127.0.0.1:6379> SADD fl:user1 user2 user3
(integer) 2
# 记录 用户2 的好友列表
127.0.0.1:6379> SADD fl:user2 user1 user4
(integer) 2

# 用户1 可能认识的人 ，把自己（user1）去掉，user4是可能认识的人
127.0.0.1:6379> SDIFF fl:user2 fl:user1
1) "user1"
2) "user4"

# 用户2 可能认识的人 ，把自己（user2）去掉，user3是可能认识的人
127.0.0.1:6379> SDIFF fl:user1 fl:user2
1) "user3"
2) "user2"
```

不过这只是推荐机制中的一种因素，可以借助其他条件，来增强推荐的准确度；





## 发布/订阅

发布/订阅是比较常用的一种模式；在分布式系统中，如果需要实时感知到一些变化，比如：某些配置发生变化需要实时同步，就可以用到发布，订阅功能

常用API

- **PUBLISH** channel message：将消息推送到指定的频道
- **SUBSCRIBE** channel [channel …]：订阅给定的一个或多个频道的信息

### Redis-cli操作

![](https://files.mdnice.com/user/5408/fa34b8d7-6a04-427c-8f9a-837aeedf8aa9.gif)

如上图所示，左侧多个客户端订阅了频道，当右侧客户端往频道发送消息的时候，左侧客户端都能收到对应的消息。





## 消息队列

说到消息队列，常用的就是Kafka、RabbitMQ等等，其实 Redis 利用 List 也能实现一个消息队列；

功能所需的指令

- **RPUSH** key value1 [value2]：在列表中添加一个或多个值；
- **BLPOP** key1 [key2] timeout：移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止；
- **BRPOP** key1 [key2] timeout：移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。

依赖调整：

Spring Boot 从 2.0版本开始，将默认的Redis客户端Jedis替换为Lettuce，在测试这块阻塞的时候，会出现一个超时的异常`io.lettuce.core.RedisCommandTimeoutException: Command timed out after 1 minute(s)`；没有找到一个好的解决方式，所以这里将 Lettuce 换回成 Jedis ，就没有问题了，pom.xml 的配置如下：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <exclusions>
        <exclusion>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
        </exclusion>
        <exclusion>
            <artifactId>lettuce-core</artifactId>
            <groupId>io.lettuce</groupId>
        </exclusion>
    </exclusions>
</dependency>
<!-- jedis客户端 -->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
<!-- spring2.X集成redis所需common-pool2，使用jedis必须依赖它-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```



测试代码：

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author 一行Java
 * @title: QueueTest
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/8/5 14:27
 */
@SpringBootTest
@Slf4j
public class QueueTest {
    private static final String REDIS_LP_QUEUE = "redis:lp:queue";
    private static final String REDIS_RP_QUEUE = "redis:rp:queue";

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 先进后出队列
     */
    @Test
    public void rightMonitor() {
        while (true) {
            Object o = stringRedisTemplate.opsForList().rightPop(REDIS_LP_QUEUE, 0, TimeUnit.SECONDS);
            log.info("先进后出队列 接收到数据：{}", o);
        }
    }

    /**
     * 先进先出队列
     */
    @Test
    public void leftMonitor() {
        while (true) {
            Object o = stringRedisTemplate.opsForList().leftPop(REDIS_RP_QUEUE, 0, TimeUnit.SECONDS);
            log.info("先进先出队列 接收到数据：{}", o);
        }
    }
}
```

- 先进先出测试效果

  ![](https://files.mdnice.com/user/5408/7c02760b-676a-43ae-a7de-4065555c2742.gif)

- 先进后出测试效果

  ![](https://files.mdnice.com/user/5408/66236b7c-d59d-4b49-ba9e-8e23d599fef8.gif)



不过，对消息的可靠性要求比较高的场景，建议还是使用专业的消息队列框架，当值被弹出之后，List 中就已经不存在对应的值了，假如此时程序崩溃，就会出现消息的丢失，无法保证可靠性；虽然说也有策略能够保证消息的可靠性，比如，在弹出的同时，将其保存到另外一个队列（BRPOPLPUSH），成功之后，再从另外的队列中移除，当消息处理失败或者异常，再重新进入队列执行，只是这样做就得不偿失了。





## 数据共享（session共享）

既然Redis能持久化数据，就可以用它来实现模块间的数据共享；SpringBoot Session 利用的这个机制来实现 Session 共享；

- 依赖

  ```xml
  <dependency>
      <groupId>org.springframework.session</groupId>
      <artifactId>spring-session-data-redis</artifactId>
  </dependency>
  ```

- 开启session共享

  ```java
  @Configuration
  @EnableRedisHttpSession
  public class RedisSessionConfig {
  }
  ```

- 测试代码

  ```java
  package com.ehang.redis.controller;
  
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;
  
  import javax.servlet.http.HttpServletRequest;
  import java.util.Enumeration;
  import java.util.HashMap;
  import java.util.Map;
  
  /**
   * @author session 共享
   * @title: RedisSessionController
   * @projectName ehang-spring-boot
   * @description: TODO
   * @date 2022/8/5 15:58
   */
  @RestController
  @RequestMapping("session")
  public class RedisSessionController {
  
      /**
       * 设置session的值
       * @param request
       * @return
       */
      @GetMapping("set")
      public Map set(HttpServletRequest request) {
          String id = request.getSession().getId();
          Map<String, String> vas = new HashMap<>();
  
          String key = "key";
          String value = "value";
          vas.put("id", id);
          vas.put(key, value);
          // 自定义session的值
          request.getSession().setAttribute(key, value);
  
          return vas;
      }
  
      /**
       * 获取session的值
       * @param request
       * @return
       */
      @GetMapping("get")
      public Map get(HttpServletRequest request) {
          Map<String, Object> vas = new HashMap<>();
  
          // 遍历所有的session值
          Enumeration<String> attributeNames = request.getSession().getAttributeNames();
          while (attributeNames.hasMoreElements()) {
              String k = attributeNames.nextElement();
              Object va = request.getSession().getAttribute(k);
              vas.put(k, va);
          }
  
          vas.put("id", request.getSession().getId());
  
          return vas;
      }
  }
  ```

- 测试

  开启两个服务，分别接听8080和8081，8080调用赋值接口，8081调用获取接口，如下图，可以看到，两个服务共享了一份Session数据；

  ![](https://files.mdnice.com/user/5408/fc98e5b3-6ffe-4c49-ba01-4a58bbb71400.png)

- Redis中保存的数据

  ```java
  127.0.0.1:6379> keys spring:*
  1) "spring:session:sessions:expires:6f1d7d53-fe01-4e80-9e6a-5ff54fffa92a"
  2) "spring:session:expirations:1659688680000"
  3) "spring:session:sessions:6f1d7d53-fe01-4e80-9e6a-5ff54fffa92a"
  ```

  

## 商品筛选

商城类的应用，都会有类似于下图的一个商品筛选的功能，来帮用户快速搜索理想的商品；

![](https://files.mdnice.com/user/5408/593981a2-7905-4bb8-9178-0be74f314332.png)

假如现在iphone 100 、华为mate 5000 已发布，在各大商城上线；下面就通过 Redis 的 set 来实现上述的商品筛选功能；

功能所需命令

- **SADD** key member [member …]：添加一个或多个元素
- **SINTER** key [key …]：返回给定所有集合的交集

### Redis-cli 客户端测试

```shell
# 将iphone100 添加到品牌为苹果的集合
127.0.0.1:6379> sadd brand:apple iphone100
(integer) 1

# 将meta5000 添加到品牌为苹果的集合
127.0.0.1:6379> sadd brand:huawei meta5000
(integer) 1

# 将 meta5000 iphone100 添加到支持5T内存的集合
127.0.0.1:6379> sadd ram:5t iphone100 meta5000
(integer) 2

# 将 meta5000 添加到支持10T内存的集合
127.0.0.1:6379> sadd ram:10t meta5000
(integer) 1

# 将 iphone100 添加到操作系统是iOS的集合
127.0.0.1:6379> sadd os:ios iphone100
(integer) 1

# 将 meta5000 添加到操作系统是Android的集合
127.0.0.1:6379> sadd os:android meta5000
(integer) 1

# 将 iphone100 meta5000 添加到屏幕为6.0-6.29的集合中
127.0.0.1:6379> sadd screensize:6.0-6.29 iphone100 meta5000
(integer) 2

# 筛选内存5T、屏幕在6.0-6.29的机型
127.0.0.1:6379> sinter ram:5t screensize:6.0-6.29
1) "meta5000"
2) "iphone100"

# 筛选内存10T、屏幕在6.0-6.29的机型
127.0.0.1:6379> sinter ram:10t screensize:6.0-6.29
1) "meta5000"

# 筛选内存5T、系统为iOS的机型
127.0.0.1:6379> sinter ram:5t screensize:6.0-6.29 os:ios
1) "iphone100"

# 筛选内存5T、屏幕在6.0-6.29、品牌是华为的机型
127.0.0.1:6379> sinter ram:5t screensize:6.0-6.29 brand:huawei
1) "meta5000"
```



## 购物车

**商品缓存**

电商项目中，商品消息，都会做缓存处理，特别是热门商品，访问用户比较多，由于商品的结果比较复杂，店铺信息，产品信息，标题、描述、详情图，封面图；为了方便管理和操作，一般都会采用 Hash 的方式来存储（key为商品ID，field用来保存各项参数，value保存对于的值）

**购物车**

当商品信息做了缓存，购物车需要做的，就是通过Hash记录商品ID，以及需要购买的数量（其中key为用户信息，field为商品ID，value用来记录购买的数量） ；

功能所需命令

- **HSET** key field value : 将哈希表 key 中的字段 field 的值设为 value ;
- **HMSET** key field1 value1 [field2 value2 ] ：同时将多个 field-value (域-值)对设置到哈希表 key 中。
- **HGET** key field：获取存储在哈希表中指定字段的值。
- **HGETALL** key ：获取在哈希表中指定 key 的所有字段和值
- **HINCRBY** key field increment ：为哈希表 key 中的指定字段的整数值加上增量 increment 。
- **HLEN** key：获取哈希表中字段的数量

### Redis-cli 客户端测试

```java
# 购物车添加单个商品
127.0.0.1:6379> HSET sc:u1 c001 1
(integer) 1
# 购物车添加多个商品
127.0.0.1:6379> HMSET sc:u1 c002 1 coo3 2
OK
# 添加商品购买数量
127.0.0.1:6379> HINCRBY sc:u1 c002 1
(integer) 2
# 减少商品的购买数量
127.0.0.1:6379> HINCRBY sc:u1 c003 -1
(integer) 1
# 获取单个的购买数量
127.0.0.1:6379> HGET sc:u1 c002
"2"
# 获取购物车的商品数量
127.0.0.1:6379> HLEN sc:u1
(integer) 3
# 购物车详情
127.0.0.1:6379> HGETALL sc:u1
1) "c001"
2) "1"
3) "c002"
4) "2"
5) "coo3"
6) "2"
```



## 定时取消订单（key过期监听）

电商类的业务，一般都会有订单30分钟不支付，自动取消的功能，此时就需要用到定时任务框架，Quartz、xxl-job、elastic-job 是比较常用的 Java 定时任务；我们也可以通过 Redis 的定时过期、以及过期key的监听，来实现订单的取消功能；

- Redis key 过期提醒配置

  修改 redis 相关事件配置。找到 redis 配置文件 redis.conf，查看 notify-keyspace-events 配置项，如果没有，添加 notify-keyspace-events Ex，如果有值，则追加 Ex，相关参数说明如下：

  - `K`：keyspace 事件，事件以 keyspace@ 为前缀进行发布

  - `E`：keyevent 事件，事件以 keyevent@ 为前缀进行发布

  - `g`：一般性的，非特定类型的命令，比如del，expire，rename等

  - `$`：字符串特定命令

  - `l`：列表特定命令

  - `s`：集合特定命令

  - `h`：哈希特定命令

  - `z`：有序集合特定命令

  - `x`：过期事件，当某个键过期并删除时会产生该事件

  - `e`：驱逐事件，当某个键因 maxmemore 策略而被删除时，产生该事件

  - `A`：g$lshzxe的别名，因此”AKE”意味着所有事件

- 添加RedisKeyExpirationListener的监听

  ```java
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.data.redis.connection.RedisConnectionFactory;
  import org.springframework.data.redis.listener.RedisMessageListenerContainer;
  
  @Configuration
  public class RedisListenerConfig {
  
      @Bean
      RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
  
          RedisMessageListenerContainer container = new RedisMessageListenerContainer();
          container.setConnectionFactory(connectionFactory);
          return container;
      }
  }
  ```

  `KeyExpirationEventMessageListener` 接口监听所有 db 的过期事件 `keyevent@*:expired"`

  ```java
  package com.ehang.redis.config;
  
  
  import lombok.extern.slf4j.Slf4j;
  import org.springframework.data.redis.connection.Message;
  import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
  import org.springframework.data.redis.listener.RedisMessageListenerContainer;
  import org.springframework.stereotype.Component;
  
  /**
   * 监听所有db的过期事件__keyevent@*__:expired"
   *
   * @author 一行Java
   * @title: RedisKeyExpirationListener
   * @projectName ehang-spring-boot
   * @description: TODO
   * @date 2022/8/5 16:36
   */
  @Component
  @Slf4j
  public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
  
      public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
          super(listenerContainer);
      }
  
      /**
       * 针对 redis 数据失效事件，进行数据处理
       *
       * @param message
       * @param pattern
       */
      @Override
      public void onMessage(Message message, byte[] pattern) {
  
          // 获取到失效的 key，进行取消订单业务处理
          // 由于这里是监听了所有的key，如果只处理特定数据的话，需要做特殊处理
          String expiredKey = message.toString();
          log.info("过期的Key：{}", expiredKey);
      }
  }
  ```

- 测试

  为了快速验证效果，这里 将过期时间调整为2秒；

  注意，由于过期之后，Redis中的Key已经不存在了，因此，**一定要将订单号作为key**，不能作为值保存，否则监听到过期Key之后，将拿不到过期的订单号；

  ![](https://files.mdnice.com/user/5408/8f160797-d7e1-4c7d-9aae-cdd5b562a61a.gif)

- 不推荐使用

  基于这一套机制，确实能够实现订单的超时取消，但是还是不太建议使用，这里仅作为一个思路；原因主要有以下几个：

  1. redis 的过期删除策略是采用定时离线扫描，或者访问时懒性检测删除，并没有办法保证时效性，有可能key已经到期了，但Redis并没有扫描到，导致通知的延迟；
  2. 消息发送即忘（fire and forget），并不会保证消息的可达性，如果此时服务不在线或者异常，通知就再也收不到了；



## 物流信息（时间线）

寄快递、网购的时候，查询物流信息，都会给我们展示xxx时候，快递到达什么地方了，这就是一个典型的时间线列表；

![](https://files.mdnice.com/user/5408/ac62d49b-68b3-4425-9bc6-f93bef34416c.png)

数据库的做法，就是每次变更就插入一条带时间的信息记录，然后根据时间和ID（ID是必须的，如果出现两个相同的时间，单纯时间排序，会造成顺序不对），来排序生成时间线；

我们也可以通过 Redis 的 List 来实现时间线功能，由于 List 采用的是双向链表，因此升序，降序的时间线都能正常满足；

- **RPUSH** key value1 [value2]：在列表中添加一个或多个值，（升序时间线）
- **LPUSH** key value1 [value2]：将一个或多个值插入到列表头部（降序时间线）
- **LRANGE** key start stop：获取列表指定范围内的元素

Redis-cli 客户端测试

- 升序

  ```java
  127.0.0.1:6379> RPUSH time:line:asc 20220805170000
  (integer) 1
  127.0.0.1:6379> RPUSH time:line:asc 20220805170001
  (integer) 2
  127.0.0.1:6379> RPUSH time:line:asc 20220805170002
  (integer) 3
  127.0.0.1:6379> LRANGE time:line:asc 0 -1
  1) "20220805170000"
  2) "20220805170001"
  3) "20220805170002"
  ```

- 降序

  ```java
  127.0.0.1:6379> LPUSH time:line:desc 20220805170000
  (integer) 1
  127.0.0.1:6379> LPUSH time:line:desc 20220805170001
  (integer) 2
  127.0.0.1:6379> LPUSH time:line:desc 20220805170002
  (integer) 3
  127.0.0.1:6379> LRANGE time:line:desc 0 -1
  1) "20220805170002"
  2) "20220805170001"
  3) "20220805170000"
  ```



好了，关于Redis 的妙用，就介绍到这里；有了这些个场景的运用，下次再有面试官问你，Redis除了缓存还做过什么，相信聊上个1把小时，应该不成问题了。

> 测试源码：https://github.com/vehang/ehang-spring-boot/tree/main/spring-boot-011-redis  
>
> 大部分用例都在test目录下

![](https://files.mdnice.com/user/5408/9236d204-4552-4cb6-a109-2f6505768c8e.png)

整理不易，期待您的点赞、分享、转发...