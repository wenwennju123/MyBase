# 群友Java面试题学习-0805

## 01-算法类

**areas.txt保存了以下数据，行内数据为：地名, 行政级别,上级地名（请留意东莞无区县这一级）**

```
长潭社区居委会，村，五经富镇；
五经富镇，镇街，揭西县；
揭西县，区县，揭阳市；
揭阳市，市，广东省；
广东省，省；
林村居委会，村，塘厦镇；
塘厦镇，镇街，东莞市；
东莞市，市，广东省；
福中居委会，村，莲花街道；
莲花街道，镇街，福田区；
福田区，区县，深圳市；
深圳市，市，广东省；
```

### Q：请写一份代码，实现传入地名，按以下格式输出它本身和所有上级，如传入“福中居委会”，输出：

```
福中居委会
	|-莲花街道
		|-福田区
			|-深圳市
				|-广东省
```

### ANS：

#### gpt-4o

将txt文件中的数据读取，并存储为树形结构。输入地名时，查找该地名所在节点并输出其所有父节点

```Java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* 封装每行的信息的类
* 算法题，直接写在主类里
**/
class Location {
    //地名
    String name;
    //行政级别
    String level;
    //上级地名
    String parentName;
    //存储所有的下级Location
    List<Location> children;
    //记得提供无参构造
	//全参构造
    Location(String name, String level, String parentName) {
        this.name = name;
        this.level = level;
        this.parentName = parentName;
        this.children = new ArrayList<>(8);
    }
	//重写toString()
    @Override
    public String toString() {
        return "Location{name='" + name + '\'' + ", level='" + level + '\'' + ", children=" + children + '}';
    }
}
/**
* 主类，含main方法入口
**/
public class LocationTree {
	//维护一个map存储所有的Loaction
    private static Map<String, Location> locationMap = new HashMap<>(16);
	//main入口
    public static void main(String[] args) {
        String fileName = "question.txt";
        readFile(fileName);
        Location root = buildTree();
        printTree(root, 0);
    }
	//读取文件并封装Loaction
    private static void readFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // 使用BufferedReader逐行读取
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("，");
                String name = parts[0].trim();
                String level = parts[1].trim();
                //没有上级则将parentName设置为null
                String parentName = parts.length == 3 ? parts[2].trim() : null;
                //将解析后的Location构造赋值并存入map
                locationMap.put(name, new Location(name, level, parentName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	//创建Location树形结构
    private static Location buildTree() {
        Location root = null;
        //遍历树形结构，没有上级节点的是根节点，其余节点根据其上级地点名添加到对应父节点的子节点列表中
        for (Location location : locationMap.values()) {
            if (location.parentName == null) {
                root = location;
            } else {
                Location parent = locationMap.get(location.parentName);
                if (parent != null) {
                    parent.children.add(location);
                }
            }
        }
        return root;
    }
	//打印树形结构
    private static void printTree(Location location, int level) {
        if (location == null) return;
        for (int i = 0; i < level; i++) {
            System.out.print("--");
        }
        System.out.println(location.name);
        // 递归打印树形结构
        for (Location child : location.children) {
            printTree(child, level + 1);
        }
    }
    // 查找指定地名并依照格式打印其所有父节点
    private static void printAncestors(String name) {
        Location location = locationMap.get(name);
        if (location == null) {
            System.out.println("未找到该地名：" + name);
            return;
        }

        List<Location> ancestors = new ArrayList<>();
        while (location != null) {
            ancestors.add(location);
            location = locationMap.get(location.parentName);
        }

        Collections.reverse(ancestors);
        for (int i = 0; i < ancestors.size(); i++) {
            for (int j = 0; j < i; j++) {
                System.out.print("    |-");
            }
            System.out.println(ancestors.get(i).name);
        }
    }
}

```
树形数据打印如下：

```
广东省
--揭阳市
----揭西县
------五经富镇
--------长潭社区居委会
--东莞市
----塘厦镇
------林村居委会
--深圳市
----福田区
------莲花街道
--------福中居委会
```

打印结果如下

```
福中居委会
    |-莲花街道
        |-福田区
            |-深圳市
                |-广东省
================================
请输入地点：并按Enter键确认输入
福田区
福田区
    |-深圳市
        |-广东省
================================


================================
请输入地点：并按Enter键确认输入
长潭社区居委会
长潭社区居委会
    |-五经富镇
        |-揭西县
            |-揭阳市
                |-广东省
================================


================================
请输入地点：并按Enter键确认输入
林村居委会
林村居委会
    |-塘厦镇
        |-东莞市
            |-广东省
================================
```

### ANS：

#### E:\00群友面经\Answers\AnswerForQuestion\com\leowork\answers\AnswersForQuestions.java

```Java
package com.leowork.answers;

import javax.xml.transform.Source;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 封装每行信息的类
 * 算法题，直接写在主类里面
 * @author Leo
 */
class Location{
    private String name;
    private String level;
    private String parentName;
    /**
     * 存储子节点关系
     */
    private List<Location> children;
    public Location() {}
    public Location(String name, String level, String parentName) {
        this.name = name;
        this.level = level;
        this.parentName = parentName;
        //初始化List<Location> children
        children = new ArrayList<>(8);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {        this.name = name;    }
    public String getLevel() {        return level;    }
    public void setLevel(String level) {        this.level = level;    }
    public String getParentName() {        return parentName;    }
    public void setParentName(String parentName) {        this.parentName = parentName;    }
    public List<Location> getChildren() {        return children;    }
    public void setChildren(List<Location> children) {        this.children = children;    }
    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", parentName='" + parentName + '\'' +
                ", children=" + children +
                '}';
    }
}
/**
 * 主类，包含main方法入口
 * @author Leo
 * @version 1.0
 * @className Answers
 * @since 1.0
 **/
public class AnswersForQuestions {
    /**
     * 维护一个hashMap郝村所有的Location
     */
    private static Map<String, Location> locationMap = new HashMap<>(16);

    /**
     * 入口
     * @param args args
     */
    public static void main(String[] args) {
        //指定文件名
        String fileName = "E:\\00群友面经\\areas.txt";
        readFile(fileName);
        Location root = buildTree();
        //测试打印完整树形结构，并用于结果校对
        printTree(root, 0);
        System.out.println("================================");
        //算法题测试输入
        printLocation("福中居委会");
        //接收用户输入的地名
        System.out.println("请输入地点：并按Enter键确认输入");
        Scanner scanner = new Scanner(System.in);
        String targetLocation = scanner.nextLine();
        scanner.close();
        //根据用户输入的地点，查询其父级地点并按照格式打印
        printLocation(targetLocation);
    }
    /**
     * 读取文件并封装Location
     * @param fileName 文件名
     */
    private static void readFile(String fileName) {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = br.readLine()) != null){
                String[] parts = line.split("，");
                String name = parts[0].trim();
                String level = parts[1].trim();
                //没有上级则将parentName设置为null
                String parentName = parts.length == 3 ? parts[2].trim().substring(0, parts[2].trim().length() - 1) : null;
                //将解析后的Location构造赋值并存入map
                locationMap.put(name, new Location(name, level, parentName));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * 创建Location树形结构
     */
    private static Location buildTree() {
        Location root = null;
        //遍历树形结构，没有上级节点的是根节点，其余节点根据其上级地点名添加到对应父节点的子节点列表中
        for (Location location : locationMap.values()){
            if (location.getParentName() == null){
                root = location;
            } else {
                Location parent = locationMap.get(location.getParentName());
                if (parent != null){
                    parent.getChildren().add(location);
                }
            }

        }
        return root;
    }
    /**
     * 打印树形结构
     */
    private static void printTree(Location location, int level) {
        if (location == null){
            return;
        }
        //遍历
        for (int i = 0; i< level; i++){
            System.out.print("--");
        }
        System.out.println(location.getName());
        //递归遍历子节点
        for (Location child : location.getChildren()){
            printTree(child, level + 1);
        }
    }
    /**
     * 查找到目标地点，并依照格式打印其所有父节点
     */
    private static void printLocation(String targetLocation) {
        //依次保存其所有父节点
        List<Location> locationList = new ArrayList<>(8);
        //依照输入的String获取对应的Location对象
        Location location = locationMap.get(targetLocation);
        if (location == null){
            System.out.println("未找到该地点");
            return;
        }
        //打印第一级
        System.out.println(targetLocation);
        Location parentLocation = locationMap.get(location.getParentName());
        //父节点不为空，则取出再上一级
        while (parentLocation != null){
            locationList.add(parentLocation);
            parentLocation = locationMap.get(parentLocation.getParentName());
        }
        //打印
        for (int i = 0; i <= locationList.size() - 1; i++){
            System.out.print("    ");
            for (int k = 0; k< i; k++){
                System.out.print("    ");
            }
            System.out.println("|-" + locationList.get(i).getName());
        }
        System.out.println("================================");
    }
}
```

## 02-SQL类-考察：单表自连接

有成绩表数据如下，缺少的数据为缺考

```sql
+--------+-------+-------+
|  姓名  |  科目  |  成绩  |
+--------+-------+-------+
| 张三   | 数学   | 90    |
| 张三   | 语文   | 50    |
| 张三   | 地理   | 40    |
| 张三   | 政治   | 65    |
| 李四   | 语文   | 55    |
| 李四   | 数学   | 46    |
| 李四   | 政治   | 55    |
| 王五   | 语文   | 45    |
| 王五   | 政治   | 30    |
+--------+-------+-------+

```

### Q：查询出各科参加考试人员的人数、平均成绩和及格率

考察：group 的理解，数字的转化

### ANS：

```sql
SELECT
    科目,
    COUNT(*) AS 参加人数,
    ROUND(AVG(成绩), 2) AS 平均成绩,
    CONCAT(ROUND(SUM(IF(成绩 >= 60, 1, 0)) / COUNT(*) * 100, 0), '%') AS 及格率
FROM
    score
GROUP BY
    科目;
```

#### 结果输出：

```sql
语文,3,50.00,0%
数学,2,68.00,50%
地理,1,40.00,0%
政治,3,50.00,33%
```

### Q：查询出各科缺考人员名单

考察：子查询、连接查询

### ANS：

```sql
#子查询：查所有姓名
WITH Students AS (
    #去重
    SELECT DISTINCT 姓名
    FROM score
),
	#子查询：查所有科目
     Subjects AS (
         #去重
         SELECT DISTINCT 科目
         FROM score
     ),
     #子查询：将上述两个子查询合并
     StudentSubjects AS (
         SELECT s.姓名, su.科目
         FROM Students s, Subjects su
     ),
     #将学生与科目表与score表（主表）链接，条件是在score表中同时拥有姓名和科目，这样未匹配上的数据，其成绩字段就为空
     #此步骤就已经得出正确结果，下面的查询只是输出全部
     MissingScores AS (
         SELECT ss.姓名, ss.科目
         FROM StudentSubjects ss
                  LEFT JOIN score sc ON ss.姓名 = sc.姓名 AND ss.科目 = sc.科目
         WHERE sc.成绩 IS NULL
     )
-- 查询出缺考人员的名单
SELECT 姓名, 科目
FROM MissingScores
ORDER BY 姓名, 科目;
```

#### 结果输出：

```sql
李四,地理
王五,地理
王五,数学
```

### Q：查询出每一科都不及格的人员的名单以及该人员的平均分

此问题的条件是先查每一科都不及格的，而不是直接查平均分不及格的。因为有可能1科及格其他均不及格最终平均分也不及格。

考察：子查询，链接查询


### ANS：

```sql
-- 子查询，找出每个学生及其不及格的科目数
WITH StudentFailCounts AS (
    SELECT 姓名, COUNT(*) AS 不及格科目数, AVG(成绩) AS 平均分
    FROM score
    WHERE 成绩 < 60
    GROUP BY 姓名
),
-- 子查询，计算每个学生的总科目数
     StudentTotalCounts AS (
         SELECT 姓名, COUNT(*) AS 总科目数
         FROM score
         GROUP BY 姓名
     )
-- 找出每一科都不及格的人的名单及其平均分
SELECT f.姓名, ROUND(f.平均分, 2) AS 平均分
FROM StudentFailCounts f
         JOIN StudentTotalCounts t ON f.姓名 = t.姓名
WHERE f.不及格科目数 = t.总科目数;
```

#### 结果输出：

```sql
李四,52.00
王五,37.50
```

## 03-SQL类-考察：多表联查

现有如下表数据

user_list表

```sql
+-------+-----------+------------+
| id    | user_name |  birthday  |
+-------+-----------+------------+
| ubcda | 张三       | 1980-11-30 |
| ubcdb | 李四       | 1971-03-10 |
| ubcdc | 王五       | 1983-06-20 |
| ubcdd | 刘二       | 1951-02-22 |
| ubcde | 李七       | 1974-01-30 |
+-------+-----------+------------+
```

doctor_list表


```sql
+-------+----------+
| id    | doct_name |
+-------+----------+
| dbcda | 医生三   |    
| dbcdb | 医生四   |
| dbcdc | 医生五   |
| dbcdd | 医生二   |
| dbcde | 医生七   |
+-------+----------+
```

hm_body_check表


```sql
+-------+----------+-----------------+----------+
| id    | user_id  | check_date      | doctorId |
+-------+----------+-----------------+----------+
| c123x | ubcda    | 2017-01-03      |  dbcda   |
| c124x | ubcda    | 2018-04-10      |  dbcdb   |
| c125x | ubcdb    | 2017-01-03      |  dbcdc   |
| c126x | ubcdb    | 2018-05-12      |  dbcdd   |
| c127x | ubcdc    | 2017-01-03      |  dbcde   |
| c128x | ubcdc    | 2018-02-08      |  dbcdc   |
| c129x | ubcdc    | 2019-02-05      |  dbcda   |
| c123y | ubcdd    | 2017-02-03      |  dbcdb   |
| c124y | ubcdd    | 2018-03-02      |  dbcda   |
| c125y | ubcdd    | 2019-01-09      |  dbcdd   |
| c126y | ubcde    | 2017-11-04      |  dbcdb   |
| c127y | ubcde    | 2018-10-03      |  dbcdc   |
+-------+----------+-----------------+----------+
```

### Q：查询出hm_body_check中每个人的体检次数，最后体检日期以及最后体检医生

### ANS：

```sql
SELECT 
    u.user_name AS 用户名,
    COUNT(c.id) AS 体检次数,
    MAX(c.check_date) AS 最后体检日期,
    d.doct_name AS 最后体检医生
FROM 
    hm_body_check c
# 链接查询，根据user_id，拿到user信息
JOIN 
    user_list u ON c.user_id = u.id
# 链接查询，根据doctor_id，拿到doctor信息
JOIN 
    doctor_list d ON c.doctorId = d.id
# 子查询，拿到每一个人的最后体检日期。日期类型的MAX就可以拿到最后日期
WHERE 
    c.check_date = (
        SELECT MAX(c2.check_date)
        FROM hm_body_check c2
        WHERE c2.user_id = c.user_id
    )
GROUP BY 
    u.user_name
ORDER BY 
    u.user_name;
```

## 04-Java开发类-考察：定时任务，爬虫，数据库批量操作

### Q：现有任务如下：从给定表中，拿取key，并从第三方搜索引擎网站例如百度，抓取含该关键词key的第一条结果的页面内容。第一条结果的标题保存为 title 字段，内容取简介保存为content字段。

爬取成功，对应key，对数据表中存入 title，content，status状态字段设置为success，completed_on字段记录成功时间

在爬取过程中，可能由于网络等原因爬取失败，这时将status状态字段设置为fail，completed_on字段、title、content均为默认值null。并进行重试，重新爬取。（这里我建议爬虫程序先重试几次后均失败再向数据库中写入失败信息）

百度的搜索为： [春节_百度搜索 (baidu.com)](https://www.baidu.com/s?wd=春节)

```html
URL：
https://www.baidu.com/s?wd=%E6%98%A5%E8%8A%82
https://www.baidu.com/s?wd=春节
这种也叫做百分号编码。汉字等非ASCII的字符会被转换成特定的编码方式，以便在网络传输中不会出现错误和乱码。这样的编码只包含ASCII字符，不会因为包含特殊字符例如空格，汉字等而导致乱码问题。

第一个标签：
<span>
    <!--597-->
    <em>春节</em>
    "（Spring Festival），是中国民间最隆重最富有特色的传统节日之一。一般指除夕和正月初一，是一年的第一天，又叫阴历年，俗称“过年”，从腊八或小年开始，到元宵节，都叫过年。
    <em>春节</em>
    "历史悠久，起源于早期人类的原始信仰与自然崇拜，由上古时代岁首祈岁祭..."
    <!--597-->
</span>
```

提供task数据表，供1000万条数据。

task表：

表设计的status比较臃肿，自处建议只有1、0、即为成功或失败，默认是失败。并且以statu建立索引，全部都是基于status的判断，加索引好。

```sql
+-----------------------------+------------+----------------------------------+
| id          | key    |   title   |  content    |  status      |  completed_on
+-----------------------------+------------+----------------------------------+
| 1           | 中国   |   NULL    |  NULL       |  未完成       |  NULL
| 2           | 新冠   |   xxxxx   |  yyyyyyy    |  已完成       |  2021-02-11 10:30
| 3           | 春节   |   NULL    |  NULL       |  进行中       |  2021-02-11 10:30
| 4           | 宁夏   |   xxxxx   |  yyyyyyy    |  异常         |  NULL

…..

| 10000000    | 宁夏   |   xxxxx   |  yyyyyyy    |  异常         |  NULL
```

#### 1、描述此任务实现的过程，难点，潜在的问题以及解决思路

百度的反爬虫

#### 2、说明代码结构，类结构设计，方法及功能设计以及实现逻辑。

思路：爬取任务以及重试任务。爬取任务为例效率不加判断无论成功与否都进行写入。重试任务加入重试3次机制。

也可以不涉及重试任务，直接将重试逻辑加入爬取任务，然后多次执行爬取任务即可。

并行操作就得保证每次从数据库表中读取到的id是不重复的。否则有可能出现写入覆盖的情况。

#### 3、预估耗时，最终爬取均需要成功，任务将部署在三台64g的内存的服务器上同时运行。

三台即为设计数据分片，根据id分片分为三份。

爬的时间远高于数据库操作时间，起决定性因素的就是爬一次平均耗时以及每台服务器可以同时运行多少爬虫（mq消费者）

**即网络I/O的耗时远高于磁盘I/O，**并且涉及到网页获取与解析。

对于每次读取的1w条数据，爬虫耗时远高于数据库操作时间。

主要影响因素在于单台服务器上同时运行的**爬虫（消费者）数量**，以及平均单次爬取解析所消耗的**单次时间**。

爬取解析时间取决于**网络IO**

爬虫数量取决于**cpu线程数**以及**内存**大小以及**单个爬虫占多少cpu线程**。布置过多则会导致多余的实例分不到cpu解析速度变慢。解析所需的计算越多，单个爬虫所需的cpu资源越多。

#### ANS：

首先爬虫程序会根据 key （可能要转换编码格式例如百分号编码URL）与 要爬的 URL 解析到了标签中的内容，假定返回结果json为

需要与爬虫程序约定好返回的json格式，字段，便于解析。

```json
<!-- 发送给爬虫的json信息 -->
{
    "id":"3",
    "key":"春节",   
}

<!-- 成功返回的json信息 -->
{
    "id":"3",
    "key":"春节",
    "title":"春节_百度百科",
    "content":"春节是中国民间最富有传统特色的节日之一。一般指除夕和正月初一，是一年的第一天，又叫阴历年，俗称“过年”，从腊八或小年开始，到元宵节，都叫过年。",
    "status":"success",
    "completedOn":"2021-02-11 10:30"
}

<!-- 失败返回的json信息 -->
{
    "id":"3",
    "key":"春节",
    "title":"",
    "content":"",
    "status":"fail",
    "completedOn":""
}
```

要实现这个任务，我们需要设计一个高效的分布式爬虫系统。以下是详细的设计和实现步骤，包括数据库操作、批量读取写入、并行处理和优化建议。

1. 数据库设计

假设 `task` 表的结构如下：

```sql
CREATE TABLE task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    `key` VARCHAR(255) NOT NULL,
    title VARCHAR(255) DEFAULT NULL,
    content TEXT DEFAULT NULL,
    status VARCHAR(50) DEFAULT NULL,
    complete_on DATETIME DEFAULT NULL
);
```

2. 程序设计

##### 使用 Spring Boot + MyBatis 来实现。程序将分为以下几个部分：

1. **数据读取**：从数据库中读取任务。
2. **爬虫实现**：根据关键字拼接 URL，爬取数据。
3. **数据写入**：将爬取到的数据写入数据库。
4. **任务调度和重试机制**：处理失败任务并重试。

###### 数据读取TaskMapper.java

```java
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface TaskMapper {

    @Select("SELECT id, `key` FROM task WHERE status IS NULL LIMIT #{limit}")
    List<Task> getPendingTasks(int limit);

    @Update("UPDATE task SET title=#{title}, content=#{content}, status=#{status}, complete_on=#{completeOn} WHERE id=#{id}")
    void updateTask(Task task);

    @Update("UPDATE task SET status=#{status}, complete_on=#{completeOn} WHERE id=#{id}")
    void updateTaskStatus(Task task);
}
```

###### 爬虫实现，使用 `RestTemplate` 来实现爬虫。WebScraper.java

```java
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Date;

@Component
public class WebScraper {

    private final RestTemplate restTemplate = new RestTemplate();

    public Task fetchTaskDetails(Task task) {
        String url = "https://www.baidu.com/s?wd=" + task.getKey();
        // 伪代码：根据 URL 爬取数据
        // String response = restTemplate.getForObject(url, String.class);

        // 假设我们解析 response 并获取 title 和 content
        // 这里使用伪数据
        String title = "示例标题";
        String content = "示例内容";

        task.setTitle(title);
        task.setContent(content);
        task.setStatus("success");
        task.setCompleteOn(new Date());
        
        return task;
    }
}
```

Service 层TaskService.java

```java
import java.util.List;

public interface TaskService {
    void processTasks();
    void retryFailedTasks();
}
```

###### TaskServiceImpl.java

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private WebScraper webScraper;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    @Transactional
    public void processTasks() {
        List<Task> tasks = taskMapper.getPendingTasks(1000); // 批量读取

        for (Task task : tasks) {
            executorService.submit(() -> {
                //多线程开三个爬虫一同处理这1000个批量读取的task数据
                Task updatedTask = webScraper.fetchTaskDetails(task);
                //这里是不论成功与否都进行了写入。后续再根据status判断。为了提高操作的效率，不判断，先全部过一次。
                //这里也可以使用MQ来配合MyBatis进行批量插入
                taskMapper.updateTask(updatedTask);
            });
        }
    }
	//重试被定义了一个任务，逻辑依旧是读取status为fail的数据
    @Override
    @Transactional
    public void retryFailedTasks() {
        // 重试逻辑类似，读取 status 为 fail 的任务进行重试
        List<Task> tasks = taskMapper.getPendingTasks(1000);

        for (Task task : tasks) {
            executorService.submit(() -> {
                int retryCount = 0;
                while (retryCount < 3) { // 重试3次
                    Task updatedTask = webScraper.fetchTaskDetails(task);
                    //判断是否成功
                    if ("success".equals(updatedTask.getStatus())) {
                        taskMapper.updateTask(updatedTask);
                        break;
                    }
                    retryCount++;
                }
                // 重试3次
                // 如果有坏数据，可能反复进行重试任务很多次都还是失败，需要更新表记录重试次数，太多的标记为坏数据，status变为-1并写入另一个表等方式。
                if (retryCount >= 3) {
                    task.setStatus("fail");
                    taskMapper.updateTaskStatus(task);
                }
            });
        }
    }
}
```

###### Controller 层TaskController.java

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/process-tasks")
    public String processTasks() {
        taskService.processTasks();
        return "Task processing started";
    }

    @GetMapping("/retry-failed-tasks")
    public String retryFailedTasks() {
        taskService.retryFailedTasks();
        return "Failed task retry started";
    }
}
```

7. 优化建议

用MQ就是要分清消费者，生产者以及消息要传递的内容。

1. **使用消息队列**：为了实现更好的任务调度和并行处理，可以引入消息队列如 RabbitMQ 或 Kafka。每个任务被提交到队列中，

2. 多个消费者从队列中消费任务并处理。可以优化上述的线程池操作。就是读取到的1000条数据，交由mq负载均衡分发给几个不同的消费者（task爬虫）（之前是多线程）来处理

3. **批量处理**：在读取和写入数据库时，尽量使用批量操作。MyBatis 支持批量插入和更新。

4. **数据库索引**：确保在 `status` 字段上创建索引，以加快查询速度。

5. **并行处理**：使用线程池（如上例中的 `ExecutorService`）来并行处理任务。

6. **重试机制**：实现重试逻辑，处理网络波动导致的失败。

##### 使用RabbitMQ来优化上述多线程操作（分发给多个消费者爬虫并行task）、MySQL批量写入

配置 RabbitMQ

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    queue: taskQueue
```

定义一个 `TaskMessage` 类来表示消息：

```java
public class TaskMessage {
    private Long id;
    private String key;

    // getters and setters
}
```

定义批量消息模型`BatchTaskMessage.java`

```java
import java.util.List;

public class BatchTaskMessage {
    private List<Task> tasks;

    // getters and setters
}
```

###### 修改 `TaskServiceImpl` 以发送单个爬虫任务消息

```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebScraper webScraper;

    @Override
    @Transactional
    public void processTasks() {
        List<Task> tasks = taskMapper.getPendingTasks(1000); // 批量读取

        for (Task task : tasks) {
            TaskMessage taskMessage = new TaskMessage();
            taskMessage.setId(task.getId());
            taskMessage.setKey(task.getKey());
            // 发送单个任务消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, taskMessage);
        }
    }

    @Override
    @Transactional
    public void retryFailedTasks() {
        List<Task> tasks = taskMapper.getPendingTasks(1000); // 批量读取

        for (Task task : tasks) {
            TaskMessage taskMessage = new TaskMessage();
            taskMessage.setId(task.getId());
            taskMessage.setKey(task.getKey());
            // 发送单个任务消息 实际上是一样的消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, taskMessage);
        }
    }
}
```

###### 修改 `TaskMessageListener` 以接收单个爬虫任务消息并发送批量消息

```java
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMessageListener {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private WebScraper webScraper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final int BATCH_SIZE = 100;
    private List<Task> batchTasks = new ArrayList<>();
	// 接收任务消息
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(TaskMessage taskMessage) {
        Task task = new Task();
        task.setId(taskMessage.getId());
        task.setKey(taskMessage.getKey());

        Task updatedTask = webScraper.fetchTaskDetails(task);
        batchTasks.add(updatedTask);
		//消息积累达到100时
        if (batchTasks.size() >= BATCH_SIZE) {
            // 发送批量插入消息的方法
            sendBatchTasks();
        }
    }

    private void sendBatchTasks() {
        if (!batchTasks.isEmpty()) {
            BatchTaskMessage batchTaskMessage = new BatchTaskMessage();
            batchTaskMessage.setTasks(batchTasks);
            //发送消息
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.BATCH_ROUTING_KEY, batchTaskMessage);
            batchTasks.clear();
        }
    }
}
```

修改 `RabbitMQConfig` 配置类`RabbitMQConfig.java`

```java
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "taskQueue";
    public static final String EXCHANGE_NAME = "taskExchange";
    public static final String ROUTING_KEY = "task.routingKey";
    public static final String BATCH_ROUTING_KEY = "task.batchRoutingKey";
    public static final String BATCH_QUEUE_NAME = "batchTaskQueue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Queue batchQueue() {
        return new Queue(BATCH_QUEUE_NAME);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding batchBinding(Queue batchQueue, TopicExchange exchange) {
        return BindingBuilder.bind(batchQueue).to(exchange).with(BATCH_ROUTING_KEY);
    }
}
```

###### 创建 **`BatchTaskMessageListener`** 消费批量消息并批量写入数据库

```java
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatchTaskMessageListener {

    @Autowired
    private TaskMapper taskMapper;

    @RabbitListener(queues = RabbitMQConfig.BATCH_QUEUE_NAME)
    public void receiveBatchMessage(BatchTaskMessage batchTaskMessage) {
        List<Task> tasks = batchTaskMessage.getTasks();
        if (tasks != null && !tasks.isEmpty()) {
            taskMapper.updateTasks(tasks);
        }
    }
}
```

修改 `TaskMapper` 以支持批量更新，Mapper接口

```java
import java.util.List;
import org.apache.ibatis.annotations.UpdateProvider;

public interface TaskMapper {

    // 其他方法省略

    @UpdateProvider(type = TaskSqlProvider.class, method = "updateTasks")
    void updateTasks(List<Task> tasks);
}
```

`TaskSqlProvider.java`  mapper接口实现，MyBatis动态实现批量插入SQL，就是把所有的插入拼接为一个很长的sql语句。

```java
import org.apache.ibatis.jdbc.SQL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskSqlProvider {

    public String updateTasks(Map<String, Object> params) {
        List<Task> tasks = (List<Task>) params.get("list");
        return tasks.stream()
                .map(task -> new SQL() {{
                    UPDATE("task");
                    if (task.getTitle() != null) {
                        SET("title = #{title}");
                    }
                    if (task.getContent() != null) {
                        SET("content = #{content}");
                    }
                    if (task.getStatus() != null) {
                        SET("status = #{status}");
                    }
                    if (task.getCompleteOn() != null) {
                        SET("complete_on = #{completeOn}");
                    }
                    WHERE("id = #{id}");
                }}.toString())
                .collect(Collectors.joining("; "));
    } 
}
```

##### 使用数据分片，并修改程序的排至与兼容性，让三台机器均摊处理

###### 数据分片策略：

假设 `task` 表中的 `id` 字段是自增主键，我们可以根据 `id` 范围将数据分成三份。例如：

- 服务器1 处理 `id` 在 1 到 3333333 之间的数据
- 服务器2 处理 `id` 在 3333334 到 6666666 之间的数据
- 服务器3 处理 `id` 在 6666667 到 10000000 之间的数据

也可也留有冗余，例如服务器1、2处理400w，服务器3处理200w+执行出错处理任务。

在每台服务器上启动任务处理时，指定不同的 ID 范围。例如：

- 服务器1：

	```
	http://localhost:8080/process-tasks?startId=1&endId=3333333
	```

- 服务器2：

	```
	http://localhost:8080/process-tasks?startId=3333334&endId=6666666
	```

- 服务器3：

	```
	http://localhost:8080/process-tasks?startId=6666667&endId=10000000
	```

###### 修改 `TaskController` 以支持分片启动任务处理

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;
	//需要额外提供分片的起止id
    @GetMapping("/process-tasks")
    public String processTasks(@RequestParam int startId, @RequestParam int endId) {
        taskService.processTasks(startId, endId);
        return "Task processing started for range: " + startId + " to " + endId;
    }

    @GetMapping("/retry-failed-tasks")
    public String retryFailedTasks() {
        taskService.retryFailedTasks();
        return "Failed task retry started";
    }
}
```

###### 修改 `TaskServiceImpl` 以支持分片读取数据

```java
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebScraper webScraper;

    private static final int BATCH_SIZE = 10000; // 批量大小

    @Override
    @Transactional
    public void processTasks(int startId, int endId) {
        //根据起止id，分片读取
        int currentId = startId;

        while (currentId <= endId) {
            //分片读取方法
            List<Task> tasks = taskMapper.getPendingTasksInRange(currentId, BATCH_SIZE);
            if (tasks.isEmpty()) {
                break;
            }

            for (Task task : tasks) {
                TaskMessage taskMessage = new TaskMessage();
                taskMessage.setId(task.getId());
                taskMessage.setKey(task.getKey());
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, taskMessage);
            }

            currentId = tasks.get(tasks.size() - 1).getId() + 1;
        }
    }

    @Override
    @Transactional
    public void retryFailedTasks() {
        // 重试逻辑类似，读取 status 为 fail 的任务进行重试
        List<Task> tasks = taskMapper.getPendingTasks(1000);

        for (Task task : tasks) {
            TaskMessage taskMessage = new TaskMessage();
            taskMessage.setId(task.getId());
            taskMessage.setKey(task.getKey());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, taskMessage);
        }
    }
}
```

###### 修改 `TaskMapper` 以支持按范围读取数据

```java
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TaskMapper {

    @Select("SELECT id, `key` FROM task WHERE status IS NULL AND id >= #{startId} LIMIT #{limit}")
    List<Task> getPendingTasksInRange(@Param("startId") int startId, @Param("limit") int limit);

    @UpdateProvider(type = TaskSqlProvider.class, method = "updateTasks")
    void updateTasks(List<Task> tasks);
}
```

##### 计算耗时

1000w数据分片为3份，假设三台服务器，单台服务器上运行20个爬虫，单次爬虫时间平均为3秒，失败率0.1%

那么单台服务器需要处理333.33w，单个爬虫消费者需要处理16.66w数据，算上失败率单个耗时约为50w秒，即139小时，5.8天

如果cpu为16核32线程，实际可用内存为40g，爬虫实例的平均消耗内存为200-500MB，每秒进行1-2次请求，平均页面返回大小为100-500kb。

计算时假设爬虫实例平均消耗内存300mb，则可以最多运行136个实例。但考虑CPU，每个实例平均占用0.5个CPU线程，则最多可以布置64个实例。

考虑到其他程序的占用，建议部署50-60个实例，以保障每个实例有足够的资源进行高效的页面抓取和解析。

## 05-Java开发类-考察：反射机制，代码生成自动化，restful接口调用

现有一个restful接口项目，使用SpringMVC实现，为提高开发效率。现在需要实现根据接口本身代码自动生成调用接口的代码的功能。请阐述一下实现此功能的思路。图例中为Kotlin



**Swagger-UI ？**以及各种api测试工具例如postman

反射拿到Controller方法，方法名，注解，注释，uri，

分析参数，请求类型，参数类型，返回值json

调用的方法根据请求类型，参数类型，生成相应的输入界面，并接受结果，用于调试

## 06-代码编写类-考察：代码阅读，审查，纠错

### Q：请阅读以下代码，说明此代码的作用，并描述代码可能存在哪些问题。

```java
public static Properties getProperties(String fileName) {
    Properties result = new Properties();
    try {
         result.load(new InputStreamReader(PropertiesUtil.class.getResourceAsStream(fileName), "UTF-8"));
    } catch (Exception e) {
       e.printStackTrace();
    }
    return result;
}
```

### ANS：

public static修饰的是 公开的 类方法，通常是个公用的工具方法或通用操作。

该类是一个加载文件名为 fileName的文件并经过PropertiesUtil处理后返回结果

1. **潜在的资源泄漏**

- `InputStream` 未被显式关闭。如果 `PropertiesUtil.class.getResourceAsStream(fileName)` 打开了一个文件输入流，在出现异常的情况下，流不会被关闭，可能会导致资源泄漏。
- 解决方法：使用 `try-with-resources` 语句来确保资源在使用完后被关闭。等效于在finally语句块中关闭

```java
		try (InputStream inputStream = PropertiesUtil.class.getResourceAsStream(fileName);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF-8)) {
            if (inputStream != null) {
                result.load(reader);
            } else {
                logger.log("File not found: " + fileName);
                result.set 默认值;
            }
        } catch (Exception e) {
            // 使用适当的日志框架记录异常
            logger.log("Error loading properties file: " + fileName);
            result.set 默认值;
        }
		return result;
```

2. **错误信息暴露**

- 在 `catch` 块中，异常信息直接被打印到标准输出。这可能会在生产环境中暴露内部实现细节给最终用户，增加了信息泄露的风险。
- 解决方法：记录日志时要注意安全性，可以使用适当的日志框架，并避免在生产环境中打印详细的堆栈跟踪信息。

3. **文件名处理**

- 如果 `fileName` 是由外部传入的，它可能包含恶意输入，如路径遍历攻击，试图访问不应该访问的文件。
- 解决方法：对传入的 `fileName` 进行严格的校验，确保它仅指向预期的文件路径。

4. **字符集硬编码**

- 虽然 `UTF-8` 是一个常用的字符集，但硬编码字符集会使代码在某些特定场景下缺乏灵活性。
- 解决方法：根据具体需求，可以将字符集作为可配置的参数。

## 07-应用类-考察：生产环境问题排查

现有基于spring mvc的应用，提供了将数据库中的内容导出为标准格式的excel文件，供用户检查核对的功能，该应用还提供了restful接口供安卓终端访问。现发现当多人同时使用导出excel文件的功能时，服务器负载会瞬时升高，到一定程度时restful接口的正常访问受到影响，出现超时。

### Q:何种措施优化应用代码，使得导出excel文件和restful接口均能正常提供服务。

### ANS:限流，读写分离，异步任务，分布式处理

为了解决多人同时使用导出 Excel 文件功能时导致服务器负载瞬时升高，并影响 RESTful 接口的正常访问的问题，可以考虑以下几种方案：

###### 1. **任务异步化**
将导出 Excel 文件的功能改为异步处理，避免长时间占用服务器资源。

- **方法**：使用 Spring 的 `@Async` 注解或其他异步处理框架，将导出任务放到后台处理。
- **实现**：用户发起导出请求时，立即返回一个任务 ID，然后在后台生成 Excel 文件。任务完成后，用户可以通过任务 ID 下载生成的文件。

```java
@Service
public class ExcelExportService {
    @Async
    public CompletableFuture<String> exportExcelAsync() {
        // 导出 Excel 的具体逻辑
        String filePath = generateExcelFile();
        return CompletableFuture.completedFuture(filePath);
    }
}

@RestController
public class ExportController {
    @Autowired
    private ExcelExportService excelExportService;

    @PostMapping("/export")
    public ResponseEntity<String> exportExcel() {
        CompletableFuture<String> future = excelExportService.exportExcelAsync();
        // 返回任务ID或状态
        return ResponseEntity.ok("Export task started. Check status later.");
    }
}
```

###### 2. **限流和负载均衡**
对导出 Excel 文件的请求进行限流，确保 RESTful 接口的请求不会被过多的导出任务影响。

- **方法**：使用限流框架（如 `Bucket4j`、`RateLimiter`）对导出请求进行限流。
- **实现**：设置每单位时间内允许的导出请求数量，超出限制的请求返回友好的错误提示。

```java
@RestController
public class ExportController {
    private final RateLimiter rateLimiter = RateLimiter.create(2.0); // 每秒两个请求

    @PostMapping("/export")
    public ResponseEntity<String> exportExcel() {
        if (rateLimiter.tryAcquire()) {
            CompletableFuture<String> future = excelExportService.exportExcelAsync();
            return ResponseEntity.ok("Export task started. Check status later.");
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests, please try again later.");
        }
    }
}
```

###### 3. **优化导出逻辑**
优化导出 Excel 文件的逻辑，提高效率，减少对服务器资源的占用。加速读取比如数据库读写分离

- **方法**：使用高效的 Excel 生成库（如 Apache POI 的 SXSSFWorkbook，适合处理大数据量）。
- **实现**：在导出逻辑中使用流式处理，避免一次性加载过多数据到内存中。

```java
public String generateExcelFile() {
    try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
        // 具体导出逻辑
        // 使用流式处理大数据
    } catch (IOException e) {
        e.printStackTrace();
    }
    return filePath;
}
```

###### 4. **分布式处理**
将导出任务分散到多台服务器上处理，降低单台服务器的负载。

- **方法**：使用分布式任务队列（如 RabbitMQ、Kafka）将导出**任务分发**到不同的服务器处理。
- **实现**：每台服务器从队列中获取任务并进行处理，最终结果返回到原始服务器或用户。

###### 5. **资源隔离**
将导出任务和 RESTful 接口部署到不同的服务器或容器中，进行物理隔离。

- **方法**：使用容器化技术（如 Docker）将导出任务和 RESTful 接口分别部署到不同的容器中。
- **实现**：通过容器编排工具（如 Kubernetes）管理容器，分配资源，保证不同功能互不影响。
- 即负责导出任务的cpu飙高，不至于影响到其他的接口服务调用，



# 群友Java面试题学习-0000
