package cn.darkjrong.verification.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.darkjrong.verification.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * redis 配置
 * @author Rong.Jia
 * @date 2019/01/14 17:20
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
	
	@Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;
	
	private Duration timeToLive = Duration.ofSeconds(60);

    /**
     * 在没有指定缓存Key的情况下，key生成策略
     * @date 2019/04/22 14:02:22
     * @return KeyGenerator
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * 缓存管理器 使用Lettuce，和jedis有很大不同
     * @date 2019/04/22 14:02:22
     * @return CacheManager 缓存管理器
     */
    @Bean
    @Primary
    @Override
    public CacheManager cacheManager() {

        //关键点，spring cache的注解使用的序列化都从这来，没有这个配置的话使用的jdk自己的序列化，实际上不影响使用，只是打印出来不适合人眼识别
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()

                //key序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))

                //value序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))

                .disableCachingNullValues()

                //缓存过期时间
                .entryTtl(timeToLive);


        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory)
                .cacheDefaults(config)
                .transactionAware();

        return builder.build();
    }


    /**
     * RedisTemplate配置 在单独使用redisTemplate的时候 重新定义序列化方式
     * @date 2019/04/22 14:02:22
     * @return RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        //使用fastjson序列化
        //FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(valueSerializer());
        template.setHashValueSerializer(valueSerializer());

        template.setHashKeySerializer(keySerializer());

        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(keySerializer());

        template.setConnectionFactory(lettuceConnectionFactory);
        template.afterPropertiesSet();

        return template;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {

        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);

        //序列化时允许非常量字段均输出类型
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        return jackson2JsonRedisSerializer;

        //或者使用GenericJackson2JsonRedisSerializer
        //return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * 注入封装RedisTemplate
     * @autor Rong.Jia
     * @date 2019/01/14 17:20
     * @return RedisUtils
     */
    @Bean(name = "redisUtils")
    public RedisUtils redisUtil(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.setRedisTemplate(redisTemplate);
        return redisUtils;
    }

}
