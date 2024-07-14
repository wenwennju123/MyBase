# 复习Spring SpringMVC MyBatis

## 框架

框架的本质是通过对通用代码的封装，提前编写了一套接口和类，开发人员在做项目的时候可以直接引入这些接口和类。

引入框架，基于这些现有的接口和类进行开发，可以大大提高开发效率。

### 三层架构：

​    web前端---ajax请求----表现层UI（返回JSON数据给前端）
​                           |
​                        业务逻辑层BLL
​                           |
​                       数据访问层DAL持久层
​                           |
​                       数据库

MyBatis相当于JDBC的一些高级框架，其中封装了JDBC,专门用来访问数据库（以前的名字叫做ibatis）

JDBC的不足
		例：String sql 定义预编译对象的sql语句时，很多内容已经固定，新增了字段，占位符等操作都需要改源代码，违背OCP原则
        即：sql语句不应该写死到java程序中
    	例：JDBC代码比较繁琐，字段较多时，行很多时，代码十分繁琐
   	 例：查询结果集遍历时，字段较多结果较多时，手动new对象封装查询结果，代码繁琐。要通过反射机制创建对象

### ORM思想：

​	对象关系映射
​		Object  	Java虚拟机中的Java对象
​		Rational		关系型数据库
​		Mapping		将java虚拟机中的java对象，映射到数据库表中的一行记录，或者将数据库表中的一行记录映射为一个java对象。

Java中的实体类可以看作数据库的一张表。
对应的，表中的每一条记录都可以封装成一个Java的实体类的对象来进行接收保存。
数据库表中的字段信息对应Java类中的属性，这里满足一定格式的Java实体类可以叫做bean，pojo，domain，entity等

MyBatis框架是基于ORM的框架，意味着MyBatis可以完成java对象和数据库中一条记录的转换
		MyBatis是一个半自动化的ORM框架，其中的SQL语句是程序员手动编写，方便进行sql调优
		Hibernate框架是全自动的，sql语句自动生成，
		MyBatis-plus也是全自动化的

dtd：文档类型规范，文档类型约束
		举例：DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"     "http://mybatis.org/dtd/mybatis-3-config.dtd"
		规定了mybatis-config.xml中的文件格式，顺序，嵌套规则，属性值，必须严格遵循

开发步骤，引入框架，插件等通用
		打包方式jar
		引入依赖：myBatis MySql驱动
		从XML配置我呢见中构建SqlSessionFactory 	对应一个项目一个
			编写MyBatis核心配置文件 mybatis-config.xml （其位置和名称不固定，但约定俗成）
        	mybatis-config中可以配置链接数据库的各种信息等。
        	放置在类的根路径下
		编写xxxMapper.xml（对应一个表一个）
			用来专门编写SQL语句的配置文件
       	 t_user表，对应UserMapper.xml
        	t_student,对应StudentMapper.xml
        	放置在类的根路径下
		在mybatis-config.xml文件中指定xxxMapper.xml文件的路径
        	resources属性会自动从类的根路径下开始寻找
		编写myBatis程序
			使用myBatis的类库，连接数据库，完成增删改查操作
        	在MyBatis中，负责执行SQL语句的对象是 SqlSession （session是会话，代表着JVM与数据库的会话）
        	想获取SqlSession对象，就要先获取SqlSessionFactory对象
       	 每个基于MyBatis的应用都是以一个SqlSessionFactory为核心的（工厂模式）
        	SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得（build方法）
        	而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出SqlSessionFactory 实例

```java
public class MyBatisIntroductionTest {
    public static void main(String[] args) throws IOException {
        /**
         * 获取SqlSessionFactoryBuilder对象
         */
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        /**
         * 获取SqlSessionFactory对象
         * 传参 输入流 指向mybatis-config.xml
         * 使用mybatis的封装好的工具类进行获取，无需手动new InputStream
         * 手动new一个FileInputStream 缺点是路径是固定的，可移植性降低。改动就会违背OCP原则
         * getResourcesAsStream默认也是从根路径下寻找资源
         InputStream is = ClassLoader.getSystemClassLoader().getResourcesAsStream("mybatis-config.xml");
        经过分析Resources源代码
        底层实现就是这样，先获取系统类加载器 ClassLoader.getSystemClassLoader()
        在调用其中的方法 getResourcesAsStream 这个方法就是从类路径中加载的
         */
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        /**
         * 获取SqlSession对象
         * 默认情况下SqlSession不支持自动提交事务，需要手动提交
         * 可以在openSession()中传参 true表示开启自动提交，false表示关闭自动提交开启事务
         */
        SqlSession sqlSession = sqlSessionFactory.openSession();
        /**
         * 执行SQL语句
         * 传参 sql 语句的id
         * 返回值是影响数据库表当中的记录条数
         * SQL语句执行后需要手动提交事务
         */
        int count = sqlSession.insert("insertCar");
        System.out.println("插入了" + count + "条记录");
        sqlSession.commit();
        /**
         * 关闭流
         */
        if (is != null) {
            is.close();
        }
    }
}
```

## MyBatis

### 事务管理机制

​    通过以下标签的配置来进行mybatis的事务管理
​    <transactionManager type="JDBC"/>
​    type属性的值有两个
​        JDBC        JDBC事务管理器
​        MANAGED     MANAGED事务管理器
​    JDBC事务管理器
​        mybatis框架自己管理事务，底层依旧采用原生的JDBC代码去管理事务
​            conn.setAutoCommit(false);//sqlSessionFactory.openSession();时
​            ...业务代码...
​            conn.commit();//sqlSession.commit();时可以在openSession()中传参 true表示开启自动提交，false表示关闭自动提交开启事务
​        底层不会执行conn.setAutoCommit(false)
​        JDBC中默认的事务是自动提交，是true，只要执行任意一条DML语句，就会自动提交事务
​    MANAGED事务管理器
​        mybatis不再负责事务的管理，交由第三方框架负责，例如 Spring
​        后期SSM集成后，一般都采用 MANAGED 方式，交由Spring统一管理
​        如果没有第三方框架接管，则事务不会开启，自动提交

### MyBatis集成日志框架logback组件

​    例如输出sql语句，链接对象信息，创建与销毁与资源占用情况
​    可以方便调试
​    myBatis常见的集成日志组件有：
​        STDOUT_LOGGING标准日志框架myBatis已经内部实现，只要开启即可
​        SLF4J | LOG4J | LOG4J2 | STDOUT_LOGGING 为常用框架
​        SLF是一个日志标准，日志门面，其中有一个框架叫logback，它实现了SLF规范
​    在maven中引入logback依赖，
​    引入logback所必须的xml配置文件，
​        名字必须是logback.xml或logback-test.xml
​        位置必须放入类的根路径下
​        xml配置文件中可以设置日志的格式，输出，大小存放时间等等信息

### myBatis增删改查CRUD

​    Create Retrieve Update Delete
​    值不可以写死到配置文件中，一定是前端的form表单提交过来数据，将值传递给后端，动态生成sql语句
​    在原生JDBC中，占位符是 ？ 然后setString
​    在myBatis中占位符的语法：（等效JDBC中的 ？ ）
​        #{map集合的key}
​    执行SQL语句,传入两个参数，一个是sql语句的id。另一个参数是用来封装数据的对象
​    sqlSession.insert("insertCar",map1)
​    对象中属性封装的数据就会填入Sql语句中的占位符
​    典型的ORM思想 对象关系映射
​    举例以map集合对象封装一组数据
​    注意，如果key不存在，获取的是null。一但对应字段有非空约束，则寄
​    map集合的key命名需要遵循 见名知意

java程序中使用POJO类给SQL语句的占位符传值
    Car car = new Car(null,"155","秦",30.0,"2020-09-05","电车");
    占位符中填写pojo类中变量的属性名
    insert into t_car(id,car_num,brand,guide_price,produce_time,car_type)
    values (null,#{carNum},#{brand},#{guidePrice},#{produceTime},#{carType})

占位符中的属性名必须正确，因为底层会找这个属性对应的get方法的方法名，严格来说并不是属性名，需要遵守命名规范
底层通过反射机制寻找pojo类对应的get方法。反之只要提供对应的get方法，名字不一定必须是属性名（get方法去掉get然后首字母小写）
（如果名字写错，里面有个错误信息时ReflectException）
（可以类比EL表达式${}）

//这里的 13 会被包装成Object 然后传给占位符
int count = sqlSession.delete("deleteById",13);
<!--当参数只有一个的时候，命名不加以限制-->
<delete id="deleteById">
    delete from t_car where id = #{id};
</delete>

根据主键查询，返回的结果一定是一个
resultType指定结果集要封装的java对象的类型,必须指定类名，填入全限定类名
<select id="selectById" resultType="com.leowork.mybatis.pojo.Car">
    select * from t_car where id = #{id};
</select>
返回结果是一个包装了数据的java对象，这个对象是结果集对象，ResultSet对象
ResultSet对象取出数据的方法
需要指定返回结果集的类型才可以返回
Car car = sqlSession.selectOne("selectById",1);
对象中要想有查询到的值，类中的属性名需要和数据库中字段名相同。
底层依旧是使用反射机制，然后获取属性的set方法，通过set方法给对象中属性赋值
解决办法：（手动）
    修改pojo类中的set方法方法名，不推荐
    select语句中将结果起别名 as ，需要与属性名一致
或者可以修改settings中的设置参数

查询多个对象时
    返回对象的list集合 List<Car>
    <!--返回结果集List集合 指定的结果集类型是List集合中元素的类型，全限定类名-->
List<Car> carList = sqlSession.selectList("selectAll");

遍历返回的List集合
for (Car car : carList){
    System.out.println(car);
}

Lambda表达式写法 carList.forEach(car -> System.out.println(car));
更简化的写法 carList.forEach(System.out::println);

map可以传值，pojo也可以传值

出现时区错误时，在JDBC的URL后添加属性?serverTimezone=UTC即可
    <property name="url" value="jdbc:mysql://localhost:3306/mytestbase?serverTimezone=UTC"/>

<mapper namespace="abc">
    mapper中的namespace属性 用来指定命名空间，防止sql语句的id重复导致程序无法识别
    完整id写法
        namespace.id
        "abc.selectAll"
    这样就可以准确定位需要执行的sql语句。在有多个表。多个Mapper.xml的时候是必须的

### MyBatis核心配置文件 mybatis_config.xml 深入分析

​    configuration标签，根标签，唯一
​    环境标签 environment
​    <environments default="mytestbaseDB">
​        default表示众多环境中，不指定环境的id的情况下，默认使用的环境

​    其中一个环境，链接的数据库是mytestbase。环境environment都有id。可以使用不同的数据库
​    一般一个环境对应一个数据库。例如线上的数据库，线下测试用数据库，商品，订单，用户信息等等
​    而一个 environment 环境(数据库)会对应一个 SqlSessionFactory 对象
​    <environment id="mytestbaseDB">
​       <transactionManager type="JDBC"/>
​        <dataSource type="POOLED">
​            <!--数据库链接信息-->
​            <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
​            <property name="url" value="jdbc:mysql://localhost:3306/mytestbase?serverTimezone=UTC"/>
​            <property name="username" value="root"/>
​            <property name="password" value="Lbw151290007"/>
​        </dataSource>
​    </environment>

​    举例另一个数据库，另一个环境
​    <environment id="mybatisDB">
​        <transactionManager type="JDBC"/>
​        <dataSource type="POOLED">
​            <!--数据库链接信息-->
​            <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
​            <property name="url" value="jdbc:mysql://localhost:3306/mybatis?serverTimezone=UTC"/>
​            <property name="username" value="root"/>
​            <property name="password" value="Lbw151290007"/>
​        </dataSource>
​    </environment>

</environments>

例：SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourcesAsStream("mybatis-config.xml"))
        会采用默认default的环境 mytestbaseDB
   SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourcesAsStream("mybatis-config.xml"),"mybatisDB")
        会采用指定ID的环境 mybatisDB

一个项目可能使用多个数据库，每个数据库中都有多张表，每个表都有不同的sql语句操作
    一个mybatis-config中可以配置多个environment（对应数据库），使用id进行区分 一个environment（数据库）一个SqlSessionFactory
    一个environment下可以有多个Mapper（对应表） 使用namespace进行区分，一个库中有多张表
    一个Mapper中有多个sql语句，使用id进行区分 sqlSession执行 "namespace.sqlID" 定位sql语句（一张表中的数据会根据业务不同由不同的sql语句来进行操作）

### 深入分析config配置文件

​    <transactionManager type="JDBC"/>
​    transactionManager标签，配置事务管理器，指定mybatis具体使用什么方式管理事务
​        JDBC事务管理器
​            mybatis框架自己管理事务，底层依旧采用原生的JDBC代码去管理事务
​                conn.setAutoCommit(false);//sqlSessionFactory.openSession();时
​                ...业务代码...
​                conn.commit();//sqlSession.commit();时

​        可以在openSession()中传参 true表示开启自动提交，false表示关闭自动提交开启事务
​        关闭时，底层不会执行conn.setAutoCommit(false)
​        JDBC中默认的事务是自动提交，是true，只要执行任意一条DML语句，就会自动提交事务
​    MANAGED事务管理器
​        mybatis不再负责事务的管理，将事务管理交给其他的JEE（JavaEE）容器管理，交由第三方框架负责，例如 Spring
​        后期SSM集成后，一般都采用 MANAGED 方式，交由Spring统一管理
​        如果没有第三方框架接管，则事务不会开启，自动提交
​    JDBC MANAGED 二选一

在myBatis中 提供了一个事务管理器接口 Transaction
    两个实现类implementation
        JdbcTransaction
        ManagedTransaction

深入分析config配置文件

​    <dataSource type="POOLED">
​    dataSource 数据源，为程序提供链接对象Connection（只要是给程序链接对象的都叫做数据源 数据源是一套JDK中的规范 javax.sql.DataSource）
​    数据库连接池就是一个数据源。
​    数据源可以自己编写实现手写组件。也有很多框架对其进行了实现，常用的有Spring中的druid连接池，c3p0连接池，dbcp等
​    手动编写：
​        实现javax.sql.DataSource接口，实现其中所有方法

type属性值三选一：UNPOOLED POOLED JNDI
    UNPOOLED: 不使用数据库链接池技术，每一次请求过来之后都会新建Connection对象
    POOLED: 使用mybatis自己实现的数据库连接池。具有多个属性用来配置POOLED数据源
    JNDI: 集成第三方的数据库连接池。为了能在如EJB或应用服务器这类容器中使用。容器可以集中火灾外部配置数据源
        然后放置一个JNDI上下文的数据源引用

JNDI：是一套规范，大部分的web容器例如Tomcat,Jetty等都实现了JNDI规范
    Java Naming and Directory Interface；Java命名和目录接口。

POOLED 和 UNPOOLED 的区别
    有连接池的时候，sqlSession.close()并不会真正关闭链接对象，而是将链接对象返回连接池中
    Returned connection xxxxxxxx to pool
    下一次获取的时候，是从连接池中取出链接对象
    Checked out connection xxxxxxxx from pool

连接池的优点：
    每一次获取链接都从池中拿，用完归还，效率较高
    每一次只能从池中那，所以链接对象的创建数量是可以控制的。
    当连接池中所有链接对象均被占用时，其他请求在外部等待，通过合理配置降低服务器压力

POOLED的配置属性：
<dataSource type="POOLED">
    <!--数据库链接信息-->
    <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/mytestbase?serverTimezone=UTC"/>
    <property name="username" value="root"/>
    <property name="password" value="Lbw151290007"/>
    <!--配置链接池属性-->
    <!--连接池中具有很多参数，需要设置调节，根据不同业务需求，数据特点，时间高峰等情况进行调节，才能达到事半功倍的效果-->
    <property name="poolMaximumActiveConnections" value="3">
        配置链接池中最大的正在使用的链接对象的数量上限，默认为10
        也就是连接池当中最多的正在活动的链接对象数量
        测试方法：
            获取连接对象但不关闭，让后面的发生等待 Waiting as long as 20000 milliseconds for connection
        一个链接对象占用过久会有超时过期时间，过期后返回连接池，供给后面等待的请求使用
            Claimed overdue connection xxxxxxxx
    <property name="poolMaximumCheckoutTime" value="10000">
        在被强制返回之前，池中链接被检出 check out 的时间，也就是超时时间，默认20000ms
    <property name="poolTimeToWait" value="5000">
         这是一个底层设置，如果获取连接花费了相当长的时间，连接池会打印状态日志并重新尝试获取一个连接
         （避免在误配置的情况下一直失败且不打印日志），默认值：20000 毫秒
         如果拿不到链接对象，这条会在上一条的超时时间内一直输出日志
    <property name="poolMaximumIdleConnections" value="3">
        设置任意时间可能存在的空闲连接数，超过这个数量的空闲链接会被真正关闭
        用来限制资源的使用
    上述属性在第三方连接池中也有实现，实际使用中需要根据实际数据情况进行调整
</dataSource>

### 深入分析config配置文件

​    properties标签的使用
​    在其中定义全局变量，在之后的标签中可以取用，便于统一管理和修改
​    <properties>
​        <property name="jdbc.username" value="root"/>
​    </properties>
​    取用：${jdbc.username}
​    类比EL表达式

建议使用properties配置文件的形式，放在根路径下
引入配置文件<properties resources="jdbc.properties" />
jdbc.username=root
取用：${jdbc.username}  实际开发中会交由客户完成修改配置文件，例如用户名和密码
    

mappers标签用于指定对应的数据库表Mapper映射文件，resources会从根路径下寻找

### 手写MyBatis框架

​    dom4j解析XML文件
​    操作复习：
​        添加dom4j库到项目中。dom4j是一个Java的XML解析库，可以从dom4j官网下载jar包，并将其添加到项目中。
​        创建Document对象。Document对象是dom4j中的核心对象，用于表示整个XML文档的根节点。
​            //其中，SAXReader是dom4j提供的一个XML解析器，可以用来读取XML文件，并将其转换为Document对象。
​            SAXReader reader = new SAXReader();
​            document = reader.read(new File("test.xml"));
​        获取根节点。通过Document对象的getRootElement()方法可以获取XML文档的根节点
​            Element root = document.getRootElement();
​        遍历XML节点。可以使用dom4j的XPath表达式或者 递归遍历 的方式来遍历XML节点。
​            //其中，element.elements()方法可以获取当前节点的所有子节点。
​            public static void traverse(Element element) {
​                System.out.println(element.getName());
​                List<Element> elements = element.elements();
​                for (Element e : elements) {
​                    traverse(e);
​                }
​            }
​        获取节点属性和文本内容。可以通过Element对象的attributeValue()方法获取节点的属性值，
​        通过Element对象的getText()方法获取节点的文本内容。
​            Element element = root.element("book");
​            String id = element.attributeValue("id");
​            String title = element.elementText("title");

在maven中引入 dom4j 依赖与 jaxen 依赖
String 类常用方法复习：
    length(); charAt(int index); subString(int beginIndex,int endIndex);
    indexOf(String str); lastIndexOf(String str); equals(Object obj);
    compareTo(String anotherString); toLowerCase(); toUpperCase();
    trim(); replace(char oldChar,char newChar); split(String regex);
    startsWith(); endsWith(); isEmpty();

手写MyBatis框架----重点----笔记参考leoBatis
    面向接口编程
    先建一个SqlSessionFactoryBuilder类，定义build方法用来创建SqlSessionFactory
    需要输入配置文件config的流。
    根据配置文件中的信息，说明
    SqlSessionFactory对象中应该封装有 事务管理器Transaction 和 mapper映射信息
    Transaction具有多种，定义接口方便管理
    目前提供JdbcTransaction和ManagedTransaction实现类
    而实现事务管理器就需要准备数据库链接对象，链接对象从数据库连接池中获得
    连接池需要遵循javax.sql.DataSource 规范接口
    目前提供三种连接池UNPOOLED POOLED JNDI

***************************************************************
MyBatis框架中 POOLED 连接池的实现代码原理
    public class PooledDataSource implements DataSource
    属性采用 protected 修饰
PooledDataSource类主要包含以下功能，方法：
    实现了DataSource接口，可以获取数据库连接。
    封装了UnpooledDataSource对象，用于创建数据库连接。
    维护了一个连接池，通过PoolState类实现，可以对连接池进行管理。
    可以设置连接池的配置信息，例如最大连接数、最大空闲连接数等。
    可以获取连接池的状态信息，例如当前活跃连接数、当前空闲连接数等。
    可以获取连接池的属性，例如数据库驱动属性等。
    可以强制关闭连接池中的所有数据库连接。
    可以通过PooledConnection类创建数据库连接，并对数据库连接进行初始化和验证。

private只能在类内部访问，而protected可以在类内部和子类中访问。
    通常情况下，我们使用private修饰符来保护类的内部实现细节，防止外部代码对其进行意外修改，
    而使用protected修饰符来允许子类访问父类的成员，以实现继承和多态等面向对象编程的特性。

**********************************************************
手写MyBatis框架中的重点
    sqlSession对象的insert方法与selectOne方法
    insert
    需要将mapper中的sql占位符#{} 替换为 JDBC中占位符 ？
    然后获取#{}中的属性名，拼接get方法，获取对象中的属性值，然后对应给 ？ 占位符传值生成预编译sql语句
    然后执行
    selectOne
    首先占位符的操作同上，根据传递过来的参数，给占位符传值拼接sql语句并执行
    需要解析查询结果集 rs.getMataData
    然后将列名作为属性名，拼接set方法，将查询到的数据封装到对象中并返回

一开始使用SqlSessionUtils 封装了用来获取sqlSession的方法、提交方法、关闭方法等等
封装 工具 的思想
工具越来越多变成框架

***

在WEB应用中使用MyBatis，采用MVC架构模式

复习JavaWeb中学习的MVC架构模式
MVC：Viewer Controller  Model
三层架构：展示层 业务层 持久层
DAO：数据访问对象，专门负责数据库表的CRUD操作

流程
    从前端向后端的顺序

在更新完java内存中account对象的余额后，需要在数据库中也进行更新
此时需要事务机制，mybatis默认情况下不会自动提交事务，需要保证转出账户与转入账户的修改操作原子性

重点：事务的控制
    事务需要统一管理：
    旧：在DAO中，在selectByActNo方法和updateByActNo方法中使用sqlSession对象执行了sql语句，并直接提交事务
    应该在service业务方法中，先开启事务，然后调用完dao的方法处理完数据后，统一提交事务，最后返回处理结果
    故此：解决方案：dao类中提供commit方法供业务类service调用
        或者在service中获取sqlSession对象，保证这个对象与执行sql语句的会话对象一致，即可控制事务
        引入ThreadLocal，或者将sqlSession对象作为参数传给dao类
        即为：由service来管理sqlSession会话对象的开启与关闭

​	ThreadLocal中的对象是线程级别的，相当于线程域（会话域，请求域类比）
​    此线程中的所有方法共享其中的对象，保证一个线程对应一个sqlSession对象
​    将sqlSession对象放入ThreadLocal中，可以保证线程安全性，线程隔离，并发下多线程安全

​    业务处理完毕，关闭sqlSession对象
​    并从ThreadLocal中解绑，从当前线程中移除sqlSession对象
​    因为web容器支持线程池，用过的线程对象下一次请求还可能被使用，

​    以后会引入代理机制进行管理

方法之间数据的传递有可能需要加密

ThreadLocalMap是ThreadLocal类的内部类，用于存储每个线程的本地变量。它的实现是基于哈希表的，
    每个线程都有自己的哈希表，用于存储本地变量。
    ThreadLocalMap的优点是：
        线程安全：每个线程都有自己的哈希表，不会出现线程安全问题。
        高效：使用哈希表存储本地变量，查找和插入的时间复杂度都是O(1)。
        可扩展：哈希表的大小是根据需要动态调整的，可以根据实际情况灵活扩展。
    ThreadLocalMap的使用场景是：
    线程隔离：当多个线程需要访问同一个对象的时候，可以使用ThreadLocal来实现线程隔离，
        每个线程都有自己的本地变量，不会相互干扰。
    线程上下文传递：当需要在多个线程之间传递上下文信息的时候，可以使用ThreadLocal来存储上下文信息，
        每个线程都可以从ThreadLocal中获取自己的上下文信息。
    避免传递参数：当需要在多个方法之间传递参数的时候，可以使用ThreadLocal来存储参数，
        避免在方法之间频繁传递参数。

ThreadLocalMap使用哪一种Map集合。Java中常用的Map集合有
    HashMap、ConcurrentHashMap、Hashtable、TreeMap等。
    对于线程数较少的情况下，使用HashMap或ConcurrentHashMap都可以，
    它们的查找和插入操作都是O(1)的时间复杂度，效率比较高。但是，如果线程数较多，
    每个线程都有自己的哈希表，就会占用大量的内存，这时候可以考虑使用ConcurrentHashMap。
    如果需要线程安全，可以使用Hashtable，它的线程安全是通过synchronized实现的，效率比较低。
    如果需要有序性，可以使用TreeMap，它是基于红黑树实现的，可以保证元素的有序性。
    综上所述，对于ThreadLocalMap的实现，可以根据具体的需求选择不同的Map集合，
    一般情况下，使用HashMap或ConcurrentHashMap都可以满足需求。

ConcurrentHashMap是Java中线程安全的哈希表，它的实现基于分段锁（Segment），
    每个Segment实际上是一个小的哈希表，每个Segment都有自己的锁，不同的Segment之间互不影响，可以实现并发读写。
    ConcurrentHashMap的优点是：
        高并发：ConcurrentHashMap使用分段锁，可以实现高并发的读写操作。
        线程安全：ConcurrentHashMap是线程安全的，可以在多线程环境下使用。
        高效：ConcurrentHashMap的查找和插入操作都是O(1)的时间复杂度，效率比较高。
        可扩展：ConcurrentHashMap的大小是根据需要动态调整的，可以根据实际情况灵活扩展。
    ConcurrentHashMap的常见使用情景是：
    缓存：ConcurrentHashMap可以用于缓存，可以将缓存的数据存储在ConcurrentHashMap中，
        多个线程可以并发访问缓存，提高缓存的效率。
    并发计算：ConcurrentHashMap可以用于并发计算，多个线程可以并发地向ConcurrentHashMap中添加计算结果，
        最终得到最终的计算结果。
    并发读写：ConcurrentHashMap可以用于并发读写，多个线程可以并发地读写ConcurrentHashMap，
        提高并发读写的效率。
    分布式锁：ConcurrentHashMap可以用于分布式锁的实现，多个节点可以共享一个ConcurrentHashMap，
        使用Segment的锁机制可以实现分布式锁。
    总之，ConcurrentHashMap是一个高效、线程安全的哈希表，适用于多线程并发读写的场景，
        特别是在缓存、并发计算、并发读写、分布式锁等方面有着广泛的应用。

分布式锁是一种用于分布式系统中的同步机制，它可以保证在分布式环境下对共享资源的访问是互斥的。
    实现分布式锁的目的是为了避免多个客户端同时访问共享资源而导致的数据不一致或者业务逻辑错误。
    实现分布式锁的方式有很多种，常见的有：
        基于数据库：可以使用数据库的事务机制实现分布式锁。
        基于缓存：可以使用缓存的原子性操作实现分布式锁，如Redis的setnx命令。
        基于ZooKeeper：可以使用ZooKeeper的临时节点实现分布式锁。
    在实际开发中，分布式锁的使用场景很多，常见的有：
        防止重复提交：在高并发的场景下，用户可能会重复提交请求，可以使用分布式锁来避免重复提交。
        分布式任务调度：多个节点可能同时执行同一个任务，可以使用分布式锁来保证只有一个节点执行任务。
        分布式缓存更新：多个节点可能同时更新同一个缓存，可以使用分布式锁来保证只有一个节点更新缓存。
        分布式事务：在分布式事务中，需要保证各个节点的操作是同步的，可以使用分布式锁来实现。
********************************************************************************************
MyBatis三大对象的作用域Scope
    SqlSessionFactoryBuilder（短）
        这个类可以被实例化，使用和丢弃，主要功能是build，解析配置文件（事务管理器，数据源，映射文件及其配置等等），创建SqlSessionFactory对象
        一但创建了SqlSessionFactory，就不再需要它了，可以被垃圾回收器回收
        因此SqlSessionFactoryBuilder实例的最佳作用域是方法作用域，也就是局部方法变量。
        可以使用一个SqlSessionFactoryBuilder来创建多个SqlSessionFactory实例，但不要过久保留，保证所有的xml解析资源可以被释放给更重要的事情
    SqlSessionFactory
        SqlSessionFactory对象一旦被创建，就应该在应用的运行期间一致存在。不应该被丢弃或者重新创建另一个实例
        一个数据库对应一个SqlSessionFactory对象，在应用运行期间不可以重复创建多次，
        SqlSessionFactory的最佳作用域是应用作用域
        常见实现方式：单例模式或者静态单例模式 （静态代码块）
    SqlSession
        每个线程都应该有他自己的SqlSession实例，SqlSession是非线程安全的，不可以被共享。
        他的最佳作用域是请求域或者方法作用域。绝不可以将其引用放入静态域，实例变量，或任何类型的托管域中(例如HttpSession)
        如果是使用Web框架开发，因为web都是支持多线程的，如果要将sqlSession放入和Http请求类似的作用域中，
        就必须保证收到http请求，开启一个sqlSession,返回响应后，必须关闭。
        确保关闭可以使用try catch finally语句块，将关闭sqlSession的语句写入finally中

项目中存在的问题：
    Dao实现类，代码固定，代码重复，只负责增删改查，该类显得有些冗余
    引入：动态代理机制
        只要提供接口，可以在内存中直接动态生成dao的实现类
        javassist组件
        可以编辑和创建java字节码的类库，Javassist对字节码操作为JBoss实现动态 AOP 框架

**********************************************************************************************
Javassist
    可以理解为一种增强的Java反射机制

步骤：
    获取类池
    通过类池对象创造类(ct类)
    创造接口-将接口加入类中
    创造方法-将方法加入类中
    实现接口中所有方法，动态获取方法返回值类型，方法名，形参列表
    给方法对应的方法体，返回语句，动态拼接。
    在内存中生成class字节码文件

class.forName 类加载
获取并执行方法 invoke

重点
    需要根据xml配置文件mapper映射文件中的标签，来获取具体执行的sql语句种类
    而sqlSession对象中封装了映射文件中的配置，故调用此工具方法也需要传入sqlSession对象

sqlSession接口继承closeable接口
具有方法Configuration getConfiguration()
而Configuration类的对象封装了配置信息

SqlCommandType是枚举类型，共有下列六种
UNKNOWN, INSERT, UPDATE, DELETE, SELECT, FLUSH

对于需要的sqlId，myBatis具有明文***规定***：
sql语句的id是由框架使用者提供的，具有多变形，对于框架的开发人员来说是未知的。
于是规定：凡是使用GenerateDaoProxy机制生成dao实现类的，sqlId的命名规范是强制的
namespace必须是dao接口的全限定名称，id必须是dao接口中方法名
namespace + id 可以保证sqlId全局不会重复

**************************************************************************************************
MyBatis提供的代理机制
    mybatis使用了代理模式，可以动态的在内存中生成dao接口的代理类mapper，然后创建代理类的实例。
代码：
    private AccountDao accountDao = SqlSessionUtil.openSession().getMapper(AccountDao.class);
    使用sqlSession对象来调用，也需要对应接口的类作为参数传入

面向接口的方式进行CRUD
    SqlSession sqlSession = SqlSessionUtils.openSession();
    CaeMapper mapper = sqlSession.getMapper(CarMapper.class);
    使用mapper对象执行CRUD方法
    这个mapper就是代理机制动态生成的dao实现类，内部具有原来的selectOne，insert等方法
*************************************************************************************
MyBatis小技巧
    1、在mybatis中 #{} 和 ${} 都是占位符，区别是：
        #{} 底层使用PreparedStatement 预编译sql语句，然后给sql语句的占位符 ？ 传值
            原理见预编译sql对象的优劣
        ${} 底层使用Statement 先进行sql语句的拼接(String) 存在sql注入的风险
            例如需要传关键字，业务需求先拼接sql语句再执行的情况
            例如最后需要order by asc/desc 等情况，需要拼接sql语句，则采用此种情况
    举例：向SQL语句中拼接表名，现实业务中需要分表存储的时候。
        数据量十分庞大的时候，只用一张表存储，查询效率较低。
        可以将这些数据有规律的分表存储，来减少扫描的数据量，进而提高查询效率
        例如日志表：专门存储日志信息的t_log，这张表中每天都会产生很多log，随着时间的推移，表中数据会增多。
            可以每天生成一个信标，每张表以当前日期作为标志
            t_log20230520
            t_log20230521
            t_log20230522
            ...
        此时表名采用${}来进行拼接：SQL语句：
        insert into t_log_${date}("","","") values("","","");

2、批量删除。sql语句写法：
    or： delete from t_car where id=1 or id=2 or id=3;
    使用动态sql来完成
    in： delete from t_car where id in(1,2,3);
    依旧是字符串拼接，所以需要${}占位符来完成拼接  in(${ids})

3、模糊查询 like
    select * from t_car where brand like '%${奔驰}%';
    先进行sql语句拼接，使用${}占位符。使用#{}由于在引号中，则不会被识别，导致模糊查询出错。
    或者使用concat函数，使用mysql数据库的内置函数来进行字符串拼接
        brand like concat('%',#{brand},'%');
        brand like concat('%','${brand}','%'); 手动添加单引号
    或者使用双引号，比较常用
        brand like "%"#{奔驰}"%"
        让jdbc识别到中间的占位符 ？
4、别名机制
    在mybatis-config.xml文件中的typeAliases标签中
    <typeAliases>
        <typeAlias type="com.leowork.mybatis.pojo.Car" alias="Car">
    </typeAliases>
    别名不区分大小写，注意namespace不可以写别名，必须是接口的全限定类名
    alisa这个别名属性可以省略，默认情况就是类的简名，getSimpleName()
5、指定包名批量作为别名
    只要指定pojo类所在的包，则pojo下的所有java类都默认起别名
    <typeAliases>
        <package name="com.leowork.mybatis.pojo">
    </typeAliases>
6、mapper标签的配置
    <mappers>
        <mapper resource=""/>
        <mapper url=""/>
        <mapper class=""/>
    </mappers>
    resource:从类的根路径下开始查找资源，配置文件必须放入类路径当中
    url: 绝对路径 不要求配置文件放入类路径当中，只需要提供绝对路径即可。不推荐，移植性差
    class:  全限定接口名，带有包名
        如果指定com.leowork.mybatis.mapper.CarMapper
        则mabtis框架会到com/leowork/mybatis/mapper路径下寻找CarMapper.xml文件
        也就是需要保证CarMapper.xml文件与CarMapper接口必须在同一个目录下
        可以在java-resources目录下建一个与接口相同的目录结构
                     -com
                        -leowork
                            -mybatis
                                -mapper
        因为java下和java-resources下都被视为根路径
同理：可以配置package。开发中使用，前提是xml文件与接口必须放在同路径下，并且名字一致
    <mappers>
        <package name="com.leowork.mybatis.mapper">
    <mappers/>
    从根路径下开始寻找
    注意resources下建多重目录需要使用 / 因为resources下不是建包
7、使用IDEA配置mybatis-config.xml 和 mapper.xml 文件的模板
    file-settings 在设置中添加具体的模板代码
8、获取并使用自动生成的主键值
    useGeneratedKeys="true"  表示使用自动生成的主键
    keyProperty="id"         执行主键值赋值给对象的哪个属性字段,表示赋值给car对象的id属性
    <insert id="insertCar" useGeneratedKeys="true" keyProperty="id">
        insert into t_car values(null,#{carNum},#{brand});
    </insert>

*************************************************************************************
MyBatis参数处理*****核心*****
    接口中定义的方法的参数列表。（方法的参数可以向映射文件中的sql语句中的#{}占位符传值）
    myBatis具有类型自动推断机制
  **简单类型参数
        byte short int long float double char
        Byte Short Integer Long Float Double Character
        String
        java.util.Date
        java.sql.Date
  **Map参数
    map的key需要与占位符对应，传map就相当于传一个pojo
  **实体类参数
    pojo 类中参数名需要与占位符对应，并且查询语句需要起别名与属性名对应才返回查询结果数据包装的对象
        底层调用get set 方法来传值
  **多参数
    底层存在map集合中
    map.put("arg0", name);
    map.put("arg1", age);
    ...
    map.put("param1", name);
    map.put("param2", age);
    给占位符传值的时候，参数是arg 参数列表从左到右从0开始计数 对应
    或者param 参数列表从左到右从1开始计数 对应
    对应指代，可以混用
    #{param1},#{arg1}
  **@Param注解（命名参数）**重点
    可以将arg0,arg1,param1,param2...这种进行命名，增加可读性
    @Param("name")String name

*******************************************************************
代理模式
    代理对象
    代理方法
    目标对象
    目标方法
Param注解源码分析
    List<Student> students = mapper.selectByNameAndAge("张三", 20);
    由代理对象mapper，调用代理方法
        首先跳入MapperProxy代理类，动态代理 invoke方法
        Object invoke(Object proxy,Method method,Object[] args)
            Object proxy 代理对象
            Method method 目标方法
            Object[] args 目标方法的参数列表  {“张三”，‘男’}

​            getNamedParams(Object[] args)
​                使用names这一个SortedMap<Integer,String>可排序集合来存储指定的参数名与参数的对应
​                key：0 value：“name” -----entry对象
​                key：1 value：“sex”

​                然后会判断参数是否有注解(没有注解并且参数只有一个时候，直接返回) 多个注解时
​                hasParamAnnotation
​                有@Param注解时候，会新建一个param的map<String,Object>集合。
​                之后遍历这个集合，添加元素，对应关系
​                    param.put(entry.getValue(),args[entry.getKey()])
​                                “name”          args[0] “张三”

*****************************************************************************************
MyBaTis查询技巧专题
    mysql中字段名与java中pojo的属性名不一致时，可以在sql语句中起别名，来对应上。
    myBatis底层会调用对应的set get方法来给属性赋值

可以返回单个 Student
也可以返回多个 List<Student> students

在模糊查询的时候，返回结果条数未知，可能查不到为0，可能1条，可能多条
如果使用一个pojo类来接收多条记录就会报错，TooManyResultsException
因为一个pojo类来接收只能接收0或1条记录，因为底层会调用selectOne()
所以当返回结果对象多于1条的时候，就不可以采用pojo对象去接收，要采用List集合接收
相反，List集合可以接收接收0或1条或更多条记录(List集合底层有初始化容量以及自动扩容问题)

当返回的数据没有合适的实体类对应的话，例如多表联查，可以采用Map集合接收，字段名为key，字段值为value
查询觉果如果可以保证只有一条数据，返回一个map集合即可 Map<String,Object>   resultType="map"
如果为多条数据，则返回List<Map<String,Object>>集合，里面存储多个map形式的记录 底层调用selectList
当数据量较大的时候，List集合查找较为繁琐，可以采用Map<String,Map<String,Object>> 这样来接收 底层调用selectMap
    将查询结果map 对应的id主键 作为外层map的key
用法：
    @MapKey("id")
    Map<Long,Map<String,Object>> selectAllRetMap();
通常情况下实际开发中，非特殊情况一般都是建一个pojo来接收

****resultMap 结果映射 解决原本sql语句起 别名 的麻烦
    可以在mapper标签中定义一个结果映射，指定数据库表中的字段名和Java的pojo类的属性名的对应关系
        type属性为pojo类的类名
        id属性为resultMap的唯一标识，用于select标签中，提供resultMap取代原来的resultType

​    property属性为pojo类的属性名
​    column属性为数据库表的字段名
​    也可以配置写入#{}占位符中的属性，来不让MyBatis进行自动类型推断来提高效率
​    javaType属性
​    jdbcType属性

<resultMap id="" type="">
    主键配置为id标签(符合数据库设计范式) 提高myBatis执行效率
    <id property="" column=""/>
    如果属性名与字段名相同，可以省略不写
    <result property="" column=""/>
    <result property="" column=""/>
    <result property="" column=""/>
    ...
</resultMap>

****开启全局驼峰命名自动映射
    将数据库表中列的名字自动映射为Java对象属性名的驼峰命名格式
    前提：Java与Sql均遵循命名规范
    Java：首字母小写，后面每个单词首字母大写，遵循驼峰命名规范
    SQL：全部小写，单词之间采用下划线分隔
    举例：
    carNum   car_num
    produceTime   produce_time
    开启：
    在mybatis核心配置文件mybatis-config.xml文件中配置
    <settings>
        <setting name="mapUnderscoreToCamelCase" value ="true"/>
    </settings>
    注意：
    只适用与数据库表的列名与pojo类属性名可以对应上，以及部分汉语拼音及数字场合不适用

返回总记录条数
Long selectTotal();
对应
select count(*) from t_car
sql组函数 count 如果count(column_name) 会自动去除空值null

**********************************************************************
动态SQL
    实际开发中会遇到业务场景，需要对SQL语句进行动态拼接
    例如：批量删除，多条件查询等情况
        根据用户在前端动态选择的信息，后端接收，将其动态的拼接到sql中
    关键：掌握sql语法与标签灵活配合使用

if标签-多会被嵌套到其他标签中
    <if test="判断条件">
        sql片段
    </if>
    如果判断条件（布尔表达式）为真，则将sql片段拼接到sql语句中，反之不拼接
    test属性值必须是布尔型 T/F
    sql语句的拼接需要遵循sql语法，并且需要注意健壮性
    例如 where 1 = 1 and ... and ... and ...

choose when otherwise标签
    原理同 else if 语法
    <choose>
        <when></when>
        <when></when>
        <when></when>
        ...
        <otherwise></otherwise>
    </choose>
    只会有一个分支被选择

### 动态SQL-select

where标签
    让where子句更加动态只能，当所有条件都为空时不会生成where子句。
    并且自动去除某些条件前面多余的and或or
    <where>
        <if test="">

​    </if>
​    <if test="">

​    </if>
​    <if test="">

​    </if>
​    ...
</where>

动态SQL-select
trim标签
    prefix 在trim标签中的语句前添加内容
    suffix 在trim标签中的语句后添加内容
    prefixOverrides 前缀覆盖掉
    suffixOverrides 后缀覆盖掉
    也是用于多条件查询

动态SQL-update
set标签
    动态生成set关键字，并且可以自动去除最后多余的 ","
    当提交的数据为空时，生成的sql就不会包含这个对应的字段

### 动态SQL-delete-insert

foreach标签
    可以用来批量删除或插入数据，根据id主键操作，传参id数组
    作用是遍历数组，替代原来delete语句或insert语句 写入in(1,2,3...)或values(),(),()...中的数据
    delete语句 in的写法
    where id in (
    <foreach collection="ids" item="id" separator=",">
        #{id}
    </foreach>
    )
    delete语句 or 的写法
    where
    <foreach collection="ids" item="id" separator="or">
        id = #{id}
    </foreach>
    insert语句 传入的则为封装数据的pojo对象集合，批量插入
    values
    <foreach collection="cars" item="car" separator=",">
        (null,#{car.carNum},#{car.brand},#{car.guidePrice},#{car.produceTime},#{car.carType})
    </foreach>

sql标签与include标签-了解
    可以用来提取公共sql代码，类似配置文件中的 properties标签 配置全局变量
    声明sql片段：
        <sql id="carColumnNames">
            sql片段，增强复用性
        </sql>
    引用sql片段：
        select
            <include refid="carColumnNames">
        from t_car

**************************************************************************
之前一对一的对应关系叫低级映射

### MyBatis的高级映射以及延迟加载

​    多张表的情况，多对一，一对多，数据与数据之间存在对应关系(外键，关系表等)

分析ORM中的映射关系
多的一方是Student 一的一方是Cls
谁在前谁是主表 多对一 多是主表 一对多 一是主表

情况：
    多对一：多个学生对应一个班级
    在JVM中：Student对象为主对象，Cls对象为副对象
        可以在Student类中提供Cls属性，让Student对象中具有对应的Cls对象的引用，产生关联
    MyBatis的处理方式：
        一：一条sql语句，级联属性映射(join)
        二：一条sql语句，association 标签
        三：两条sql语句，分步查询，常用方法。优点：复用性强，并且支持懒加载（延迟加载）

分步查询：
    <association property="cls"
                 select="com.leowork.mybatis.mapper.ClsMapper.selectByIdStep2"
                 column="cid"/>
    复用性：
        可以单独修改每一步骤，大步骤拆解成N多小步骤，也会更加灵活，
    延迟加载(懒加载)：
        用到的时候再执行查询语句，不用的时候不查询。尽可能少查，可以提高性能，
        ORM的各种实现框架基本都支持延迟加载机制
    延迟加载的开启：
        association标签中添加 fetchType="lazy"
        在标签中的设置是局部设置，只针对当前标签中的sql语句
        开启全局延迟加载需要在mybatis-config核心配置文件中配置settings
            添加lazyLoadingEnabled="true"
********建议开启全局延迟加载，局部取消局部设置eager
    注意：默认情况下，不开启延迟记载，即为：fetchType="eager"

一对多的映射原理
    一个班级对应多个学生
    班级表为一的一方，学生表为多的一方，一对多，一在前，t_class班级表为主表，t_stu学生表是副表
    在班级表中添加一个集合，描述这一个班级中对应的多个学生
        例如 List<Student> students;
    MyBatis的处理方式：
        一：collection标签
            描述主对象Cls类中的集合属性，一个班级中包含多个学生的对应关系
            <collection property="students" ofType="Stu">
                <id property="sid" column="sid"/>
                <result property="sname" column="sname"/>
            </collection>
        二：分步查询
            <collection property="students" ofType="Stu">
                <id property="sid" column="sid"/>
                <result property="sname" column="sname"/>
            </collection>
        同样具有复用性强与提高性能的优点、

其他情况：
    多对多映射关系：分解为两个一对多
    一对一映射关系：
************************************************************************************
MyBatis的缓存机制
    实际上，各大关系型数据库中的数据，都是存储在文件当中，
    ORM O 对象是JVM存储在内存中，与文件相互映射
    缓存的理解：硬盘是持久化设备。
        执行DQL(select查询语句)的时候，将查询结果放入缓存当中(内存当中)
        如果下一次还是执行完全相同的dql语句，直接从缓存中拿数据，不再查数据库(去硬盘上找数据)
        可以提高执行效率，减少IO次数(读写文件)
        （如果两条相同dql之间有增删改操作，则mybatis会将原来的缓存清空，下一次dql只能再从硬盘中找）
Cache
缓存机制是程序开发中优化程序执行效率的重要手段，常见的技术有：
    字符串常量池
    运行时常量池
    数据库连接池
    线程池
    Redis
    根据实际业务，数据情况，调整缓存的度

MyBatis提供的缓存：（缓存只针对dql语句）
    一级缓存：将查询到的数据存储到SqlSession中(类似于web中的Session会话域)（当前sql会话的环境，范围较小）
    二级缓存：将查询到的数据存储到SqlSessionFactory中（一个数据库一个，代表整个数据库的环境，范围较大）
    继承其他的第三方缓存：例如Java开发的EhCache，C开发的MemCache

缓存中会存放查找到的java对象

一级缓存：
    默认开启，不需要配置
    原理：只要使用同一个SqlSession对象执行同一条dql语句，就会走缓存
    不走缓存：SqlSession对象不同，或者dql不同
    失效：在第一次dql与第二次dql之间做了：
        执行sqlSession的clearCache()方法，手动清空缓存
        执行insert delete update增删改语句(与是否影响当前的表无关)
        此时缓存失效

二级缓存：四条缺一不可
    添加标签
    <cache/> （一）
    表示当前sqlMapper.xml文件中所有sql语句 使用二级缓存
    二级缓存也是默认开启的，但是需要对每个mapper xml文件单独开启。（二）
    并且使用二级缓存的实体类对象必须是可序列化的，也就是必须实现java.io.serializable接口  （三）
    SqlSession对象关闭或者提交之后，一级缓存中的数据才会被写入到二级缓存当中，此时二级缓存才可以使用。  （四）
    默认情况下首先从一级缓存中找数据（就近原则）

Cache Hit Ratio 缓存命中率

sqlSession1.close();  表示sqlSession1中的一级缓存中的数据会放入二级缓存

<cache/>的配置
flushInterval 刷新间隔，不配置则不刷新。一刷新缓存失效
size 设置二级缓存中存储java对象的数量上限，默认为1024，超出则开始eviction
eviction 剔除，指定从缓存中某个对象的淘汰算法，默认采用LRU策略
    LRU 最近最少使用(不代表不常用) 间隔时间内使用频率最低的对象
    LFU 最少使用
    FIFO 先入先出（队列Queen）数据缓存器
    SOFT 软引用被淘汰
    WEAK 弱引用被淘汰
readOnly
    true 多条相同的sql语句执行之后返回的对象是共享的同一个（引用指向相同的JVM中的对象），
            性能好，但存在多线程并发安全问题
    false 多条相同的sql语句执行之后返回的对象是克隆出的副本，调用了clone方法，性能降低，但多线程安全

缓存集成EhCache
    EhCache是替代自带的二级缓存的，一级缓存是无法替代的
    引入依赖 mybatis-ehcache
    类的根路径下新建ehcache.xml文件，并完成配置
    修改SqlMapper xml文件中的cache标签，添加type属性
        type="org.mybatis.caches.ehcache.EhcacheCache"

**********************************************************************************
MyBatis的逆向工程
    根据数据库表你想生成Java的pojo类，SqlMapper.xml文件，以及Mapper接口等
    需要使用逆向工程插件
    编写配置文件generatorConfig.xml
    配置信息：
        pojo类名，包名以及生成未知
        SqlMapper xml文件名以及生成未知
        Mapper接口名以及生成位置
        链接数据库的信息
        指定参与逆向工程的表
    可以完成基础内容的编写，然后根据业务需求进行完善
    业务中的CRUD通常较为复杂繁琐，需要多次调整

增强版 通过targetRuntime来设置
<context id="DB2Tables" targetRuntime="MyBatis3">
会提供更多的方法以及Example(封装查询条件的类)
使用：
    mapper.selectByExample(查询条件);  //没有则填写null
QBC查询风格
    先新建CarExample对象来封装查询条件
    CarExample carExample = new CarExample();
    调用其中的各种方法来创建查询条件(写where后的条件，不过是java语法) 会根据字段名生成对应条件的方法
    举例：
        carExample.createCriteria().andBrandEqualsTo(...).andGuidePriceGreaterThan()...;
        等同于 where ... and brand = #{...} and guide_price > #{...}
        类似的还有Like Between GreaterThan in IsNotNull LessThen 等等表示关系的方法
        也可以.or()
这种面向对象，使用方法描述条件，不直接编写sql语句，看不到sql语句的查询方式
叫QBC查询风格 Query by Criteria

******************************************************************************
MyBatis使用PageHelper分页工具
    标准通用的分页sql语句：limit关键字
    语法:
        limit startIndex(起始下标从0开始，为0时可以省略),pageSize
    select * from table_name
    limit
        (pageNum - 1) * pageSize,pageSize;
    可以取表中的部分数据

用户从浏览器前端发送的请求，会携带页码pageNum给服务器
每一次在进行分页请求发送的时候，都是要发送两个数据的，页码pageNum和每页显示的记录条数pageSize
    例：uri?pageNum=5&pageSize=10

使用myBatis的分页插件：
    可以完成较难的与分页相关的数据，例如是否还有下一页，上一页，分页导航，总页数等等
    使用：
    maven引入依赖：
        pagehelper
    在mybatis-config.xml中配置插件
        plugin interceptor="com.github.pagehelper.PageInterceptor"
    编写测试程序
    获取PageInfo对象（PageHelper提供的用来封装分页相关信息的对象）
        new PageInfo<Cars>(查询结果对象, 页面导航数)
        包含了诸多信息
        PageInfo{pageNum=2, pageSize=3, size=3, startRow=4, endRow=6, total=7, pages=3,
        list=Page{count=true, pageNum=2, pageSize=3, startRow=3, endRow=6, total=7, pages=3, reasonable=false, pageSizeZero=false}
        [Car{id=14, carNum='123', brand='BYD', guidePrice=10.00, produceTime='2020-11-11', carType='电车'},
        Car{id=15, carNum='155', brand='秦', guidePrice=30.00, produceTime='2020-09-05', carType='电车'},
        Car{id=16, carNum='456', brand='丰123田', guidePrice=1233.00, produceTime='2123-10-11', carType='电车'}],
        prePage=1, nextPage=3, isFirstPage=false, isLastPage=false, hasPreviousPage=true, hasNextPage=true, navigatePages=3,
        navigateFirstPage=1, navigateLastPage=3, navigatepageNums=[1, 2, 3]}
        可以通过carPageInfo的各种方法来调用上面的参数 get方法

*****************************************************************************
MyBatis的注解式开发(了解)
    可以减少sql映射文件的配置，对于十分简洁的sql语句可以采用注解这种形式。对于稍微复杂的sql语句建议配置xml
    注解添加在Mapper接口的方法上
    @Insert("sql语句")

@Delete("sql语句")

@Update("sql语句")

@Select("sql语句")

写在上述注解语句下，一同注解方法，(同简单的resultMap)用来对应pojo类的属性名与表的列名
@Results({
    @Result(property = "" ,column = ""),
    @Result(property = "" ,column = ""),
    @Result(property = "" ,column = "")
})

***

## SPRING















