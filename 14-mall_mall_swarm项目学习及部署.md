## 简历编写

**思考** 升级网关是否需要更换域名来引流

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

#### mall-security模块

​		不具备SpringBootApplication

​		引入common模块，springboot（web、security、data-redis），jjwt

​		提供









### mall-admin模块

​		具备SpringBootApplication





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















