## 简历编写

**思考** 升级网关是否需要更换域名来引流

上海市的人口约2500万，户籍人口约1500w，共16个区块。

2022年时，上海的机动车保有量就已经突破500万辆



增强了代码的管理意识（git）

增强了代码的规范意识（Alibaba Java开发规范）方法的权限修饰，异常的抛出

增强了配置意识，绝大部分的固定数字，字符串都要以配置文件的形式编写，要么写死为常量，要么读取配置文件以备后续修改。不允许存在”魔法值“

Spring注入规范，尽量使用构造注入，对于Config类中的属性不要直接 @Autowired注入，而是使用@Autowired修饰构造器，并定义属性为final。也不推荐set注入

提取通用工具类，值，增强复用性

工具类的使用，泛型的使用，

通过方法重载，传入不同参数的形式，提高利用率，例如返回成功结果，返回失败结果

规范化注解，提高效率的同时减少配合的失误。规范的思想

工作流 确认需求---表设计powerdesigner---生成sql脚本---生成表---mybatisgenerator生成实体类model、mapper、xml、以及工具方法类Example---前端需求---controller----service---调用mapper方法





缓存 双写，延迟双删

网关--服务限流、熔断、降级-------爬虫、集中访问攻击、实际遇到的：连续刷太久淘宝会变卡，无法加载。反爬虫

压力测试

数据，服务扩容

数据迁移

数据容灾预案与演练



**环境：**国内新冠 2019年底开始，2020年-2021年封禁最严格 2020年1月南京封禁 2022年初南京基本解除封控，部分偶有局部封控 2023初全国疫情基本全面控制，结束封城

封控区，管控区

**核心：**老板是做医疗器械 口罩 酒精 防护用品 在疫情中发家，购买厂房，转型美妆药妆，购买软件服务

老板接一些工厂的代工 ， 原料加工，保健品等。老板也自己有仓库，接货转销（食品 零食，小型家电，日用品，五金，生活用品）

合作渠道：线下医美店、社区医院、社区零售店（远程仓库）（跟着外卖起来的虚拟店铺）

**举例：**疫情期间小区内为主的 采购微信app，买菜买生活用品。

社区用户使用，绑定社群绑定小区店主，以小区为单位，关注小区内代表，推举投票机制，选定商品与需求量，定量进货

一个小区分为几个店家作为主要用户，每一个店家可以推选3-5个居民代表（防控的合作一位 买口罩，业委会的两位 基础生活物资，其他的两位 其他生活用品例如水果，KFC等）

物流--冷链（有储存要求的：药品针剂，一次性器械）

快递--天天、顺丰

# P1--SSM单体项目

企业内部使用
企业办公效能软件

升级框架，迁移升级

优化SQL语句，优化架构设计，业务分离
excel表格读取与生成
用户日志分析员工贡献，工时，出差，报销月度，年度统计
探亲、差旅报销，年假，全薪病假转化，加班工时计算，调休计算。请假、缺勤统计、绩效考核数据评估

业务审批，请假，报销单审批流程，归档

发票图片上传，识别数字，Tesseract-OCR
先上传图片，服务器存储了图片后，用图片路径去调用Tesseract-OCR，获得识别结果
并对识别结果进行特定优化：发票、信息填报单、报销填报等

# P1--电商 mall

### 技术选型：

**后端：**SpirngBoot，MyBatis，RabbitMQ，Nginx，Druid，JWT，SpringSecurityOauth2，Seata

**存储：**MySQL，ElasticSearch，Redis，MongoDB，OSS，MinIO

**插件：**MyBatisGenerator，Lombok，Hutool，PageHalper，Swagger-UI

**部署：**Docker，Jenkins  **监控：**LogStash，Kibana

**前端：**Vue，Element，Vuex，v-charts，Js-cookie  次要可不写

#### 选的技术用在了哪里：

​		Springboot + MyBatis 搭建项目基础架构

​		Redis 处理 Service，RedisService

​		JWT，SpringSecutiry  安全验证 JWT的生成，校验，过期时间，刷新

```Java
//根据claims生成token        
return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
//从token中获取claims以便读取用户名、过期时间等信息
claims = Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody();
```

​		工具方法中，方法的可见等级，对外的只有关键几个public方法，在public方法中调用自己的private protected方法，提高安全性

​		Swagger-UI 生成在线api文档，注解修饰controller方法

​		HuTool工具包：StrUtil.isEmpty() 验证非空字符串、CollUtil.isEmpty 集合工具类验证非空，字符串脱敏工具类DesensitizedUtil.mobilPhone carLicense等

对于字符串脱敏，是在json数据序列化时，也可以在这时自定义json的脱敏序列化类，来处理需要脱敏的字符串，判断原字段上是否有特定注解，有就加以脱敏处理、执行对应的脱敏逻辑

### 开发工具

IDEA，RedisDesktop,Robomongo,SwitchHosts,X-shell,PowerDesigner,PostMan,Navicat

### 开发环境

JDK1.8, MySQL5.7, Redis7.0, MongoDB5.0, RabbitMQ3.10.5, Nginx1.22, Elasticsearch7.17.3, Logstash7.17.3, Kibana7.17.3

## 简介：

**前台：**首页门户、商品推荐、商品搜索、商品展示、购物车、订单流程、会员中心、客户服务、帮助中心等模块

**后台：**商品管理、订单管理、会员管理、促销管理、运营管理、内容管理、统计报表、财务管理、权限管理、设置等模块

**模块细分：**商品模块：商品管理，商品分类管理、商品类型管理、品牌管理、参数管理等

​		订单模块：订单管理、订单设置、退货申请处理、退货原因设置、

​		营销模块：秒杀活动管理、优惠价管理、品牌推荐、新品推荐、人气推荐、专题推荐、首页广告、

### 系统架构图

![re_mall_system_arch](E:\mall-main-jpgresource\re_mall_system_arch.jpg)

### 功能结构图

![re_mall_business_arch](E:\mall-main-jpgresource\re_mall_business_arch.jpg)

### 业务逻辑及表结构

#### 推荐内容及评论及帮助

![mind_content](E:\mall-main-jpgresource\mind_content.jpg)

#### 用户及会员

![mind_member](E:\mall-main-jpgresource\mind_member.jpg)

#### 订单

![mind_order](E:\mall-main-jpgresource\mind_order.jpg)

#### APP设计

![mind_portal](E:\mall-main-jpgresource\mind_portal.jpg)

#### 商品

![mind_product](E:\mall-main-jpgresource\mind_product.jpg)

#### 活动促销

![mind_sale](E:\mall-main-jpgresource\mind_sale.jpg)

## 架构及业务解析

### **SpringBoot+MyBatis 搭建基本骨架**

​		依照设计的表，接口列表逐一编写，实现Controller与Service与ServiceImpl（增删改查）。并按规范记录操作日志

### **整合Swagger-UI实现在线API文档**

常用注解

- @Api：用于修饰Controller类，生成Controller相关文档信息

- @ApiOperation：用于修饰Controller类中的方法，生成接口方法相关文档信息

- @ApiParam：用于修饰接口中的参数，生成接口参数相关文档信息

- @ApiModelProperty：用于修饰实体类的属性，当实体类是请求参数或返回结果时，直接生成相关文档信息

添加配置

```xml
<!--Swagger-UI API文档生产工具-->
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger2</artifactId>
  <version>2.7.0</version>
</dependency>
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger-ui</artifactId>
  <version>2.7.0</version>
</dependency>
```

添加Java配置项 Configuration

```java
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包下controller生成API文档
                .apis(RequestHandlerSelectors.basePackage("com.macro.mall.tiny.controller"))
                //为有@Api注解的Controller生成API文档
			   // .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //为有@ApiOperation注解的方法生成API文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SwaggerUI演示")
                .description("mall-tiny")
                .contact("macro")
                .version("1.0")
                .build();
    }
}
```

**修改MyBatis Generator注释的生成规则**

CommentGenerator为MyBatis Generator的自定义注释生成器，修改addFieldComment方法使其生成Swagger的@ApiModelProperty注解来取代原来的方法注释，添加addJavaFileComment方法，使其能在import中导入@ApiModelProperty，否则需要手动导入该类，在需要生成大量实体类时，是一件非常麻烦的事。

```java
public class CommentGenerator extends DefaultCommentGenerator {
    private boolean addRemarkComments = false;
    private static final String EXAMPLE_SUFFIX="Example";
    private static final String API_MODEL_PROPERTY_FULL_CLASS_NAME="io.swagger.annotations.ApiModelProperty";

    /**
     * 设置用户配置的参数
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
    }

    /**
     * 给字段添加注释
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        String remarks = introspectedColumn.getRemarks();
        //根据参数和备注信息判断是否添加备注信息
        if(addRemarkComments&&StringUtility.stringHasValue(remarks)){
//            addFieldJavaDoc(field, remarks);
            //数据库中特殊字符需要转义
            if(remarks.contains("\"")){
                remarks = remarks.replace("\"","'");
            }
            //给model的字段添加swagger注解
            //核心功能改写，以添加JavaDoc注解的方式，添加 @ApiModelProperty 注解
            field.addJavaDocLine("@ApiModelProperty(value = \""+remarks+"\")");
        }
    }

    /**
     * 给model的字段添加注释
     */
    private void addFieldJavaDoc(Field field, String remarks) {
        //文档注释开始
        field.addJavaDocLine("/**");
        //获取数据库字段的备注信息
        String[] remarkLines = remarks.split(System.getProperty("line.separator"));
        for(String remarkLine:remarkLines){
            field.addJavaDocLine(" * "+remarkLine);
        }
        addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
        super.addJavaFileComment(compilationUnit);
        //只在model中添加swagger注解类的导入
        if(!compilationUnit.isJavaInterface()&&!compilationUnit.getType().getFullyQualifiedName().contains(EXAMPLE_SUFFIX)){
            compilationUnit.addImportedType(new FullyQualifiedJavaType(API_MODEL_PROPERTY_FULL_CLASS_NAME));
        }
    }
}
```

**访问Swagger-UI接口文档地址**

接口地址：http://localhost:8080/swagger-ui.html

Swagger-UI也集成了在线接口测试功能，可以直接在在线文档上使用（比较简陋）

### 整合redis实现缓存





### 整合SpringSecurity和JWT实现认证授权





### 整合SpringTask实现定时任务





### 整合Elasticsearch实现商品搜索





### 整合MongoDB实现文档操作





### 整合RabbitMQ实现延迟消息





### 整合OSS、MinIO实现文件上传





## 源码解析：

### mall父模块

​	引入mall-common、mall-mbg、mall-security子模块

​	均不含SpringBootApplication

​	引入springboot（aop、actuctor、test、configuration）、hutool、lombok、pagehelper、druid、swagger-UI、MyBatisgenerator、mysql-Connector、spirng-data-commons、JWT、aliyun-sdk-oss、logstash、minio

​	定义打包项目时的docker镜像配置

#### mall-common模块

​	**引入**springboot（web、data-redis、validation）、logstash-logback-encoder、spirng-data-commons、springfox-boot-starter

​	**提供：**通用分页数据封装类，将分页查找（PageHelper、SpringData）得到的list中的数据中的分页数据，封装到该分页数据封装类

​			通用返回结果封装类，提供泛型，接收多类型的数据 private T data；根据不同调用方法，返回不同的返回结果封装类CommonResult<T>

​			返回结果常量类ResultCode、提供五种供返回码。

​			配置Redis的基础配置、RedisConfig，配置JSON序列化器，Json转化为对象，缓存有效期

​			配置Swagger的基础配置、SwaggerConfig

​			自定义Swagger配置接口

​			配置logback日志输出

​			Controller层的日志封装类

​			自定义API异常、断言处理类（抛出API异常用的）

​			全局异常处理类，处理自定义的API异常、BindException参数绑定异常

​			统一日志处理切面 WebLogAdpect，切点处理所有模块下的controller，

@Around，获取请求对象，拿到其中需要处理的信息，记录日志，统计方法耗时

​			Redis操作Service接口与实现类

​			获取真实请求IP地址的工具类

#### mall-mbg模块

​			引入父模块、pagehelper、mybatis-generator、mysql-connector

​			专门用来生成mabatis mapper model 映射的

​			配置CommentGenerator类与GeneratorConfig.xml配置文件，调用Generator即可生成

​			提供所有表对应的 xml文件，mapper接口，Example工具方法类， 以及 moddel实体类。

​			Mapper 提供基础的（主键）增删改查方法和定制（Example）方法 ，

​			Example类 中根据每个字段的查询以及模糊查询 使用内部维护的Criteria 对xml文件中的sql语句进行增补。

#### mall-security模块

​		不具备SpringBootApplication

​		引入common模块，springboot（web、security、data-redis），jjwt

​		提供JwtTokenUtil，提供token的生成、校验以及从token中获取信息并校验的功能

​		提供SpringUtil，从SpringApplicationContextSpring，应用上下文中获取执行name和clazz的Bean

​		提供RedisCacheAdpect，为需要redis接入缓存的service提供自定义的切面，

防止Redis缓存宕机影响到正常业务逻辑。切点为 service.*CacheService.*(..)，即所有名字后缀为CacheService的service方法都被增强

并设置 doAround() 增强，执行方法，捕获异常有@CacheException修饰的方法要抛出缓存异常，没有的记录日志

​		提供CacheException自定义注解，用于AOP手动抛出异常，

​		配置SpringSecurity，包括CommonSecurityConfig，加载配置了通用Bean（包括要用到的JwtTokenUtil），Security通用Bean以及动态权限通用Bean（也就是下述中需要注入的，有 @Bean加载了Component，后面才能 @Autowired注入）。

配置SecurityFilterChain。

​	配置放行url，IgnoreUrlsConfig，配置放行白名单的资源路径，框架会从该路径下读取放行url，通常指定在配置文件中

````java
@ConfigurationProperties(prefix = "secure.ignored")
````

​	允许跨域请求的OPTIONS请求，默认全部请求都需要身份认证，关闭跨站请求防护以及不使用session。

​	自定义权限拒绝处理类restfulAccessDeniedHandler。用来返回 **没有权限访问** 的情况下的返回结果。

````java
        //指定是 权限验证-无权访问-的返回结果
        response.setHeader("Access-Control-Allow-Origin", "*");
        //不使用缓存
        response.setHeader("Cache-Control","no-cache");
        //配置返回的类型 json
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        //返回JSON，结果是 CommonResult.forbidden, 403forbidden
        response.getWriter().println(JSONUtil.parse(CommonResult.forbidden(e.getMessage())));
        //PrintWriter IO流 最后要flush下
        response.getWriter().flush();
````

​	自定义权限拒绝处理类restAuthenticationEntryPoint，

```java
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        //返回JSON，结果是 CommonResult.unauthorized, 401已过期
        response.getWriter().println(JSONUtil.parse(CommonResult.unauthorized(authException.getMessage())));
        response.getWriter().flush();
```

​	自定义权限拦截器JWT过滤器**JwtAuthenticationTokenFilter**。从userDetailService中获取用户信息，在引入JwtTokenUtil将用户信息封装，并对用户信息进行验证。验证成功则添加安全认证信息到安全上下文SecurityContext中。该过滤器用来处理 收到一个带有JWT的请求时，校验用户名，成功则创建认证对象并设置到安全上下文中。

​	有动态权限配置时，添加动态权限校验过滤器 **DynamicSecurityFilter** extends AbstractSecurityInterceptor implements Filter。用来实现基于路径的动态权限过滤。首先配置放行OPTIONS请求与白名单中的请求。然后调用AccessDecisionManager中的decide方法进行鉴权操作。当接口未被配置资源时直接放行，有资源则将 **访问所需资源** 与 **用户拥有资源** 进行比对，成功则放行，失败则throw new AccessDeniedException("抱歉，您没有访问权限");

​	这些自定义配置以component类的形式来编写，定义了具体的内容。

### mall-admin模块

​		具备SpringBootApplication

​		引入 mall-mbg模块，mall-security模块，oss云存储和minio。

**配置：**application.yml     指定开发环境dev，servlet开启文件上传，限制为10MB，配置my-batis的mapper路径，jwt的基础配置请求头、密钥、超时时间。redis的基础配置，key和超时时间。security的白名单，阿里云oss的对外访问域名，身份验证，存储空间，单次文件上传大小，路径前缀，以及成功过后的回调地址。

​		application-dev.yml   开发时用到的数据源单独配置，mybatis的数据源配置，druid连接池，redis，minio，以及日志logging，logstash

​		application-prop.yml   生产环境配置，依照自己的环境替换即可，在上述中进行切换。

**提供：** Cms、Oms、Pms、Sms、Ums、在内的22个表的 Dao.xml 和对应的 Dao 实体类

​	业务层提供Cms、Oms、Pms、Sms、Ums处理所需要的Service接口与实现类，共31个service

​	控制层提供Cms、Oms、Pms、Sms、Ums所需业务的Controller、与service一一对应，共31个controller

​	参数信息，数据封装，数据展示，操作返回结果 等所需要的Dto，包含 Dto、Param、Result、Item、Detail等，共29个Dto

**Config配置：**	Cors全局跨域配置、mall-security模块相关配置、MyBatis相关配置、Oss配置、Swagger配置

​		和鉴权所需的用户信息封装类 AdminUserDetails

**校验器Validator：** 提供状态约束校验器来验证状态是否在指定范围内

**业务逻辑：**  （该包下所有controller方法均被AOP切，WebLog的doAround()，功能：统计执行时间，记录webLog并格式化为JSON记录到LOGGER中，具备 @ApiOperation注解修饰的controller方法额外添加记录供Swagger使用）

​		**CmsPrefrenceAreaController：** 商品优选管理

```java
Uri:  /prefrenceArea/listAll
//用于获取所有商品优选List。并将结果封装到CommonResult.success(data)中返回。
```

​		Service：调用mapper，listAll

​		**CmsSubjectController：** 商品专题管理

```java
Uri:  /subject/listAll
//获取全部商品专题
Uri:  /subject/list
    (@RequestParam(value = "keyword", required = false) String keyword,
     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize)
//根据专题名称分页获取商品专题
```

​		Service：调用mapper，listAll查全部；list 用pageHepler分页查询，模糊查询

​		**MinioController：** MinIO对象存储管理

```java
Uri:  /minio/upload
    (@RequestPart("file") MultipartFile file)
//接收文件上传。下述所有操作均使用try-catch包裹
//首先从配置文件中读取minio配置信息并创建一个MinIo的Java客户端，并创建储存桶并设置只读权限(该类中提供的方法createBucketPolicyConfigDto)
//然后根据前端传的文件file，获取文件名并拼接当前日期。
//使用putObject将该文件上传到上述的储存桶中，
//若上传成功，则将成功信息封装到minioUploadDto中返回CommonResult.success
//若失败则打印异常信息，返回CommonResult.failed
    
Uri:  /minio/delete
    (@RequestParam("objectName") String objectName)
//删除对应文件名的文件，注意，这里传入的文件名是上面储存文件时，经过拼接日期和格式化后的文件名
```

​		**OmsCompanyAddressController：** 收货地址管理

```java
Uri:  /companyAddress/list
//获取所有收获地址
```

​		**OmsOrderController：** 订单管理

```java
Uri:  /order/list
    (OmsOrderQueryParam queryParam,
     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum)
//分页查询订单
    
Uri:  /order/update/delivery
    (@RequestBody List<OmsOrderDeliveryParam> deliveryParamList)
//批量发货
    
Uri:  /order/update/close
    (@RequestParam("ids") List<Long> ids, @RequestParam String note)
//批量关闭订单
    
Uri:  /order/delete    
    (@RequestParam("ids") List<Long> ids)
//批量删除
    
Uri:  /order/{id}
	(@PathVariable Long id)
//根据id获取订单详情：订单信息，商品信息，操作记录

Uri:  /order/update/receiverInfo
    (@RequestBody OmsReceiverInfoParam receiverInfoParam)
//修改收货人信息

Uri:  /order/update/moneInfo
    (@RequestBody OmsMoneyInfoParam moneyInfoParam)
//修改订单费用信息
    
Uri:  /order/update/note
    (@RequestParam("id") Long id,
     @RequestParam("note") String note,
     @RequestParam("status") Integer status)
//修改订单信息
```

​		**OmsOrderReturnApplyController：** 订单退货管理

```java
Uri:  /returnApply/list
    (OmsReturnApplyQueryParam queryParam,
     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum)
//分页查询退货申请
    
Uri:  /returnApply/{id}
	(@PathVariable Long id)
//获取退货申请详情

Uri:  /delete
    (@RequestParam("ids") List<Long> ids)
//批量删除退货申请

Uri:  /update/status/{id}
	(@PathVariable Long id, @RequestBody OmsUpdateStatusParam statusParam)
//修改退货申请状态
```

​		**OmsOrderReturnReasonController：** 退货原因管理

```java
@RequestMapping("/returnReason")

    @ApiOperation("添加退货原因")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody OmsOrderReturnReason returnReason) {}

    @ApiOperation("修改退货原因")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody OmsOrderReturnReason returnReason) {}

    @ApiOperation("批量删除退货原因")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {}

    @ApiOperation("分页查询退货原因")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<OmsOrderReturnReason>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {}
        
    @ApiOperation("获取单个退货原因详情信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OmsOrderReturnReason> getItem(@PathVariable Long id) {}

    @ApiOperation("修改退货原因启用状态")
    @RequestMapping(value = "/update/status", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@RequestParam(value = "status") Integer status,
                                     @RequestParam("ids") List<Long> ids) {}
```

订单设置管理

Oss文件上传管理

​	配置oss基础信息，生成ossClient，配置存储目录、文件大小、回调、oss提交节点、签名生成、返回结果

商品品牌管理

商品属性分类管理

商品属性管理

​	举例：新增/删除商品属性后，需要一同更新与其管理那的表 商品属性分类表 中的 数量 字段

​	在同一个Service方法中，对两个mapper进行操作，可以添加事务保证两次操作的一致性

商品分类管理

​	批量插入方法，维护一个List，将传入的List集合中的数据遍历写入，在调用insertList方法来批量插入

​	根据实际业务关系决定，一张表中的某个字段更新时，与其相关联的表中的对应字段也需要一同更新

​	注意：操作mapper的service方法，与执行 复杂 或 可提取公用的 业务逻辑处理数据的方法建议分开，调用即可。

**商品管理**

​	相关联很的表，这个service引入了8个mapper。对于表关系的建立、更新可以单独封装方法（可以使用反射机制获取方法名来增加泛用性）。

​	复数参数条件查询，根据传入的条件添加不同的criteria追加条件，追加到sql语句上。

```java
    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (productQueryParam.getPublishStatus() != null) {
            criteria.andPublishStatusEqualTo(productQueryParam.getPublishStatus());
        }
        if (productQueryParam.getVerifyStatus() != null) {
            criteria.andVerifyStatusEqualTo(productQueryParam.getVerifyStatus());
        }
        if (!StrUtil.isEmpty(productQueryParam.getKeyword())) {
            criteria.andNameLike("%" + productQueryParam.getKeyword() + "%");
        }
        if (!StrUtil.isEmpty(productQueryParam.getProductSn())) {
            criteria.andProductSnEqualTo(productQueryParam.getProductSn());
        }
        if (productQueryParam.getBrandId() != null) {
            criteria.andBrandIdEqualTo(productQueryParam.getBrandId());
        }
        if (productQueryParam.getProductCategoryId() != null) {
            criteria.andProductCategoryIdEqualTo(productQueryParam.getProductCategoryId());
        }
        return productMapper.selectByExample(productExample);
    }
```

模糊查询

```java
    @Override
    public List<PmsProduct> list(String keyword) {
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if(!StrUtil.isEmpty(keyword)){
            criteria.andNameLike("%" + keyword + "%");
            productExample.or().andDeleteStatusEqualTo(0).andProductSnLike("%" + keyword + "%");
        }
        return productMapper.selectByExample(productExample);
    }
```

建立和插入关系表操作方法封装

```java
	/**
     * 建立和插入关系表操作
     *
     * @param dao       可以操作的dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            for (Object item : dataList) {
                //反射拿到 setId 方法和 setProductId方法，并执行
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            //反射拿到insertList方法
            Method insertList = dao.getClass().getMethod("insertList", List.class);
            insertList.invoke(dao, dataList);
        } catch (Exception e) {
            LOGGER.warn("创建商品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
```

商品SKU库存管理

优惠卷管理

优惠卷领取记录管理

限时购活动管理

限时购和商品关系管理

限时购场次管理

首页轮播广告管理

首页品牌推荐管理

首页新品管理

首页人气推荐管理

首页专题推荐管理

后台用户管理

会员等级管理

后台菜单管理

后台资源分类管理

后台资源管理

后台用户角色管理







### mall-portal模块

​		具备SpringBootApplication







### mall-search模块

​		具备SpringBootApplication









## 问题与思考：

OSS对象存储存了商品图（比如原来是本地存的，压力太大转到OSS）
用户查询订单信息时，先只显示部分信息，订单详情，物流信息及详情需要额外点击再查询
轮播广告，商品信息，评论，品牌等如何更新，各类记录如何归档



### 表设计：

#### oms_order 电商订单表：

id 订单id UUID 主键 BTREE，member_id 用户账号id 外键，coupon_id 优惠卷id 外键，
order_sn 订单编号，create_time 提交时间，member_username 用户账号
total_amount 订单总金额，pay_amount 实际支付金额，freight_amount 运费金额，promotion_amount 促销优化金额（促销价，满减，阶梯价等）
integration_amount 积分抵扣金额，coupon_amount 优惠卷抵扣金额，discount_amount 管理员后台调整订单使用的折扣金额
pay_type 支付方式 默认值-1 未支付0 支付宝1 微信2 银行卡3
source_type 订单来源 默认值-1 PC端订单0 app订单1 微信小程序2
status 订单状态 默认值-1 待付款0 待发货1 已发货2 已完成3 已关闭4 无效订单5
order_type 订单类型 默认值-1 普通订单0 秒杀订单1
delivery_company 物流公司（配送方式），delivery_sn 物流单号，auto_confirm_day 自动确认时间（天）
integration 可以获得的积分，growth 可以获得的成长值，promotion_info 活动信息
bill_type 发票类型 默认值-1 不开发票0 电子发票1 纸质发票2，bill_header 发票抬头，bill_content发票内容，
bill_receiver_phone收票人电话，bill receiver_email收票人邮箱
receiver_name 收货人姓名，receiver_phone 收货人电话，receiver_post_code 收货人邮编，receiver_province 收货人省份/直辖市
receiver_city收货人所在城市，receiver_region收货人所在区，receiver_detail_address收货人所在详细地址
order_note订单备注
confirm_status确认收获状态 默认值-1 未收货0 已收货1，delete_status 删除状态 默认值-1 未删除0 用户不可见1 已删除2
use_integration下单使用的积分，payment_time支付时间，delivery_time发货时间
receive_time确认收货时间，comment_time评价时间，modify_time修改时间

建表： 40+字段
````mysql
CREATE TABLE `oms_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `member_id` bigint(20) NOT NULL,
  `coupon_id` bigint(20) NULL DEFAULT NULL,
  `order_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `member_username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户帐号',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '应付金额（实际支付金额）',
  `freight_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费金额',
  `promotion_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '促销优化金额（促销价、满减、阶梯价）',
  `integration_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '积分抵扣金额',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券抵扣金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '管理员后台调整订单使用的折扣金额',
  `pay_type` int(1) NULL DEFAULT NULL COMMENT '支付方式：0->未支付；1->支付宝；2->微信',
  `source_type` int(1) NULL DEFAULT NULL COMMENT '订单来源：0->PC订单；1->app订单',
  `status` int(1) NULL DEFAULT NULL COMMENT '订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单',
  `order_type` int(1) NULL DEFAULT NULL COMMENT '订单类型：0->正常订单；1->秒杀订单',
  `delivery_company` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流公司(配送方式)',
  `delivery_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `auto_confirm_day` int(11) NULL DEFAULT NULL COMMENT '自动确认时间（天）',
  `integration` int(11) NULL DEFAULT NULL COMMENT '可以获得的积分',
  `growth` int(11) NULL DEFAULT NULL COMMENT '可以活动的成长值',
  `promotion_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动信息',
  `bill_type` int(1) NULL DEFAULT NULL COMMENT '发票类型：0->不开发票；1->电子发票；2->纸质发票',
  `bill_header` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票抬头',
  `bill_content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票内容',
  `bill_receiver_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收票人电话',
  `bill_receiver_email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收票人邮箱',
  `receiver_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人电话',
  `receiver_post_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人邮编',
  `receiver_province` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份/直辖市',
  `receiver_city` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市',
  `receiver_region` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `receiver_detail_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `note` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `confirm_status` int(1) NULL DEFAULT NULL COMMENT '确认收货状态：0->未确认；1->已确认',
  `delete_status` int(1) NOT NULL DEFAULT 0 COMMENT '删除状态：0->未删除；1->已删除',
  `use_integration` int(11) NULL DEFAULT NULL COMMENT '下单时使用的积分',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '确认收货时间',
  `comment_time` datetime NULL DEFAULT NULL COMMENT '评价时间',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;
````

#### pms_product 商品信息表：

建表：40+字段
````MySQL
CREATE TABLE `pms_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,   主键
  `brand_id` bigint(20) NULL DEFAULT NULL,   外键
  `product_category_id` bigint(20) NULL DEFAULT NULL,    外键
  `feight_template_id` bigint(20) NULL DEFAULT NULL,     外键
  `product_attribute_category_id` bigint(20) NULL DEFAULT NULL,    外键
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '货号',
  `delete_status` int(1) NULL DEFAULT NULL COMMENT '删除状态：0->未删除；1->已删除',
  `publish_status` int(1) NULL DEFAULT NULL COMMENT '上架状态：0->下架；1->上架',
  `new_status` int(1) NULL DEFAULT NULL COMMENT '新品状态:0->不是新品；1->新品',
  `recommand_status` int(1) NULL DEFAULT NULL COMMENT '推荐状态；0->不推荐；1->推荐',
  `verify_status` int(1) NULL DEFAULT NULL COMMENT '审核状态：0->未审核；1->审核通过',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `sale` int(11) NULL DEFAULT NULL COMMENT '销量',
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `promotion_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '促销价格',
  `gift_growth` int(11) NULL DEFAULT 0 COMMENT '赠送的成长值',
  `gift_point` int(11) NULL DEFAULT 0 COMMENT '赠送的积分',
  `use_point_limit` int(11) NULL DEFAULT NULL COMMENT '限制使用的积分数',
  `sub_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品描述',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '市场价',
  `stock` int(11) NULL DEFAULT NULL COMMENT '库存',
  `low_stock` int(11) NULL DEFAULT NULL COMMENT '库存预警值',
  `unit` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品重量，默认为克',
  `preview_status` int(1) NULL DEFAULT NULL COMMENT '是否为预告商品：0->不是；1->是',
  `service_ids` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮',
  `keywords` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `album_pics` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '画册图片，连产品图片限制为5张，以逗号分割',
  `detail_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `detail_desc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `detail_html` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '产品详情网页内容',
  `detail_mobile_html` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '移动端网页详情',
  `promotion_start_time` datetime NULL DEFAULT NULL COMMENT '促销开始时间',
  `promotion_end_time` datetime NULL DEFAULT NULL COMMENT '促销结束时间',
  `promotion_per_limit` int(11) NULL DEFAULT NULL COMMENT '活动限购数量',
  `promotion_type` int(1) NULL DEFAULT NULL COMMENT '促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购',
  `brand_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `product_category_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品信息' ROW_FORMAT = DYNAMIC;
````

#### ums_member 会员表
````MySQL
建表：
CREATE TABLE `ums_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_level_id` bigint(20) NULL DEFAULT NULL,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `status` int(1) NULL DEFAULT NULL COMMENT '帐号启用状态:0->禁用；1->启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `icon` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` int(1) NULL DEFAULT NULL COMMENT '性别：0->未知；1->男；2->女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所做城市',
  `job` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职业',
  `personalized_signature` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `source_type` int(1) NULL DEFAULT NULL COMMENT '用户来源',
  `integration` int(11) NULL DEFAULT NULL COMMENT '积分',
  `growth` int(11) NULL DEFAULT NULL COMMENT '成长值',
  `luckey_count` int(11) NULL DEFAULT NULL COMMENT '剩余抽奖次数',
  `history_integration` int(11) NULL DEFAULT NULL COMMENT '历史积分数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE,
  UNIQUE INDEX `idx_phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员表' ROW_FORMAT = DYNAMIC;
````

### 表汇总：

cms_help 帮助表
cms_help_category 帮助分类表
cms_member_report 用户举报表
cms_prefrence_area 优选专区
cms_prefrence_product_relation 优选专区与商品关系表
cms_subject 专题表
cms_subject_category 专题分类表
cms_subject_comment 专题评论表
cms_subject_product_relation 专题与商品关系表
cms_topic 话题表
cms_topic_category 话题分类表
cms_topic_comment 专题评论表

oms_cart_item 购物车表
oms_company_address 公司仓库收发货地址表
oms_order 订单表
oms_order_item 订单所包含的商品表
oms_order_operate_history 订单状态操作历史表
oms_order_return_apply 订单退货申请表
oms_order_return_reason 订单退货原因表
oms_order_setting 订单设置表
	
pms_album 相册表
pms_album_pic 相册图片表
pms_brand 品牌表
pms_comment 商品评价表
pms_comment_apply 商品评价回复表
pms_feight_template 运费模板表
pms_member_price 商品会员价格表
pms_product 商品信息表
pms_product_attribute 商品属性参数表
pms_product_attribute_category 商品属性分类表
pms_product_attribute_value 产品参数信息存储表
pms_product_category 产品分类表
pms_product_category_attribute_relation 产品分类与产品属性关系表（筛选用）
pms_product_full_reduction 产品满减表
pms_product_ladder 产品阶梯价格表
pms_product_operate_log 产品操作日志表
pms_product_vertify_record 商品审核记录表
pms_sku_stock 商品sku码库存表
	
sms_coupon 优惠券表
sms_coupon_history 优惠券领取使用历史表
sms_coupon_product_category_relation 优惠卷与产品分类关系表
sms_coupon_product_relation 优惠卷与产品关系表
sms_flash_promotion 限时购表
sms_flash_promotion_log 限购通知记录表
sms_flash_promotion_product_relation 商品限时购与商品关系表
sms_flash_promotion_session 限时购场次表
sms_home_advertise 首页轮播广告表
sms_home_brand 首页推荐品牌表
sms_home_new_product 好物上新表
sms_home_recommend_product 商品人气推荐表
sms_home_recommend_subject 首页专题推荐表
	
ums_admin 后台用户表
ums_admin_login_log 后台用户登录日志表
ums_admin_permission_relation 后台用户与用户权限关系表
ums_admin_role_relation 后台用户与用户角色关系表
ums_growth_change_history 成长值变化历史记录表
ums_integration_change_history 积分变化历史记录表
ums_integration_sonsume_setting 积分消费设置表
ums_member 会员表
ums_member_level 会员等级表
ums_member_login_log 会员登录记录表
ums_member_member_tag_relation 会员与用户标签关系表
ums_member_product_category_relation 会员与产品分类关系表
ums_member_receive_address 会员收获地址表
ums_member_rule_setting 会员积分成长规则表
ums_member_statistics_info 会员统计信息
ums_member_tag 用户标签表
ums_member_task 会员任务表
ums_memu 后台菜单表
ums_permission 后台用户权限表
ums_resource 后台资源表
ums_resource_category 资源分类表
ums_role 后台用户角色表
ums_role_menu_relation 后台角色与后台菜单关系表
ums_role_premission_relation 后台用户角色和用户权限关系表
ums_role_resource_relation 后台角色与后台资源关系表

### 表结构：

见powerdesigner



# P1--电商 mall_swarm 升级版

SpringCloud SpringCloudAlibaba Nacos GateWay

## 架构升级

![mall_micro_service_arch](E:\mall-main-jpgresource\mall_micro_service_arch.jpg)















# P2--GitHub开源项目  企业级低代码平台JeecgBoot















