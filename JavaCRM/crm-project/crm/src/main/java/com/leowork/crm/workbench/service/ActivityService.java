package com.leowork.crm.workbench.service;

import com.leowork.crm.workbench.entity.Activity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Leo
 * @version 1.0
 * @className ActivityService
 * @since 1.0
 **/

public interface ActivityService {

    /**
     * 保存创建市场活动信息
     * @param activity 市场活动信息
     * @return 影响记录条数
     */
    int saveCreateActivity(Activity activity);

    /**
     * 根据查询条件，分页查询市场活动信息
     * @param map 查询条件
     * @return 查询结果集
     */
    List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    /**
     * 根据查询条件，查询出所有符合要求的市场活动记录条数
     * @param map 查询条件
     * @return 记录条数
     */
    int queryCountOfActivityByCondition(Map<String, Object> map);

    /**
     * 根据id 删除市场活动记录
     * @param ids 需要删除的id数组
     * @return 影响记录条数
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 依据id查找市场活动记录
     * @param id 查找的id
     * @return 市场活动记录对象
     */
    Activity queryActivityById(String id);
}
