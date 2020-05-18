/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50638
Source Host           : localhost:3306
Source Database       : beam

Target Server Type    : MYSQL
Target Server Version : 50638
File Encoding         : 65001

Date: 2020-02-12 13:00:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for b_article
-- ----------------------------
DROP TABLE IF EXISTS `b_article`;
CREATE TABLE `b_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `short_code` varchar(64) DEFAULT NULL COMMENT '短码',
  `head_img` varchar(255) DEFAULT '' COMMENT '头像',
  `author` varchar(255) DEFAULT '' COMMENT '作者',
  `content` longtext COMMENT '内容',
  `frozen` tinyint(1) DEFAULT '1' COMMENT '是否可用',
  `read_num` int(11) DEFAULT '0',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  `text_type` smallint(4) DEFAULT '0' COMMENT '0：markdown 1：富文本',
  `tag` varchar(64) DEFAULT '' COMMENT '标签',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of b_article
-- ----------------------------
INSERT INTO `b_article` VALUES ('2', 'Spring Boot多环境搭建（命令行指定的方式）', '00QOV9xI', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、原理：SpringApplication类中的run(String... args)一路跟进...需查看源码。最终可看到：SpringBoot可通过命令行设置参数。\n![134981442a3978a393db2f61.png](http://img.hsshy.cn/upload/20190819/4778b19e3c6d4b96acf99adbb6af4742.png)\n### 2、父工程pom文件，不像Maven打包的方式，需要定义一个替换符\n```\n<build>\n        <pluginManagement>\n            <plugins>\n                <plugin>\n                    <groupId>org.apache.maven.plugins</groupId>\n                    <artifactId>maven-compiler-plugin</artifactId>\n                    <version>3.1</version>\n                    <configuration>\n                        <source>${java.version}</source>\n                        <target>${java.version}</target>\n                    </configuration>\n                </plugin>\n                <plugin>\n                    <groupId>org.springframework.boot</groupId>\n                    <artifactId>spring-boot-maven-plugin</artifactId>\n                </plugin>\n            </plugins>\n        </pluginManagement>\n        <plugins>\n            <plugin>\n                <groupId>org.apache.maven.plugins</groupId>\n                <artifactId>maven-resources-plugin</artifactId>\n              \n            </plugin>\n\n        </plugins>\n\n        <resources>\n            <resource>\n                <directory>src/main/resources</directory>\n                <filtering>true</filtering>\n            </resource>\n            <resource>\n                <directory>src/main/java</directory>\n                <includes>\n                    <include>**/*.xml</include>\n                </includes>\n            </resource>\n        </resources>\n    </build>\n\n    <profiles>\n        <profile>\n            <id>local</id>\n            <properties>\n                <spring.active>local</spring.active>\n            </properties>\n            <activation>\n                <activeByDefault>true</activeByDefault>\n            </activation>\n        </profile>\n        <profile>\n            <id>dev</id>\n            <properties>\n                <spring.active>dev</spring.active>\n            </properties>\n        </profile>\n        <profile>\n            <id>prod</id>\n            <properties>\n                <spring.active>prod</spring.active>\n            </properties>\n        </profile>\n    </profiles>\n```\n### 3、子工程\n```\n<build>\n        <plugins>\n            <plugin>\n                <groupId>org.springframework.boot</groupId>\n                <artifactId>spring-boot-maven-plugin</artifactId>\n            </plugin>\n            <plugin>\n                <groupId>org.apache.maven.plugins</groupId>\n                <artifactId>maven-compiler-plugin</artifactId>\n            </plugin>\n        </plugins>\n        <resources>\n            <resource>\n                <directory>src/main/resources</directory>\n            </resource>\n            <resource>\n                <directory>src/main/java</directory>\n                <includes>\n                    <include>**/*.xml</include>\n                </includes>\n            </resource>\n        </resources>\n    </build>\n```\n### 4、application.yml（以---进行环境拆分）\n```\nserver:\n   port: 9090\n---\n\nserver:\n   port: 9090\nspring:\n  profiles: local\n  datasource:\n      url: jdbc:mysql://127.0.0.1:3306/demo?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8\n      username: root\n      password: 123456\n      filters: log4j,wall,mergeStat\n      hikari:\n        readOnly: false\n        connectionTimeout: 60000\n        idleTimeout: 60000\n        validationTimeout: 3000\n        maxLifetime: 60000\n        loginTimeout: 5\n        maximumPoolSize: 60\n        minimumIdle: 10\n  redis:\n    database: 0\n    host: 127.0.0.1\n    port: 6379\n    password:      # 密码（默认为空）\n    timeout: 6000ms  # 连接超时时长（毫秒）\n\n\n---\n\nserver:\n  port: 9090\n\nspring:\n  profiles: produce\n  datasource:\n      url: jdbc:mysql://127.0.0.1:3306/demo?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8\n      username: root\n      password: 123456\n      filters: log4j,wall,mergeStat\n      hikari:\n        readOnly: false\n        connectionTimeout: 60000\n        idleTimeout: 60000\n        validationTimeout: 3000\n        maxLifetime: 60000\n        loginTimeout: 5\n        maximumPoolSize: 60\n        minimumIdle: 10\n  redis:\n    database: 0\n    host: 127.0.0.1\n    port: 6379\n    password:  # 密码（默认为空）\n    timeout: 6000ms  # 连接超时时长（毫秒）\n```\n### 5、以拆分文件的形式也可以\n#### application.yml\n```\nserver:\n   port: 9090\n```\n#### application-local.yml\n```\nserver:\n   port: 9090\nspring:\n  profiles: local\n  datasource:\n      url: jdbc:mysql://127.0.0.1:3306/light_tool?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8\n      username: root\n      password: 123456\n      filters: log4j,wall,mergeStat\n      hikari:\n        readOnly: false\n        connectionTimeout: 60000\n        idleTimeout: 60000\n        validationTimeout: 3000\n        maxLifetime: 60000\n        loginTimeout: 5\n        maximumPoolSize: 60\n        minimumIdle: 10\n  redis:\n    database: 0\n    host: 127.0.0.1\n    port: 6379\n    password:      # 密码（默认为空）\n    timeout: 6000ms  # 连接超时时长（毫秒）\n```\n\n#### application-prod.yml\n```\nserver:\n   port: 9090\nspring:\n  profiles: prod\n  datasource:\n      url: jdbc:mysql://127.0.0.1:3306/light_tool?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8\n      username: root\n      password: 123456\n      filters: log4j,wall,mergeStat\n      hikari:\n        readOnly: false\n        connectionTimeout: 60000\n        idleTimeout: 60000\n        validationTimeout: 3000\n        maxLifetime: 60000\n        loginTimeout: 5\n        maximumPoolSize: 60\n        minimumIdle: 10\n  redis:\n    database: 0\n    host: 127.0.0.1\n    port: 6379\n    password:      # 密码（默认为空）\n    timeout: 6000ms  # 连接超时时长（毫秒）\n```\n### 6、idea运行方式\n![222222222222.png](http://img.hsshy.cn/upload/20190819/9781402821684317986c0e4c7c21d926.png)\n### 7、打包及运行\n```\npackage -Dmaven.test.skip\nnohup java -jar xxx.jar --spring.profiles.active=prod >/dev/null 2>&1 &\n```\n', '1', '112', '150', '0', 'Spring Boot,多环境', '2019-08-12 10:32:37', '2019-12-05 10:08:36');
INSERT INTO `b_article` VALUES ('3', 'WebMvcConfigurationSupport和WebMvcConfigurer的区别', '00QAQ2O3', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、WebMvcConfigurationSupport的出现\nspringboot2.0之后配置拦截器extends 的WebMvcConfigurerAdapter过时，取而代之的是WebMvcConfigurationSupport。WebMvcConfigurerAdapter只是对WebMvcCofigurer的空实现,而WebMvcConfigurationSupport的实现的方法更全面\n### 2、注意点\n继承WebMvcConfigurationSupport会发现Spring Boot的WebMvc自动配置失效(WebMvcAutoConfiguration自动化配置)。\n导致无法视图解析器无法解析并返回到对应的视图。\n### 3、示例\n```\npackage com.hsshy.beam.config;\nimport com.alibaba.fastjson.serializer.ToStringSerializer;\nimport com.alibaba.fastjson.serializer.SerializeConfig;\nimport com.alibaba.fastjson.serializer.SerializerFeature;\nimport com.alibaba.fastjson.serializer.ValueFilter;\nimport com.alibaba.fastjson.support.config.FastJsonConfig;\nimport com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;\nimport org.springframework.context.annotation.Configuration;\nimport org.springframework.http.MediaType;\nimport org.springframework.http.converter.HttpMessageConverter;\nimport org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;\nimport java.math.BigInteger;\nimport java.nio.charset.Charset;\nimport java.util.ArrayList;\nimport java.util.List;\n\n@Configuration\npublic class WebCatMvcConfiguration extends WebMvcConfigurationSupport {\n\n    @Override\n    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {\n        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();\n        converter.setFastJsonConfig(fastjsonConfig());\n        converter.setSupportedMediaTypes(getSupportedMediaType());\n    }\n\n    /**\n     * fastjson的配置\n     */\n    public FastJsonConfig fastjsonConfig() {\n        FastJsonConfig fastJsonConfig = new FastJsonConfig();\n        fastJsonConfig.setSerializerFeatures(\n                SerializerFeature.PrettyFormat,\n                SerializerFeature.WriteMapNullValue,\n                SerializerFeature.WriteEnumUsingToString\n        );\n        fastJsonConfig.setDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n        ValueFilter valueFilter = new ValueFilter() {\n            public Object process(Object o, String s, Object o1) {\n                if (null == o1) {\n                    o1 = \"\";\n                }\n                return o1;\n            }\n        };\n        fastJsonConfig.setCharset(Charset.forName(\"utf-8\"));\n        fastJsonConfig.setSerializeFilters(valueFilter);\n\n        //解决Long转json精度丢失的问题\n        SerializeConfig serializeConfig = SerializeConfig.globalInstance;\n        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);\n        serializeConfig.put(Long.class, ToStringSerializer.instance);\n        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);\n        fastJsonConfig.setSerializeConfig(serializeConfig);\n        return fastJsonConfig;\n    }\n\n    /**\n     * 支持的mediaType类型\n     */\n    public List<MediaType> getSupportedMediaType() {\n        ArrayList<MediaType> mediaTypes = new ArrayList<>();\n        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);\n        return mediaTypes;\n    }\n}\n```', '1', '16', '100', '0', 'Spring Boot,版本升级', '2019-08-12 10:34:25', '2019-08-19 08:39:03');
INSERT INTO `b_article` VALUES ('7', 'Spring Boot多环境搭建（Maven方式实现）', '00WxDZYs', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 实现原理：在打包的时候，装入不同环境的配置文件。\n### 1、父工程pom文件\n###### 注意点：<delimiter>@</delimiter>不可缺少，后面在application.yml中会用到。\n```\n<build>\n		<pluginManagement>\n			<plugins>\n				<plugin>\n					<groupId>org.apache.maven.plugins</groupId>\n					<artifactId>maven-compiler-plugin</artifactId>\n					<version>3.1</version>\n					<configuration>\n						<source>${java.version}</source>\n						<target>${java.version}</target>\n					</configuration>\n				</plugin>\n				<plugin>\n					<groupId>org.springframework.boot</groupId>\n					<artifactId>spring-boot-maven-plugin</artifactId>\n				</plugin>\n			</plugins>\n		</pluginManagement>\n		<plugins>\n			<plugin>\n				<groupId>org.apache.maven.plugins</groupId>\n				<artifactId>maven-resources-plugin</artifactId>\n				<configuration>\n					<delimiters>\n						<delimiter>@</delimiter> <!-- 替换符 -->\n					</delimiters>\n					<useDefaultDelimiters>false</useDefaultDelimiters>\n				</configuration>\n			</plugin>\n\n		</plugins>\n\n		<resources>\n			<resource>\n				<directory>src/main/resources</directory>\n				<filtering>true</filtering>\n			</resource>\n			<resource>\n				<directory>src/main/java</directory>\n				<includes>\n					<include>**/*.xml</include>\n				</includes>\n			</resource>\n		</resources>\n	</build>\n\n	<profiles>\n		<profile>\n			<id>local</id>\n			<properties>\n				<spring.active>local</spring.active>\n			</properties>\n			<activation>\n				<activeByDefault>true</activeByDefault>\n			</activation>\n		</profile>\n		<profile>\n			<id>dev</id>\n			<properties>\n				<spring.active>dev</spring.active>\n			</properties>\n		</profile>\n		<profile>\n			<id>prod</id>\n			<properties>\n				<spring.active>prod</spring.active>\n			</properties>\n		</profile>\n	</profiles>\n```\n### 2、子工程\n###### 注意点：<filtering>true</filtering> 不可缺少，否则运行会报错\n```\n <build>\n        <plugins>\n            <plugin>\n                <groupId>org.springframework.boot</groupId>\n                <artifactId>spring-boot-maven-plugin</artifactId>\n            </plugin>\n            <plugin>\n                <groupId>org.apache.maven.plugins</groupId>\n                <artifactId>maven-compiler-plugin</artifactId>\n            </plugin>\n        </plugins>\n        <resources>\n            <resource>\n                <directory>src/main/resources</directory>\n                <filtering>true</filtering>\n            </resource>\n            <resource>\n                <directory>src/main/java</directory>\n                <includes>\n                    <include>**/*.xml</include>\n                </includes>\n            </resource>\n        </resources>\n    </build>\n```\n### 3、application.yml（以---进行环境拆分）\n###### 注意点：@spring.active@就是在上面定义的分隔符，对应的值在父级工程的pom文件里，默认为local。\n```\nspring:\n  profiles:\n    active: @spring.active@\n---\n\nserver:\n   port: 9090\nspring:\n  profiles: local\n  datasource:\n      url: jdbc:mysql://127.0.0.1:3306/demo?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8\n      username: root\n      password: 123456\n      filters: log4j,wall,mergeStat\n      hikari:\n        readOnly: false\n        connectionTimeout: 60000\n        idleTimeout: 60000\n        validationTimeout: 3000\n        maxLifetime: 60000\n        loginTimeout: 5\n        maximumPoolSize: 60\n        minimumIdle: 10\n  redis:\n    database: 0\n    host: 127.0.0.1\n    port: 6379\n    password:      # 密码（默认为空）\n    timeout: 6000ms  # 连接超时时长（毫秒）\n\n\n---\n\nserver:\n  port: 9090\n\nspring:\n  profiles: produce\n  datasource:\n      url: jdbc:mysql://127.0.0.1:3306/demo?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8\n      username: root\n      password: 123456\n      filters: log4j,wall,mergeStat\n      hikari:\n        readOnly: false\n        connectionTimeout: 60000\n        idleTimeout: 60000\n        validationTimeout: 3000\n        maxLifetime: 60000\n        loginTimeout: 5\n        maximumPoolSize: 60\n        minimumIdle: 10\n  redis:\n    database: 0\n    host: 127.0.0.1\n    port: 6379\n    password:  # 密码（默认为空）\n    timeout: 6000ms  # 连接超时时长（毫秒）\n```\n### 4、以拆分文件的形式也可以\n#### application.yml\n```\nspring:\n  profiles:\n    active: @spring.active@\n```\n#### application-local.yml\n```\nserver:\n   port: 9090\nspring:\n  profiles: local\n  datasource:\n      url: jdbc:mysql://127.0.0.1:3306/light_tool?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8\n      username: root\n      password: 123456\n      filters: log4j,wall,mergeStat\n      hikari:\n        readOnly: false\n        connectionTimeout: 60000\n        idleTimeout: 60000\n        validationTimeout: 3000\n        maxLifetime: 60000\n        loginTimeout: 5\n        maximumPoolSize: 60\n        minimumIdle: 10\n  redis:\n    database: 0\n    host: 127.0.0.1\n    port: 6379\n    password:      # 密码（默认为空）\n    timeout: 6000ms  # 连接超时时长（毫秒）\n```\n#### application-prod.yml\n```\nserver:\n   port: 9090\nspring:\n  profiles: prod\n  datasource:\n      url: jdbc:mysql://127.0.0.1:3306/light_tool?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8\n      username: root\n      password: 123456\n      filters: log4j,wall,mergeStat\n      hikari:\n        readOnly: false\n        connectionTimeout: 60000\n        idleTimeout: 60000\n        validationTimeout: 3000\n        maxLifetime: 60000\n        loginTimeout: 5\n        maximumPoolSize: 60\n        minimumIdle: 10\n  redis:\n    database: 0\n    host: 127.0.0.1\n    port: 6379\n    password:      # 密码（默认为空）\n    timeout: 6000ms  # 连接超时时长（毫秒）\n```\n### 5、Maven打包方式\n###### -P指的是环境 \n```\nmvn package -Dmaven.test.skip=true -P prod -f pom.xml\n```\n', '1', '340', '150', '0', 'Spring Boot,多环境', '2019-08-19 08:37:14', '2019-12-05 10:08:21');
INSERT INTO `b_article` VALUES ('8', 'Cron表达式', '00WpQuPK', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、规则\n```\n1. Seconds （秒） \n\n2. Minutes （分） \n\n3. Hours （时） \n\n4. Day-of-Month （天） \n\n5. Month （月） \n\n6. Day-of-Week （周） \n\n7. Year (年 可选字段) \n\nSeconds (秒) ：可以用数字0－59 表示；\n\nMinutes(分) ：可以用数字0－59 表示；\n\nHours(时) ：可以用数字0-23表示；\n\nDay-of-Month(天) ：可以用数字1-31 中的任一一个值，但要注意一些特别的月份2月份没有只能1-28，有些月份没有31；\n\nMonth(月) ：可以用0-11 或用字符串 “JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV and DEC” 表示；\n\nDay-of-Week(*每周*)*：*可以用数字1-7表示（1 ＝ 星期日）或用字符口串“SUN, MON, TUE, WED, THU, FRI and SAT”表示；\n\n“/”：为特别单位，表示为“每”如“0/10”表示每隔10分钟执行一次,“0”表示为从“0”分开始, “3/20”表示表示每隔20分钟执行一次，“3”表示从第3分钟开始执行；\n\n“?”：表示每月的某一天，或第周的某一天；\n\n“L”：用于每月，或每周，表示为每月的最后一天，或每个月的最后星期几如“6L”表示“每月的最后一个星期五”；\n\n“W”：表示为最近工作日，如“15W”放在每月（day-of-month）字段上表示为“到本月15日最近的工作日”；\n\n“#”：是用来指定“的”每月第n个工作日,例 在每周（day-of-week）这个字段中内容为”6#3” or “FRI#3” 则表示“每月第三个星期五”；\n\n“*” 代表整个时间段。\n```\n### 2、常用表达式\n```\n每隔10秒执行一次：*/10 * * * * ?\n\n每隔10分钟执行一次：0 */10 * * * ?\n\n每天23点执行一次：0 0 23 * * ?\n\n每天凌晨1点执行一次：0 0 1 * * ?\n\n每月1号凌晨1点执行一次：0 0 1 1 * ?\n\n每月最后一天23点执行一次：0 0 23 L * ?\n\n每周星期天凌晨1点实行一次：0 0 1 ? * L\n\n在26分、29分、33分执行一次：0 26,29,33 * * * ?\n\n每天的0点、3点、5点、7点都执行一次：0 0 0,5,7,3 * * ?\n```', '1', '17', '100', '0', 'Schedule,Quartz', '2019-08-19 08:42:27', '2019-08-19 09:28:31');
INSERT INTO `b_article` VALUES ('9', 'DeferredResult之异步请求处理', '00WEzRGg', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、为什么使用DeferredResult\n###### 项目中我们某些接口有耗时操作，例如发送邮件、发送短信等有涉及调用的第三方接口的地方，因为第三方具有不确定性，可能耗时很久才会有数据返回。数据库某些慢查询。通常一些耗时且不必马上有返回结果的我们会采用消息中间件，例如 [RabbitMQ](https://www.jianshu.com/p/3a3cc080c28c)、[Disruptor](https://www.jianshu.com/p/9a2610f393c0)等，但是有一些耗时但是又需要等待结果返回的，我们可以使用DeferredResult进行异步处理。以此提高我们服务器的性能。\n### 2、实战\n###### R为自定义的返回结果集\n###### tomcat并发了大概在300到500，此接口被大量访问时若没进行异步处理，服务器资源一下子就会耗完。\n```\n    @GetMapping(\"/test\")\n    public DeferredResult<R> test(){\n        //设置10秒超时\n        DeferredResult deferredResult = new DeferredResult(10000L);\n        //模拟耗时操作\n        try\n        {\n              Thread.sleep(5000);\n        }\n        catch (InterruptedException e)\n        {\n              e.printStackTrace();\n        }\n\n        deferredResult.setResult(R.ok());\n        return deferredResult;\n    }\n```\n###### R.java\n```\npackage com.gizhi.guns.core.util;\nimport com.gizhi.guns.core.enumeration.RetEnum;\n\nimport java.util.HashMap;\n\n/**\n * @description: 封装返回结果类\n * @author: hs\n * @create: 2018-09-21 22:42:04\n **/\npublic class R<T> extends HashMap<String, Object> {\n	private static final long serialVersionUID = 1L;\n	\n	public R(int code,String msg) {\n		put(\"code\", code);\n		put(\"msg\", msg);\n		put(\"data\", null);\n		put(\"error\",false);\n	}\n\n	public static R fail() {\n\n		return new R(RetEnum.ERROR.getRet(),RetEnum.ERROR.getMsg());\n	}\n	\n	public static R fail(String msg) {\n		return fail(RetEnum.ERROR.getRet(), msg);\n	}\n	\n	public static R fail(int code, String msg) {\n		R r = new R(code,msg);\n		r.put(\"error\",true);\n		r.put(\"data\",null);\n		return r;\n	}\n\n	public static <T> R<T> fail(T data) {\n		R r = new R(RetEnum.ERROR.getRet(),RetEnum.ERROR.getMsg());\n		r.put(\"data\",data);\n		r.put(\"error\",true);\n		return r;\n	}\n\n\n	public static <T> R<T> fail(int code,String msg,T data) {\n		R r = new R(code,msg);\n		r.put(\"data\",data);\n		r.put(\"error\",true);\n		return r;\n	}\n\n\n\n	public static R ok() {\n\n		return new R(RetEnum.SUCCESS.getRet(),RetEnum.SUCCESS.getMsg());\n	}\n\n\n\n	public static R ok(String msg) {\n		R r = new R(RetEnum.SUCCESS.getRet(),msg);\n		r.put(\"data\",msg);\n		r.put(\"error\",false);\n\n		return r;\n	}\n\n	public static R ok(int code, String msg) {\n		R r = new R(code,msg);\n		r.put(\"code\", code);\n		r.put(\"msg\", msg);\n		r.put(\"data\", null);\n		r.put(\"error\",false);\n\n		return r;\n	}\n\n\n	public static <T> R<T> ok(T data) {\n		R r = new R(RetEnum.SUCCESS.getRet(),RetEnum.SUCCESS.getMsg());\n		r.put(\"data\",data);\n		r.put(\"error\",false);\n		return r;\n	}\n\n\n	public static <T> R<T> ok(int code,String msg,T data) {\n		R r = new R(code,msg);\n		r.put(\"data\",data);\n		r.put(\"error\",false);\n		return r;\n	}\n\n\n	@Override\n	public R put(String key, Object value) {\n		super.put(key, value);\n		return this;\n	}\n\n\n}\n\n```\n', '1', '28', '100', '0', 'DeferredResult,异步', '2019-08-20 08:25:05', '2019-08-20 08:25:06');
INSERT INTO `b_article` VALUES ('10', 'Spring Boot整合RabbitMQ（延迟队列）', '00WMN9yk', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、什么是RabbitMQ？\n消息队列，主要是用来实现应用程序的异步和解耦，同时也能起到消息缓冲，消息分发的作用。实现AMQP（高级消息队列协议）。当生产者大量产生数据时，消费者无法快速消费，那么需要一个中间层。保存这个数据。服务器端用Erlang语言编写，支持多种客户端。\n### 2、什么是AMQP？\n即Advanced Message Queuing Protocol，高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。消息中间件主要用于组件之间的解耦，消息的发送者无需知道消息使用者的存在，反之亦然。AMQP的主要特征是面向消息、队列、路由（包括点对点和发布/订阅）、可靠性、安全。\n### 3、相关概念\n 发消息者（生产者）、队列、收消息者（消费者），RabbitMQ 在这个基本概念之上, 多做了一层抽象, 在发消息者和 队列之间, 加入了交换器 (Exchange). 这样发消息者和队列就没有直接联系, 转而变成发消息者把消息给交换器, 交换器根据调度策略再把消息再给队列。\n![134981445ff2571de8631400.png](http://img.hsshy.cn/upload/20190820/c4fb1966c2244e0ca0816f7effd8ea1c.png)\n\n- 左侧 P 代表 生产者，也就是往 RabbitMQ 发消息的程序。\n- 中间即是 RabbitMQ，其中包括了 交换机 和 队列。\n- 右侧 C 代表 消费者，也就是往 RabbitMQ 拿消息的程序。\n### 4、交换机（Exchange）四种类型：Direct、topic、Headers、Fanout\n\n- Direct：direct 类型的行为是\"先匹配, 再投送\". 即在绑定时设定一个 routing_key, 消息的routing_key 匹配时, 才会被交换器投送到绑定的队列中去.\n- Topic：按规则转发消息（最灵活）\n- Headers：设置header attribute参数类型的交换机\n- Fanout：转发消息到所有绑定队列\n### 5、代码示例\n###### pom文件添加依赖\n```\n<dependency>\n        <groupId>org.springframework.boot</groupId>\n        <artifactId>spring-boot-starter-amqp</artifactId>\n</dependency>\n```\n###### 消息生产者（以下各个类型共用此消息发送者）\n```\npackage com.gizhi.beam.modular.amqp.service;\n\nimport org.springframework.amqp.core.AmqpTemplate;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.stereotype.Component;\n\nimport java.util.Date;\n\n@Component\npublic class ProducerService {\n \n    @Autowired\n    private AmqpTemplate rabbitTemplate;\n \n    public void send() {\n        String context = \"hello \" + new Date();\n        System.out.println(\"Sender : \" + context);\n        this.rabbitTemplate.convertAndSend(\"task_queue\", context);\n    }\n\n    public void topicSend1() {\n        String context = \"hi, i am message 1\";\n        System.out.println(\"Sender : \" + context);\n        this.rabbitTemplate.convertAndSend(\"exchange\", \"topic.message\", context);\n    }\n\n    public void topicSend2() {\n        String context = \"hi, i am messages 2\";\n        System.out.println(\"Sender : \" + context);\n        this.rabbitTemplate.convertAndSend(\"exchange\", \"topic.messages\", context);\n    }\n\n    public void fanoutsend() {\n        String context = \"hi, fanout msg \";\n        System.out.println(\"Sender : \" + context);\n        this.rabbitTemplate.convertAndSend(\"fanoutExchange\",\"\", context);\n    }\n \n}\n```\n#### 1、Direct类型\n###### RabbitConfig \n```\npackage com.gizhi.beam.modular.amqp.config;\nimport org.springframework.amqp.core.Queue;\nimport org.springframework.context.annotation.Bean;\nimport org.springframework.context.annotation.Configuration;\n@Configuration\npublic class RabbitConfig {\n \n    @Bean\n    public Queue Queue() {\n        return new Queue(\"task_queue\");\n    }\n \n}\n\n```\n###### 消费者\n```\npackage com.gizhi.beam.modular.amqp.service;\n\nimport org.springframework.amqp.rabbit.annotation.RabbitHandler;\nimport org.springframework.amqp.rabbit.annotation.RabbitListener;\nimport org.springframework.stereotype.Component;\n\n@Component\n@RabbitListener(queues = \"task_queue\")\npublic class CustomerService {\n \n    @RabbitHandler\n    public void process(String hello) {\n        System.out.println(\"Receiver  : \" + hello);\n    }\n \n}\n```\n#### 2、Topic类型\n###### TopicRabbitConfig\n```\npackage com.gizhi.beam.modular.amqp.config;\n\nimport org.springframework.amqp.core.Binding;\nimport org.springframework.amqp.core.BindingBuilder;\nimport org.springframework.amqp.core.Queue;\nimport org.springframework.amqp.core.TopicExchange;\nimport org.springframework.context.annotation.Bean;\nimport org.springframework.context.annotation.Configuration;\n//topic 是RabbitMQ中最灵活的一种方式，可以根据routing_key自由的绑定不同的队列\n@Configuration\npublic class TopicRabbitConfig {\n\n    final static String message = \"topic.message\";\n    final static String messages = \"topic.messages\";\n\n    @Bean\n    public Queue queueMessage() {\n        return new Queue(TopicRabbitConfig.message);\n    }\n\n    @Bean\n    public Queue queueMessages() {\n        return new Queue(TopicRabbitConfig.messages);\n    }\n\n    @Bean\n    TopicExchange exchange() {\n        return new TopicExchange(\"exchange\");\n    }\n\n    @Bean\n    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {\n        return BindingBuilder.bind(queueMessage).to(exchange).with(\"topic.message\");\n    }\n\n    @Bean\n    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {\n        // #：相当于一个或者多个单词，例如一个匹配模式是topic.#，那么，以topic开头的路由键都是可以的\n        // *：相当于一个单词，例如一个匹配模式是topic.*，那么，以topic开头的路由键,后面接一个单词的都可以\n        return BindingBuilder.bind(queueMessages).to(exchange).with(\"topic.#\");\n    }\n}\n```\n###### 消费者\n```\n@Component\npublic class TopicCustomerService {\n\n    @RabbitListener(queues = \"topic.message\")\n    public void message(String msg) {\n        System.out.println(\"message  : \" + msg);\n    }\n    @RabbitListener(queues = \"topic.messages\")\n    public void messages(String msg) {\n        System.out.println(\"messages  : \" + msg);\n    }\n\n \n}\n```\n#### 3、Fanout类型\n###### FanoutRabbitConfig\n```\npackage com.gizhi.beam.modular.amqp.config;\n\nimport org.springframework.amqp.core.Binding;\nimport org.springframework.amqp.core.BindingBuilder;\nimport org.springframework.amqp.core.FanoutExchange;\nimport org.springframework.amqp.core.Queue;\nimport org.springframework.context.annotation.Bean;\nimport org.springframework.context.annotation.Configuration;\n//Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。\n\n@Configuration\npublic class FanoutRabbitConfig {\n    @Bean\n    public Queue AMessage() {\n        return new Queue(\"fanout.A\");\n    }\n \n    @Bean\n    public Queue BMessage() {\n        return new Queue(\"fanout.B\");\n    }\n \n    @Bean\n    public Queue CMessage() {\n        return new Queue(\"fanout.C\");\n    }\n \n    @Bean\n    FanoutExchange fanoutExchange() {\n        return new FanoutExchange(\"fanoutExchange\");\n    }\n \n    @Bean\n    Binding bindingExchangeA(Queue AMessage, FanoutExchange fanoutExchange) {\n        return BindingBuilder.bind(AMessage).to(fanoutExchange);\n    }\n \n    @Bean\n    Binding bindingExchangeB(Queue BMessage, FanoutExchange fanoutExchange) {\n        return BindingBuilder.bind(BMessage).to(fanoutExchange);\n    }\n \n    @Bean\n    Binding bindingExchangeC(Queue CMessage, FanoutExchange fanoutExchange) {\n        return BindingBuilder.bind(CMessage).to(fanoutExchange);\n    }\n \n}\n```\n###### 消费者\n```\npackage com.gizhi.beam.modular.amqp.service;\n\nimport org.springframework.amqp.rabbit.annotation.RabbitListener;\nimport org.springframework.stereotype.Component;\n\n@Component\npublic class FanoutCustomerAService {\n\n    @RabbitListener(queues = \"fanout.A\")\n    public void fanoutA(String msg) {\n        System.out.println(\"A  : \" + msg);\n    }\n\n    @RabbitListener(queues = \"fanout.B\")\n    public void fanoutB(String msg) {\n        System.out.println(\"B  : \" + msg);\n    }\n\n    @RabbitListener(queues = \"fanout.C\")\n    public void fanoutC(String msg) {\n        System.out.println(\"C  : \" + msg);\n    }\n \n}\n```\n### 4、延迟队列\n###### AMQP协议和RabbitMQ队列本身没有直接支持延迟队列功能，但是我们可以通过RabbitMQ的两个特性来曲线实现延迟队列：\n- Time To Live(TTL)\n- Dead Letter Exchanges（DLX）\n###### 原理：\nRabbitMQ可以针对Queue设置x-expires 或者 针对Message设置 x-message-ttl，来控制消息的生存时间，如果超时(两者同时设置以最先到期的时间为准)，则消息变为dead letter(死信)。RabbitMQ的Queue可以配置x-dead-letter-exchange 和x-dead-letter-routing-key（可选）两个参数，如果队列内出现了dead letter，则按照这两个参数重新路由转发到指定的队列。\nx-dead-letter-exchange：出现dead letter之后将dead letter重新发送到指定exchange\nx-dead-letter-routing-key：出现dead letter之后将dead letter重新按照指定的routing-key发送。\n![1349814428f5a3841e112fe3.png](http://img.hsshy.cn/upload/20190820/be00ce20f47b4f4f93b2ed2c4f280fc3.png)\n###### DelayRabbitConfig\n```\npackage com.gizhi.beam.modular.amqp.config;\n\nimport lombok.extern.slf4j.Slf4j;\nimport org.springframework.amqp.core.*;\nimport org.springframework.context.annotation.Bean;\nimport org.springframework.context.annotation.Configuration;\n \nimport java.util.HashMap;\nimport java.util.Map;\n \n@Configuration\n@Slf4j\npublic class DelayRabbitConfig {\n \n \n    /**\n     * 延迟队列 TTL 名称\n     */\n    private static final String ORDER_DELAY_QUEUE = \"user.order.delay.queue\";\n    /**\n     * DLX，dead letter发送到的 exchange\n     * 延时消息就是发送到该交换机的\n     */\n    public static final String ORDER_DELAY_EXCHANGE = \"user.order.delay.exchange\";\n    /**\n     * routing key 名称\n     * 具体消息发送在该 routingKey 的\n     */\n    public static final String ORDER_DELAY_ROUTING_KEY = \"order_delay\";\n \n    public static final String ORDER_QUEUE_NAME = \"user.order.queue\";\n    public static final String ORDER_EXCHANGE_NAME = \"user.order.exchange\";\n    public static final String ORDER_ROUTING_KEY = \"order\";\n \n    /**\n     * 延迟队列配置\n     * <p>\n     * 1、params.put(\"x-message-ttl\", 5 * 1000);\n     * 第一种方式是直接设置 Queue 延迟时间 但如果直接给队列设置过期时间,这种做法不是很灵活,（当然二者是兼容的,默认是时间小的优先）\n     * 2、rabbitTemplate.convertAndSend(book, message -> {\n     * message.getMessageProperties().setExpiration(2 * 1000 + \"\");\n     * return message;\n     * });\n     * 第二种就是每次发送消息动态设置延迟时间,这样我们可以灵活控制\n     **/\n    @Bean\n    public Queue delayOrderQueue() {\n        Map<String, Object> params = new HashMap<>();\n        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，\n        params.put(\"x-dead-letter-exchange\", ORDER_EXCHANGE_NAME);\n        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。\n        params.put(\"x-dead-letter-routing-key\", ORDER_ROUTING_KEY);\n        return new Queue(ORDER_DELAY_QUEUE, true, false, false, params);\n    }\n    /**\n     * 需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。\n     * 这是一个完整的匹配。如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，\n     * 不会转发dog.puppy，也不会转发dog.guard，只会转发dog。\n     * @return DirectExchange\n     */\n    @Bean\n    public DirectExchange orderDelayExchange() {\n        return new DirectExchange(ORDER_DELAY_EXCHANGE);\n    }\n    @Bean\n    public Binding dlxBinding() {\n        return BindingBuilder.bind(delayOrderQueue()).to(orderDelayExchange()).with(ORDER_DELAY_ROUTING_KEY);\n    }\n \n    @Bean\n    public Queue orderQueue() {\n        return new Queue(ORDER_QUEUE_NAME, true);\n    }\n    /**\n     * 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。\n     * 符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”。\n     **/\n    @Bean\n    public TopicExchange orderTopicExchange() {\n        return new TopicExchange(ORDER_EXCHANGE_NAME);\n    }\n \n    @Bean\n    public Binding orderBinding() {\n        // TODO 如果要让延迟队列之间有关联,这里的 routingKey 和 绑定的交换机很关键\n        return BindingBuilder.bind(orderQueue()).to(orderTopicExchange()).with(ORDER_ROUTING_KEY);\n    }\n \n}\n\n```\n###### 生产者DelaySender \n```\npackage com.gizhi.beam.modular.amqp.service;\n\nimport com.gizhi.beam.modular.amqp.config.DelayRabbitConfig;\nimport com.gizhi.beam.modular.amqp.dto.Order;\nimport lombok.extern.slf4j.Slf4j;\nimport org.springframework.amqp.core.AmqpTemplate;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.stereotype.Component;\n \nimport java.util.Date;\n \n@Component\n@Slf4j\npublic class DelaySender {\n \n    @Autowired\n    private AmqpTemplate amqpTemplate;\n \n    public void sendDelay(Order order) {\n        log.info(\"【订单生成时间】\" + new Date().toString() +\"【1分钟后检查订单是否已经支付】\" + order.toString() );\n        this.amqpTemplate.convertAndSend(DelayRabbitConfig.ORDER_DELAY_EXCHANGE, DelayRabbitConfig.ORDER_DELAY_ROUTING_KEY, order, message -> {\n            // 如果配置了 params.put(\"x-message-ttl\", 5 * 1000); 那么这一句也可以省略,具体根据业务需要是声明 Queue 的时候就指定好延迟时间还是在发送自己控制时间\n            message.getMessageProperties().setExpiration(1 * 1000 * 60 + \"\");\n            return message;\n        });\n    }\n}\n\n```\n###### 消费者DelayReceiver\n```\npackage com.gizhi.beam.modular.amqp.service;\n\nimport com.gizhi.beam.modular.amqp.config.DelayRabbitConfig;\nimport com.gizhi.beam.modular.amqp.dto.Order;\nimport com.rabbitmq.client.Channel;\nimport lombok.extern.slf4j.Slf4j;\nimport org.springframework.amqp.core.Message;\nimport org.springframework.amqp.rabbit.annotation.RabbitListener;\nimport org.springframework.stereotype.Component;\n \nimport java.util.Date;\n \n@Component\n@Slf4j\npublic class DelayReceiver {\n \n    @RabbitListener(queues = {DelayRabbitConfig.ORDER_QUEUE_NAME})\n    public void orderDelayQueue(Order order, Message message, Channel channel) {\n        log.info(\"###########################################\");\n        log.info(\"【orderDelayQueue 监听的消息】 - 【消费时间】 - [{}]- 【订单内容】 - [{}]\",  new Date(), order.toString());\n        if(order.getOrderStatus() == 0) {\n            order.setOrderStatus(2);\n            log.info(\"【该订单未支付，取消订单】\" + order.toString());\n        } else if(order.getOrderStatus() == 1) {\n            log.info(\"【该订单已完成支付】\");\n        } else if(order.getOrderStatus() == 2) {\n            log.info(\"【该订单已取消】\");\n        }\n        log.info(\"###########################################\");\n    }\n}\n\n```\n### 5、源码地址：https://gitee.com/hsshy/beam-example\n\n\n### 参考链接\nhttps://blog.csdn.net/ztx114/article/details/78410727\nhttps://blog.csdn.net/woaitingting1985/article/details/79087357\nhttps://blog.csdn.net/lizc_lizc/article/details/80722763\nhttps://www.jianshu.com/p/911d987b5f11\nhttps://www.cnblogs.com/cag2050/p/7724055.html\n\n\n', '1', '16', '100', '0', '延迟队列', '2019-08-20 10:25:15', '2019-08-20 10:25:15');
INSERT INTO `b_article` VALUES ('11', 'MySQL 主从配置', '00PB2LWv', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、环境\n###### 主库  地址：192.168.0.1   数据库用户：root  数据库密码 ******\n###### 从库  地址：192.168.0.2    数据库用户: slave01 数据库密码 ******\n\n### 2、同步数据库数据（使用navicat 工具进行数据传输 ）\n![image.png](http://img.hsshy.cn/upload/20190820/fb400fba69654754a89348d8a72c286e.png)\n\n### 3、修改主库\n```\nvim /etc/my.cnf\n```\n```\n[mysqld]\nlog-bin=mysql-bin   //[必须]启用二进制日志\nserver-id=61      //[必须]服务器唯一ID，默认是1，一般取IP最后一段\n```\n	   \n### 3、修改从库slave:\n```\n[mysqld]\nlog-bin=mysql-bin   //[不是必须]启用二进制日志\nserver-id=1      //[必须]服务器唯一ID，默认是1，一般取IP最后一段	\n```   \n	   \n### 4、重启两台服务器的mysql\n\n### 5、在主库上建立帐户并授权slave:\n###### a、创建用户：\n```\nCREATE USER \'slave01\'@\'%\' IDENTIFIED BY \'123456\'; \n```\n###### b、在主服务器上授权slave:\n```\nGRANT REPLICATION SLAVE ON *.* to \'slave01\'@\'%\' identified by \'123456\';\n```\n	\n	\n### 6、登录主库，查询master的状态\n```\nshow master status;\n```\n```\n+------------------+----------+--------------+------------------+\n| File | Position | Binlog_Do_DB | Binlog_Ignore_DB |\n+------------------+----------+--------------+------------------+\n| mysql-bin.000004 | 308 | | |\n+------------------+----------+--------------+------------------+\n1 row in set (0.00 sec)\n```\n\n### 7、登陆从库，配置Slave：\n```	\nchange master to master_host=\'192.168.0.1\',master_user=\'slave01\',master_password=\'123456\',master_log_file=\'mysql-bin.000006\',master_log_pos=16363218;	\n	//注意不要断开，308数字前后无单引号。\n	//master_host是主服务器的ip\n	//mstert_user是第5步在主服务器上建立的账号用户名\n	//master_passowrd是第5步在主服务器上建立的账号密码\n	//master_log_file是第6步在主服务器上查询出来的表格中的第一个字段\n	//master_log_pos是第6步在主服务器上查询出来的表格中的第二个字段\n```\n### 8、 启动复制功能\n```\nstart slave;\n```\n\n### 9、检查从库复制功能状态：\n```\nshow slave status\\G\n```\n```\n   *************************** 1. row ***************************\n\n              Slave_IO_State: Waiting for master to send event\n              Master_Host: 192.168.2.222  //主服务器地址\n              Master_User: username   //授权帐户名，尽量避免使用root\n              Master_Port: 3306    //数据库端口，部分版本没有此行\n              Connect_Retry: 60\n              Master_Log_File: mysql-bin.000004\n              Read_Master_Log_Pos: 600     //#同步读取二进制日志的位置，大于等于 \n              Exec_Master_Log_Pos\n              Relay_Log_File: ddte-relay-bin.000003\n              Relay_Log_Pos: 251\n              Relay_Master_Log_File: mysql-bin.000004\n              Slave_IO_Running: Yes    //此状态必须YES\n              Slave_SQL_Running: Yes     //此状态必须YES\n                    ......\n```\n### 10、测试主从复制\n\n	\n	\n	\n	\n	\n	\n\n\n\n\n\n\n\n', '1', '12', '100', '0', 'MySQL,主从复制', '2019-08-20 10:29:07', '2019-08-20 10:36:20');
INSERT INTO `b_article` VALUES ('12', 'CentOS7安装MySQL5.7（yum）', '00Pivdjt', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、配置YUM源\n#### 在MySQL官网中下载YUM源rpm安装包：https://dev.mysql.com/downloads/repo/yum/\n```\nwget http://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm\n```\n#### 安装mysql源\n```\nyum localinstall mysql57-community-release-el7-8.noarch.rpm\n```\n#### 检查mysql源是否安装成功\n```\nyum repolist enabled | grep \"mysql.*-community.*\"\n```\n![image.png](https://upload-images.jianshu.io/upload_images/13498144-df07bd235bf5bc35.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)\n### 2、安装MySQL\n ```\nyum install mysql-community-server\n```\n### 3、启动MySQL服务\n```\nsystemctl start mysqld\n```\n### 4、开机启动\n```\nsystemctl enable mysqld\nsystemctl daemon-reload\n```\n### 5、修改root本地登录密码\nmysql安装完成之后，在/var/log/mysqld.log文件中给root生成了一个默认密码。通过下面的方式找到root默认密码，然后登录mysql进行修改：\n```\nset password for \'root\'@\'localhost\'=password(\'password!\'); \n```\n![image.png](https://upload-images.jianshu.io/upload_images/13498144-c1957a4544ef2021.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)\n### 6、允许远程连接\n```\ngrant all on *.* to root@\'%\' identified by \'password\' with grant option; \nflush privileges;\n```\n### 7、配置mysql\n```\n[client]\ndefault-character-set = utf8mb4\n\n[mysql]\ndefault-character-set = utf8mb4\n\n[mysqld]\ncharacter-set-client-handshake = FALSE\ncharacter-set-server=utf8mb4\ncollation-server = utf8mb4_unicode_ci\ninit_connect=\'SET NAMES utf8mb4\'\n\n```\n### 8、重启\n```\nservice mysqld restart #重启\nservice mysqld start #启动\nservice mysqld stop #关闭\n```', '1', '12', '100', '0', 'MySQL', '2019-08-21 07:01:34', '2019-08-21 07:07:38');
INSERT INTO `b_article` VALUES ('13', 'CentOS安装Redis（单机）', '00P8pnLd', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、下载安装redis\n```\nwget http://download.redis.io/releases/redis-4.0.6.tar.gz #获取压缩包\ntar xzf redis-4.0.6.tar.gz\n## 编译\ncd redis-4.0.6/src\nmake && make install\n```\n### 注意点：如果是腾讯云的服务器请先安装c编译器并编译，不然****make && make install****会出如下错误。\n![image.png](http://img.hsshy.cn/upload/20190821/b2ca0faa4ed1429b97dd51b00b52f9f0.png)!\n#### 安装c编译器\n```\nyum install gcc-c++ \n```\n#### 若未安装C编译器，就已经 make && make install 请先进行make distclean\n```\nmake distclean\nmake && make install\n```\n#### 不然接下来可能出现如下错误，“jemalloc/jemalloc.h：没有那个文件或目录“问题，在进行编译（因为上次编译失败，有残留的文件）\n![image.png](https://upload-images.jianshu.io/upload_images/13498144-8c275754d56c1183.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)\n\n### 2、修改配置文件\n```\nvim redis.conf\n#如果要远程访问则注释掉\n#bind 127.0.0.1\n#允许后台运行\ndaemonize yes\n#连接密码\nrequirepass password\n```\n###  3、启动\n```\ncd redis-4.0.6/src\n./redis-server ../redis.conf\n#查看进程\nps -ef|grep redis\n```\n', '1', '11', '100', '0', 'Redis', '2019-08-21 07:21:52', '2019-08-21 07:21:52');
INSERT INTO `b_article` VALUES ('14', 'CentOS7.x安装Nginx（yum）', '00qhyABC', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、添加源\n#### 默认情况Centos7中无Nginx的源，Nginx官网提供了Centos的源地址。因此可以如下执行命令添加源：\n```\nsudo rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm\n```\n### 2、安装Nginx\n#### 通过yum search nginx看看是否已经添加源成功。如果成功则执行下列命令安装Nginx。\n```\nsudo yum install -y nginx\n```\n\n### 3、启动Nginx并设置开机自动运行\n```\nsudo systemctl start nginx.service\nsudo systemctl enable nginx.service\n```\n', '1', '24', '100', '0', 'Nginx', '2019-08-21 07:29:50', '2019-12-03 03:57:03');
INSERT INTO `b_article` VALUES ('15', 'CentOS搭建高可用集群Redis Cluster', '00PX6Qco', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、下载安装Redis\n```\nwget http://download.redis.io/releases/redis-4.0.6.tar.gz #获取压缩包\ntar xzf redis-4.0.6.tar.gz\n## 编译\ncd redis-4.0.6/src\nmake\nmake install\n## 启动\n./redis-server\nps -aux | grep redis #查看redis进程\nkill -9 pid #杀死进程 pid是上一步的进程id\n```\n### 2、搭建集群（伪集群）\n#### 集群道理相同，只是将redis安装在不同的服务器上。每个节点的IP改成自己服务器对应的内网IP。\n#### a、在redis-4.0.6目录下新建文件夹redis-cluster，并在该目录下再新建6个子目录：7000，7001，7002，7003，7004，7005。\n![image.png](http://img.hsshy.cn/upload/20190822/aad5491b86c7411ba8996e28a29f231f.png)\n#### b、在7000下新建redis.conf\n```\ndaemonize yes\n#开启后台运行\n\nport 7000\n#工作端口\n\nbind xxx.xxx.xx.xx\n#绑定机器的内网IP，如果是要内网访问，一定要设置为内网IP，不要用127.0.0.1\n\ndir /opt/data/redis-4.0.6/redis-cluster/7000/\n#指定工作目录，rdb,aof持久化文件将会放在该目录下，不同实例一定要配置不同的工作目\n录\n\ncluster-enabled yes\n#启用集群模式\n\ncluster-config-file nodes-7000.conf\n#生成的集群配置文件名称，集群搭建成功后会自动生成，在工作目录下\n\ncluster-node-timeout 5000\n#节点宕机发现时间，可以理解为主节点宕机后从节点升级为主节点时间\n\nappendonly yes\n#开启AOF模式\n\npidfile /opt/data/redis-4.0.6/redis-cluster/7000/redis_.pid\n#pid file所在目录\n```\n#### c、复制五份到7001，7002，7003，7004，7005下，并修改为对应的端口。可以用以下命令快速替换要修改的地方。\n```\nsed -i \'s/7000/7001/g\' 7001/redis.conf\n```\n#### d、由于创建集群需要用到redis-trib这个命令，它依赖Ruby和RubyGems，因此我们要先安装一下。\n```\nyum install ruby\nyum install rubygems\ngem install redis --version 3.3.3\n```\n#### e、Ruby安装完成之后，开始启动6个节点。为了方便我们使用脚本。脚本存放位置与上面新建的的六个文件夹同级\n```\necho -e \"-----start-----\"\necho -e \"kill redis.\\n\"\nps -ef|grep redis|grep -v grep|cut -c 9-15|xargs kill -9\necho -e \"remove nodes config.\\n\"\nfor((i=7000;i<7006;i++));\ndo\ncd /opt/data/redis-4.0.6/redis-cluster/\"$i\"\nrm -rf appendonly.aof\nrm -rf dump.rdb\nrm -rf redis_.pid\nrm -rf nodes-\"$i\".conf\ndone\necho -e \"start nodes server.\\n\"\nfor((i=7000;i<7006;i++));\ndo\ncd /opt/data/redis-4.0.6/redis-cluster/\"$i\"\n./../../src/redis-server ./redis.conf\ndone\necho -e \"----end----\"\n\n```\n#### 查看redis进程\n![image.png](http://img.hsshy.cn/upload/20190822/8ee57c7adbd7494d983cb6e79e234d2b.png)\n\n#### f、开始创建集群，--replicas 1中的1代表每个master跟随的节点个数。\n```\n./src/redis-trib.rb create --replicas 1 ip:7000 ip:7001 ip:7002 ip:7003 ip:7004 ip:7005\n### 例如\n./src/redis-trib.rb create --replicas 1 10.135.6.107:7000 10.135.6.107:7001 10.135.6.107:7002 10.135.6.107:7003 10.135.6.107:7004 10.135.6.107:7005\n```\n\n![image.png](https://upload-images.jianshu.io/upload_images/13498144-07ef90b726d0e974.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)\n#### 输入yes\n![image.png](http://img.hsshy.cn/upload/20190822/5a9e79be06ed4b2da3afce01c86f6bfa.png)\n\n### 3、如果想让redis-cluster可以远程访问\n#### a、在以上六份redis.conf中加入\n```\n# 设置密码\nmasterauth yourpassword\nrequirepass yourpassword\n```\n#### b、需要修改/usr/local/share/gems/gems/redis-3.3.3/lib/redis目录下的client.rb文件，将password属性设置为redis.conf中的requirepass的值，不同的操作系统client.rb的位置可能不一样，可以使用 find / -name \"client.rb\"全盘查找一下\n![image.png](http://img.hsshy.cn/upload/20190822/3434d835d3d74a1e94274f1461a2b264.png)\n\n### 4、SpringBoot集群配置\n```\n  redis:\n    database: 0\n    timeout: 6000ms  # 连接超时时长（毫秒）\n    cluster:\n      nodes: ip:7000,ip:7001,ip:7002,ip:7003,ip:7004,ip:7005\n      connTimeOut: 1000 #连接server超时时间\n      soTimeOut: 1000 #等待response超时时间\n      maxAttempts: 2 #连接失败重试次数\n      password: testpwd    # 密码（默认为空）\n```\n\n\n\n\n\n', '1', '18', '100', '0', 'Redis,集群,Cluster', '2019-08-21 07:48:29', '2019-08-22 07:27:50');
INSERT INTO `b_article` VALUES ('16', 'CentOS安装Java环境', '00qGckBh', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 方式一（解压版安装）\n#### 1、下载\n#### 下载地址：https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html\n![image.png](https://upload-images.jianshu.io/upload_images/13498144-b2585765799a6248.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)\n#### 注意：勾选上方的同意\n#### 2、上传到Linux\n```\nscp -r jdk-8u191-linux-x64.tar.gz root@xxx.xx.xx.xx:/opt/data/\n```\n#### 3、解压\n```\ntar -zxvf jdk-8u191-linux-x64.tar.gz\n```\n#### 4、配置环境变量\n```\nvim /etc/profile\n```\n##### 在文件最末尾，添加以下内容\n```\nexport JAVA_HOME=/opt/data/jdk1.8.0_191\nexport PATH=$JAVA_HOME/bin:$PATH \nexport CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar \n```\n#### 5、激活配置文件\n```\nsource /etc/profile\njava -version\n```\n### 方式二 通过yum安装open-jdk\n#### 1、查看安装\n```\nyum search java|grep jdk\nyum install java-1.8.0-openjdk\n```\n#### 2、移动默认安装目录\n#### 默认安装目录：/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.141-1.b16.el7_3.x86_64\n```\nmv  /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.141-1.b16.el7_3.x86_64 /opt/data/jdk1.8\n```\n#### 3、配置环境变量\n```\nJAVA_HOME=/opt/data/jdk1.8\nJRE_HOME=$JAVA_HOME/jre\nCLASS_PATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib\nPATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin\nexport JAVA_HOME JRE_HOME CLASS_PATH PATH\n```\n#### 4、激活配置文件\n```\nsource /etc/profile\njava -version\n```\n\n\n\n\n\n', '1', '22', '100', '0', 'Java环境', '2019-08-21 08:02:07', '2019-08-21 08:02:07');
INSERT INTO `b_article` VALUES ('17', 'CentOS7.x防火墙之firewalld', '00WRD2CO', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '###### 1、开启\n```\nservice firewalld start 或者 systemctl start firewalld.service\n```\n###### 2、重启\n```\nservice firewalld restart\n```\n###### 3、关闭\n```\nservice firewalld stop 或者  systemctl stop firewalld.service\n```\n###### 4、端口开放\n```\nfirewall-cmd --zone=public --add-port=8080/tcp --permanent\nfirewall-cmd --zone=public --add-port=3306/tcp --permanent\nfirewall-cmd --zone=public --add-port=80/tcp --permanent\nfirewall-cmd --zone=public --add-port=443/tcp --permanent\nfirewall-cmd --reload\n```\n###### 5、查看firewall服务状态\n```\nsystemctl status firewalld\n```\n###### 6、查看firewall的状态\n```\nfirewall-cmd --state\n```\n###### 7、查看防火墙规则\n```\nfirewall-cmd --list-all \n```\n###### 8、查询端口是否开放\n```\nfirewall-cmd --query-port=8080/tcp\n```\n###### 9、开放80端口\n```\nfirewall-cmd --permanent --add-port=80/tcp\n```\n###### 10、移除端口\n```\nfirewall-cmd --permanent --remove-port=8080/tcp\n```\n###### 11、重启防火墙(修改配置后要重启防火墙)\n```\nfirewall-cmd --reload\n ```\n###### 12、设置firewalld开机启动\n```\nsystemctl enable firewalld.service \n```\n###### 13、禁止firewalld开机启动\n```\nsystemctl disable firewalld.service\n```\n###### 14、参数解释\n```\n1、firwall-cmd：是Linux提供的操作firewall的一个工具；\n2、--permanent：表示设置为持久；\n3、--add-port：标识添加的端口；\n```\n', '1', '6', '100', '0', 'firewalld,防火墙', '2019-08-21 08:22:47', '2019-08-21 08:22:47');
INSERT INTO `b_article` VALUES ('18', 'CentOS6.x防火墙之iptables', '000aTL85', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '###### 1、查看防火墙状态\n```\nservice iptables status  \n```\n###### 2、启动防火墙\n```\nservice iptables start  \n```\n###### 3、停止防火墙\n```\nservice iptables stop  \n```\n###### 4、重启防火墙\n```\nservice iptables restart  \n```\n###### 5、永久关闭防火墙\n```\nchkconfig iptables off  \n```\n###### 6、永久关闭后重启\n```\nchkconfig iptables on　　\n```\n###### 7、开启80端口\n```\nvim /etc/sysconfig/iptables\n### 加入如下代码\n-A INPUT -m state --state NEW -m tcp -p tcp --dport 80 -j ACCEPT\n### 重启防火墙\nservice iptables restart  \n```\n', '1', '5', '100', '0', '防火墙,iptables', '2019-08-21 08:25:08', '2019-08-21 08:25:08');
INSERT INTO `b_article` VALUES ('19', 'beam-parent文档', '000eZ9dw', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '# beam-parent\n\n#### 演示地址（演示账号只提供界面展示，没有按钮权限即看不到按钮）\nhttp://www.hsshy.com/#/login\n演示账户：test 123456\n\n#### 项目介绍\n\n- 基于SpringBoot 2，一款快速开发的脚手架。\n- 有配套的代码生成器可供使用。\n- 基础模块：\n  -  **用户管理**\n  -  **角色管理** \n  -  **菜单管理**\n  -  **部门管理**\n  -  **定时任务**\n  -  **字典管理**\n  -  **登陆日志**\n  -  **业务日志**\n  -  **博客管理**\n- 项目特点：\n  - **持久层：mybatis持久化，使用MyBatis-Plus优化，减少sql开发量；Transtraction注解事务。**\n  - **使用SpringBoot自动装配，配置简便，开箱即用**\n  - **将service、dao、entity接口提为公共模块，接口模块与后台管理系统可共用。不共用，只需将相应的模块放在对应的子工程中**\n  - **接口模块已添加拦截和post请求签名，提高了安全性**\n  - **后端可使用map+wrapper返回方式返回字段的字典值，无需再前端一一对应字典值所对应的中文名称**\n  - **前后端分离**\n  - **异步插入日志**\n  - **实现了用户角色菜单权限动态配置，精确到按钮级别**\n  - **线上日志分类**\n  - **图片存储（七牛云）（注册地址：https://portal.qiniu.com/signup?code=1h8cpibemhb9u）**\n  - **定时任务：Quartz，已搭建成界面化方式，配置即可使用**\n  - **工具类：excel导入导出，汉字转拼音，字符串工具类，数字工具类，发送邮件，redis工具类，MD5加密，HTTP工具类，防注入工具类,i18n 国际化多语言工具等等。**\n#### 技术选型\n- 核心框架：Spring Boot 2.1.5\n- 安全框架：Apache Shiro 1.4\n- 视图框架：Spring MVC 5.0\n- 持久层框架：MyBatis-Plus 3.1.2\n- 定时器：Quartz 2.3\n- 数据库连接池：hikari\n- 后台管理系统前端：Vue2.x\n- 缓存：Redis\n- 图片存储：七牛云（七牛云注册便有10G免费空间，注册地址：https://portal.qiniu.com/signup?code=1h8cpibemhb9u）。\n#### 项目结构\n````\nbeam-parent\n├─beam-common     公共模块\n│ \n├─beam-admin     管理后台\n│        └─resources \n│           ├─application.yml  配置文件\n│           ├─logback-spring.xml  日志配置文件\n│ \n├─beam-rest        API服务 （post请求签名、token)\n│             \n│ \n├─beam-web  公用实体类、dao、service\n│   \n│ \n├─html/beam-manager-system 前端代码\n│ \n├─doc  数据库sql文件\n│ \n│ \n│ \n````\n#### 软件需求\n- IntelliJ IDEA\n- JetBrains WebStorm\n- JDK1.8\n- MySQL5.5+\n- Maven3.0+\n- Redis\n- lombok插件\n\n#### 运行流程\n###### 指定运行环境\n![image.png](http://img.hsshy.cn/upload/20190821/2683498c87f24613886694f813f44c2e.png)\n![image.png](http://img.hsshy.cn/upload/20190821/31332a378a604e13947e6525cb81c43b.png)\n\n#### 部署流程\n\n##### 软件安装（Linux）\n- Java环境：http://www.hsshy.com/#/blog/detail/00qGckBh\n- Redis：http://www.hsshy.com/#/blog/detail/00P8pnLd\n- MySQL：http://www.hsshy.com/#/blog/detail/00Pivdjt\n- Nginx：http://www.hsshy.com/#/blog/detail/00qhyABC\n##### 后端：\n- 服务器选的是阿里云（注册地址：https://chuangke.aliyun.com/invite?userCode=647hkjjy）\n- 打包：```package -Dmaven.test.skip```\n- 运行：```nohup java -jar xxx.jar --spring.profiles.active=prod >/dev/null 2>&1 &```\n- 查看运行日志：```tail -f beam-admin-logs/log_total.log```\n- doc目录下提供运行脚本，脚本与jar包放在同级目录下即可。```chmod 777 deploy.sh```（添加读写执行权限），脚本运行报错（执行dos2unix deploy.sh，window环境下与Linux环境下文本格式有所不同）\n\n##### 前端（这边是用nginx进行部署）：\n- 打包：npm run build\n- 上传：进入dist文件夹，scp -r * root@xx.xx.xx.xx:/etc/nginx/html/beam-manage-system/\n- 给文件夹设置权限。\n- nginx配置请参考doc下的beam.conf文件,可直接传到服务器下的nginx/conf.d/下进行使用，记得删除默认的default.conf文件。\n\n#### 部署可能出现的问题\n- 出现表不存在（定时任务相关的表改成大写或者将数据库改成大小写不敏感）\n- 脚本运行报错（执行dos2unix deploy.sh，window环境下与Linux环境下文本格式有所不同）\n- Nginx 403：给前端文件夹设置可读写的权限。\n- Nginx 404：后台管理系统接口要与前端对应上，详情看doc下beam.conf的配置。\n#### 常见问题\n- 上传图片失败，请修改sys_config中的七牛云配置，改为自己的七牛云配置。（七牛云注册便有10G免费空间，注册地址：https://portal.qiniu.com/signup?code=1h8cpibemhb9u）。\n- 提示账户验证失败，检查是否安装Redis，以及用户名密码是否正确。\n- set、get报红，请安装Lombok插件。详情请参照软件需求。\n\n<br>\n\n### 代码生成器\n#### https://gitee.com/hsshy/beam-generator\n\n#### Spring Boot其他案例：https://gitee.com/hsshy/beam-example\n- 秒杀案例模块（加锁、aop加锁、redis锁、消息队列）\n- 动态数据源案例模块\n- Spring Boot 整合RabbitMQ案例\n- Spring Boot 整合dubbo消费者\n- Spring Boot 整合dubbo服务提供者\n- Spring Boot 整合email发送邮件（异步发送、消息队列发送）\n- Spring Boot 整合Kafka案例\n#### 有需要dubbo的请移至：https://gitee.com/hsshy/beam-dubbo\n\n#### 有问题请关注公众号回复消息\n![image.png](http://img.hsshy.cn/upload/20190821/04ce4ff9ddf64816a4831223654d588b.png)', '1', '126', '1', '0', '', '2019-08-21 09:07:35', '2019-08-22 08:31:47');
INSERT INTO `b_article` VALUES ('20', 'Vue访问过慢问题解决', '000ctPhA', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、组件使用懒加载\n```\ncomponent: resolve => require([\'../components/common/Home.vue\'], resolve)\n```\n### 2、Gzip进行压缩\n##### 压缩前 访问时间将近8秒\n![image.png](http://img.hsshy.cn/upload/20190822/08aa99d0f366496d93828aa30b2f42a6.png)\n##### 开启Nginx压缩，在nginx.conf的http中加入以下代码 (nginx安装教程：http://www.hsshy.com/#/blog/detail/00qhyABC)\n```\ngzip on;#开启或关闭gzip on off　　 \ngzip_static on;#是否开启gzip静态资源\ngzip_min_length  5k; #gzip压缩最小文件大小，超出进行压缩（自行调节）\ngzip_buffers     4 16k;\n#gzip_http_version 1.0;\ngzip_comp_level 3;#压缩级别:1-10，数字越大压缩的越好，时间也越长 cpu损耗越大\ngzip_types       text/plain application/javascript text/css application/xml text/javascript application/x-httpd-php image/jpeg image/gif image/png;#  压缩文件类型\ngzip_vary on;#跟Squid等缓存服务有关，on的话会在Header里增加 \"Vary: Accept-Encoding\"\n```\n##### 重启nginx：```service nginx restart```\n##### 压缩后时间 访问时间只需不到2秒\n![image.png](http://img.hsshy.cn/upload/20190822/d67ab82901fb4a0db645667959c3eca5.png)\n### 3、Vue打包也进行gzip压缩\n##### 在devDependencies加入以下代码，进行npm install \n```\n\"compression-webpack-plugin\": \"~1.1.11\"\n```\n##### 在config/index.js里的build加入以下代码，重新打包上传：npm run build\n```\n    productionGzip: true,\n    productionGzipExtensions: [\'js\', \'css\'],\n``` \n##### 压缩后时间 访问时间还是略有提升\n![image.png](http://img.hsshy.cn/upload/20190822/110eba5bbdf143ef87acee858548bf5e.png)\n', '1', '44', '100', '0', 'Vue,Nginx,gzip', '2019-08-22 03:51:34', '2019-08-22 04:00:59');
INSERT INTO `b_article` VALUES ('21', 'Spring Boot+Hirika 实现动态数据源（AOP）', '0004FUIo', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、实现原理\n###### AbstractRoutingDataSource中，determineTargetDataSource 方法通过数据源的标识获取当前数据源；determineCurrentLookupKey方法则是获取数据源标识，实现动态切换数据源，需要实现determineCurrentLookupKey方法，动态提供数据源标识即可。这边使用AOP识别方法上的注解进行数据源切换。没用注解使用默认数据源。\n\n### 2、实例代码\n###### DataSource 注解\n```\npackage com.hsshy.beam.dynamic.datasource.annotion;\nimport java.lang.annotation.*;\n\n/**\n * 多数据源标识\n */\n@Inherited\n@Retention(RetentionPolicy.RUNTIME)\n@Target({ ElementType.METHOD })\npublic @interface DataSource {\n	String name() default \"\";\n}\n```\n######  DataSourceAspect ：多数据源，切面处理类\n```\npackage com.hsshy.beam.dynamic.datasource.aop;\nimport com.hsshy.beam.dynamic.datasource.annotion.DataSource;\nimport com.hsshy.beam.dynamic.datasource.config.DynamicContextHolder;\nimport org.aspectj.lang.ProceedingJoinPoint;\nimport org.aspectj.lang.annotation.Around;\nimport org.aspectj.lang.annotation.Aspect;\nimport org.aspectj.lang.annotation.Pointcut;\nimport org.aspectj.lang.reflect.MethodSignature;\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\nimport org.springframework.core.Ordered;\nimport org.springframework.core.annotation.Order;\nimport org.springframework.stereotype.Component;\nimport java.lang.reflect.Method;\n\n/**\n * 多数据源，切面处理类\n */\n@Aspect\n@Component\n@Order(Ordered.HIGHEST_PRECEDENCE)\npublic class DataSourceAspect {\n    protected Logger logger = LoggerFactory.getLogger(getClass());\n\n    @Pointcut(\"@annotation(com.hsshy.beam.dynamic.datasource.annotion.DataSource) \" +\n            \"|| @within(com.hsshy.beam.dynamic.datasource.annotion.DataSource)\")\n    public void dataSourcePointCut() {\n\n    }\n\n    @Around(\"dataSourcePointCut()\")\n    public Object around(ProceedingJoinPoint point) throws Throwable {\n        MethodSignature signature = (MethodSignature) point.getSignature();\n        Class targetClass = point.getTarget().getClass();\n        Method method = signature.getMethod();\n        DataSource targetDataSource = (DataSource)targetClass.getAnnotation(DataSource.class);\n        DataSource methodDataSource = method.getAnnotation(DataSource.class);\n        if(targetDataSource != null || methodDataSource != null){\n            String value;\n            if(methodDataSource != null){\n                value = methodDataSource.name();\n            }else {\n                value = targetDataSource.name();\n            }\n            DynamicContextHolder.push(value);\n            logger.debug(\"set datasource is {}\", value);\n        }\n\n        try {\n            return point.proceed();\n        } finally {\n            DynamicContextHolder.poll();\n            logger.debug(\"clean datasource\");\n        }\n    }\n}\n```\n###### DataSourceProperties ：默认数据源属性\n```\npackage com.hsshy.beam.dynamic.datasource.property;\nimport com.zaxxer.hikari.HikariDataSource;\nimport lombok.Data;\n/**\n * <p>数据库数据源配置</p>\n */\n@Data\npublic class DataSourceProperties {\n\n    private String jdbcUrl = \"jdbc:mysql://127.0.0.1:3306/beam?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull\";\n\n    private String username = \"root\";\n\n    private String password = \"root\";\n\n    private String driverClassName = \"com.mysql.cj.jdbc.Driver\";\n\n    private long connectionTimeout = 60000L;\n\n    private long idleTimeout = 58000L;\n\n    private long validationTimeout = 3000L;\n\n    private long maxLifetime = 60000L;\n\n    private int maximumPoolSize = 30;\n\n    private int minimumIdle = 10;\n\n}\n```\n###### DynamicDataSourceProperties ：多个数据源属性读取配置\n```\npackage com.hsshy.beam.dynamic.datasource.property;\nimport org.springframework.boot.context.properties.ConfigurationProperties;\nimport java.util.LinkedHashMap;\nimport java.util.Map;\n@ConfigurationProperties(prefix = \"beam.dynamic\")\npublic class DynamicDataSourceProperties {\n\n    private Map<String, DataSourceProperties> datasource = new LinkedHashMap<>();\n\n    public Map<String, DataSourceProperties> getDatasource() {\n        return datasource;\n    }\n\n    public void setDatasource(Map<String, DataSourceProperties> datasource) {\n        this.datasource = datasource;\n    }\n}\n```\n###### DynamicContextHolder：多数据源上下文\n```\npackage com.hsshy.beam.dynamic.datasource.config;\nimport java.util.ArrayDeque;\nimport java.util.Deque;\n\n/**\n * 多数据源上下文\n */\npublic class DynamicContextHolder {\n    @SuppressWarnings(\"unchecked\")\n    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = new ThreadLocal() {\n        @Override\n        protected Object initialValue() {\n            return new ArrayDeque();\n        }\n    };\n\n    /**\n     * 获得当前线程数据源\n     *\n     * @return 数据源名称\n     */\n    public static String peek() {\n        return CONTEXT_HOLDER.get().peek();\n    }\n\n    /**\n     * 设置当前线程数据源\n     *\n     * @param dataSource 数据源名称\n     */\n    public static void push(String dataSource) {\n        CONTEXT_HOLDER.get().push(dataSource);\n    }\n\n    /**\n     * 清空当前线程数据源\n     */\n    public static void poll() {\n        Deque<String> deque = CONTEXT_HOLDER.get();\n        deque.poll();\n        if (deque.isEmpty()) {\n            CONTEXT_HOLDER.remove();\n        }\n    }\n\n}\n```\n###### DynamicDataSourceFactory ：工具类\n```\npackage com.hsshy.beam.dynamic.datasource.config;\nimport com.hsshy.beam.dynamic.datasource.property.DataSourceProperties;\nimport com.zaxxer.hikari.HikariDataSource;\nimport java.sql.SQLException;\n\n/**\n * DruidDataSource\n */\npublic class DynamicDataSourceFactory {\n\n    public static HikariDataSource buildDruidDataSource(DataSourceProperties properties) {\n        HikariDataSource hikariDataSource = new HikariDataSource();\n        hikariDataSource.setJdbcUrl(properties.getJdbcUrl());\n        hikariDataSource.setUsername(properties.getUsername());\n        hikariDataSource.setPassword(properties.getPassword());\n        hikariDataSource.setDriverClassName(properties.getDriverClassName());\n        hikariDataSource.setConnectionTimeout(properties.getConnectionTimeout());\n        hikariDataSource.setIdleTimeout(properties.getIdleTimeout());\n        hikariDataSource.setValidationTimeout(properties.getValidationTimeout());\n        hikariDataSource.setMaxLifetime(properties.getMaxLifetime());\n        hikariDataSource.setMaximumPoolSize(properties.getMaximumPoolSize());\n        hikariDataSource.setMinimumIdle(properties.getMinimumIdle());\n        hikariDataSource.setReadOnly(false);\n\n        try {\n            hikariDataSource.getConnection();\n        } catch (SQLException e) {\n            e.printStackTrace();\n        }\n        return hikariDataSource;\n    }\n}\n```\n###### DynamicDataSourceConfig ：多数据源配置类 \n```\npackage com.hsshy.beam.dynamic.datasource.config;\nimport com.hsshy.beam.dynamic.datasource.property.DataSourceProperties;\nimport com.hsshy.beam.dynamic.datasource.property.DynamicDataSourceProperties;\nimport com.zaxxer.hikari.HikariDataSource;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.boot.context.properties.ConfigurationProperties;\nimport org.springframework.boot.context.properties.EnableConfigurationProperties;\nimport org.springframework.context.annotation.Bean;\nimport org.springframework.context.annotation.Configuration;\nimport java.util.HashMap;\nimport java.util.Map;\n/**\n * 配置多数据源\n */\n@Configuration\n@EnableConfigurationProperties(DynamicDataSourceProperties.class)\npublic class DynamicDataSourceConfig {\n    @Autowired\n    private DynamicDataSourceProperties properties;\n\n    @Bean\n    @ConfigurationProperties(prefix = \"spring.datasource.hikari\")\n    public DataSourceProperties dataSourceProperties() {\n        return new DataSourceProperties();\n    }\n\n    @Bean\n    public DynamicDataSource dynamicDataSource(DataSourceProperties dataSourceProperties) {\n        DynamicDataSource dynamicDataSource = new DynamicDataSource();\n        dynamicDataSource.setTargetDataSources(getDynamicDataSource());\n        //默认数据源\n        HikariDataSource defaultDataSource = DynamicDataSourceFactory.buildDruidDataSource(dataSourceProperties);\n        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);\n        return dynamicDataSource;\n    }\n\n    private Map<Object, Object> getDynamicDataSource(){\n        Map<String, DataSourceProperties> dataSourcePropertiesMap = properties.getDatasource();\n        Map<Object, Object> targetDataSources = new HashMap<>(dataSourcePropertiesMap.size());\n        dataSourcePropertiesMap.forEach((k, v) -> {\n            HikariDataSource hikariDataSource = DynamicDataSourceFactory.buildDruidDataSource(v);\n            targetDataSources.put(k, hikariDataSource);\n        });\n        return targetDataSources;\n    }\n\n}\n```\n###### DynamicDataSource：实现AbstractRoutingDataSource的方法，使用DataSourceContextHolder统一数据源管理\n```\npackage com.hsshy.beam.common.mutidatasource;\n\nimport org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;\n\n/**\n * 动态数据源\n */\npublic class DynamicDataSource extends AbstractRoutingDataSource {\n\n	@Override\n	protected Object determineCurrentLookupKey() {\n		return DataSourceContextHolder.getDataSourceType();\n	}\n\n}\n```\n### 3、源码地址：https://gitee.com/hsshy/beam-parent\n### 4、关注公众号，回复加群，加入Java互助群\n![image.png](http://img.hsshy.cn/upload/20190821/04ce4ff9ddf64816a4831223654d588b.png)', '1', '3', '100', '0', '', '2019-12-05 09:36:58', '2019-12-05 10:09:56');
INSERT INTO `b_article` VALUES ('22', 'Spring Boot读取配置的几种方式', '00qtgc8J', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、@Value注解读取方式\n###### application.properties或者yml\n```\nbeam:\n  wxapp:\n    swagger-open: true\n    file-upload-path: /upload\n\n```\n###### BeamWxappProperties \n```\npackage com.gizhi.beam.config.properties;\nimport java.io.File;\n\nimport static com.gizhi.beam.core.utils.ToolUtil.getTempPath;\nimport static com.gizhi.beam.core.utils.ToolUtil.isEmpty;\n@Component\npublic class BeamWxappProperties {\n\n    @Value(\"beam.wxapp.fileUploadPath\")\n    private String fileUploadPath;\n\n    @Value(\"beam.wxapp.swaggerOpen\")\n    private Boolean swaggerOpen;\n\n    private Boolean haveCreatePath = false;\n\n\n    public String getFileUploadPath() {\n        //如果没有写文件上传路径,保存到临时目录\n        if (isEmpty(fileUploadPath)) {\n            return getTempPath();\n        } else {\n            //判断有没有结尾符,没有得加上\n            if (!fileUploadPath.endsWith(File.separator)) {\n                fileUploadPath = fileUploadPath + File.separator;\n            }\n            //判断目录存不存在,不存在得加上\n            if (!haveCreatePath) {\n                File file = new File(fileUploadPath);\n                file.mkdirs();\n                haveCreatePath = true;\n            }\n            return fileUploadPath;\n        }\n    }\n\n    public void setFileUploadPath(String fileUploadPath) {\n        this.fileUploadPath = fileUploadPath;\n    }\n}\n\n```\n### 2、@ConfigurationProperties注解读取方式\n```\npackage com.gizhi.beam.config.properties;\n\nimport org.springframework.beans.factory.annotation.Value;\nimport org.springframework.boot.context.properties.ConfigurationProperties;\nimport org.springframework.context.annotation.Configuration;\n\nimport java.io.File;\n\nimport static com.gizhi.beam.core.utils.ToolUtil.getTempPath;\nimport static com.gizhi.beam.core.utils.ToolUtil.isEmpty;\n\n\n@Configuration\n@ConfigurationProperties(prefix = BeamWxappProperties.BEAM_WXAPP_PREFIX)\npublic class BeamWxappProperties {\n\n    public static final String BEAM_WXAPP_PREFIX = \"beam.wxapp\";\n\n\n\n\n    private String fileUploadPath;\n\n    private Boolean swaggerOpen;\n\n\n\n    private Boolean haveCreatePath = false;\n\n\n    public String getFileUploadPath() {\n        //如果没有写文件上传路径,保存到临时目录\n        if (isEmpty(fileUploadPath)) {\n            return getTempPath();\n        } else {\n            //判断有没有结尾符,没有得加上\n            if (!fileUploadPath.endsWith(File.separator)) {\n                fileUploadPath = fileUploadPath + File.separator;\n            }\n            //判断目录存不存在,不存在得加上\n            if (!haveCreatePath) {\n                File file = new File(fileUploadPath);\n                file.mkdirs();\n                haveCreatePath = true;\n            }\n            return fileUploadPath;\n        }\n    }\n\n    public void setFileUploadPath(String fileUploadPath) {\n        this.fileUploadPath = fileUploadPath;\n    }\n}\n\n```\n### 3、读取指定文件\n#### dbconfig.properties\n```\ndb.username=root\ndb.password=test\n```\n#### DbConfig (通过Properties加载文件)\n```\npackage com.gizhi.beam.constant;\n\nimport java.io.IOException;\nimport java.io.InputStream;\nimport java.util.Properties;\n\n/**\n * @description:\n * @author: hs\n * @create: 2019-02-28 10:18:36\n **/\npublic class DbConfig {\n\n    public static String USERNAME;\n\n    public static String PASSWORD;\n\n    static{\n        Properties prop = new Properties();\n        InputStream in = DbConfig.class.getResourceAsStream(\"/dbconfig.properties\");\n        try {\n            prop.load(in);\n            USERNAME = prop.getProperty(\"db.username\").trim();\n            PASSWORD = prop.getProperty(\"db.password\").trim();\n\n        } catch (IOException e) {\n            e.printStackTrace();\n        }\n    }\n\n}\n```\n#### DbConfig1 (@PropertySource+@Value注解读取方式)\n```\npackage com.gizhi.beam.constant;\nimport org.springframework.beans.factory.annotation.Value;\nimport org.springframework.context.annotation.PropertySource;\nimport org.springframework.stereotype.Component;\n@Component\n@PropertySource(value = {\"/db-config.properties\"})\npublic class DbConfig1 {\n\n\n    @Value(\"${db.username}\")\n    private String username;\n\n    @Value(\"${db.password}\")\n\n\n    private String password;\n\n\n    public String getUsername() {\n\n        return username;\n\n    }\n\n\n    public void setUsername(String username) {\n\n        this.username = username;\n\n\n    }\n\n\n    public String getPassword() {\n\n        return password;\n\n    }\n\n\n    public void setPassword(String password) {\n\n        this.password = password;\n\n    }\n\n\n}\n```\n#### DbConfig2 (@PropertySource+@ConfigurationProperties注解读取方式)\n```\npackage com.gizhi.beam.constant;\n\nimport org.springframework.boot.context.properties.ConfigurationProperties;\nimport org.springframework.context.annotation.PropertySource;\nimport org.springframework.stereotype.Component;\n\n@Component\n@ConfigurationProperties(prefix = \"db\")\n@PropertySource(value = {\"/db-config.properties\"})\npublic class DbConfig2 {\n\n\n    private String username;\n\n\n    private String password;\n\n\n    public String getUsername() {\n\n        return username;\n\n    }\n\n\n    public void setUsername(String username) {\n\n\n        this.username = username;\n\n\n    }\n\n\n    public String getPassword() {\n\n\n        return password;\n\n\n    }\n\n\n    public void setPassword(String password) {\n\n\n        this.password = password;\n\n\n    }\n\n\n}\n```\n### 注意\n###### @PropertySource不支持yml文件读取。\n### 4、关注我的公众号，互相学习。\n![image.png](http://img.hsshy.cn/upload/20190821/04ce4ff9ddf64816a4831223654d588b.png)', '1', '3', '100', '0', '', '2019-12-05 09:43:21', '2019-12-05 10:09:45');
INSERT INTO `b_article` VALUES ('23', 'Spring Boot自定义自动化配置模块（原理与实战）', '00qYOAhI', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 1、原理\n###### SpringBoot 自动配置主要通过 @EnableAutoConfiguration、**@Conditional**、 @EnableConfigurationProperties 或者 @ConfigurationProperties 等几个注解来进行自动配置完成的。\n###### @EnableAutoConfiguration 开启自动配置，主要作用就是调用 Spring-Core 包里的 loadFactoryNames()，将 autoconfig 包里的已经写好的自动配置加载进来。\n\n###### @Conditional 条件注解，通过判断类路径下有没有相应配置的 jar 包来确定是否加载和自动配置这个类。\n\n###### @EnableConfigurationProperties 的作用就是，给自动配置提供具体的配置参数，只需要写在 application.properties 中，就可以通过映射写入配置类的 **POJO** 属性中。\n\n###### 从**@SpringBootApplication** =》**@EnableAutoConfiguration** =》**@Import({AutoConfigurationImportSelector.class})** =》**selectImports**\n![134981448f676132bc49007e.png](http://img.hsshy.cn/upload/20191205/b4d8d469cba745e1b12ef0d8b68ddec9.png)\n![1349814459154f4a423a124d.png](http://img.hsshy.cn/upload/20191205/a7b4834975fd4a60aaacef18d8d1dbdb.png)\n![13498144c9dcfc55289151fe.png](http://img.hsshy.cn/upload/20191205/5a27136612a54179a1bb4c0bdc43cb5f.png)\n![134981442b0816b7bc28ffbd.png](http://img.hsshy.cn/upload/20191205/e1a625f5668447c4ad0a872fd9e4440e.png)\n### 2、常用注解详解\n#### @ConditionalOnBean\n###### 仅仅在当前上下文中存在某个对象时，才会实例化一个Bean\n\n#### @ConditionalOnClass\n###### 某个class位于类路径上，才会实例化一个Bean)，该注解的参数对应的类必须存在，否则不解析该注解修饰的配置类\n\n#### @ConditionalOnExpression\n###### 当表达式为true的时候，才会实例化一个Bean\n\n#### @ConditionalOnMissingBean\n###### 仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean，该注解表示，如果存在它修饰的类的bean，则不需要再创建这个bean，可以给该注解传入参数例如\n**@ConditionOnMissingBean(name = \"example\")**，这个表示如果name为“example”的bean存在，这该注解修饰的代码块不执行\n\n###### @ConditionalOnMissingClass\n某个class类路径上不存在的时候，才会实例化一个Bean\n\n#### @ConditionalOnNotWebApplication\n###### 不是web应用时，才会执行\n### 3、实战\n#### 1、自定义一个core模块，往其他使用core模块的模块自动配置Fastjson\n###### DefaultFastjsonConfig\n```\npackage com.gizhi.beam.core.config;\nimport com.alibaba.fastjson.serializer.SerializeConfig;\nimport com.alibaba.fastjson.serializer.SerializerFeature;\nimport com.alibaba.fastjson.serializer.ToStringSerializer;\nimport com.alibaba.fastjson.serializer.ValueFilter;\nimport com.alibaba.fastjson.support.config.FastJsonConfig;\nimport com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;\nimport org.springframework.boot.autoconfigure.condition.ConditionalOnClass;\nimport org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;\nimport org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;\nimport org.springframework.context.annotation.Bean;\nimport org.springframework.context.annotation.Configuration;\nimport org.springframework.http.MediaType;\nimport java.math.BigInteger;\nimport java.nio.charset.Charset;\nimport java.util.ArrayList;\nimport java.util.List;\n\n@Configuration(\"defaultFastjsonConfig\")\n@ConditionalOnClass(com.alibaba.fastjson.JSON.class)\n@ConditionalOnMissingBean(FastJsonHttpMessageConverter.class)\n@ConditionalOnWebApplication\npublic class DefaultFastjsonConfig {\n\n    @Bean\n    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {\n        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();\n        converter.setFastJsonConfig(fastjsonConfig());\n        converter.setSupportedMediaTypes(getSupportedMediaType());\n        return converter;\n    }\n\n    /**\n     * fastjson的配置\n     */\n    public FastJsonConfig fastjsonConfig() {\n        FastJsonConfig fastJsonConfig = new FastJsonConfig();\n        fastJsonConfig.setSerializerFeatures(\n                SerializerFeature.PrettyFormat,\n                SerializerFeature.WriteMapNullValue,\n                SerializerFeature.WriteEnumUsingToString\n        );\n        fastJsonConfig.setDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n        ValueFilter valueFilter = new ValueFilter() {\n            public Object process(Object o, String s, Object o1) {\n                if (null == o1) {\n\n                    o1 = \"\";\n                }\n                return o1;\n            }\n        };\n        fastJsonConfig.setCharset(Charset.forName(\"utf-8\"));\n        fastJsonConfig.setSerializeFilters(valueFilter);\n\n        //解决Long转json精度丢失的问题\n        SerializeConfig serializeConfig = SerializeConfig.globalInstance;\n        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);\n        serializeConfig.put(Long.class, ToStringSerializer.instance);\n        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);\n        fastJsonConfig.setSerializeConfig(serializeConfig);\n        return fastJsonConfig;\n    }\n\n    /**\n     * 支持的mediaType类型\n     */\n    public List<MediaType> getSupportedMediaType() {\n        ArrayList<MediaType> mediaTypes = new ArrayList<>();\n        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);\n        return mediaTypes;\n    }\n\n}\n\n```\n###### 在resources里的META-INF里添加文件spring.factories，并写入以下内容\n```\n# AutoConfiguration\norg.springframework.boot.autoconfigure.EnableAutoConfiguration=\\\ncom.gizhi.beam.core.config.DefaultFastjsonConfig,\\\n```\n###### 这样在别的模块引用core模块时，将会自动装配DefaultFastjsonConfig\n#### 2、在core模块里写入一份通用mybatis-plus配置，在其他模块应用core模块时即可使用此配置\n###### DefaultProperties\n```\npackage com.gizhi.beam.core.config;\nimport org.springframework.context.annotation.Configuration;\nimport org.springframework.context.annotation.PropertySource;\n@Configuration\n@PropertySource(\"classpath:/default-config.properties\")\npublic class DefaultProperties {\n\n}\n\n```\n###### 在resources下新建一份default-config.properties\n```\nmybatis-plus.mapper-locations=classpath*:com/gizhi/beam/**/mapping/*.xml\nmybatis-plus.typeAliasesPackage=com.gizhi.beam.*.entity\nmybatis-plus.global-config.db-config.id-type=id_worker\nmybatis-plus.global-config.db-config.field-strategy=not_empty\nmybatis-plus.global-config.db-config.column-underline=true\nmybatis-plus.global-config.db-config.logic-delete-value=1\nmybatis-plus.global-config.db-config.logic-not-delete-value=0\nmybatis-plus.global-config.db-config.db-type=mysql\nmybatis-plus.global-config.refresh=true\nmybatis-plus.configuration.map-underscore-to-camel-case=true\nmybatis-plus.configuration.cache-enabled=false\nmybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl\n```\n###### 在resources里的META-INF里添加文件spring.factories，并写入以下内容\n```\n# AutoConfiguration\norg.springframework.boot.autoconfigure.EnableAutoConfiguration=\\\ncom.gizhi.beam.core.config.DefaultFastjsonConfig,\\\ncom.gizhi.beam.core.config.DefaultProperties\n```\n###### 这样在别的模块引用core模块时，将会自动装配DefaultProperties ，注入常用配置。\n### 3、关注我的公众号，获取更多实用文章。\n![image.png](http://img.hsshy.cn/upload/20190821/04ce4ff9ddf64816a4831223654d588b.png)', '1', '6', '100', '0', '', '2019-12-05 09:56:29', '2019-12-05 10:08:53');
INSERT INTO `b_article` VALUES ('24', 'Spring Boot之AOP详解', '00qTRfhN', 'http://img.hsshy.cn/head_img.jpg', 'yellow_shy', '### 一、AOP简介（摘抄）\naop 全称 Aspect Oriented Programming ，面向切面，AOP主要实现的目的是针对业务处理过程中的切面进行提取，它所面对的是处理过程中的某个步骤或阶段，以获得逻辑过程中各部分之间低耦合性的隔离效果。其与设计模式完成的任务差不多，是提供另一种角度来思考程序的结构，来弥补面向对象编程的不足。\n### 二、搭建aop，通过自定义注解实现日志插入\n#### 1、依赖\n```\n <dependency>\n            <groupId>org.springframework.boot</groupId>\n            <artifactId>spring-boot-starter-aop</artifactId>\n        </dependency>\n```\n#### 2、自定义注解\n```\n@Target(ElementType.METHOD)\n@Retention(RetentionPolicy.RUNTIME)\n@Documented\npublic @interface SysLog {\n\n	String value() default \"\";\n}\n```\n#### 3、面向切面实现类\n```\n@Aspect\n@Component\npublic class SysLogAspect {\n	@Autowired\n	private SysLogService sysLogService;\n	\n	@Pointcut(\"@annotation(io.renren.common.annotation.SysLog)\")\n	public void logPointCut() { \n		\n	}\n\n	@Around(\"logPointCut()\")\n	public Object around(ProceedingJoinPoint point) throws Throwable {\n		long beginTime = System.currentTimeMillis();\n		//执行方法\n		Object result = point.proceed();\n		//执行时长(毫秒)\n		long time = System.currentTimeMillis() - beginTime;\n\n		//保存日志\n		saveSysLog(point, time);\n\n		return result;\n	}\n\n	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {\n		MethodSignature signature = (MethodSignature) joinPoint.getSignature();\n		Method method = signature.getMethod();\n\n		SysLogEntity sysLog = new SysLogEntity();\n		SysLog syslog = method.getAnnotation(SysLog.class);\n		if(syslog != null){\n			//注解上的描述\n			sysLog.setOperation(syslog.value());\n		}\n\n		//请求的方法名\n		String className = joinPoint.getTarget().getClass().getName();\n		String methodName = signature.getName();\n		sysLog.setMethod(className + \".\" + methodName + \"()\");\n\n		//请求的参数\n		Object[] args = joinPoint.getArgs();\n		try{\n			String params = new Gson().toJson(args[0]);\n			sysLog.setParams(params);\n		}catch (Exception e){\n\n		}\n\n		//获取request\n		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();\n		//设置IP地址\n		sysLog.setIp(IPUtils.getIpAddr(request));\n\n		//用户名\n		String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();\n		sysLog.setUsername(username);\n\n		sysLog.setTime(time);\n		sysLog.setCreateDate(new Date());\n		//保存系统日志\n		sysLogService.insert(sysLog);\n	}\n}\n```\n### 三、Aop 常用术语\n#### 切面（Aspect）：\n一个关注点的模块化，这个关注点可能会横切多个对象。事务管理是J2EE应用中一个关于横切关注点的很好的例子。在Spring AOP中，切面可以使用基于模式或者基于@Aspect注解的方式来实现。\n\n#### 连接点（Joinpoint）：\n在程序执行过程中某个特定的点，比如某方法调用的时候或者处理异常的时候。在Spring AOP中，一个连接点总是表示一个方法的执行。\n\n#### 通知（Advice）：\n在切面的某个特定的连接点上执行的动作。其中包括了“around”、“before”和“after”等不同类型的通知（通知的类型将在后面部分进行讨论）。许多AOP框架（包括Spring）都是以拦截器做通知模型，并维护一个以连接点为中心的拦截器链。\n\n#### 切入点（Pointcut）：\n匹配连接点的断言。通知和一个切入点表达式关联，并在满足这个切入点的连接点上运行（例如，当执行某个特定名称的方法时）。切入点表达式如何和连接点匹配是AOP的核心：Spring缺省使用AspectJ切入点语法。\n\n#### 引入（Introduction）：\n用来给一个类型声明额外的方法或属性（也被称为连接类型声明（inter-type declaration））。Spring允许引入新的接口（以及一个对应的实现）到任何被代理的对象。例如，你可以使用引入来使一个bean实现IsModified接口，以便简化缓存机制。\n\n#### 目标对象（Target Object）：\n被一个或者多个切面所通知的对象。也被称做被通知（advised）对象。既然Spring AOP是通过运行时代理实现的，这个对象永远是一个被代理（proxied）对象。\n\n#### AOP代理（AOP Proxy）：\nAOP框架创建的对象，用来实现切面契约（例如通知方法执行等等）。在Spring中，AOP代理可以是JDK动态代理或者CGLIB代理。\n\n#### 织入（Weaving）：\n把切面连接到其它的应用程序类型或者对象上，并创建一个被通知的对象。这些可以在编译时（例如使用AspectJ编译器），类加载时和运行时完成。Spring和其他纯Java AOP框架一样，在运行时完成织入。\n### 四、常用注解\n#### @Aspect\n标注此类为注解实现类\n#### @Order(1)\norder越小越是最先执行，但更重要的是最先执行的最后结束。order默认值是2147483647\n#### \n#### @Pointcut\n```\n    /*\n     * 定义一个切入点 （匹配注解方法）\n     */\n    @Pointcut(\"@annotation(com.hsshy.beam.annotation.SysLog)\")\n    public void logPointCut() {\n    }\n \n \n    // 用@Pointcut来注解一个切入方法\n    @Pointcut(\"execution(* com.hsshy.beam.controller.*.**(..))\")\n    public void excudeController() {\n    }\n```\n#### @Before\n前置通知：在某连接点之前执行的通知，但这个通知不能阻止连接点之前的执行流程\n#### @AfterReturning \n后置通知：在某连接点正常完成后执行的通知，通常在一个匹配的方法返回的时候执行。\n#### @AfterThrowing\n异常通知：在方法抛出异常退出时执行的通知。　　　　　　　\n#### @After \n最终通知。当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）。\n#### @Around\n       环绕通知：包围一个连接点的通知，如方法调用。这是最强大的一种通知类型。环绕通知可以在方法调用前后完成自定义的行为。它也会选择是否继续执行连接点或直接返回它自己的返回值或抛出异常来结束执行。\n       环绕通知最麻烦，也最强大，其是一个对方法的环绕，具体方法会通过代理传递到切面中去，切面中可选择执行方法与否，执行方法几次等。\n       环绕通知使用一个代理ProceedingJoinPoint类型的对象来管理目标对象，所以此通知的第一个参数必须是ProceedingJoinPoint类型，在通知体内，调用ProceedingJoinPoint的proceed()方法会导致后台的连接点方法执行。proceed 方法也可能会被调用并且传入一个Object[]对象-该数组中的值将被作为方法执行时的参数。\n\n', '1', '1', '100', '0', 'AOP,动态代理', '2020-01-09 14:21:30', '2020-01-09 14:21:30');

-- ----------------------------
-- Table structure for b_article_category
-- ----------------------------
DROP TABLE IF EXISTS `b_article_category`;
CREATE TABLE `b_article_category` (
  `id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT '分类名称',
  `frozen` tinyint(1) DEFAULT NULL COMMENT '是否可用',
  `sort` int(11) DEFAULT '100' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of b_article_category
-- ----------------------------
INSERT INTO `b_article_category` VALUES ('1161478346828165121', 'Spring Boot', '1', '100', '2019-08-14 03:23:12', '2019-08-19 06:01:50');
INSERT INTO `b_article_category` VALUES ('1162258659862941698', 'Redis', '1', '100', '2019-08-16 07:03:53', '2019-08-16 07:03:53');
INSERT INTO `b_article_category` VALUES ('1163004671050608641', 'Java', '1', '100', '2019-08-18 08:28:16', '2019-08-18 08:28:16');
INSERT INTO `b_article_category` VALUES ('1163004722057539586', 'MySQL', '1', '100', '2019-08-18 08:28:28', '2019-08-18 08:28:28');
INSERT INTO `b_article_category` VALUES ('1163757628241784834', 'RabbitMQ', '1', '100', '2019-08-20 10:20:15', '2019-08-20 10:20:15');
INSERT INTO `b_article_category` VALUES ('1164070585143037953', 'CentOS', '1', '100', '2019-08-21 07:03:50', '2019-08-21 07:03:50');
INSERT INTO `b_article_category` VALUES ('1164075393157599234', 'Nginx', '1', '100', '2019-08-21 07:22:56', '2019-08-21 07:22:56');
INSERT INTO `b_article_category` VALUES ('1164378872417841154', 'Vue', '1', '100', '2019-08-22 03:28:51', '2019-08-22 03:28:51');

-- ----------------------------
-- Table structure for b_article_category_ref
-- ----------------------------
DROP TABLE IF EXISTS `b_article_category_ref`;
CREATE TABLE `b_article_category_ref` (
  `article_id` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of b_article_category_ref
-- ----------------------------
INSERT INTO `b_article_category_ref` VALUES ('3', '1161478346828165121');
INSERT INTO `b_article_category_ref` VALUES ('8', '1163004671050608641');
INSERT INTO `b_article_category_ref` VALUES ('8', '1161478346828165121');
INSERT INTO `b_article_category_ref` VALUES ('9', '1163004671050608641');
INSERT INTO `b_article_category_ref` VALUES ('10', '1161478346828165121');
INSERT INTO `b_article_category_ref` VALUES ('10', '1163757628241784834');
INSERT INTO `b_article_category_ref` VALUES ('11', '1163004722057539586');
INSERT INTO `b_article_category_ref` VALUES ('12', '1163004722057539586');
INSERT INTO `b_article_category_ref` VALUES ('13', '1162258659862941698');
INSERT INTO `b_article_category_ref` VALUES ('15', '1162258659862941698');
INSERT INTO `b_article_category_ref` VALUES ('16', '1163004671050608641');
INSERT INTO `b_article_category_ref` VALUES ('17', '1164070585143037953');
INSERT INTO `b_article_category_ref` VALUES ('18', '1164070585143037953');
INSERT INTO `b_article_category_ref` VALUES ('20', '1164378872417841154');
INSERT INTO `b_article_category_ref` VALUES ('14', '1164075393157599234');
INSERT INTO `b_article_category_ref` VALUES ('7', '1161478346828165121');
INSERT INTO `b_article_category_ref` VALUES ('2', '1161478346828165121');
INSERT INTO `b_article_category_ref` VALUES ('23', '1161478346828165121');
INSERT INTO `b_article_category_ref` VALUES ('21', '1161478346828165121');
INSERT INTO `b_article_category_ref` VALUES ('24', '1161478346828165121');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('RenrenScheduler', 'TASK_1080345897063223297', 'DEFAULT', '0 * * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE,
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('RenrenScheduler', 'TASK_1080345897063223297', 'DEFAULT', null, 'com.hsshy.beam.admin.common.quartz.ScheduleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B455973720039636F6D2E68737368792E6265616D2E61646D696E2E6D6F64756C61722E7379732E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200074C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000269647400104C6A6176612F6C616E672F4C6F6E673B4C000A6D6574686F644E616D6571007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B78720031636F6D2E68737368792E6265616D2E7765622E6D6F64756C61722E626173652E656E746974792E52657374456E74697479056C713023FDB9490200024C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000A75706461746554696D6571007E000D78720035636F6D2E68737368792E6265616D2E7765622E6D6F64756C61722E626173652E656E746974792E4162737472616374456E7469747907AF5ED0FBB5DF0F0200024A000B63757272656E74506167654A00087061676553697A6578720035636F6D2E62616F6D69646F752E6D796261746973706C75732E657874656E73696F6E2E6163746976657265636F72642E4D6F64656C000000000000000102000078700000000000000000000000000000000A7372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001680B7908A0787371007E001177080000016C3EC7089878740008746573745461736B74000B30202A202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700EFE28DE95011001740005746573743270740006E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0017000000007800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('RenrenScheduler', 'LAPTOP-JP1B0EBJ1580898902058', '1580898935488', '15000');

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE,
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('RenrenScheduler', 'TASK_1080345897063223297', 'DEFAULT', 'TASK_1080345897063223297', 'DEFAULT', null, '1580898960000', '-1', '5', 'PAUSED', 'CRON', '1580898902000', '0', null, '2', '');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2048) DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='系统配置信息表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES ('1', 'CLOUD_STORAGE_CONFIG_KEY', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"uTUUJ1fidpo5Hc6b7qJXd2i5SyVUzsBvQJgKn_N6\",\"qiniuBucketName\":\"hsshy\",\"qiniuDomain\":\"http://img.hsshy.cn\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"_XKtAamyCjnQN_rW-cqjSOb5X5bmDONgfDBknzbR\",\"type\":1}', '1', '云存储配置信息', '2019-08-27 16:00:09', '2019-08-27 16:00:11');
INSERT INTO `sys_config` VALUES ('2', 'MiniAppConfig', '{\"appId\":\"wxee7d54a4d020f91e\",\"originalId\":\"\",\"appSecret\":\"4a768565a9a8866e5e32c75fd7f41cd8\",\"mchId\":\"\",\"mchKey\":\"\",\"keyStoreFilePath\":\"\"}', '1', '小程序配置', '2019-08-27 16:00:09', '2019-08-27 16:00:11');
INSERT INTO `sys_config` VALUES ('3', 'BdAiConfig', '{\"imgIdentifyClientId\":\"GvRG8m4zbSv5pGIjjklgU6oo\",\"imgIdentifyClientSecret\":\"XE25zPo39Rh9YN4KOIRtf8rDUeIyh4BW\",\"wordIdentifyClientId\":\"pYyymWG9DnFf8hCuWz3UrtfI\",\"wordIdentifyClientSecret\":\"Ng8wB34nqNi2bHRkOr3O6OBrPCS1s9gu\"}', '1', '百度图片识别AI配置', '2019-08-27 16:02:36', '2019-08-27 16:02:39');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1163640150102687746', '0', '总公司', '1', '0', '2019-08-20 02:33:26', '2019-08-20 02:33:26');
INSERT INTO `sys_dept` VALUES ('1163640207627567105', '1163640150102687746', '福州分公司', '1', '0', '2019-08-20 02:33:40', '2019-08-20 02:33:40');
INSERT INTO `sys_dept` VALUES ('1163640286849581058', '1163640150102687746', '厦门分公司', '1', '0', '2019-08-20 02:33:58', '2019-08-20 02:33:58');
INSERT INTO `sys_dept` VALUES ('1163640382295162881', '1163640207627567105', '技术部', '1', '0', '2019-08-20 02:34:21', '2019-08-20 02:34:21');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pid` bigint(20) DEFAULT NULL COMMENT '父级字典',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `des` varchar(255) DEFAULT NULL COMMENT '描述',
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('16', '0', '状态', '', 'frozen', '1', '2019-04-12 10:50:21', '2019-07-24 06:59:39');
INSERT INTO `sys_dict` VALUES ('17', '16', '启用', null, '1', '1', '2019-04-12 10:50:25', '2019-04-12 10:50:28');
INSERT INTO `sys_dict` VALUES ('18', '16', '禁用', null, '0', '2', '2019-04-12 10:50:30', '2019-04-12 10:50:33');
INSERT INTO `sys_dict` VALUES ('29', '0', '性别', '', 'sex', '2', '2019-04-12 10:50:36', '2019-04-13 07:29:59');
INSERT INTO `sys_dict` VALUES ('30', '29', '男', null, '1', '1', '2019-04-12 10:50:41', '2019-04-12 10:50:43');
INSERT INTO `sys_dict` VALUES ('31', '29', '女', null, '2', '2', '2019-04-12 10:50:46', '2019-04-12 10:50:48');
INSERT INTO `sys_dict` VALUES ('32', '0', '菜单类型', null, 'menu_type', '3', '2019-04-12 18:30:54', '2019-04-12 18:30:57');
INSERT INTO `sys_dict` VALUES ('33', '32', '目录', null, '0', '1', '2019-04-12 18:31:48', '2019-04-12 18:31:50');
INSERT INTO `sys_dict` VALUES ('34', '32', '菜单', null, '1', '2', '2019-04-12 18:32:10', '2019-04-12 18:32:12');
INSERT INTO `sys_dict` VALUES ('35', '32', '按钮', null, '2', '3', '2019-04-12 18:32:25', '2019-04-12 18:32:28');
INSERT INTO `sys_dict` VALUES ('36', '0', '定时任务状态', null, 'schedule_status', '4', '2019-04-13 11:49:28', '2019-04-13 11:49:30');
INSERT INTO `sys_dict` VALUES ('37', '36', '正常', null, '1', '1', '2019-04-13 11:50:08', '2019-04-13 11:50:10');
INSERT INTO `sys_dict` VALUES ('38', '36', '暂停', null, '0', '2', '2019-04-13 11:50:24', '2019-04-13 11:50:27');

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_name` varchar(64) DEFAULT NULL COMMENT '日志名称',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `succeed` varchar(32) DEFAULT NULL COMMENT '是否成功',
  `message` varchar(64) DEFAULT NULL COMMENT '消息',
  `ip_address` varchar(64) DEFAULT NULL COMMENT 'ip',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1217365092330835970 DEFAULT CHARSET=utf8mb4 COMMENT='登陆日志';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES ('1161909897993818114', '登录日志', '1072429679148908546', '成功', null, '223.104.212.57', '2019-08-15 07:58:02', '2019-08-15 07:58:02');
INSERT INTO `sys_login_log` VALUES ('1161934138113953793', '登录日志', '1072429679148908546', '成功', null, '115.192.111.165', '2019-08-15 09:34:21', '2019-08-15 09:34:21');
INSERT INTO `sys_login_log` VALUES ('1161939995908280322', '登录日志', '1072429679148908546', '成功', null, '220.249.52.58', '2019-08-15 09:57:37', '2019-08-15 09:57:37');
INSERT INTO `sys_login_log` VALUES ('1162159449334255617', '登录日志', '1072429679148908546', '成功', null, '36.7.154.161', '2019-08-16 00:29:39', '2019-08-16 00:29:39');
INSERT INTO `sys_login_log` VALUES ('1162233095637602305', '登录日志', '1072429679148908546', '成功', null, '118.242.26.254', '2019-08-16 05:22:18', '2019-08-16 05:22:18');
INSERT INTO `sys_login_log` VALUES ('1162251827009196033', '登录日志', '1072429679148908546', '成功', null, '183.129.132.226', '2019-08-16 06:36:44', '2019-08-16 06:36:44');
INSERT INTO `sys_login_log` VALUES ('1162269526284210178', '登录日志', '1072429679148908546', '成功', null, '36.5.133.57', '2019-08-16 07:47:04', '2019-08-16 07:47:04');
INSERT INTO `sys_login_log` VALUES ('1162273159998509058', '登录日志', '1072429679148908546', '成功', null, '121.35.129.129', '2019-08-16 08:01:30', '2019-08-16 08:01:30');
INSERT INTO `sys_login_log` VALUES ('1162278919612567553', '登录日志', '1072429679148908546', '成功', null, '221.2.151.4', '2019-08-16 08:24:23', '2019-08-16 08:24:23');
INSERT INTO `sys_login_log` VALUES ('1162365102753484802', '登录日志', '1072429679148908546', '成功', null, '114.242.249.7', '2019-08-16 14:06:51', '2019-08-16 14:06:51');
INSERT INTO `sys_login_log` VALUES ('1162396319515316226', '登录日志', '1072429679148908546', '成功', null, '91.72.173.26', '2019-08-16 16:10:54', '2019-08-16 16:10:54');
INSERT INTO `sys_login_log` VALUES ('1162403093840564225', '登录日志', '1072429679148908546', '成功', null, '218.20.200.57', '2019-08-16 16:37:49', '2019-08-16 16:37:49');
INSERT INTO `sys_login_log` VALUES ('1162403129597005826', '登录日志', '1072429679148908546', '成功', null, '218.20.200.57', '2019-08-16 16:37:57', '2019-08-16 16:37:57');
INSERT INTO `sys_login_log` VALUES ('1162403502634209281', '登录日志', '1072429679148908546', '成功', null, '218.20.200.57', '2019-08-16 16:39:26', '2019-08-16 16:39:26');
INSERT INTO `sys_login_log` VALUES ('1162485511838666753', '登录日志', '1072429679148908546', '成功', null, '113.201.136.227', '2019-08-16 22:05:19', '2019-08-16 22:05:19');
INSERT INTO `sys_login_log` VALUES ('1162493662973562882', '登录日志', '1072429679148908546', '成功', null, '113.201.136.227', '2019-08-16 22:37:42', '2019-08-16 22:37:42');
INSERT INTO `sys_login_log` VALUES ('1162505915756089346', '登录日志', '1072429679148908546', '成功', null, '221.219.106.34', '2019-08-16 23:26:23', '2019-08-16 23:26:23');
INSERT INTO `sys_login_log` VALUES ('1162551160908713986', '登录日志', '1072429679148908546', '成功', null, '223.71.97.4', '2019-08-17 02:26:11', '2019-08-17 02:26:11');
INSERT INTO `sys_login_log` VALUES ('1162613284485894145', '登录日志', '1072429679148908546', '成功', null, '111.201.249.109', '2019-08-17 06:33:02', '2019-08-17 06:33:02');
INSERT INTO `sys_login_log` VALUES ('1162634978466369537', '登录日志', '1072429679148908546', '成功', null, '117.159.17.197', '2019-08-17 07:59:14', '2019-08-17 07:59:14');
INSERT INTO `sys_login_log` VALUES ('1162635168980045826', '登录日志', '1072429679148908546', '成功', null, '117.159.17.197', '2019-08-17 08:00:00', '2019-08-17 08:00:00');
INSERT INTO `sys_login_log` VALUES ('1162654892967235585', '登录日志', '1072429679148908546', '成功', null, '101.88.233.91', '2019-08-17 09:18:22', '2019-08-17 09:18:22');
INSERT INTO `sys_login_log` VALUES ('1162684545337561089', '登录日志', '1072429679148908546', '成功', null, '120.244.111.77', '2019-08-17 11:16:12', '2019-08-17 11:16:12');
INSERT INTO `sys_login_log` VALUES ('1162712421772464130', '登录日志', '1072429679148908546', '成功', null, '223.197.116.5', '2019-08-17 13:06:58', '2019-08-17 13:06:58');
INSERT INTO `sys_login_log` VALUES ('1162715755283648514', '登录日志', '1072429679148908546', '成功', null, '120.244.111.77', '2019-08-17 13:20:13', '2019-08-17 13:20:13');
INSERT INTO `sys_login_log` VALUES ('1162715924989382658', '登录日志', '1072429679148908546', '成功', null, '120.244.111.77', '2019-08-17 13:20:53', '2019-08-17 13:20:53');
INSERT INTO `sys_login_log` VALUES ('1162729584059125761', '登录日志', '1072429679148908546', '成功', null, '120.244.111.77', '2019-08-17 14:15:10', '2019-08-17 14:15:10');
INSERT INTO `sys_login_log` VALUES ('1162729978692800513', '登录日志', '1072429679148908546', '成功', null, '120.244.111.77', '2019-08-17 14:16:44', '2019-08-17 14:16:44');
INSERT INTO `sys_login_log` VALUES ('1162740218817581058', '登录日志', '1072429679148908546', '成功', null, '223.73.120.115', '2019-08-17 14:57:25', '2019-08-17 14:57:25');
INSERT INTO `sys_login_log` VALUES ('1162907942105550849', '登录日志', '1072429679148908546', '成功', null, '223.73.120.115', '2019-08-18 02:03:54', '2019-08-18 02:03:54');
INSERT INTO `sys_login_log` VALUES ('1163025489282895874', '登录日志', '1072429679148908546', '成功', null, '120.244.111.77', '2019-08-18 09:50:59', '2019-08-18 09:50:59');
INSERT INTO `sys_login_log` VALUES ('1163054001553313794', '登录日志', '1072429679148908546', '成功', null, '119.250.173.4', '2019-08-18 11:44:17', '2019-08-18 11:44:17');
INSERT INTO `sys_login_log` VALUES ('1163060543744806913', '登录日志', '1072429679148908546', '成功', null, '114.236.138.114', '2019-08-18 12:10:17', '2019-08-18 12:10:17');
INSERT INTO `sys_login_log` VALUES ('1163076786539368449', '登录日志', '1072429679148908546', '成功', null, '120.85.77.80', '2019-08-18 13:14:49', '2019-08-18 13:14:49');
INSERT INTO `sys_login_log` VALUES ('1163250981374169090', '登录日志', '1072429679148908546', '成功', null, '61.237.228.2', '2019-08-19 00:47:01', '2019-08-19 00:47:01');
INSERT INTO `sys_login_log` VALUES ('1163268994995490817', '登录日志', '1072429679148908546', '成功', null, '221.217.49.123', '2019-08-19 01:58:36', '2019-08-19 01:58:36');
INSERT INTO `sys_login_log` VALUES ('1163275804515143681', '登录日志', '1072429679148908546', '成功', null, '125.71.216.103', '2019-08-19 02:25:39', '2019-08-19 02:25:39');
INSERT INTO `sys_login_log` VALUES ('1163281577647443969', '登录日志', '1072429679148908546', '成功', null, '117.89.17.233', '2019-08-19 02:48:35', '2019-08-19 02:48:35');
INSERT INTO `sys_login_log` VALUES ('1163340174641238017', '登录日志', '1072429679148908546', '成功', null, '221.217.49.123', '2019-08-19 06:41:26', '2019-08-19 06:41:26');
INSERT INTO `sys_login_log` VALUES ('1163340696043556866', '登录日志', '1072429679148908546', '成功', null, '61.51.193.115', '2019-08-19 06:43:30', '2019-08-19 06:43:30');
INSERT INTO `sys_login_log` VALUES ('1163349562407985154', '登录日志', '1072429679148908546', '成功', null, '121.204.50.218', '2019-08-19 07:18:44', '2019-08-19 07:18:44');
INSERT INTO `sys_login_log` VALUES ('1163351393028743170', '登录日志', '1', '成功', null, '121.204.50.218', '2019-08-19 07:26:01', '2019-08-19 07:26:01');
INSERT INTO `sys_login_log` VALUES ('1163367359410278402', '登录日志', '1', '成功', null, '121.204.50.218', '2019-08-19 08:29:27', '2019-08-19 08:29:27');
INSERT INTO `sys_login_log` VALUES ('1163367728123154434', '登录日志', '1072429679148908546', '成功', null, '121.204.50.218', '2019-08-19 08:30:55', '2019-08-19 08:30:55');
INSERT INTO `sys_login_log` VALUES ('1163367881265582081', '登录日志', '1', '成功', null, '121.204.50.218', '2019-08-19 08:31:32', '2019-08-19 08:31:32');
INSERT INTO `sys_login_log` VALUES ('1163377830805983234', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-19 09:11:04', '2019-08-19 09:11:04');
INSERT INTO `sys_login_log` VALUES ('1163419256703881218', '登录日志', '1072429679148908546', '成功', null, '1.207.113.98', '2019-08-19 11:55:41', '2019-08-19 11:55:41');
INSERT INTO `sys_login_log` VALUES ('1163419775270850561', '登录日志', '1072429679148908546', '成功', null, '1.207.113.98', '2019-08-19 11:57:44', '2019-08-19 11:57:44');
INSERT INTO `sys_login_log` VALUES ('1163635620833181698', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-20 02:15:26', '2019-08-20 02:15:26');
INSERT INTO `sys_login_log` VALUES ('1163647784151199746', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-20 03:03:46', '2019-08-20 03:03:46');
INSERT INTO `sys_login_log` VALUES ('1163650736597340161', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-20 03:15:30', '2019-08-20 03:15:30');
INSERT INTO `sys_login_log` VALUES ('1163716205274492930', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-20 07:35:39', '2019-08-20 07:35:39');
INSERT INTO `sys_login_log` VALUES ('1163719956328665090', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-20 07:50:33', '2019-08-20 07:50:33');
INSERT INTO `sys_login_log` VALUES ('1163722628851056641', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-20 08:01:10', '2019-08-20 08:01:10');
INSERT INTO `sys_login_log` VALUES ('1163723491946573825', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-20 08:04:36', '2019-08-20 08:04:36');
INSERT INTO `sys_login_log` VALUES ('1164002240311095298', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-21 02:32:15', '2019-08-21 02:32:15');
INSERT INTO `sys_login_log` VALUES ('1164068160063574017', '登录日志', '1072429679148908546', '成功', null, '110.87.39.6', '2019-08-21 06:54:11', '2019-08-21 06:54:11');
INSERT INTO `sys_login_log` VALUES ('1164069418962948098', '登录日志', '1', '成功', null, '110.87.39.6', '2019-08-21 06:59:12', '2019-08-21 06:59:12');
INSERT INTO `sys_login_log` VALUES ('1164199967899684865', '登录日志', '1072429679148908546', '成功', null, '112.10.160.151', '2019-08-21 15:37:57', '2019-08-21 15:37:57');
INSERT INTO `sys_login_log` VALUES ('1164200327376703489', '登录日志', '1072429679148908546', '成功', null, '112.10.160.151', '2019-08-21 15:39:23', '2019-08-21 15:39:23');
INSERT INTO `sys_login_log` VALUES ('1164240013080039426', '登录日志', '1072429679148908546', '成功', null, '220.165.183.220', '2019-08-21 18:17:04', '2019-08-21 18:17:04');
INSERT INTO `sys_login_log` VALUES ('1164328624236433410', '登录日志', '1072429679148908546', '成功', null, '182.144.176.42', '2019-08-22 00:09:11', '2019-08-22 00:09:11');
INSERT INTO `sys_login_log` VALUES ('1164376092701585410', '登录日志', '1072429679148908546', '成功', null, '113.77.192.252', '2019-08-22 03:17:48', '2019-08-22 03:17:48');
INSERT INTO `sys_login_log` VALUES ('1164378818474897409', '登录日志', '1', '成功', null, '27.156.25.250', '2019-08-22 03:28:38', '2019-08-22 03:28:38');
INSERT INTO `sys_login_log` VALUES ('1164383322435973122', '登录日志', '1', '成功', null, '27.156.25.250', '2019-08-22 03:46:32', '2019-08-22 03:46:32');
INSERT INTO `sys_login_log` VALUES ('1164403183849533441', '登录日志', '1072429679148908546', '成功', null, '60.174.249.202', '2019-08-22 05:05:27', '2019-08-22 05:05:27');
INSERT INTO `sys_login_log` VALUES ('1164416105824874497', '登录日志', '1072429679148908546', '成功', null, '115.192.196.75', '2019-08-22 05:56:48', '2019-08-22 05:56:48');
INSERT INTO `sys_login_log` VALUES ('1164438394802122753', '登录日志', '1', '成功', null, '127.0.0.1', '2019-08-22 07:25:22', '2019-08-22 07:25:22');
INSERT INTO `sys_login_log` VALUES ('1164455056166023169', '登录日志', '1', '成功', null, '27.156.25.250', '2019-08-22 08:31:35', '2019-08-22 08:31:35');
INSERT INTO `sys_login_log` VALUES ('1164459270061617153', '登录日志', '1072429679148908546', '成功', null, '113.68.66.142', '2019-08-22 08:48:19', '2019-08-22 08:48:19');
INSERT INTO `sys_login_log` VALUES ('1164465054698893314', '登录日志', '1072429679148908546', '成功', null, '1.119.5.162', '2019-08-22 09:11:18', '2019-08-22 09:11:18');
INSERT INTO `sys_login_log` VALUES ('1164471065555955713', '登录日志', '1072429679148908546', '成功', null, '113.88.161.215', '2019-08-22 09:35:12', '2019-08-22 09:35:12');
INSERT INTO `sys_login_log` VALUES ('1164471224419414018', '登录日志', '1072429679148908546', '成功', null, '113.88.161.215', '2019-08-22 09:35:49', '2019-08-22 09:35:49');
INSERT INTO `sys_login_log` VALUES ('1164472741402701825', '登录日志', '1072429679148908546', '成功', null, '183.62.6.148', '2019-08-22 09:41:51', '2019-08-22 09:41:51');
INSERT INTO `sys_login_log` VALUES ('1164495384554172418', '登录日志', '1072429679148908546', '成功', null, '220.248.243.122', '2019-08-22 11:11:50', '2019-08-22 11:11:50');
INSERT INTO `sys_login_log` VALUES ('1164495504851005441', '登录日志', '1072429679148908546', '成功', null, '220.248.243.122', '2019-08-22 11:12:18', '2019-08-22 11:12:18');
INSERT INTO `sys_login_log` VALUES ('1164503943685795841', '登录日志', '1072429679148908546', '成功', null, '175.189.13.101', '2019-08-22 11:45:50', '2019-08-22 11:45:50');
INSERT INTO `sys_login_log` VALUES ('1164516834258382849', '登录日志', '1072429679148908546', '成功', null, '220.248.243.122', '2019-08-22 12:37:04', '2019-08-22 12:37:04');
INSERT INTO `sys_login_log` VALUES ('1164557715988475905', '登录日志', '1072429679148908546', '成功', null, '59.42.97.190', '2019-08-22 15:19:31', '2019-08-22 15:19:31');
INSERT INTO `sys_login_log` VALUES ('1164678777564753922', '登录日志', '1072429679148908546', '成功', null, '171.210.198.201', '2019-08-22 23:20:34', '2019-08-22 23:20:34');
INSERT INTO `sys_login_log` VALUES ('1164769713896484865', '登录日志', '1072429679148908546', '成功', null, '123.172.50.108', '2019-08-23 05:21:55', '2019-08-23 05:21:55');
INSERT INTO `sys_login_log` VALUES ('1164781442785275906', '登录日志', '1072429679148908546', '成功', null, '117.185.36.177', '2019-08-23 06:08:31', '2019-08-23 06:08:31');
INSERT INTO `sys_login_log` VALUES ('1164786103978225666', '登录日志', '1072429679148908546', '成功', null, '210.12.126.4', '2019-08-23 06:27:03', '2019-08-23 06:27:03');
INSERT INTO `sys_login_log` VALUES ('1164838403568496641', '登录日志', '1072429679148908546', '成功', null, '117.136.38.151', '2019-08-23 09:54:52', '2019-08-23 09:54:52');
INSERT INTO `sys_login_log` VALUES ('1164838481263783937', '登录日志', '1072429679148908546', '成功', null, '117.136.38.151', '2019-08-23 09:55:10', '2019-08-23 09:55:10');
INSERT INTO `sys_login_log` VALUES ('1164903090259685378', '登录日志', '1072429679148908546', '成功', null, '221.177.253.30', '2019-08-23 14:11:54', '2019-08-23 14:11:54');
INSERT INTO `sys_login_log` VALUES ('1164909058196434946', '登录日志', '1072429679148908546', '成功', null, '49.94.174.5', '2019-08-23 14:35:37', '2019-08-23 14:35:37');
INSERT INTO `sys_login_log` VALUES ('1164909224471228418', '登录日志', '1072429679148908546', '成功', null, '49.94.174.5', '2019-08-23 14:36:17', '2019-08-23 14:36:17');
INSERT INTO `sys_login_log` VALUES ('1164909317119209474', '登录日志', '1072429679148908546', '成功', null, '49.94.174.5', '2019-08-23 14:36:39', '2019-08-23 14:36:39');
INSERT INTO `sys_login_log` VALUES ('1164949176705806338', '登录日志', '1072429679148908546', '成功', null, '120.244.41.84', '2019-08-23 17:15:02', '2019-08-23 17:15:02');
INSERT INTO `sys_login_log` VALUES ('1165116927697154050', '登录日志', '1072429679148908546', '成功', null, '115.200.225.195', '2019-08-24 04:21:37', '2019-08-24 04:21:37');
INSERT INTO `sys_login_log` VALUES ('1165136691148550145', '登录日志', '1072429679148908546', '成功', null, '223.104.212.242', '2019-08-24 05:40:09', '2019-08-24 05:40:09');
INSERT INTO `sys_login_log` VALUES ('1165239204115836930', '登录日志', '1072429679148908546', '成功', null, '112.10.77.40', '2019-08-24 12:27:30', '2019-08-24 12:27:30');
INSERT INTO `sys_login_log` VALUES ('1165240178205192193', '登录日志', '1072429679148908546', '成功', null, '112.10.77.40', '2019-08-24 12:31:22', '2019-08-24 12:31:22');
INSERT INTO `sys_login_log` VALUES ('1165294728882614274', '登录日志', '1072429679148908546', '成功', null, '27.189.223.71', '2019-08-24 16:08:08', '2019-08-24 16:08:08');
INSERT INTO `sys_login_log` VALUES ('1165296063497564162', '登录日志', '1072429679148908546', '成功', null, '27.189.223.71', '2019-08-24 16:13:26', '2019-08-24 16:13:26');
INSERT INTO `sys_login_log` VALUES ('1165301171325169665', '登录日志', '1072429679148908546', '成功', null, '27.189.223.71', '2019-08-24 16:33:44', '2019-08-24 16:33:44');
INSERT INTO `sys_login_log` VALUES ('1165428980223766530', '登录日志', '1072429679148908546', '成功', null, '106.19.219.235', '2019-08-25 01:01:36', '2019-08-25 01:01:36');
INSERT INTO `sys_login_log` VALUES ('1165440375526977537', '登录日志', '1072429679148908546', '成功', null, '27.189.223.71', '2019-08-25 01:46:53', '2019-08-25 01:46:53');
INSERT INTO `sys_login_log` VALUES ('1165496871438708738', '登录日志', '1072429679148908546', '成功', null, '49.94.88.182', '2019-08-25 05:31:23', '2019-08-25 05:31:23');
INSERT INTO `sys_login_log` VALUES ('1165498265361117186', '登录日志', '1072429679148908546', '成功', null, '49.94.88.182', '2019-08-25 05:36:55', '2019-08-25 05:36:55');
INSERT INTO `sys_login_log` VALUES ('1165655513194491906', '登录日志', '1072429679148908546', '成功', null, '183.192.42.24', '2019-08-25 16:01:46', '2019-08-25 16:01:46');
INSERT INTO `sys_login_log` VALUES ('1165818594457481217', '登录日志', '1072429679148908546', '成功', null, '223.104.3.61', '2019-08-26 02:49:47', '2019-08-26 02:49:47');
INSERT INTO `sys_login_log` VALUES ('1166177813643849730', '登录日志', '1072429679148908546', '成功', null, '211.139.95.71', '2019-08-27 02:37:12', '2019-08-27 02:37:12');
INSERT INTO `sys_login_log` VALUES ('1166195251848605697', '登录日志', '1072429679148908546', '成功', null, '183.67.56.82', '2019-08-27 03:46:30', '2019-08-27 03:46:30');
INSERT INTO `sys_login_log` VALUES ('1166227031465062402', '登录日志', '1072429679148908546', '成功', null, '117.147.16.30', '2019-08-27 05:52:46', '2019-08-27 05:52:46');
INSERT INTO `sys_login_log` VALUES ('1166247898878242817', '登录日志', '1072429679148908546', '成功', null, '210.13.127.88', '2019-08-27 07:15:42', '2019-08-27 07:15:42');
INSERT INTO `sys_login_log` VALUES ('1166643800415596546', '登录日志', '1072429679148908546', '成功', null, '218.17.162.223', '2019-08-28 09:28:52', '2019-08-28 09:28:52');
INSERT INTO `sys_login_log` VALUES ('1166756375132303362', '登录日志', '1072429679148908546', '成功', null, '49.7.4.84', '2019-08-28 16:56:12', '2019-08-28 16:56:12');
INSERT INTO `sys_login_log` VALUES ('1166756459907575810', '登录日志', '1072429679148908546', '成功', null, '49.7.4.84', '2019-08-28 16:56:32', '2019-08-28 16:56:32');
INSERT INTO `sys_login_log` VALUES ('1166756574277857282', '登录日志', '1072429679148908546', '成功', null, '221.12.166.104', '2019-08-28 16:56:59', '2019-08-28 16:56:59');
INSERT INTO `sys_login_log` VALUES ('1166877040267161601', '登录日志', '1072429679148908546', '成功', null, '14.221.166.35', '2019-08-29 00:55:41', '2019-08-29 00:55:41');
INSERT INTO `sys_login_log` VALUES ('1166952687534534658', '登录日志', '1072429679148908546', '成功', null, '103.78.130.82', '2019-08-29 05:56:16', '2019-08-29 05:56:16');
INSERT INTO `sys_login_log` VALUES ('1166957173317496834', '登录日志', '1072429679148908546', '成功', null, '111.194.242.165', '2019-08-29 06:14:06', '2019-08-29 06:14:06');
INSERT INTO `sys_login_log` VALUES ('1167076934047166466', '登录日志', '1072429679148908546', '成功', null, '119.86.73.230', '2019-08-29 14:09:59', '2019-08-29 14:09:59');
INSERT INTO `sys_login_log` VALUES ('1167265646706094082', '登录日志', '1072429679148908546', '成功', null, '101.254.237.138', '2019-08-30 02:39:52', '2019-08-30 02:39:52');
INSERT INTO `sys_login_log` VALUES ('1167331559258124290', '登录日志', '1072429679148908546', '成功', null, '101.254.237.138', '2019-08-30 07:01:46', '2019-08-30 07:01:46');
INSERT INTO `sys_login_log` VALUES ('1167340462205235201', '登录日志', '1072429679148908546', '成功', null, '110.184.66.27', '2019-08-30 07:37:09', '2019-08-30 07:37:09');
INSERT INTO `sys_login_log` VALUES ('1167340636637949953', '登录日志', '1072429679148908546', '成功', null, '110.184.66.27', '2019-08-30 07:37:51', '2019-08-30 07:37:51');
INSERT INTO `sys_login_log` VALUES ('1167341276781015041', '登录日志', '1072429679148908546', '成功', null, '101.254.237.138', '2019-08-30 07:40:23', '2019-08-30 07:40:23');
INSERT INTO `sys_login_log` VALUES ('1167343329511141378', '登录日志', '1072429679148908546', '成功', null, '222.93.22.26', '2019-08-30 07:48:33', '2019-08-30 07:48:33');
INSERT INTO `sys_login_log` VALUES ('1167344514242637826', '登录日志', '1072429679148908546', '成功', null, '101.254.237.138', '2019-08-30 07:53:15', '2019-08-30 07:53:15');
INSERT INTO `sys_login_log` VALUES ('1167352264339156993', '登录日志', '1072429679148908546', '成功', null, '27.156.26.246', '2019-08-30 08:24:03', '2019-08-30 08:24:03');
INSERT INTO `sys_login_log` VALUES ('1167399547021721602', '登录日志', '1072429679148908546', '成功', null, '115.63.127.158', '2019-08-30 11:31:56', '2019-08-30 11:31:56');
INSERT INTO `sys_login_log` VALUES ('1167674087329136642', '登录日志', '1072429679148908546', '成功', null, '123.15.57.171', '2019-08-31 05:42:51', '2019-08-31 05:42:51');
INSERT INTO `sys_login_log` VALUES ('1167751501145079809', '登录日志', '1072429679148908546', '成功', null, '124.200.144.178', '2019-08-31 10:50:28', '2019-08-31 10:50:28');
INSERT INTO `sys_login_log` VALUES ('1167777586507317250', '登录日志', '1072429679148908546', '成功', null, '39.128.38.2', '2019-08-31 12:34:08', '2019-08-31 12:34:08');
INSERT INTO `sys_login_log` VALUES ('1167778728062648322', '登录日志', '1072429679148908546', '成功', null, '39.128.38.2', '2019-08-31 12:38:40', '2019-08-31 12:38:40');
INSERT INTO `sys_login_log` VALUES ('1167781404196372481', '登录日志', '1072429679148908546', '成功', null, '39.128.38.2', '2019-08-31 12:49:18', '2019-08-31 12:49:18');
INSERT INTO `sys_login_log` VALUES ('1167788355223453697', '登录日志', '1072429679148908546', '成功', null, '39.128.38.2', '2019-08-31 13:16:55', '2019-08-31 13:16:55');
INSERT INTO `sys_login_log` VALUES ('1167995700490698754', '登录日志', '1072429679148908546', '成功', null, '112.49.235.213', '2019-09-01 03:00:50', '2019-09-01 03:00:50');
INSERT INTO `sys_login_log` VALUES ('1168327748661182465', '登录日志', '1072429679148908546', '成功', null, '211.139.95.71', '2019-09-02 01:00:16', '2019-09-02 01:00:16');
INSERT INTO `sys_login_log` VALUES ('1168337982691639297', '登录日志', '1', '成功', null, '27.156.26.246', '2019-09-02 01:40:56', '2019-09-02 01:40:56');
INSERT INTO `sys_login_log` VALUES ('1184023460016328706', '登录日志', '1', '成功', null, '127.0.0.1', '2019-10-15 08:29:26', '2019-10-15 08:29:26');
INSERT INTO `sys_login_log` VALUES ('1184037635782381570', '登录日志', '1072429679148908546', '成功', null, '127.0.0.1', '2019-10-15 09:25:45', '2019-10-15 09:25:45');
INSERT INTO `sys_login_log` VALUES ('1184039480792657921', '登录日志', '1072429679148908546', '成功', null, '220.160.2.89', '2019-10-15 09:33:05', '2019-10-15 09:33:05');
INSERT INTO `sys_login_log` VALUES ('1184040175637835778', '登录日志', '1072429679148908546', '成功', null, '220.160.2.89', '2019-10-15 09:35:51', '2019-10-15 09:35:51');
INSERT INTO `sys_login_log` VALUES ('1184040243870773249', '登录日志', '1', '成功', null, '220.160.2.89', '2019-10-15 09:36:07', '2019-10-15 09:36:07');
INSERT INTO `sys_login_log` VALUES ('1184040325416431618', '登录日志', '1072429679148908546', '成功', null, '220.160.2.89', '2019-10-15 09:36:27', '2019-10-15 09:36:27');
INSERT INTO `sys_login_log` VALUES ('1184045707601850370', '登录日志', '1072429679148908546', '成功', null, '121.13.43.179', '2019-10-15 09:57:50', '2019-10-15 09:57:50');
INSERT INTO `sys_login_log` VALUES ('1184046915804672002', '登录日志', '1072429679148908546', '成功', null, '121.13.43.179', '2019-10-15 10:02:38', '2019-10-15 10:02:38');
INSERT INTO `sys_login_log` VALUES ('1184065661688557569', '登录日志', '1072429679148908546', '成功', null, '45.127.187.240', '2019-10-15 11:17:07', '2019-10-15 11:17:07');
INSERT INTO `sys_login_log` VALUES ('1184288663411830785', '登录日志', '1072429679148908546', '成功', null, '27.156.26.24', '2019-10-16 02:03:15', '2019-10-16 02:03:15');
INSERT INTO `sys_login_log` VALUES ('1184475303816146945', '登录日志', '1072429679148908546', '成功', null, '183.255.41.24', '2019-10-16 14:24:54', '2019-10-16 14:24:54');
INSERT INTO `sys_login_log` VALUES ('1184646739394506754', '登录日志', '1072429679148908546', '成功', null, '118.186.228.195', '2019-10-17 01:46:07', '2019-10-17 01:46:07');
INSERT INTO `sys_login_log` VALUES ('1184659265599516673', '登录日志', '1072429679148908546', '成功', null, '183.51.123.185', '2019-10-17 02:35:53', '2019-10-17 02:35:53');
INSERT INTO `sys_login_log` VALUES ('1184667686872756225', '登录日志', '1072429679148908546', '成功', null, '183.51.123.185', '2019-10-17 03:09:21', '2019-10-17 03:09:21');
INSERT INTO `sys_login_log` VALUES ('1185105141954457602', '登录日志', '1072429679148908546', '成功', null, '61.135.24.246', '2019-10-18 08:07:39', '2019-10-18 08:07:39');
INSERT INTO `sys_login_log` VALUES ('1185137719122014210', '登录日志', '1072429679148908546', '成功', null, '112.32.8.167', '2019-10-18 10:17:06', '2019-10-18 10:17:06');
INSERT INTO `sys_login_log` VALUES ('1185398433665462273', '登录日志', '1072429679148908546', '成功', null, '112.32.34.166', '2019-10-19 03:33:05', '2019-10-19 03:33:05');
INSERT INTO `sys_login_log` VALUES ('1185517088759820290', '登录日志', '1072429679148908546', '成功', null, '125.33.216.32', '2019-10-19 11:24:34', '2019-10-19 11:24:34');
INSERT INTO `sys_login_log` VALUES ('1185766696109391873', '登录日志', '1072429679148908546', '成功', null, '127.0.0.1', '2019-10-20 03:56:25', '2019-10-20 03:56:25');
INSERT INTO `sys_login_log` VALUES ('1185838325591650306', '登录日志', '1072429679148908546', '成功', null, '121.13.43.10', '2019-10-20 08:41:03', '2019-10-20 08:41:03');
INSERT INTO `sys_login_log` VALUES ('1186074963232108545', '登录日志', '1072429679148908546', '成功', null, '36.7.87.91', '2019-10-21 00:21:22', '2019-10-21 00:21:22');
INSERT INTO `sys_login_log` VALUES ('1186118066525646849', '登录日志', '1072429679148908546', '成功', null, '223.104.49.144', '2019-10-21 03:12:39', '2019-10-21 03:12:39');
INSERT INTO `sys_login_log` VALUES ('1186199034854846465', '登录日志', '1072429679148908546', '成功', null, '120.197.222.165', '2019-10-21 08:34:23', '2019-10-21 08:34:23');
INSERT INTO `sys_login_log` VALUES ('1186209824873852929', '登录日志', '1072429679148908546', '成功', null, '121.204.51.166', '2019-10-21 09:17:16', '2019-10-21 09:17:16');
INSERT INTO `sys_login_log` VALUES ('1186218906192130049', '登录日志', '1072429679148908546', '成功', null, '111.36.138.31', '2019-10-21 09:53:21', '2019-10-21 09:53:21');
INSERT INTO `sys_login_log` VALUES ('1186449424397578241', '登录日志', '1072429679148908546', '成功', null, '14.23.122.34', '2019-10-22 01:09:21', '2019-10-22 01:09:21');
INSERT INTO `sys_login_log` VALUES ('1186450390555504642', '登录日志', '1072429679148908546', '成功', null, '14.23.122.34', '2019-10-22 01:13:11', '2019-10-22 01:13:11');
INSERT INTO `sys_login_log` VALUES ('1186458536862756865', '登录日志', '1072429679148908546', '成功', null, '36.248.247.254', '2019-10-22 01:45:33', '2019-10-22 01:45:33');
INSERT INTO `sys_login_log` VALUES ('1186513239273320450', '登录日志', '1072429679148908546', '成功', null, '14.23.122.34', '2019-10-22 05:22:55', '2019-10-22 05:22:55');
INSERT INTO `sys_login_log` VALUES ('1186529404653613057', '登录日志', '1072429679148908546', '成功', null, '171.8.173.42', '2019-10-22 06:27:09', '2019-10-22 06:27:09');
INSERT INTO `sys_login_log` VALUES ('1186571580687200258', '登录日志', '1072429679148908546', '成功', null, '115.171.120.96', '2019-10-22 09:14:45', '2019-10-22 09:14:45');
INSERT INTO `sys_login_log` VALUES ('1186571842197860354', '登录日志', '1072429679148908546', '成功', null, '221.219.159.16', '2019-10-22 09:15:47', '2019-10-22 09:15:47');
INSERT INTO `sys_login_log` VALUES ('1186571857393823746', '登录日志', '1072429679148908546', '成功', null, '221.219.159.16', '2019-10-22 09:15:51', '2019-10-22 09:15:51');
INSERT INTO `sys_login_log` VALUES ('1186575297469689857', '登录日志', '1072429679148908546', '成功', null, '117.158.4.243', '2019-10-22 09:29:31', '2019-10-22 09:29:31');
INSERT INTO `sys_login_log` VALUES ('1186575742829277186', '登录日志', '1072429679148908546', '成功', null, '115.171.120.96', '2019-10-22 09:31:17', '2019-10-22 09:31:17');
INSERT INTO `sys_login_log` VALUES ('1187286806097244162', '登录日志', '1', '成功', null, '121.204.59.24', '2019-10-24 08:36:48', '2019-10-24 08:36:48');
INSERT INTO `sys_login_log` VALUES ('1187288424694382594', '登录日志', '1', '成功', null, '127.0.0.1', '2019-10-24 08:43:14', '2019-10-24 08:43:14');
INSERT INTO `sys_login_log` VALUES ('1187302979752898561', '登录日志', '1072429679148908546', '成功', null, '153.37.175.10', '2019-10-24 09:41:04', '2019-10-24 09:41:04');
INSERT INTO `sys_login_log` VALUES ('1187351248675745793', '登录日志', '1072429679148908546', '成功', null, '223.104.240.171', '2019-10-24 12:52:52', '2019-10-24 12:52:52');
INSERT INTO `sys_login_log` VALUES ('1187908350838255617', '登录日志', '1072429679148908546', '成功', null, '183.194.175.69', '2019-10-26 01:46:36', '2019-10-26 01:46:36');
INSERT INTO `sys_login_log` VALUES ('1187909283232034818', '登录日志', '1072429679148908546', '成功', null, '183.194.175.69', '2019-10-26 01:50:18', '2019-10-26 01:50:18');
INSERT INTO `sys_login_log` VALUES ('1187934114518347778', '登录日志', '1072429679148908546', '成功', null, '114.222.222.199', '2019-10-26 03:28:58', '2019-10-26 03:28:58');
INSERT INTO `sys_login_log` VALUES ('1188018547919863809', '登录日志', '1', '成功', null, '127.0.0.1', '2019-10-26 09:04:29', '2019-10-26 09:04:29');
INSERT INTO `sys_login_log` VALUES ('1188328012835139586', '登录日志', '1072429679148908546', '成功', null, '27.19.127.233', '2019-10-27 05:34:11', '2019-10-27 05:34:11');
INSERT INTO `sys_login_log` VALUES ('1188502566308098050', '登录日志', '1072429679148908546', '成功', null, '124.236.165.95', '2019-10-27 17:07:48', '2019-10-27 17:07:48');
INSERT INTO `sys_login_log` VALUES ('1188704026618703873', '登录日志', '1072429679148908546', '成功', null, '113.71.137.3', '2019-10-28 06:28:20', '2019-10-28 06:28:20');
INSERT INTO `sys_login_log` VALUES ('1188706630237106178', '登录日志', '1072429679148908546', '成功', null, '113.71.137.3', '2019-10-28 06:38:40', '2019-10-28 06:38:40');
INSERT INTO `sys_login_log` VALUES ('1188707104336064513', '登录日志', '1072429679148908546', '成功', null, '113.71.137.3', '2019-10-28 06:40:33', '2019-10-28 06:40:33');
INSERT INTO `sys_login_log` VALUES ('1188707144043540481', '登录日志', '1072429679148908546', '成功', null, '113.71.137.3', '2019-10-28 06:40:43', '2019-10-28 06:40:43');
INSERT INTO `sys_login_log` VALUES ('1188707297823502337', '登录日志', '1072429679148908546', '成功', null, '113.71.137.3', '2019-10-28 06:41:20', '2019-10-28 06:41:20');
INSERT INTO `sys_login_log` VALUES ('1188707546734473217', '登录日志', '1072429679148908546', '成功', null, '113.71.137.3', '2019-10-28 06:42:19', '2019-10-28 06:42:19');
INSERT INTO `sys_login_log` VALUES ('1188721090532159489', '登录日志', '1072429679148908546', '成功', null, '117.136.38.147', '2019-10-28 07:36:08', '2019-10-28 07:36:08');
INSERT INTO `sys_login_log` VALUES ('1189068742251720706', '登录日志', '1072429679148908546', '成功', null, '61.178.98.66', '2019-10-29 06:37:35', '2019-10-29 06:37:35');
INSERT INTO `sys_login_log` VALUES ('1189351267918360577', '登录日志', '1072429679148908546', '成功', null, '123.8.116.228', '2019-10-30 01:20:14', '2019-10-30 01:20:14');
INSERT INTO `sys_login_log` VALUES ('1189352034410639361', '登录日志', '1072429679148908546', '成功', null, '123.8.116.228', '2019-10-30 01:23:17', '2019-10-30 01:23:17');
INSERT INTO `sys_login_log` VALUES ('1189416594140246018', '登录日志', '1072429679148908546', '成功', null, '27.129.164.254', '2019-10-30 05:39:49', '2019-10-30 05:39:49');
INSERT INTO `sys_login_log` VALUES ('1189880570754052097', '登录日志', '1072429679148908546', '成功', null, '42.88.103.30', '2019-10-31 12:23:30', '2019-10-31 12:23:30');
INSERT INTO `sys_login_log` VALUES ('1190161073369460737', '登录日志', '1072429679148908546', '成功', null, '36.7.159.55', '2019-11-01 06:58:07', '2019-11-01 06:58:07');
INSERT INTO `sys_login_log` VALUES ('1190237524328394753', '登录日志', '1072429679148908546', '成功', null, '123.127.36.236', '2019-11-01 12:01:54', '2019-11-01 12:01:54');
INSERT INTO `sys_login_log` VALUES ('1190257276371283970', '登录日志', '1072429679148908546', '成功', null, '220.200.41.210', '2019-11-01 13:20:23', '2019-11-01 13:20:23');
INSERT INTO `sys_login_log` VALUES ('1190263861193682946', '登录日志', '1072429679148908546', '成功', null, '120.36.232.36', '2019-11-01 13:46:33', '2019-11-01 13:46:33');
INSERT INTO `sys_login_log` VALUES ('1190516491073564673', '登录日志', '1072429679148908546', '成功', null, '101.132.192.59', '2019-11-02 06:30:25', '2019-11-02 06:30:25');
INSERT INTO `sys_login_log` VALUES ('1190622603714179074', '登录日志', '1072429679148908546', '成功', null, '113.246.153.188', '2019-11-02 13:32:04', '2019-11-02 13:32:04');
INSERT INTO `sys_login_log` VALUES ('1191283754789711874', '登录日志', '1072429679148908546', '成功', null, '58.16.228.146', '2019-11-04 09:19:15', '2019-11-04 09:19:15');
INSERT INTO `sys_login_log` VALUES ('1193777391343710210', '登录日志', '1', '成功', null, '121.204.50.32', '2019-11-11 06:28:04', '2019-11-11 06:28:04');
INSERT INTO `sys_login_log` VALUES ('1193779074249416706', '登录日志', '1072429679148908546', '成功', null, '121.204.50.32', '2019-11-11 06:34:45', '2019-11-11 06:34:45');
INSERT INTO `sys_login_log` VALUES ('1194138843845910529', '登录日志', '1072429679148908546', '成功', null, '113.132.23.27', '2019-11-12 06:24:21', '2019-11-12 06:24:21');
INSERT INTO `sys_login_log` VALUES ('1194144821479890946', '登录日志', '1072429679148908546', '成功', null, '121.204.50.32', '2019-11-12 06:48:06', '2019-11-12 06:48:06');
INSERT INTO `sys_login_log` VALUES ('1194162962075377666', '登录日志', '1072429679148908546', '成功', null, '113.132.23.27', '2019-11-12 08:00:11', '2019-11-12 08:00:11');
INSERT INTO `sys_login_log` VALUES ('1194163094003015682', '登录日志', '1072429679148908546', '成功', null, '113.132.23.27', '2019-11-12 08:00:43', '2019-11-12 08:00:43');
INSERT INTO `sys_login_log` VALUES ('1194191459409879042', '登录日志', '1072429679148908546', '成功', null, '113.105.202.166', '2019-11-12 09:53:26', '2019-11-12 09:53:26');
INSERT INTO `sys_login_log` VALUES ('1194219102410792962', '登录日志', '1072429679148908546', '成功', null, '124.207.128.50', '2019-11-12 11:43:16', '2019-11-12 11:43:16');
INSERT INTO `sys_login_log` VALUES ('1194423918718246914', '登录日志', '1072429679148908546', '成功', null, '124.207.128.50', '2019-11-13 01:17:08', '2019-11-13 01:17:08');
INSERT INTO `sys_login_log` VALUES ('1194458209389010946', '登录日志', '1072429679148908546', '成功', null, '119.130.207.227', '2019-11-13 03:33:24', '2019-11-13 03:33:24');
INSERT INTO `sys_login_log` VALUES ('1194498865385857026', '登录日志', '1072429679148908546', '成功', null, '221.238.46.186', '2019-11-13 06:14:57', '2019-11-13 06:14:57');
INSERT INTO `sys_login_log` VALUES ('1194528181654020098', '登录日志', '1072429679148908546', '成功', null, '183.15.176.32', '2019-11-13 08:11:26', '2019-11-13 08:11:26');
INSERT INTO `sys_login_log` VALUES ('1194529940371824642', '登录日志', '1072429679148908546', '成功', null, '121.204.50.32', '2019-11-13 08:18:26', '2019-11-13 08:18:26');
INSERT INTO `sys_login_log` VALUES ('1194545813878890498', '登录日志', '1072429679148908546', '成功', null, '122.225.128.143', '2019-11-13 09:21:30', '2019-11-13 09:21:30');
INSERT INTO `sys_login_log` VALUES ('1194805374623834113', '登录日志', '1072429679148908546', '成功', null, '123.125.5.211', '2019-11-14 02:32:54', '2019-11-14 02:32:54');
INSERT INTO `sys_login_log` VALUES ('1194964041637715969', '登录日志', '1', '成功', null, '112.49.235.27', '2019-11-14 13:03:24', '2019-11-14 13:03:24');
INSERT INTO `sys_login_log` VALUES ('1194979010731204609', '登录日志', '1072429679148908546', '成功', null, '122.193.193.51', '2019-11-14 14:02:52', '2019-11-14 14:02:52');
INSERT INTO `sys_login_log` VALUES ('1195004184767651842', '登录日志', '1072429679148908546', '成功', null, '119.131.119.230', '2019-11-14 15:42:54', '2019-11-14 15:42:54');
INSERT INTO `sys_login_log` VALUES ('1195164213747539970', '登录日志', '1072429679148908546', '成功', null, '222.85.230.141', '2019-11-15 02:18:48', '2019-11-15 02:18:48');
INSERT INTO `sys_login_log` VALUES ('1195175457703485441', '登录日志', '1072429679148908546', '成功', null, '171.212.139.80', '2019-11-15 03:03:29', '2019-11-15 03:03:29');
INSERT INTO `sys_login_log` VALUES ('1195209251688964097', '登录日志', '1072429679148908546', '成功', null, '118.85.207.192', '2019-11-15 05:17:46', '2019-11-15 05:17:46');
INSERT INTO `sys_login_log` VALUES ('1195711674807377922', '登录日志', '1072429679148908546', '成功', null, '119.131.119.230', '2019-11-16 14:34:13', '2019-11-16 14:34:13');
INSERT INTO `sys_login_log` VALUES ('1195714929398538241', '登录日志', '1072429679148908546', '成功', null, '119.131.119.230', '2019-11-16 14:47:09', '2019-11-16 14:47:09');
INSERT INTO `sys_login_log` VALUES ('1196098990277287937', '登录日志', '1072429679148908546', '成功', null, '101.247.128.179', '2019-11-17 16:13:16', '2019-11-17 16:13:16');
INSERT INTO `sys_login_log` VALUES ('1196099100985942018', '登录日志', '1072429679148908546', '成功', null, '49.7.4.87', '2019-11-17 16:13:43', '2019-11-17 16:13:43');
INSERT INTO `sys_login_log` VALUES ('1196242830543970305', '登录日志', '1072429679148908546', '成功', null, '218.93.241.234', '2019-11-18 01:44:51', '2019-11-18 01:44:51');
INSERT INTO `sys_login_log` VALUES ('1196252799758655490', '登录日志', '1072429679148908546', '成功', null, '123.147.248.32', '2019-11-18 02:24:27', '2019-11-18 02:24:27');
INSERT INTO `sys_login_log` VALUES ('1196269366626115586', '登录日志', '1072429679148908546', '成功', null, '123.147.248.32', '2019-11-18 03:30:17', '2019-11-18 03:30:17');
INSERT INTO `sys_login_log` VALUES ('1196341440887545858', '登录日志', '1072429679148908546', '成功', null, '116.25.249.145', '2019-11-18 08:16:41', '2019-11-18 08:16:41');
INSERT INTO `sys_login_log` VALUES ('1196372175828705282', '登录日志', '1', '成功', null, '27.156.25.115', '2019-11-18 10:18:49', '2019-11-18 10:18:49');
INSERT INTO `sys_login_log` VALUES ('1196373592773648385', '登录日志', '1072429679148908546', '成功', null, '211.103.128.62', '2019-11-18 10:24:27', '2019-11-18 10:24:27');
INSERT INTO `sys_login_log` VALUES ('1196373605813739522', '登录日志', '1072429679148908546', '成功', null, '27.156.25.115', '2019-11-18 10:24:30', '2019-11-18 10:24:30');
INSERT INTO `sys_login_log` VALUES ('1196385327953895425', '登录日志', '1072429679148908546', '成功', null, '211.103.128.62', '2019-11-18 11:11:05', '2019-11-18 11:11:05');
INSERT INTO `sys_login_log` VALUES ('1196402480912424961', '登录日志', '1072429679148908546', '成功', null, '211.103.128.62', '2019-11-18 12:19:14', '2019-11-18 12:19:14');
INSERT INTO `sys_login_log` VALUES ('1196622288954220546', '登录日志', '1072429679148908546', '成功', null, '39.168.138.214', '2019-11-19 02:52:41', '2019-11-19 02:52:41');
INSERT INTO `sys_login_log` VALUES ('1196721925753036802', '登录日志', '1072429679148908546', '成功', null, '117.89.213.254', '2019-11-19 09:28:36', '2019-11-19 09:28:36');
INSERT INTO `sys_login_log` VALUES ('1196787344383827970', '登录日志', '1072429679148908546', '成功', null, '114.253.102.202', '2019-11-19 13:48:33', '2019-11-19 13:48:33');
INSERT INTO `sys_login_log` VALUES ('1196787344476102658', '登录日志', '1072429679148908546', '成功', null, '114.253.102.202', '2019-11-19 13:48:33', '2019-11-19 13:48:33');
INSERT INTO `sys_login_log` VALUES ('1197050446518050818', '登录日志', '1072429679148908546', '成功', null, '110.87.82.203', '2019-11-20 07:14:01', '2019-11-20 07:14:01');
INSERT INTO `sys_login_log` VALUES ('1197323761711210497', '登录日志', '1072429679148908546', '成功', null, '116.22.1.140', '2019-11-21 01:20:05', '2019-11-21 01:20:05');
INSERT INTO `sys_login_log` VALUES ('1197324544947150850', '登录日志', '1072429679148908546', '成功', null, '116.22.1.140', '2019-11-21 01:23:11', '2019-11-21 01:23:11');
INSERT INTO `sys_login_log` VALUES ('1197331968701652993', '登录日志', '1072429679148908546', '成功', null, '112.91.177.227', '2019-11-21 01:52:41', '2019-11-21 01:52:41');
INSERT INTO `sys_login_log` VALUES ('1197393460444684290', '登录日志', '1072429679148908546', '成功', null, '101.224.36.219', '2019-11-21 05:57:02', '2019-11-21 05:57:02');
INSERT INTO `sys_login_log` VALUES ('1197396282280140802', '登录日志', '1072429679148908546', '成功', null, '101.224.36.167', '2019-11-21 06:08:15', '2019-11-21 06:08:15');
INSERT INTO `sys_login_log` VALUES ('1197408084917379073', '登录日志', '1072429679148908546', '成功', null, '58.20.40.5', '2019-11-21 06:55:09', '2019-11-21 06:55:09');
INSERT INTO `sys_login_log` VALUES ('1197408461607821314', '登录日志', '1072429679148908546', '成功', null, '58.20.40.5', '2019-11-21 06:56:39', '2019-11-21 06:56:39');
INSERT INTO `sys_login_log` VALUES ('1198131754350567426', '登录日志', '1072429679148908546', '成功', null, '222.185.250.190', '2019-11-23 06:50:45', '2019-11-23 06:50:45');
INSERT INTO `sys_login_log` VALUES ('1198245585773027330', '登录日志', '1072429679148908546', '成功', null, '183.42.41.23', '2019-11-23 14:23:05', '2019-11-23 14:23:05');
INSERT INTO `sys_login_log` VALUES ('1198563502284759042', '登录日志', '1072429679148908546', '成功', null, '49.5.198.70', '2019-11-24 11:26:22', '2019-11-24 11:26:22');
INSERT INTO `sys_login_log` VALUES ('1198588871184375809', '登录日志', '1072429679148908546', '成功', null, '120.236.163.95', '2019-11-24 13:07:10', '2019-11-24 13:07:10');
INSERT INTO `sys_login_log` VALUES ('1198846281882951682', '登录日志', '1072429679148908546', '成功', null, '222.185.250.190', '2019-11-25 06:10:02', '2019-11-25 06:10:02');
INSERT INTO `sys_login_log` VALUES ('1198855095847903233', '登录日志', '1072429679148908546', '成功', null, '222.91.148.113', '2019-11-25 06:45:03', '2019-11-25 06:45:03');
INSERT INTO `sys_login_log` VALUES ('1198865857433722882', '登录日志', '1072429679148908546', '成功', null, '122.234.28.3', '2019-11-25 07:27:49', '2019-11-25 07:27:49');
INSERT INTO `sys_login_log` VALUES ('1198866273676451842', '登录日志', '1072429679148908546', '成功', null, '122.234.28.3', '2019-11-25 07:29:28', '2019-11-25 07:29:28');
INSERT INTO `sys_login_log` VALUES ('1198880283922685953', '登录日志', '1072429679148908546', '成功', null, '61.153.150.115', '2019-11-25 08:25:08', '2019-11-25 08:25:08');
INSERT INTO `sys_login_log` VALUES ('1198881081306013697', '登录日志', '1072429679148908546', '成功', null, '180.164.93.231', '2019-11-25 08:28:19', '2019-11-25 08:28:19');
INSERT INTO `sys_login_log` VALUES ('1198883162083450882', '登录日志', '1072429679148908546', '成功', null, '180.164.93.231', '2019-11-25 08:36:35', '2019-11-25 08:36:35');
INSERT INTO `sys_login_log` VALUES ('1198951603519774722', '登录日志', '1072429679148908546', '成功', null, '120.204.213.185', '2019-11-25 13:08:32', '2019-11-25 13:08:32');
INSERT INTO `sys_login_log` VALUES ('1199138444617342978', '登录日志', '1072429679148908546', '成功', null, '223.255.14.206', '2019-11-26 01:30:59', '2019-11-26 01:30:59');
INSERT INTO `sys_login_log` VALUES ('1199141033576652801', '登录日志', '1072429679148908546', '成功', null, '182.149.199.158', '2019-11-26 01:41:16', '2019-11-26 01:41:16');
INSERT INTO `sys_login_log` VALUES ('1199225443609108481', '登录日志', '1072429679148908546', '成功', null, '121.207.215.87', '2019-11-26 07:16:41', '2019-11-26 07:16:41');
INSERT INTO `sys_login_log` VALUES ('1199258177572331521', '登录日志', '1072429679148908546', '成功', null, '114.84.56.30', '2019-11-26 09:26:45', '2019-11-26 09:26:45');
INSERT INTO `sys_login_log` VALUES ('1199271427668074497', '登录日志', '1072429679148908546', '成功', null, '120.236.163.95', '2019-11-26 10:19:24', '2019-11-26 10:19:24');
INSERT INTO `sys_login_log` VALUES ('1199504499164471298', '登录日志', '1072429679148908546', '成功', null, '114.84.56.30', '2019-11-27 01:45:33', '2019-11-27 01:45:33');
INSERT INTO `sys_login_log` VALUES ('1199507355242225665', '登录日志', '1072429679148908546', '成功', null, '180.169.87.206', '2019-11-27 01:56:54', '2019-11-27 01:56:54');
INSERT INTO `sys_login_log` VALUES ('1199527788834349057', '登录日志', '1072429679148908546', '成功', null, '221.222.198.86', '2019-11-27 03:18:06', '2019-11-27 03:18:06');
INSERT INTO `sys_login_log` VALUES ('1199671651548946433', '登录日志', '1072429679148908546', '成功', null, '117.136.85.38', '2019-11-27 12:49:45', '2019-11-27 12:49:45');
INSERT INTO `sys_login_log` VALUES ('1199919425007804418', '登录日志', '1072429679148908546', '成功', null, '113.87.131.215', '2019-11-28 05:14:19', '2019-11-28 05:14:19');
INSERT INTO `sys_login_log` VALUES ('1200008470182387713', '登录日志', '1072429679148908546', '成功', null, '120.199.34.99', '2019-11-28 11:08:09', '2019-11-28 11:08:09');
INSERT INTO `sys_login_log` VALUES ('1200067002730700802', '登录日志', '1072429679148908546', '成功', null, '42.88.97.230', '2019-11-28 15:00:44', '2019-11-28 15:00:44');
INSERT INTO `sys_login_log` VALUES ('1200090571929968641', '登录日志', '1072429679148908546', '成功', null, '123.139.35.18', '2019-11-28 16:34:24', '2019-11-28 16:34:24');
INSERT INTO `sys_login_log` VALUES ('1200618233047965697', '登录日志', '1072429679148908546', '成功', null, '114.226.54.12', '2019-11-30 03:31:08', '2019-11-30 03:31:08');
INSERT INTO `sys_login_log` VALUES ('1200633885494239233', '登录日志', '1072429679148908546', '成功', null, '114.94.173.44', '2019-11-30 04:33:20', '2019-11-30 04:33:20');
INSERT INTO `sys_login_log` VALUES ('1200769131153874946', '登录日志', '1072429679148908546', '成功', null, '101.245.13.86', '2019-11-30 13:30:45', '2019-11-30 13:30:45');
INSERT INTO `sys_login_log` VALUES ('1201050595086983170', '登录日志', '1072429679148908546', '成功', null, '221.192.178.15', '2019-12-01 08:09:11', '2019-12-01 08:09:11');
INSERT INTO `sys_login_log` VALUES ('1201050616377270274', '登录日志', '1072429679148908546', '成功', null, '221.192.178.15', '2019-12-01 08:09:16', '2019-12-01 08:09:16');
INSERT INTO `sys_login_log` VALUES ('1201050675600842753', '登录日志', '1072429679148908546', '成功', null, '221.192.178.15', '2019-12-01 08:09:30', '2019-12-01 08:09:30');
INSERT INTO `sys_login_log` VALUES ('1201062492934922242', '登录日志', '1072429679148908546', '成功', null, '49.233.209.71', '2019-12-01 08:56:28', '2019-12-01 08:56:28');
INSERT INTO `sys_login_log` VALUES ('1201315901915160577', '登录日志', '1072429679148908546', '成功', null, '180.167.180.19', '2019-12-02 01:43:25', '2019-12-02 01:43:25');
INSERT INTO `sys_login_log` VALUES ('1201385428455677953', '登录日志', '1072429679148908546', '成功', null, '61.158.152.66', '2019-12-02 06:19:41', '2019-12-02 06:19:41');
INSERT INTO `sys_login_log` VALUES ('1201394270656684034', '登录日志', '1072429679148908546', '成功', null, '124.74.24.194', '2019-12-02 06:54:50', '2019-12-02 06:54:50');
INSERT INTO `sys_login_log` VALUES ('1201401849109700610', '登录日志', '1', '成功', null, '121.204.51.228', '2019-12-02 07:24:56', '2019-12-02 07:24:56');
INSERT INTO `sys_login_log` VALUES ('1201412626864562177', '登录日志', '1', '成功', null, '121.204.51.228', '2019-12-02 08:07:46', '2019-12-02 08:07:46');
INSERT INTO `sys_login_log` VALUES ('1201707331020021761', '登录日志', '1', '成功', null, '121.204.51.228', '2019-12-03 03:38:49', '2019-12-03 03:38:49');
INSERT INTO `sys_login_log` VALUES ('1201749762449018881', '登录日志', '1', '成功', null, '27.156.25.12', '2019-12-03 06:27:25', '2019-12-03 06:27:25');
INSERT INTO `sys_login_log` VALUES ('1201771614932242434', '登录日志', '1072429679148908546', '成功', null, '118.194.246.217', '2019-12-03 07:54:15', '2019-12-03 07:54:15');
INSERT INTO `sys_login_log` VALUES ('1201851423750295553', '登录日志', '1', '成功', null, '112.49.235.113', '2019-12-03 13:11:23', '2019-12-03 13:11:23');
INSERT INTO `sys_login_log` VALUES ('1202070227503747074', '登录日志', '1072429679148908546', '成功', null, '61.183.245.254', '2019-12-04 03:40:50', '2019-12-04 03:40:50');
INSERT INTO `sys_login_log` VALUES ('1202115968846729217', '登录日志', '1072429679148908546', '成功', null, '61.186.156.218', '2019-12-04 06:42:36', '2019-12-04 06:42:36');
INSERT INTO `sys_login_log` VALUES ('1202134085203333122', '登录日志', '1072429679148908546', '成功', null, '222.74.217.153', '2019-12-04 07:54:35', '2019-12-04 07:54:35');
INSERT INTO `sys_login_log` VALUES ('1202137435256852481', '登录日志', '1', '成功', null, '27.156.25.12', '2019-12-04 08:07:54', '2019-12-04 08:07:54');
INSERT INTO `sys_login_log` VALUES ('1202211853437964289', '登录日志', '1072429679148908546', '成功', null, '183.63.51.77', '2019-12-04 13:03:37', '2019-12-04 13:03:37');
INSERT INTO `sys_login_log` VALUES ('1202414785273991169', '登录日志', '1', '成功', null, '27.156.27.64', '2019-12-05 02:29:59', '2019-12-05 02:29:59');
INSERT INTO `sys_login_log` VALUES ('1202480924314243073', '登录日志', '1072429679148908546', '成功', null, '180.161.75.46', '2019-12-05 06:52:48', '2019-12-05 06:52:48');
INSERT INTO `sys_login_log` VALUES ('1202488421183852545', '登录日志', '1072429679148908546', '成功', null, '182.200.126.96', '2019-12-05 07:22:35', '2019-12-05 07:22:35');
INSERT INTO `sys_login_log` VALUES ('1202521808007270402', '登录日志', '1', '成功', null, '27.156.27.64', '2019-12-05 09:35:15', '2019-12-05 09:35:15');
INSERT INTO `sys_login_log` VALUES ('1202583570580705281', '登录日志', '1072429679148908546', '成功', null, '1.207.114.168', '2019-12-05 13:40:41', '2019-12-05 13:40:41');
INSERT INTO `sys_login_log` VALUES ('1202757542098178050', '登录日志', '1072429679148908546', '成功', null, '180.161.75.46', '2019-12-06 01:11:59', '2019-12-06 01:11:59');
INSERT INTO `sys_login_log` VALUES ('1202759885749424130', '登录日志', '1072429679148908546', '成功', null, '222.221.150.17', '2019-12-06 01:21:18', '2019-12-06 01:21:18');
INSERT INTO `sys_login_log` VALUES ('1202766453324845057', '登录日志', '1072429679148908546', '成功', null, '113.232.205.247', '2019-12-06 01:47:23', '2019-12-06 01:47:23');
INSERT INTO `sys_login_log` VALUES ('1202913906187505665', '登录日志', '1072429679148908546', '成功', null, '61.149.215.18', '2019-12-06 11:33:19', '2019-12-06 11:33:19');
INSERT INTO `sys_login_log` VALUES ('1203566406930468866', '登录日志', '1072429679148908546', '成功', null, '49.65.145.16', '2019-12-08 06:46:07', '2019-12-08 06:46:07');
INSERT INTO `sys_login_log` VALUES ('1203924013021073409', '登录日志', '1072429679148908546', '成功', null, '220.178.168.108', '2019-12-09 06:27:07', '2019-12-09 06:27:07');
INSERT INTO `sys_login_log` VALUES ('1203927170375684097', '登录日志', '1072429679148908546', '成功', null, '118.186.228.195', '2019-12-09 06:39:40', '2019-12-09 06:39:40');
INSERT INTO `sys_login_log` VALUES ('1204051223430238209', '登录日志', '1072429679148908546', '成功', null, '115.60.192.88', '2019-12-09 14:52:37', '2019-12-09 14:52:37');
INSERT INTO `sys_login_log` VALUES ('1204059785422376961', '登录日志', '1072429679148908546', '成功', null, '115.60.192.88', '2019-12-09 15:26:38', '2019-12-09 15:26:38');
INSERT INTO `sys_login_log` VALUES ('1204060816684290050', '登录日志', '1072429679148908546', '成功', null, '115.60.192.88', '2019-12-09 15:30:44', '2019-12-09 15:30:44');
INSERT INTO `sys_login_log` VALUES ('1204256929513148418', '登录日志', '1072429679148908546', '成功', null, '223.71.230.230', '2019-12-10 04:30:01', '2019-12-10 04:30:01');
INSERT INTO `sys_login_log` VALUES ('1204368934517542913', '登录日志', '1072429679148908546', '成功', null, '61.183.234.146', '2019-12-10 11:55:05', '2019-12-10 11:55:05');
INSERT INTO `sys_login_log` VALUES ('1204377067558211586', '登录日志', '1072429679148908546', '成功', null, '61.183.234.146', '2019-12-10 12:27:24', '2019-12-10 12:27:24');
INSERT INTO `sys_login_log` VALUES ('1204558345515503617', '登录日志', '1072429679148908546', '成功', null, '58.243.254.6', '2019-12-11 00:27:44', '2019-12-11 00:27:44');
INSERT INTO `sys_login_log` VALUES ('1204653735183060994', '登录日志', '1072429679148908546', '成功', null, '61.170.223.116', '2019-12-11 06:46:47', '2019-12-11 06:46:47');
INSERT INTO `sys_login_log` VALUES ('1204672706791804930', '登录日志', '1072429679148908546', '成功', null, '58.210.77.166', '2019-12-11 08:02:10', '2019-12-11 08:02:10');
INSERT INTO `sys_login_log` VALUES ('1204959959984312322', '登录日志', '1072429679148908546', '成功', null, '220.160.65.24', '2019-12-12 03:03:36', '2019-12-12 03:03:36');
INSERT INTO `sys_login_log` VALUES ('1205028122558140418', '登录日志', '1072429679148908546', '成功', null, '113.88.113.241', '2019-12-12 07:34:27', '2019-12-12 07:34:27');
INSERT INTO `sys_login_log` VALUES ('1205032349347024897', '登录日志', '1072429679148908546', '成功', null, '113.88.113.241', '2019-12-12 07:51:15', '2019-12-12 07:51:15');
INSERT INTO `sys_login_log` VALUES ('1205063737446133762', '登录日志', '1', '成功', null, '220.160.65.24', '2019-12-12 17:55:59', '2019-12-12 17:55:59');
INSERT INTO `sys_login_log` VALUES ('1205067021468725249', '登录日志', '1072429679148908546', '成功', null, '1.207.109.46', '2019-12-12 18:09:02', '2019-12-12 18:09:02');
INSERT INTO `sys_login_log` VALUES ('1205302408019603458', '登录日志', '1072429679148908546', '成功', null, '182.150.24.237', '2019-12-13 09:44:22', '2019-12-13 09:44:22');
INSERT INTO `sys_login_log` VALUES ('1205302675272265730', '登录日志', '1072429679148908546', '成功', null, '39.168.138.108', '2019-12-13 09:45:26', '2019-12-13 09:45:26');
INSERT INTO `sys_login_log` VALUES ('1205306211917787138', '登录日志', '1072429679148908546', '成功', null, '220.160.65.24', '2019-12-13 09:59:29', '2019-12-13 09:59:29');
INSERT INTO `sys_login_log` VALUES ('1205313349855637505', '登录日志', '1072429679148908546', '成功', null, '220.160.65.24', '2019-12-13 10:27:51', '2019-12-13 10:27:51');
INSERT INTO `sys_login_log` VALUES ('1205328636516659201', '登录日志', '1072429679148908546', '成功', null, '211.99.28.76', '2019-12-13 11:28:36', '2019-12-13 11:28:36');
INSERT INTO `sys_login_log` VALUES ('1205395600274554882', '登录日志', '1072429679148908546', '成功', null, '58.221.148.162', '2019-12-13 15:54:41', '2019-12-13 15:54:41');
INSERT INTO `sys_login_log` VALUES ('1205657662921834498', '登录日志', '1072429679148908546', '成功', null, '119.123.177.81', '2019-12-14 09:16:02', '2019-12-14 09:16:02');
INSERT INTO `sys_login_log` VALUES ('1205676270301413377', '登录日志', '1072429679148908546', '成功', null, '119.123.177.81', '2019-12-14 10:29:58', '2019-12-14 10:29:58');
INSERT INTO `sys_login_log` VALUES ('1205679419523268609', '登录日志', '1072429679148908546', '成功', null, '119.123.177.81', '2019-12-14 10:42:29', '2019-12-14 10:42:29');
INSERT INTO `sys_login_log` VALUES ('1205796448834093058', '登录日志', '1072429679148908546', '成功', null, '59.32.193.130', '2019-12-14 18:27:31', '2019-12-14 18:27:31');
INSERT INTO `sys_login_log` VALUES ('1205796832306724865', '登录日志', '1072429679148908546', '成功', null, '59.32.193.130', '2019-12-14 18:29:02', '2019-12-14 18:29:02');
INSERT INTO `sys_login_log` VALUES ('1205797570403565569', '登录日志', '1072429679148908546', '成功', null, '59.32.193.130', '2019-12-14 18:31:58', '2019-12-14 18:31:58');
INSERT INTO `sys_login_log` VALUES ('1205797846070001666', '登录日志', '1205797789224599554', '成功', null, '59.32.193.130', '2019-12-14 18:33:04', '2019-12-14 18:33:04');
INSERT INTO `sys_login_log` VALUES ('1206257861402755073', '登录日志', '1072429679148908546', '成功', null, '183.193.146.47', '2019-12-16 01:01:00', '2019-12-16 01:01:00');
INSERT INTO `sys_login_log` VALUES ('1206260532784332801', '登录日志', '1072429679148908546', '成功', null, '183.193.146.47', '2019-12-16 01:11:37', '2019-12-16 01:11:37');
INSERT INTO `sys_login_log` VALUES ('1206456951524753409', '登录日志', '1072429679148908546', '成功', null, '59.32.193.130', '2019-12-16 14:12:07', '2019-12-16 14:12:07');
INSERT INTO `sys_login_log` VALUES ('1206465893936070658', '登录日志', '1072429679148908546', '成功', null, '1.204.233.136', '2019-12-16 14:47:39', '2019-12-16 14:47:39');
INSERT INTO `sys_login_log` VALUES ('1206472255202066433', '登录日志', '1072429679148908546', '成功', null, '183.67.16.91', '2019-12-16 15:12:55', '2019-12-16 15:12:55');
INSERT INTO `sys_login_log` VALUES ('1206474642612486145', '登录日志', '1072429679148908546', '成功', null, '1.204.233.136', '2019-12-16 15:22:25', '2019-12-16 15:22:25');
INSERT INTO `sys_login_log` VALUES ('1206476629940830209', '登录日志', '1072429679148908546', '成功', null, '222.210.137.31', '2019-12-16 15:30:18', '2019-12-16 15:30:18');
INSERT INTO `sys_login_log` VALUES ('1206481973278605313', '登录日志', '1072429679148908546', '成功', null, '125.122.210.182', '2019-12-16 15:51:32', '2019-12-16 15:51:32');
INSERT INTO `sys_login_log` VALUES ('1206484377977946113', '登录日志', '1072429679148908546', '成功', null, '113.116.22.125', '2019-12-16 16:01:06', '2019-12-16 16:01:06');
INSERT INTO `sys_login_log` VALUES ('1206484378481262594', '登录日志', '1072429679148908546', '成功', null, '113.116.22.125', '2019-12-16 16:01:06', '2019-12-16 16:01:06');
INSERT INTO `sys_login_log` VALUES ('1206486832912465922', '登录日志', '1072429679148908546', '成功', null, '120.85.128.58', '2019-12-16 16:10:51', '2019-12-16 16:10:51');
INSERT INTO `sys_login_log` VALUES ('1206489448702738434', '登录日志', '1072429679148908546', '成功', null, '125.116.208.155', '2019-12-16 16:21:15', '2019-12-16 16:21:15');
INSERT INTO `sys_login_log` VALUES ('1206492383629213698', '登录日志', '1072429679148908546', '成功', null, '120.85.128.58', '2019-12-16 16:32:54', '2019-12-16 16:32:54');
INSERT INTO `sys_login_log` VALUES ('1206518406047531010', '登录日志', '1072429679148908546', '成功', null, '182.150.24.237', '2019-12-16 18:16:19', '2019-12-16 18:16:19');
INSERT INTO `sys_login_log` VALUES ('1206565633470169090', '登录日志', '1072429679148908546', '成功', null, '120.85.128.58', '2019-12-16 21:23:59', '2019-12-16 21:23:59');
INSERT INTO `sys_login_log` VALUES ('1206755263905726465', '登录日志', '1072429679148908546', '成功', null, '111.29.208.200', '2019-12-17 09:57:30', '2019-12-17 09:57:30');
INSERT INTO `sys_login_log` VALUES ('1206920766951456769', '登录日志', '1072429679148908546', '成功', null, '120.85.128.58', '2019-12-17 20:55:09', '2019-12-17 20:55:09');
INSERT INTO `sys_login_log` VALUES ('1207143641272745985', '登录日志', '1072429679148908546', '成功', null, '116.21.231.186', '2019-12-18 11:40:46', '2019-12-18 11:40:46');
INSERT INTO `sys_login_log` VALUES ('1207200297587245057', '登录日志', '1', '成功', null, '220.160.3.197', '2019-12-18 15:25:54', '2019-12-18 15:25:54');
INSERT INTO `sys_login_log` VALUES ('1207541130266083329', '登录日志', '1072429679148908546', '成功', null, '222.185.250.190', '2019-12-19 14:00:15', '2019-12-19 14:00:15');
INSERT INTO `sys_login_log` VALUES ('1207556266590666753', '登录日志', '1072429679148908546', '成功', null, '222.185.250.190', '2019-12-19 15:00:24', '2019-12-19 15:00:24');
INSERT INTO `sys_login_log` VALUES ('1207887549262921729', '登录日志', '1072429679148908546', '成功', null, '119.39.43.27', '2019-12-20 12:56:48', '2019-12-20 12:56:48');
INSERT INTO `sys_login_log` VALUES ('1207889929928900609', '登录日志', '1072429679148908546', '成功', null, '119.39.43.27', '2019-12-20 13:06:15', '2019-12-20 13:06:15');
INSERT INTO `sys_login_log` VALUES ('1207924260068065282', '登录日志', '1072429679148908546', '成功', null, '123.185.76.156', '2019-12-20 15:22:40', '2019-12-20 15:22:40');
INSERT INTO `sys_login_log` VALUES ('1207946648478482434', '登录日志', '1072429679148908546', '成功', null, '118.122.107.244', '2019-12-20 16:51:38', '2019-12-20 16:51:38');
INSERT INTO `sys_login_log` VALUES ('1207964217092538369', '登录日志', '1', '成功', null, '220.160.3.197', '2019-12-20 18:01:27', '2019-12-20 18:01:27');
INSERT INTO `sys_login_log` VALUES ('1208016205113131009', '登录日志', '1072429679148908546', '成功', null, '222.174.155.24', '2019-12-20 21:28:02', '2019-12-20 21:28:02');
INSERT INTO `sys_login_log` VALUES ('1208176460509937665', '登录日志', '1072429679148908546', '成功', null, '1.204.232.153', '2019-12-21 08:04:50', '2019-12-21 08:04:50');
INSERT INTO `sys_login_log` VALUES ('1208287727807561729', '登录日志', '1072429679148908546', '成功', null, '59.32.193.130', '2019-12-21 15:26:58', '2019-12-21 15:26:58');
INSERT INTO `sys_login_log` VALUES ('1208295522963488769', '登录日志', '1072429679148908546', '成功', null, '59.32.193.130', '2019-12-21 15:57:56', '2019-12-21 15:57:56');
INSERT INTO `sys_login_log` VALUES ('1208337993433022465', '登录日志', '1072429679148908546', '成功', null, '115.171.7.22', '2019-12-21 18:46:42', '2019-12-21 18:46:42');
INSERT INTO `sys_login_log` VALUES ('1208633638811271170', '登录日志', '1072429679148908546', '成功', null, '180.191.103.126', '2019-12-22 14:21:29', '2019-12-22 14:21:29');
INSERT INTO `sys_login_log` VALUES ('1208653052201861122', '登录日志', '1072429679148908546', '成功', null, '117.136.77.124', '2019-12-22 15:38:38', '2019-12-22 15:38:38');
INSERT INTO `sys_login_log` VALUES ('1208663210223140866', '登录日志', '1072429679148908546', '成功', null, '183.66.53.234', '2019-12-22 16:19:00', '2019-12-22 16:19:00');
INSERT INTO `sys_login_log` VALUES ('1208669130588094466', '登录日志', '1072429679148908546', '成功', null, '120.85.128.58', '2019-12-22 16:42:31', '2019-12-22 16:42:31');
INSERT INTO `sys_login_log` VALUES ('1208723753336242177', '登录日志', '1072429679148908546', '成功', null, '120.85.128.58', '2019-12-22 20:19:34', '2019-12-22 20:19:34');
INSERT INTO `sys_login_log` VALUES ('1208725414897188866', '登录日志', '1072429679148908546', '成功', null, '120.85.128.58', '2019-12-22 20:26:11', '2019-12-22 20:26:11');
INSERT INTO `sys_login_log` VALUES ('1208788683594858497', '登录日志', '1072429679148908546', '成功', null, '223.88.1.91', '2019-12-23 00:37:35', '2019-12-23 00:37:35');
INSERT INTO `sys_login_log` VALUES ('1209020862824382466', '登录日志', '1072429679148908546', '成功', null, '122.4.80.18', '2019-12-23 16:00:11', '2019-12-23 16:00:11');
INSERT INTO `sys_login_log` VALUES ('1209022758838534146', '登录日志', '1072429679148908546', '成功', null, '123.185.76.156', '2019-12-23 16:07:43', '2019-12-23 16:07:43');
INSERT INTO `sys_login_log` VALUES ('1209047690477764610', '登录日志', '1072429679148908546', '成功', null, '183.14.29.90', '2019-12-23 17:46:47', '2019-12-23 17:46:47');
INSERT INTO `sys_login_log` VALUES ('1209100664788942850', '登录日志', '1072429679148908546', '成功', null, '120.239.158.211', '2019-12-23 21:17:17', '2019-12-23 21:17:17');
INSERT INTO `sys_login_log` VALUES ('1209101461874475009', '登录日志', '1209101358723956738', '成功', null, '120.239.158.211', '2019-12-23 21:20:27', '2019-12-23 21:20:27');
INSERT INTO `sys_login_log` VALUES ('1209101540266016769', '登录日志', '1072429679148908546', '成功', null, '120.239.158.211', '2019-12-23 21:20:46', '2019-12-23 21:20:46');
INSERT INTO `sys_login_log` VALUES ('1209324024944852993', '登录日志', '1072429679148908546', '成功', null, '1.207.112.205', '2019-12-24 12:04:50', '2019-12-24 12:04:50');
INSERT INTO `sys_login_log` VALUES ('1209382206559944705', '登录日志', '1072429679148908546', '成功', null, '219.146.83.91', '2019-12-24 15:56:02', '2019-12-24 15:56:02');
INSERT INTO `sys_login_log` VALUES ('1209387640192696322', '登录日志', '1072429679148908546', '成功', null, '218.58.50.94', '2019-12-24 16:17:37', '2019-12-24 16:17:37');
INSERT INTO `sys_login_log` VALUES ('1209390269606068226', '登录日志', '1072429679148908546', '成功', null, '182.150.24.237', '2019-12-24 16:28:04', '2019-12-24 16:28:04');
INSERT INTO `sys_login_log` VALUES ('1209403339850055682', '登录日志', '1072429679148908546', '成功', null, '124.64.18.53', '2019-12-24 17:20:01', '2019-12-24 17:20:01');
INSERT INTO `sys_login_log` VALUES ('1209403693060784130', '登录日志', '1072429679148908546', '成功', null, '124.64.16.105', '2019-12-24 17:21:25', '2019-12-24 17:21:25');
INSERT INTO `sys_login_log` VALUES ('1209485017339592705', '登录日志', '1072429679148908546', '成功', null, '27.37.82.8', '2019-12-24 22:44:34', '2019-12-24 22:44:34');
INSERT INTO `sys_login_log` VALUES ('1209492314203815938', '登录日志', '1072429679148908546', '成功', null, '223.88.1.68', '2019-12-24 23:13:34', '2019-12-24 23:13:34');
INSERT INTO `sys_login_log` VALUES ('1209651325637165058', '登录日志', '1072429679148908546', '成功', null, '219.146.191.132', '2019-12-25 09:45:25', '2019-12-25 09:45:25');
INSERT INTO `sys_login_log` VALUES ('1209651688821948417', '登录日志', '1072429679148908546', '成功', null, '219.146.191.132', '2019-12-25 09:46:52', '2019-12-25 09:46:52');
INSERT INTO `sys_login_log` VALUES ('1209662572260687874', '登录日志', '1072429679148908546', '成功', null, '223.87.163.94', '2019-12-25 10:30:06', '2019-12-25 10:30:06');
INSERT INTO `sys_login_log` VALUES ('1209709883233861634', '登录日志', '1072429679148908546', '成功', null, '223.100.176.126', '2019-12-25 13:38:06', '2019-12-25 13:38:06');
INSERT INTO `sys_login_log` VALUES ('1209809580208357377', '登录日志', '1072429679148908546', '成功', null, '117.166.85.24', '2019-12-25 20:14:16', '2019-12-25 20:14:16');
INSERT INTO `sys_login_log` VALUES ('1210039257233555457', '登录日志', '1072429679148908546', '成功', null, '125.69.43.249', '2019-12-26 11:26:55', '2019-12-26 11:26:55');
INSERT INTO `sys_login_log` VALUES ('1210060239012823041', '登录日志', '1072429679148908546', '成功', null, '183.160.123.104', '2019-12-26 12:50:17', '2019-12-26 12:50:17');
INSERT INTO `sys_login_log` VALUES ('1210061928860155906', '登录日志', '1072429679148908546', '成功', null, '183.160.123.104', '2019-12-26 12:57:00', '2019-12-26 12:57:00');
INSERT INTO `sys_login_log` VALUES ('1210072832813236226', '登录日志', '1072429679148908546', '成功', null, '123.185.76.156', '2019-12-26 13:40:20', '2019-12-26 13:40:20');
INSERT INTO `sys_login_log` VALUES ('1210185721360351233', '登录日志', '1072429679148908546', '成功', null, '113.105.128.74', '2019-12-26 21:08:55', '2019-12-26 21:08:55');
INSERT INTO `sys_login_log` VALUES ('1210439117984169985', '登录日志', '1072429679148908546', '成功', null, '219.146.83.91', '2019-12-27 13:55:49', '2019-12-27 13:55:49');
INSERT INTO `sys_login_log` VALUES ('1210468593073848322', '登录日志', '1072429679148908546', '成功', null, '115.60.17.106', '2019-12-27 15:52:57', '2019-12-27 15:52:57');
INSERT INTO `sys_login_log` VALUES ('1210903938034761729', '登录日志', '1072429679148908546', '成功', null, '36.97.185.150', '2019-12-28 20:42:51', '2019-12-28 20:42:51');
INSERT INTO `sys_login_log` VALUES ('1210904285633511425', '登录日志', '1072429679148908546', '成功', null, '42.249.57.201', '2019-12-28 20:44:14', '2019-12-28 20:44:14');
INSERT INTO `sys_login_log` VALUES ('1210904782058749953', '登录日志', '1072429679148908546', '成功', null, '42.249.57.201', '2019-12-28 20:46:12', '2019-12-28 20:46:12');
INSERT INTO `sys_login_log` VALUES ('1211249637549080577', '登录日志', '1072429679148908546', '成功', null, '112.81.233.254', '2019-12-29 19:36:32', '2019-12-29 19:36:32');
INSERT INTO `sys_login_log` VALUES ('1211471658887348225', '登录日志', '1072429679148908546', '成功', null, '220.160.82.225', '2019-12-30 10:18:46', '2019-12-30 10:18:46');
INSERT INTO `sys_login_log` VALUES ('1211483086071463937', '登录日志', '1072429679148908546', '成功', null, '27.154.74.19', '2019-12-30 11:04:11', '2019-12-30 11:04:11');
INSERT INTO `sys_login_log` VALUES ('1211571065402097666', '登录日志', '1072429679148908546', '成功', null, '110.152.93.10', '2019-12-30 16:53:47', '2019-12-30 16:53:47');
INSERT INTO `sys_login_log` VALUES ('1211590690470821890', '登录日志', '1072429679148908546', '成功', null, '110.178.219.124', '2019-12-30 18:11:46', '2019-12-30 18:11:46');
INSERT INTO `sys_login_log` VALUES ('1211844602067091458', '登录日志', '1072429679148908546', '成功', null, '42.88.98.233', '2019-12-31 11:00:43', '2019-12-31 11:00:43');
INSERT INTO `sys_login_log` VALUES ('1211906852530356225', '登录日志', '1072429679148908546', '成功', null, '222.82.84.142', '2019-12-31 15:08:04', '2019-12-31 15:08:04');
INSERT INTO `sys_login_log` VALUES ('1211910397908418561', '登录日志', '1072429679148908546', '成功', null, '222.82.84.142', '2019-12-31 15:22:10', '2019-12-31 15:22:10');
INSERT INTO `sys_login_log` VALUES ('1212605376574193665', '登录日志', '1072429679148908546', '成功', null, '117.168.88.109', '2020-01-02 13:23:46', '2020-01-02 13:23:46');
INSERT INTO `sys_login_log` VALUES ('1212928918972989441', '登录日志', '1072429679148908546', '成功', null, '218.75.137.126', '2020-01-03 10:49:24', '2020-01-03 10:49:24');
INSERT INTO `sys_login_log` VALUES ('1212940559148056578', '登录日志', '1072429679148908546', '成功', null, '123.114.99.33', '2020-01-03 11:35:39', '2020-01-03 11:35:39');
INSERT INTO `sys_login_log` VALUES ('1213022824502853634', '登录日志', '1072429679148908546', '成功', null, '180.165.239.54', '2020-01-03 17:02:33', '2020-01-03 17:02:33');
INSERT INTO `sys_login_log` VALUES ('1213250609553014786', '登录日志', '1072429679148908546', '成功', null, '112.229.125.102', '2020-01-04 08:07:41', '2020-01-04 08:07:41');
INSERT INTO `sys_login_log` VALUES ('1213255823869804545', '登录日志', '1072429679148908546', '成功', null, '39.82.174.116', '2020-01-04 08:28:24', '2020-01-04 08:28:24');
INSERT INTO `sys_login_log` VALUES ('1213288872724594690', '登录日志', '1072429679148908546', '成功', null, '106.18.90.8', '2020-01-04 10:39:44', '2020-01-04 10:39:44');
INSERT INTO `sys_login_log` VALUES ('1213308829491462146', '登录日志', '1072429679148908546', '成功', null, '202.116.155.102', '2020-01-04 11:59:02', '2020-01-04 11:59:02');
INSERT INTO `sys_login_log` VALUES ('1213488215771512833', '登录日志', '1072429679148908546', '成功', null, '61.149.148.203', '2020-01-04 23:51:51', '2020-01-04 23:51:51');
INSERT INTO `sys_login_log` VALUES ('1213758321978179586', '登录日志', '1072429679148908546', '成功', null, '101.86.77.210', '2020-01-05 17:45:09', '2020-01-05 17:45:09');
INSERT INTO `sys_login_log` VALUES ('1213951367311196162', '登录日志', '1072429679148908546', '成功', null, '39.82.174.116', '2020-01-06 06:32:15', '2020-01-06 06:32:15');
INSERT INTO `sys_login_log` VALUES ('1213988209804902402', '登录日志', '1072429679148908546', '成功', null, '60.208.83.253', '2020-01-06 08:58:39', '2020-01-06 08:58:39');
INSERT INTO `sys_login_log` VALUES ('1214145259017011202', '登录日志', '1072429679148908546', '成功', null, '122.224.178.178', '2020-01-06 19:22:42', '2020-01-06 19:22:42');
INSERT INTO `sys_login_log` VALUES ('1214165500254158849', '登录日志', '1072429679148908546', '成功', null, '112.232.104.116', '2020-01-06 20:43:08', '2020-01-06 20:43:08');
INSERT INTO `sys_login_log` VALUES ('1214445642285252610', '登录日志', '1072429679148908546', '成功', null, '27.156.27.244', '2020-01-07 15:16:19', '2020-01-07 15:16:19');
INSERT INTO `sys_login_log` VALUES ('1214463508212547585', '登录日志', '1072429679148908546', '成功', null, '42.94.203.216', '2020-01-07 16:27:19', '2020-01-07 16:27:19');
INSERT INTO `sys_login_log` VALUES ('1214553306935787522', '登录日志', '1072429679148908546', '成功', null, '60.27.105.10', '2020-01-07 22:24:08', '2020-01-07 22:24:08');
INSERT INTO `sys_login_log` VALUES ('1214557447456227330', '登录日志', '1072429679148908546', '成功', null, '39.82.174.116', '2020-01-07 22:40:36', '2020-01-07 22:40:36');
INSERT INTO `sys_login_log` VALUES ('1214558966276620290', '登录日志', '1072429679148908546', '成功', null, '39.82.174.116', '2020-01-07 22:46:38', '2020-01-07 22:46:38');
INSERT INTO `sys_login_log` VALUES ('1214722740728037378', '登录日志', '1072429679148908546', '成功', null, '58.212.250.61', '2020-01-08 09:37:25', '2020-01-08 09:37:25');
INSERT INTO `sys_login_log` VALUES ('1214722842049839106', '登录日志', '1072429679148908546', '成功', null, '58.212.250.61', '2020-01-08 09:37:49', '2020-01-08 09:37:49');
INSERT INTO `sys_login_log` VALUES ('1214725585745412098', '登录日志', '1072429679148908546', '成功', null, '114.246.212.116', '2020-01-08 09:48:43', '2020-01-08 09:48:43');
INSERT INTO `sys_login_log` VALUES ('1214727670960422913', '登录日志', '1072429679148908546', '成功', null, '113.12.75.18', '2020-01-08 09:57:00', '2020-01-08 09:57:00');
INSERT INTO `sys_login_log` VALUES ('1214728184527781890', '登录日志', '1072429679148908546', '成功', null, '106.47.239.20', '2020-01-08 09:59:02', '2020-01-08 09:59:02');
INSERT INTO `sys_login_log` VALUES ('1214732397232193537', '登录日志', '1072429679148908546', '成功', null, '58.212.250.61', '2020-01-08 10:15:47', '2020-01-08 10:15:47');
INSERT INTO `sys_login_log` VALUES ('1214732999257423873', '登录日志', '1072429679148908546', '成功', null, '124.74.24.194', '2020-01-08 10:18:10', '2020-01-08 10:18:10');
INSERT INTO `sys_login_log` VALUES ('1214735039148785666', '登录日志', '1072429679148908546', '成功', null, '124.160.66.138', '2020-01-08 10:26:17', '2020-01-08 10:26:17');
INSERT INTO `sys_login_log` VALUES ('1214792001546813442', '登录日志', '1072429679148908546', '成功', null, '59.42.26.198', '2020-01-08 14:12:38', '2020-01-08 14:12:38');
INSERT INTO `sys_login_log` VALUES ('1214793498489716738', '登录日志', '1072429679148908546', '成功', null, '59.42.26.198', '2020-01-08 14:18:34', '2020-01-08 14:18:34');
INSERT INTO `sys_login_log` VALUES ('1215143268248911874', '登录日志', '1072429679148908546', '成功', null, '60.173.247.22', '2020-01-09 13:28:26', '2020-01-09 13:28:26');
INSERT INTO `sys_login_log` VALUES ('1215156187174993921', '登录日志', '1', '成功', null, '220.160.82.213', '2020-01-09 14:19:46', '2020-01-09 14:19:46');
INSERT INTO `sys_login_log` VALUES ('1215162725067325441', '登录日志', '1072429679148908546', '成功', null, '112.4.42.213', '2020-01-09 14:45:45', '2020-01-09 14:45:45');
INSERT INTO `sys_login_log` VALUES ('1215187618118959105', '登录日志', '1072429679148908546', '成功', null, '110.87.81.17', '2020-01-09 16:24:40', '2020-01-09 16:24:40');
INSERT INTO `sys_login_log` VALUES ('1215271873285128194', '登录日志', '1072429679148908546', '成功', null, '223.88.10.238', '2020-01-09 21:59:28', '2020-01-09 21:59:28');
INSERT INTO `sys_login_log` VALUES ('1215471520712687618', '登录日志', '1072429679148908546', '成功', null, '49.77.196.84', '2020-01-10 11:12:48', '2020-01-10 11:12:48');
INSERT INTO `sys_login_log` VALUES ('1216732096822706177', '登录日志', '1072429679148908546', '成功', null, '121.31.246.70', '2020-01-13 22:41:52', '2020-01-13 22:41:52');
INSERT INTO `sys_login_log` VALUES ('1216732747740938241', '登录日志', '1072429679148908546', '成功', null, '121.31.246.70', '2020-01-13 22:44:28', '2020-01-13 22:44:28');
INSERT INTO `sys_login_log` VALUES ('1216911017857974273', '登录日志', '1072429679148908546', '成功', null, '61.131.19.218', '2020-01-14 10:32:50', '2020-01-14 10:32:50');
INSERT INTO `sys_login_log` VALUES ('1216923649772027905', '登录日志', '1072429679148908546', '成功', null, '171.217.95.14', '2020-01-14 11:23:02', '2020-01-14 11:23:02');
INSERT INTO `sys_login_log` VALUES ('1217261795114156034', '登录日志', '1072429679148908546', '成功', null, '59.42.27.69', '2020-01-15 09:46:42', '2020-01-15 09:46:42');
INSERT INTO `sys_login_log` VALUES ('1217264679935475713', '登录日志', '1072429679148908546', '成功', null, '59.42.27.69', '2020-01-15 09:58:10', '2020-01-15 09:58:10');
INSERT INTO `sys_login_log` VALUES ('1217266602373742593', '登录日志', '1217266391450583041', '成功', null, '59.42.26.18', '2020-01-15 10:05:48', '2020-01-15 10:05:48');
INSERT INTO `sys_login_log` VALUES ('1217285776261185538', '登录日志', '1072429679148908546', '成功', null, '60.208.83.253', '2020-01-15 11:22:00', '2020-01-15 11:22:00');
INSERT INTO `sys_login_log` VALUES ('1217361950784577537', '登录日志', '1', '成功', null, '127.0.0.1', '2020-01-15 16:24:41', '2020-01-15 16:24:41');
INSERT INTO `sys_login_log` VALUES ('1217365092330835969', '登录日志', '1', '成功', null, '127.0.0.1', '2020-01-15 16:37:10', '2020-01-15 16:37:10');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统首页', '/dashboard', 'sys:dashboard:info', '1', 'el-icon-star-on', '0', '1', '1', '2018-12-06 14:54:07', '2019-07-24 03:18:12', '0');
INSERT INTO `sys_menu` VALUES ('15', '0', '系统管理', '/sys', null, '0', 'el-icon-setting', '1', '1', '1', '2018-12-10 17:52:34', '2019-07-24 03:18:23', '0');
INSERT INTO `sys_menu` VALUES ('16', '15', '用户管理', '/sysuser', '', '1', '', '16', '1', '1', '2018-12-10 18:28:59', '2019-11-18 09:23:48', '0');
INSERT INTO `sys_menu` VALUES ('17', '15', '角色管理', '/sysrole', '', '1', '', '17', '1', '1', '2018-12-10 18:35:34', '2019-11-18 09:42:44', '0');
INSERT INTO `sys_menu` VALUES ('18', '15', '菜单管理', '/sysmenu', '', '1', '', '18', '1', '1', '2018-12-10 18:36:16', '2019-11-18 10:07:28', '0');
INSERT INTO `sys_menu` VALUES ('19', '15', '部门管理', '/sysdept', '', '1', '', '19', '1', '1', '2018-12-10 18:37:20', '2019-11-18 10:10:38', '0');
INSERT INTO `sys_menu` VALUES ('21', '15', '定时任务', '/schedulejob', '', '1', '', '21', '1', '1', '2018-12-10 18:39:14', '2019-11-18 10:10:24', '0');
INSERT INTO `sys_menu` VALUES ('1080412372541181953', '16', '新增', '/sysuser/add', 'sys:user:add', '2', null, '1', null, null, '2019-01-02 10:35:57', '2019-05-31 09:38:25', '0');
INSERT INTO `sys_menu` VALUES ('1080413095496585218', '16', '删除', '/sysuser/del', 'sys:user:del', '2', null, '2', null, null, '2019-01-02 10:38:50', '2019-04-15 07:21:36', '0');
INSERT INTO `sys_menu` VALUES ('1080636440926232577', '16', '重置密码', '/sysuser/reset/password', 'sys:user:resetPassword', '2', null, '3', null, null, '2019-01-03 01:26:19', '2019-04-15 07:21:46', '0');
INSERT INTO `sys_menu` VALUES ('1080636634896015362', '16', '修改密码', '/sysuser/change/password', 'sys:user:changePassword', '2', null, '4', null, null, '2019-01-03 01:27:06', '2019-04-15 07:21:52', '0');
INSERT INTO `sys_menu` VALUES ('1080637598663188481', '17', '新增', '/sysrole/add', 'sys:role:add', '2', null, '1', null, null, '2019-01-03 01:30:55', '2019-05-31 09:51:21', '0');
INSERT INTO `sys_menu` VALUES ('1080637823859564545', '17', '删除', '/sysrole/del', 'sys:role:del', '2', null, '2', null, null, '2019-01-03 01:31:49', '2019-04-15 07:22:21', '0');
INSERT INTO `sys_menu` VALUES ('1080638043456544769', '17', '权限配置', '/sysrole/configPerm', 'sys:role:configPerm', '2', null, '4', null, null, '2019-01-03 01:32:42', '2019-05-31 09:52:38', '0');
INSERT INTO `sys_menu` VALUES ('1080639293405274114', '18', '新增', '/sysmenu/add', 'sys:menu:add', '2', null, '1', null, null, '2019-01-03 01:37:40', '2019-05-31 09:55:33', '0');
INSERT INTO `sys_menu` VALUES ('1080639432148656130', '18', '删除', '/sysmenu/del', 'sys:menu:del', '2', null, '2', null, null, '2019-01-03 01:38:13', '2019-05-31 10:00:16', '0');
INSERT INTO `sys_menu` VALUES ('1080639863587348482', '19', '新增', '/sysdept/add', 'sys:dept:add', '2', null, '1', null, null, '2019-01-03 01:39:55', '2019-05-31 10:10:19', '0');
INSERT INTO `sys_menu` VALUES ('1080640119691550722', '19', '删除', '/sysdept/del', 'sys:dept:del', '2', null, '2', null, null, '2019-01-03 01:40:57', '2019-04-15 07:23:07', '0');
INSERT INTO `sys_menu` VALUES ('1080640228772814849', '19', '修改', '/sysdept/edit', 'sys:dept:edit', '2', null, '3', null, null, '2019-01-03 01:41:23', '2019-05-31 10:09:12', '0');
INSERT INTO `sys_menu` VALUES ('1080640763785650177', '21', '新增', '/schedule/add', 'sys:schedule:add', '2', null, '1', null, null, '2019-01-03 01:43:30', '2019-05-31 09:07:38', '0');
INSERT INTO `sys_menu` VALUES ('1080640900176027650', '21', '修改', '/schedule/edit', 'sys:schedule:edit', '2', null, '2', null, null, '2019-01-03 01:44:03', '2019-05-31 09:10:53', '0');
INSERT INTO `sys_menu` VALUES ('1080641066287243266', '21', '删除', '/schedule/del', 'sys:schedule:del', '2', null, '3', null, null, '2019-01-03 01:44:42', '2019-04-15 07:23:44', '0');
INSERT INTO `sys_menu` VALUES ('1116603445676101634', '15', '登陆日志', '/loginlog', '', '1', '', '22', null, null, '2019-04-12 07:26:22', '2019-11-18 10:11:27', '0');
INSERT INTO `sys_menu` VALUES ('1116613754159702018', '15', '业务日志', '/operationlog', '', '1', '', '23', null, null, '2019-04-12 08:07:19', '2019-11-18 10:13:03', '0');
INSERT INTO `sys_menu` VALUES ('1116961835054452737', '15', '字典管理', '/sysdict', '', '1', '', '24', null, null, '2019-04-13 07:10:28', '2019-11-18 10:13:13', '0');
INSERT INTO `sys_menu` VALUES ('1117688867416195074', '1116961835054452737', '新增', '/sysdict/add', 'sys:dict:add', '2', null, '1', null, null, '2019-04-15 07:19:26', '2019-05-31 10:15:40', '0');
INSERT INTO `sys_menu` VALUES ('1117689069371932674', '1116961835054452737', '删除', '/sysdict/delete', 'sys:dict:del', '2', null, '2', null, null, '2019-04-15 07:20:14', '2019-04-15 07:24:16', '0');
INSERT INTO `sys_menu` VALUES ('1117690423326818305', '1116603445676101634', '清空日志', '/sysloginLog/clear', 'sys:loginLog:clear', '2', null, '1', null, null, '2019-04-15 07:25:37', '2019-04-15 07:25:37', '0');
INSERT INTO `sys_menu` VALUES ('1117690805209808898', '1116613754159702018', '清空日志', '/sysoperationLog/clear', 'sys:operationLog:clear', '2', null, '2', null, null, '2019-04-15 07:27:08', '2019-04-15 07:27:08', '0');
INSERT INTO `sys_menu` VALUES ('1134386963948847105', '21', '运行一次', '/schedule/run', 'sys:schedule:run', '2', null, '4', null, null, '2019-05-31 09:11:43', '2019-05-31 09:11:43', '0');
INSERT INTO `sys_menu` VALUES ('1134387182992179202', '21', '停止', '/schedule/pause', 'sys:schedule:pause', '2', null, '5', null, null, '2019-05-31 09:12:35', '2019-05-31 09:12:35', '0');
INSERT INTO `sys_menu` VALUES ('1134387322570227714', '21', '恢复', '/schedule/resume', 'sys:schedule:resume', '2', null, '6', null, null, '2019-05-31 09:13:08', '2019-05-31 09:13:08', '0');
INSERT INTO `sys_menu` VALUES ('1134393918364925954', '16', '修改', '/sysuser/edit', 'sys:user:edit', '2', null, '3', null, null, '2019-05-31 09:39:21', '2019-05-31 09:39:21', '0');
INSERT INTO `sys_menu` VALUES ('1134397077300789249', '17', '编辑', '/sysrole/edit', 'sys:role:edit', '2', null, '3', null, null, '2019-05-31 09:51:54', '2019-05-31 09:51:54', '0');
INSERT INTO `sys_menu` VALUES ('1134398704145481730', '18', '修改', '/sysmenu/edit', 'sys:menu:edit', '2', null, '3', null, null, '2019-05-31 09:58:22', '2019-05-31 09:58:22', '0');
INSERT INTO `sys_menu` VALUES ('1134403202943369217', '1116961835054452737', '修改', '/sysdict/edit', 'sys:dict:edit', '2', null, '3', null, null, '2019-05-31 10:16:14', '2019-05-31 10:16:14', '0');
INSERT INTO `sys_menu` VALUES ('1134405857598062594', '16', '清除缓存', '/sysuser/clearCache', 'sys:user:clearCache', '2', null, '6', null, null, '2019-05-31 10:26:47', '2019-05-31 10:26:47', '0');
INSERT INTO `sys_menu` VALUES ('1135836344682065921', '0', '博客管理', '/blog', null, '0', 'el-icon-menu', '2', null, null, '2019-06-04 09:11:02', '2019-08-13 07:51:12', '0');
INSERT INTO `sys_menu` VALUES ('1135837162231607298', '1135836344682065921', '文章管理', '/article', '', '1', 'el-icon-notebook-1', '1', null, null, '2019-06-04 09:14:17', '2019-11-18 10:16:28', '0');
INSERT INTO `sys_menu` VALUES ('1161477550438887426', '1135836344682065921', '文章分类', '/article/category', '', '1', 'el-icon-s-flag', '2', null, null, '2019-08-14 03:20:02', '2019-11-18 10:17:14', '0');
INSERT INTO `sys_menu` VALUES ('1184031594491449345', '17', '导出', '/sys/role/export', 'sys:role:export', '2', 'el-icon-download', '10', null, null, '2019-10-15 09:01:45', '2019-10-15 09:01:45', '0');
INSERT INTO `sys_menu` VALUES ('1184035370040332290', '1161477550438887426', '新增/修改', '/blog/articlecategory/save', 'blog:category:save', '2', '', '1', null, null, '2019-10-15 09:16:45', '2019-10-15 09:18:35', '0');
INSERT INTO `sys_menu` VALUES ('1184036170217066497', '1161477550438887426', '删除', '/blog/articlecategory/delete', 'blog:category:del', '2', null, '2', null, null, '2019-10-15 09:19:56', '2019-10-15 09:19:56', '0');
INSERT INTO `sys_menu` VALUES ('1184036703585734658', '1135837162231607298', '新增/修改', '/blog/article/save', 'blog:article:save', '2', null, '1', null, null, '2019-10-15 09:22:03', '2019-10-15 09:22:03', '0');
INSERT INTO `sys_menu` VALUES ('1184037199029506049', '1135837162231607298', '删除', '/blog/article/delete', 'blog:article:del', '2', null, '2', null, null, '2019-10-15 09:24:01', '2019-10-15 09:24:01', '0');
INSERT INTO `sys_menu` VALUES ('1196362934139731969', '16', '查看', '/sysuser/list', 'sys:user:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 09:42:06', '2019-11-18 09:42:06', '0');
INSERT INTO `sys_menu` VALUES ('1196363233176829953', '17', '查看', '/sysrole/list', 'sys:role:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 09:43:17', '2019-11-18 09:43:17', '0');
INSERT INTO `sys_menu` VALUES ('1196369268801257473', '18', '查看', '/sysmenu/list', 'sys:menu:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 10:07:16', '2019-11-18 10:07:16', '0');
INSERT INTO `sys_menu` VALUES ('1196369490004656130', '19', '查看', '/sysdept/list', 'sys:dept:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 10:08:09', '2019-11-18 10:08:09', '0');
INSERT INTO `sys_menu` VALUES ('1196370026464526337', '21', '查看', '/schedulejob/list', 'sys:schedule:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 10:10:16', '2019-11-18 10:10:16', '0');
INSERT INTO `sys_menu` VALUES ('1196370286398128130', '1116603445676101634', '查看', '/loginLog/list', 'sys:loginLog:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 10:11:18', '2019-11-18 10:11:18', '0');
INSERT INTO `sys_menu` VALUES ('1196370687096766465', '1116613754159702018', '查看', '/operationLog/list', 'sys:operationLog:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 10:12:54', '2019-11-18 10:12:54', '0');
INSERT INTO `sys_menu` VALUES ('1196370945335869442', '1116961835054452737', '查看', '/sysdict/list', 'sys:dict:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 10:13:56', '2019-11-18 10:13:56', '0');
INSERT INTO `sys_menu` VALUES ('1196371477316222978', '1135837162231607298', '查看', '/article/list', 'blog:article:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 10:16:02', '2019-11-18 10:16:20', '0');
INSERT INTO `sys_menu` VALUES ('1196371743612583938', '1161477550438887426', '查看', '/blog/category/list', 'blog:category:list', '2', 'el-icon-view', '0', null, null, '2019-11-18 10:17:06', '2019-11-18 10:17:06', '0');
INSERT INTO `sys_menu` VALUES ('1201402823777869825', '1135837162231607298', '详情', '/blog/article/info', 'blog:article:info', '2', null, '5', null, null, '2019-12-02 07:28:49', '2019-12-02 07:28:49', '0');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_type` varchar(32) DEFAULT NULL COMMENT '日志类型',
  `log_name` varchar(64) DEFAULT NULL COMMENT '日志名称',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `class_name` varchar(255) DEFAULT NULL COMMENT '类名称',
  `method` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `succeed` varchar(32) DEFAULT NULL COMMENT '是否成功',
  `message` text COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1215272013072891906 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES ('1156013778021535745', '业务日志', '清空日志', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.OperationLogController', 'clear', '成功', '', '2019-07-30 01:28:57', '2019-07-30 01:28:57');
INSERT INTO `sys_operation_log` VALUES ('1161891593354907650', '业务日志', '清空日志', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.LoginLogController', 'clear', '成功', '', '2019-08-15 06:45:17', '2019-08-15 06:45:17');
INSERT INTO `sys_operation_log` VALUES ('1185137886076284930', '业务日志', '保存角色', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.RoleController', 'save', '成功', '{\"createTime\":1571393865477,\"currentPage\":0,\"id\":\"1185137885996593153\",\"pageSize\":10,\"remark\":\"22\",\"roleName\":\"11\",\"updateTime\":1571393865477}', '2019-10-18 10:17:45', '2019-10-18 10:17:45');
INSERT INTO `sys_operation_log` VALUES ('1186575791801970690', '业务日志', '保存角色', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.RoleController', 'save', '成功', '{\"createTime\":1545536715000,\"currentPage\":0,\"id\":\"1076685137679704065\",\"pageSize\":10,\"remark\":\"游客\",\"roleName\":\"游客\",\"updateTime\":1571736688916}', '2019-10-22 09:31:29', '2019-10-22 09:31:29');
INSERT INTO `sys_operation_log` VALUES ('1189880691248017409', '业务日志', '保存角色', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.RoleController', 'save', '成功', '{\"createTime\":1545536715000,\"currentPage\":0,\"id\":\"1076685137679704065\",\"pageSize\":10,\"remark\":\"游客\",\"roleName\":\"游客\",\"updateTime\":1572524638341}', '2019-10-31 12:23:58', '2019-10-31 12:23:58');
INSERT INTO `sys_operation_log` VALUES ('1189880720197103617', '业务日志', '保存角色', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.RoleController', 'save', '成功', '{\"createTime\":1545536715000,\"currentPage\":0,\"id\":\"1076685137679704065\",\"pageSize\":10,\"remark\":\"游客1\",\"roleName\":\"游客\",\"updateTime\":1572524645253}', '2019-10-31 12:24:05', '2019-10-31 12:24:05');
INSERT INTO `sys_operation_log` VALUES ('1199141194491125761', '业务日志', '保存角色', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.RoleController', 'save', '成功', '{\"createTime\":1574732514370,\"currentPage\":0,\"id\":\"1199141194411433985\",\"pageSize\":10,\"remark\":\"1\",\"roleName\":\"测试\",\"updateTime\":1574732514371}', '2019-11-26 01:41:54', '2019-11-26 01:41:54');
INSERT INTO `sys_operation_log` VALUES ('1213250750729093121', '业务日志', '保存角色', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.RoleController', 'save', '成功', '{\"createTime\":1578096494694,\"currentPage\":0,\"id\":\"1213250750527766529\",\"pageSize\":10,\"remark\":\"QWEQWE\",\"roleName\":\"QWEQ\",\"updateTime\":1578096494694}', '2020-01-04 08:08:15', '2020-01-04 08:08:15');
INSERT INTO `sys_operation_log` VALUES ('1215272013072891905', '业务日志', '保存角色', '1072429679148908546', 'com.hsshy.beam.modular.sys.controller.RoleController', 'save', '成功', '{\"createTime\":1578578401218,\"currentPage\":0,\"id\":\"1215272013001588738\",\"pageSize\":10,\"roleName\":\"qwe\",\"updateTime\":1578578401218}', '2020-01-09 22:00:01', '2020-01-09 22:00:01');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', '超级管理员', '2018-10-10 22:39:38', '2019-07-23 05:50:42');
INSERT INTO `sys_role` VALUES ('1076685137679704065', '游客', '游客1', '2018-12-23 03:45:15', '2019-10-31 12:24:05');
INSERT INTO `sys_role` VALUES ('1199141194411433985', '测试', '1', '2019-11-26 01:41:54', '2019-11-26 01:41:54');
INSERT INTO `sys_role` VALUES ('1213250750527766529', 'QWEQ', 'QWEQWE', '2020-01-04 08:08:15', '2020-01-04 08:08:15');
INSERT INTO `sys_role` VALUES ('1215272013001588738', 'qwe', null, '2020-01-09 22:00:01', '2020-01-09 22:00:01');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1120 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1093', '1076685137679704065', '1');
INSERT INTO `sys_role_menu` VALUES ('1094', '1076685137679704065', '1196362934139731969');
INSERT INTO `sys_role_menu` VALUES ('1095', '1076685137679704065', '1080412372541181953');
INSERT INTO `sys_role_menu` VALUES ('1096', '1076685137679704065', '1196363233176829953');
INSERT INTO `sys_role_menu` VALUES ('1097', '1076685137679704065', '1080637598663188481');
INSERT INTO `sys_role_menu` VALUES ('1098', '1076685137679704065', '1184031594491449345');
INSERT INTO `sys_role_menu` VALUES ('1099', '1076685137679704065', '1196369268801257473');
INSERT INTO `sys_role_menu` VALUES ('1100', '1076685137679704065', '1196369490004656130');
INSERT INTO `sys_role_menu` VALUES ('1101', '1076685137679704065', '1080639863587348482');
INSERT INTO `sys_role_menu` VALUES ('1102', '1076685137679704065', '1196370026464526337');
INSERT INTO `sys_role_menu` VALUES ('1103', '1076685137679704065', '1196370286398128130');
INSERT INTO `sys_role_menu` VALUES ('1104', '1076685137679704065', '1196370687096766465');
INSERT INTO `sys_role_menu` VALUES ('1105', '1076685137679704065', '1196370945335869442');
INSERT INTO `sys_role_menu` VALUES ('1106', '1076685137679704065', '1196371477316222978');
INSERT INTO `sys_role_menu` VALUES ('1107', '1076685137679704065', '1196371743612583938');
INSERT INTO `sys_role_menu` VALUES ('1108', '1076685137679704065', '15');
INSERT INTO `sys_role_menu` VALUES ('1109', '1076685137679704065', '16');
INSERT INTO `sys_role_menu` VALUES ('1110', '1076685137679704065', '17');
INSERT INTO `sys_role_menu` VALUES ('1111', '1076685137679704065', '18');
INSERT INTO `sys_role_menu` VALUES ('1112', '1076685137679704065', '19');
INSERT INTO `sys_role_menu` VALUES ('1113', '1076685137679704065', '21');
INSERT INTO `sys_role_menu` VALUES ('1114', '1076685137679704065', '1116603445676101634');
INSERT INTO `sys_role_menu` VALUES ('1115', '1076685137679704065', '1116613754159702018');
INSERT INTO `sys_role_menu` VALUES ('1116', '1076685137679704065', '1116961835054452737');
INSERT INTO `sys_role_menu` VALUES ('1117', '1076685137679704065', '1135836344682065921');
INSERT INTO `sys_role_menu` VALUES ('1118', '1076685137679704065', '1135837162231607298');
INSERT INTO `sys_role_menu` VALUES ('1119', '1076685137679704065', '1161477550438887426');

-- ----------------------------
-- Table structure for sys_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_schedule_job`;
CREATE TABLE `sys_schedule_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint(4) DEFAULT NULL COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1080345897063223298 DEFAULT CHARSET=utf8 COMMENT='定时任务';

-- ----------------------------
-- Records of sys_schedule_job
-- ----------------------------
INSERT INTO `sys_schedule_job` VALUES ('1080345897063223297', 'testTask', 'test2', null, '0 * * * * ?', '0', '测试', '2019-01-02 06:11:48', '2019-07-30 01:28:47');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `account` varchar(45) DEFAULT NULL COMMENT '账号',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) DEFAULT NULL COMMENT 'md5密码盐',
  `name` varchar(45) DEFAULT NULL COMMENT '名字',
  `dept_id` bigint(20) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `sex` int(11) DEFAULT NULL COMMENT '性别（1：男 2：女）',
  `email` varchar(45) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) DEFAULT NULL COMMENT '电话',
  `status` int(11) DEFAULT NULL COMMENT '状态(1：启用  2：冻结  3：删除）',
  `version` int(11) DEFAULT NULL COMMENT '保留字段',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'https://wx.qlogo.cn/mmopen/vi_32/WgSOjJRaQq8FbBHGAr5gLHdaVia7SrnOwRZ9TL8lcDicXuMgqSR8rVgebFa9uIRiaLzYrPdtIia3jqjzkHVVOS38sw/132', 'admin', '7b7de4da17b26a6ba30675d78a521d2b29ba4681d3c24060b10306168a1b758e', 'UUsm1ZEjUSS8QR7zV2Y6', 'admin', '4', '2018-10-08 16:05:42', '1', '457030599@qq.com', '1', '1', null, null, null, '2018-10-31 17:48:40', '2019-07-08 01:26:01', '0');
INSERT INTO `sys_user` VALUES ('1072429679148908546', 'https://wx.qlogo.cn/mmopen/vi_32/WgSOjJRaQq8FbBHGAr5gLHdaVia7SrnOwRZ9TL8lcDicXuMgqSR8rVgebFa9uIRiaLzYrPdtIia3jqjzkHVVOS38sw/132', 'test', '1f66ae907c49c637ee63df28d561f89bb54db18d90d2e4e04db6981d2c6df6b7', 'sMQd7t1D1CPXSMWtlFZ7', 'test', '3', null, null, null, null, '1', null, null, null, '2018-12-11 09:55:35', '2019-11-11 06:28:09', '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('40', '1072429679148908546', '1076685137679704065');
INSERT INTO `sys_user_role` VALUES ('41', '1', '1');
