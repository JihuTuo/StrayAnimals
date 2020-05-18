# beam-parent
#### 管理系统演示地址（演示账号只提供界面展示，没有按钮权限即看不到按钮）
http://www.hsshy.com/#/login
演示账户：test 123456
#### 博客地址
http://www.hsshy.com
#### 项目介绍
- 基于SpringBoot 2，一款快速开发的脚手架。admin:后台管理系统。rest:用于快速构建中小型API、RESTful API项目。
- 有配套的代码生成器可供使用。
- 基础模块：
  -  **用户管理**
  -  **角色管理** 
  -  **菜单管理**
  -  **部门管理**
  -  **定时任务**
  -  **字典管理**
  -  **登陆日志**
  -  **业务日志**
  -  **博客管理**
- 项目特点：
  - **持久层：mybatis持久化，使用MyBatis-Plus优化，减少sql开发量；Transtraction注解事务。**
  - **使用SpringBoot自动装配，配置简便，开箱即用**
  - **将service、dao、entity接口提为公共模块，接口模块与后台管理系统可共用。不共用，只需将相应的模块放在对应的子工程中**
  - **接口模块已添加拦截和post请求签名，提高了安全性**
  - **后端可使用map+wrapper返回方式返回字段的字典值，无需再前端一一对应字典值所对应的中文名称**
  - **前后端分离**
  - **异步插入日志**
  - **实现了用户角色菜单权限动态配置，精确到按钮级别**
  - **线上日志分类**
  - **图片存储（七牛云）（注册地址：https://portal.qiniu.com/signup?code=1h8cpibemhb9u）**
  - **定时任务：Quartz，已搭建成界面化方式，配置即可使用**
  - **工具类：excel导入导出，汉字转拼音，字符串工具类，数字工具类，发送邮件，redis工具类，MD5加密，HTTP工具类，防注入工具类,i18n 国际化多语言工具等等。**
  - **动态数据源模块抽离封装，即插即用**
  - **集群session共享**
#### 技术选型
- 核心框架：Spring Boot 2.1.5
- 安全框架：Apache Shiro 1.4
- 视图框架：Spring MVC 5.0
- 持久层框架：MyBatis-Plus 3.1.2
- 定时器：Quartz 2.3
- 数据库连接池：hikari
- 后台管理系统前端：Vue2.x+element-ui (可更换主题颜色)
- 缓存：Redis
- 图片存储：七牛云（七牛云注册便有10G免费空间，注册地址：https://portal.qiniu.com/signup?code=1h8cpibemhb9u）。
#### 项目结构
````
beam-parent
├─beam-common     公共模块
│             
├─beam-web  公用实体类、dao、service
│   
├─beam-dynamic-datasource  动态数据源模块（按需引入）
│ 
├─beam-admin     管理后台系统（独立的与beam-rest无关联）
│        └─resources 
│           ├─application.yml  配置文件
│           ├─logback-spring.xml  日志配置文件
│ 
├─beam-rest  API接口模块 （post请求签名、token)
│   
│ 
├─html/beam-manager-system 前端代码
│ 
├─doc  数据库sql文件
│ 
│ 
│ 
````
#### 软件需求
- IntelliJ IDEA
- JetBrains WebStorm
- JDK1.8
- MySQL5.5+
- Maven3.0+
- Redis
- lombok插件

#### 运行流程
###### 指定运行环境
![image.png](http://img.hsshy.cn/upload/20190821/2683498c87f24613886694f813f44c2e.png)
![image.png](http://img.hsshy.cn/upload/20190821/31332a378a604e13947e6525cb81c43b.png)


#### 动态数据源测试方法
- 打开beam-rest模块pom文件的注解
- 新建数据库test，将beam数据库的sys_config同步到测试数据库test，修改不同数据
- 打开TestController的注释，运行项目，环境选择test，即可进行测试。


#### 部署流程

##### 软件安装（Linux）
- Java环境：http://www.hsshy.com/#/blog/detail/00qGckBh
- Redis：http://www.hsshy.com/#/blog/detail/00P8pnLd
- MySQL：http://www.hsshy.com/#/blog/detail/00Pivdjt
- Nginx：http://www.hsshy.com/#/blog/detail/00qhyABC
##### 后端：
- 服务器选的是阿里云（注册地址：https://chuangke.aliyun.com/invite?userCode=647hkjjy）
- 打包：```package -Dmaven.test.skip```
- 运行：```nohup java -jar xxx.jar --spring.profiles.active=prod >/dev/null 2>&1 &```
- 查看运行日志：```tail -f beam-admin-logs/log_total.log```
- doc目录下提供运行脚本，脚本与jar包放在同级目录下即可。```chmod 777 deploy.sh```（添加读写执行权限），脚本运行报错（执行dos2unix deploy.sh，window环境下与Linux环境下文本格式有所不同）

##### 前端（这边是用nginx进行部署）：
- 打包：npm run build
- 上传：进入dist文件夹，scp -r * root@xx.xx.xx.xx:/etc/nginx/html/beam-manage-system/
- 给文件夹设置权限。
- nginx配置请参考doc下的beam.conf文件,可直接传到服务器下的nginx/conf.d/下进行使用，记得删除默认的default.conf文件。

#### 部署可能出现的问题
- 出现表不存在（定时任务相关的表改成大写或者将数据库改成大小写不敏感）
- 脚本运行报错（执行dos2unix deploy.sh，window环境下与Linux环境下文本格式有所不同）
- Nginx 403：给前端文件夹设置可读写的权限。
- Nginx 404：后台管理系统接口要与前端对应上，详情看doc下beam.conf的配置。
#### 常见问题
- 上传图片失败，请修改sys_config中的七牛云配置，改为自己的七牛云配置。（七牛云注册便有10G免费空间，注册地址：https://portal.qiniu.com/signup?code=1h8cpibemhb9u）。
- 提示账户验证失败，检查是否安装Redis，以及用户名密码是否正确。
- set、get报红，请安装Lombok插件。详情请参照软件需求。
<br>

### 代码生成器
#### https://gitee.com/hsshy/beam-generator


#### 有需要dubbo的请移至：https://gitee.com/hsshy/beam-dubbo

#### 还有问题请移步公众号，回复加群。
![image.png](http://img.hsshy.cn/upload/20190821/04ce4ff9ddf64816a4831223654d588b.png)