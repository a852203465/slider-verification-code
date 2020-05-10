#项目基础版本实例及滑块验证码/数字验证码(java jquery)

## 介绍

### 背景
	鉴于每一个新系统都需要初始化的项目(Spring Boot)，
	并实现用户，角色，权限资源，登录，权限管理功能，
	及依赖引入过多与使用依赖的功能少之又少的大问题，因此本系统版本由此而生。

### 目的
	规定项目版本，及Spring Boot 版本号
	实现日后每个系统的基础功能
	实现用户，角色，权限资源功能，及登录功能

### 基本版本项目介绍
    基础框架：SpringBoot 2.2.1.RELEASE
    ORM：Mybatis 1.3.2
    DB：MySQL 5.7.x
    权限管理：Shiro + Redis + JWT

 ### 项目结构简介
```text
    ├─main                                                                                                                      
    │  ├─java                                                                                                      
    │  │  └─cn                                                                                                  
    │  │      └─darkjrong                           个人项目ID
    │  │          └─basis                                 项目名
    │  │              ├─base                              基础功能包
    │  │              ├─core                               工具类包
    │  │              ├─.......                               具体项目包
```

 ### 项目包结构命名规则
        参考基础功能包命名方式

## 工具类介绍

该版本使用Hutool 工具类包，因此关于工具类使用参考 

[中文文档](https://www.hutool.cn/docs/)
[中文文档（备用）](https://www.hutool.club/docs/)

[参考API](https://apidoc.gitee.com/loolly/hutool/)

## 注意

### 登录账号
   - 账号：admin
   - 密码：unionman@123456

### 认证模式
    目前基础版本已实现基础验证，摘要认证，均已加入验证码
    
#### 模式切换
    修改application.yml 中的 auth.mode的值即可
    
#### 摘要认证存在的问题
   - 由于摘要认证需要用到MD5加密而会产生前后端MD5加密不同，因此需要对中文特殊处理。
   - 中文的处理方式需要与前端约定因此在代码中未处理，若需要处理只需要修改” LoginInterceptor”拦截器中对账号的获取处理即可

##### 建议

#####js转换
``` javascript
encodeURIComponent("")
```

##### java 解密
``` java
URLDecoder.decode("", UTF_8)
```

### MyBatis主键策略

#### 策略
   在生成表主键ID时，我们可以考虑“主键自增”或者”UUID”，但他们都有很明显的缺点
 - UUID
        太长，并且有索引碎片，索引太占用空间的问题。
        无序
- 主键自增：
        自增ID容易被被爬虫遍历数据
        分表分库会有ID冲突
- 雪花算法：
        适合在分布式场景下生产唯一ID，
        既可以保证唯一又可以排序

##### 策略模式
    - 主键生成策略，支持分布式。目前支持UUID（UUID-String主键）、ASSIGN_ID(默认雪花算法)、INPUT(用户指定)。
    - 支持注解@TableId指定主键，
    - 注解优先级大于全局配置。
    
    
##### 策略配置
yml配置，必须配置enabled: true，否则默认false不起作用
```yml
keyplugin:
  enabled: true #开启插件
  keyType: 2 #主键策略
```
        
##### 实体示例
```java
public class User {

    @TableId(IdType.ASSIGN_ID)
    private String id;

    private String userName;

    private String password;
}

```

### swagger配置
    由于swagger 扫描配置方式使用的是注解模式，因此需每个Controller 类上加入@RestController注解

### 访问路径
    http://localhost:8080









