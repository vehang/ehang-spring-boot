package com.ehang.responce;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 这里需要指定一下scanBasePackages 扫描的包路径
 * 否则可能导致common包下的一些公共类无法载入
 * <p>
 * 比如不加scanBasePackages，在当前项目扫描的就是com.ehang.responce目录
 * 但是common包下的一些aop相关的，就在com.ehang.common目录下，从而导致没有加载
 */
@SpringBootApplication(scanBasePackages = "com.ehang")
public class SpringBoot005ResponceExceptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot005ResponceExceptionApplication.class, args);
    }

    /**
     * 将Long类型的数据以String的方式返回
     * js数值最大的安全范围为：9007199254740992
     * 如果后端返回的数值超过这个数，就会出现进度丢失的问题；转成String就不会了
     * 比如1508733541883731970L   在js取到的值就为1508733541883732000
     * <p>
     * 解决方式二
     * 在返回对象的字段上面添加 @JsonSerialize(using = ToStringSerializer.class)
     * <p>
     * 解决方式三
     * 在配置文件中添加以下类之项
     * spring:
     * jackson:
     * generator:
     * write_numbers_as_strings: true #序列化的时候，将数值类型全部转换成字符串返回
     *
     * @return
     */
//    @Bean("jackson2ObjectMapperBuilderCustomizer")
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance)
//                .serializerByType(Long.TYPE, ToStringSerializer.instance);
//    }

//    非lambda写法
    @Bean("jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Jackson2ObjectMapperBuilderCustomizer customizer = new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance);
            }
        };
        return customizer;
    }
}
