package com.leowork.crm.workbench.web.controller;

import com.leowork.crm.commons.contants.Contants;
import com.leowork.crm.commons.entity.ReturnJsonObject;
import com.leowork.crm.commons.utils.DateUtils;
import com.leowork.crm.commons.utils.UUIDUtils;
import com.leowork.crm.settings.entity.User;
import com.leowork.crm.settings.service.UserService;
import com.leowork.crm.workbench.entity.Activity;
import com.leowork.crm.workbench.service.ActivityService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author Leo
 * @version 1.0
 * @className ActivityController
 * @since 1.0
 **/
@Controller
public class ActivityController {

    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;

    /**
     * 获取全部 可以是市场活动的所有者 的用户
     *
     * url和处理的资源所在路径保持一致
     * @return 返回index页面
     */
    @RequestMapping("/workbench/activity/index.action")
    public String index(HttpServletRequest request){
        System.out.println("访问ActivityController，路径为 /workbench/activity/index.action");
        /*用户发送请求，开启市场活动主页面，为请求转发。将多个地方会用到的所有者user查出，存入request域*/
        /*调用service方法，查询所有用户*/
        List<User> userList = userService.queryAllUsers();
        /*将数据存入request域中*/
        request.setAttribute("userList", userList);
        /*请求转发到 市场活动主页面*/


        return "workbench/activity/index";
    }

    /**
     * 保存市场活动
     *
     * 返回json,还是其中包含其他类型，都是Object类型，方法类型是父类型，返回值是子类，属于多态的一种运用
     * 框架自动将前端传过来的参数封装到实体类对象中 前台可以传过来六个参数，共需要九个参数，还差 id createTime createBy 需要生成并封装进去
     * @return 返回json字符串，包含保存成功与否的code状态码以及报错信息message
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/saveCreateActivity.action")
    public Object saveCreateActivity(Activity activity, HttpSession session){
        System.out.println("访问ActivityController，路径为 /workbench/activity/saveCreateActivity.action");
        /*封装 id 参数 调用封装好的工具方法*/
        activity.setId(UUIDUtils.getUUID());
        /*封装 createTime 参数 获取当前系统事件并使用工具格式化为字符串*/
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        /*封装 createBy 参数 创建者为当前登录用户，从当前session中获取 与用户的不变的id进行绑定*/
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setCreateBy(user.getId());
        /*获取用于返回的通用实体类对象，包含code message returnData 三个属性*/
        ReturnJsonObject returnJsonObject = new ReturnJsonObject();
        try {
            /*调用service层方法，保存市场活动信息*/
            int count = activityService.saveCreateActivity(activity);
            System.out.println("调用ActivityController的saveCreateActivity方法");
            /*对返回结果进行判断 写成功或写失败*/
            if (count > 0){
                System.out.println("写入活动信息成功，条数：" + count);
                returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                System.out.println("写入活动信息失败");
                returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_FILED);
                returnJsonObject.setMessage("系统繁忙，请稍后重试......");
            }
        }catch (Exception e){
            System.out.println("写入异常");
            e.printStackTrace();
            returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_FILED);
            returnJsonObject.setMessage("系统繁忙，请稍后重试......");
        }

        return returnJsonObject;
    }
    /**
     * 根据查询条件 分页查询市场活动
     *
     * 前端需要传参：查询的条件，beginNo,pageSize
     * 进行封装
     * @return 返回json字符串
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.action")
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate,
                                                int pageNo, int pageSize){
        /* 封装参数，参数的 key 需要与后面sql语句中 获取参数所用的值保持一致 */
        Map<String ,Object> map = new HashMap<>(8);
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        /*计算beginNo*/
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        /*调用Service方法查询数据*/
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        /*查询数据条数*/
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        /*根据查询结果生成相应信息打给前台，return map对象可以自动转化为json格式*/
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("activityList", activityList);
        resultMap.put("totalRows", totalRows);
        return resultMap;
    }

    /**
     * 根据前台提供的id数组删除对应市场活动记录
     * @param id 接受前端请求中的数组，保持一致
     * @return json字符串
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/deleteActivityIds.action")
    public Object deleteActivityIds(String[] id){
        /*获取响应对象*/
        ReturnJsonObject returnJsonObject = new ReturnJsonObject();
        /*判断是否出现异常*/
        try {
            /*调用service层方法，删除市场活动记录*/
            /*对于增删改操作，需要判断是否成功，需要检测异常*/
            int count = activityService.deleteActivityByIds(id);
            /*没有异常，判断影响记录条数是否为0*/
            if (count > 0){
                returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                System.out.println("被删除的记录id为：" +Arrays.toString(id));
            }else {
                returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_FILED);
                returnJsonObject.setMessage("请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_FILED);
            returnJsonObject.setMessage("请稍后重试");
        }
        return returnJsonObject;
    }

    /**
     * 根据前台提供的id数组查询对应市场活动记录
     * @param id 接受前端请求中的数组，保持一致
     * @return json字符串
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/queryActivityById.action")
    public Object queryActivityById(String id){
        /*获取响应对象*/
        ReturnJsonObject returnJsonObject = new ReturnJsonObject();
        /*调用service层方法，查询市场活动记录*/
        Activity activity = activityService.queryActivityById(id);
        /*返回查询结果，无需try catch 未查到即为空*/
        return activity;
    }


}
