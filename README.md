## 公共接口开发说明
### 文件及打包说明
- doc 文档 包括接口模板以及接口说明等
- src 接口源代码
- src_local 接口本地化新增业务代码
- lib 第三方依赖jar包
- .gitignore
  
### spring默认读取配置文件路径为src/main/resources/config/spring
如果需要增加或调整配置文件。
默认配置文件说明说明

|  文件名   | 说明  |
|  ----  | ----  |
| applicationContext-dm-main-mybaits.xml  | 数据库配置文件，不同的数据库可以使用不同的配置文件 |
| applicationContext-redis.xml  | Redis配置文件 |
| applicationContext-security.xml | 安全配置文件 |
| applicationContext-task.xml  | 任务配置 |
| applicationContext-validate.xml  | 信息校验配置 |
| applicationContext.xml  | 公共配置 |
| spring-mvc.xml  | Springmvc核心配置文件 |