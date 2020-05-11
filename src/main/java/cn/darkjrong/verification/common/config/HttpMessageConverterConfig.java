package cn.darkjrong.verification.common.config;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.util.CharsetUtil.UTF_8;

/**
 *  自定义消息转换器
 * @author Rong.Jia
 * @date 2019/01/07 25:16:44
 */
@Configuration
@ConditionalOnClass(JSON.class)
public class HttpMessageConverterConfig {

    /**
     *  http消息转换器。
     * @author Rong.Jia
     * @date 2019/01/07 25:16:44
     * @return StringHttpMessageConverter
     */
    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {

        return new StringHttpMessageConverter(CharsetUtil.charset(UTF_8));

    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverters() {

        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        //Long类型转String类型
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        // ToStringSerializer 是这个包 com.alibaba.fastjson.serializer.ToStringSerializer
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);

        fastJsonConfig.setSerializerFeatures(SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect
        );

        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        // 4.返回HttpMessageConverters对象
        return fastConverter;
    }
}
