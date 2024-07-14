package com.leowork.crm.workbench.service.impl;

import com.leowork.crm.workbench.entity.Activity;
import com.leowork.crm.workbench.mapper.ActivityMapper;
import com.leowork.crm.workbench.service.ActivityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Leo
 * @version 1.0
 * @className ActivityServiceImpl
 * @since 1.0
 **/
@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityMapper activityMapper;

    @Override
    //@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public int saveCreateActivity(Activity activity) {
        System.out.println("调用ActivityService的saveCreateActivity方法");
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        System.out.println("调用ActivityService的queryActivityByConditionForPage方法");
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        System.out.println("调用ActivityService的queryCountOfActivityByCondition方法");
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        System.out.println("调用ActivityService的deleteActivityByIds方法");
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity queryActivityById(String id) {
        System.out.println("调用ActivityService的queryActivityById方法");
        return activityMapper.selectActivityById(id);
    }
}
