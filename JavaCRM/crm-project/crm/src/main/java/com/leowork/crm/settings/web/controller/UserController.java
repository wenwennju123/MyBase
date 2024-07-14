package com.leowork.crm.settings.web.controller;

import com.leowork.crm.commons.contants.Contants;
import com.leowork.crm.commons.entity.ReturnJsonObject;
import com.leowork.crm.commons.utils.DateUtils;
import com.leowork.crm.settings.entity.User;
import com.leowork.crm.settings.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Leo
 * @version 1.0
 * @className UserController
 * @since 1.0
 **/
@Controller
public class UserController {

    @Resource
    private UserService userService;

    /**
     * url命名规则
     * 要和controller方法所响应返回的页面，页面资源所在的资源目录保持一致
     * 资源名称对应方法名
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.action")
    public String toLogin(){
        System.out.println("访问UserController，路径为 /settings/qx/user/toLogin.action 请求转发跳转到login登陆页面");
        /*
        请求转发到登录页面
         */
        return "settings/qx/user/login";
    }

    /**
     * 返回Ajax格式 统一定义为Object返回值类型
     * 多态
     * 返回值定义为父类型，之后具体采用什么json格式再转
     */
    @RequestMapping("/settings/qx/user/login.action")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemAct, HttpServletRequest request, HttpServletResponse response, HttpSession session){

        System.out.println("访问UserController，路径为 /settings/qx/user/login.action");
        System.out.println("loginAct:" + loginAct + ";loginPwd:" + loginPwd);
        /*
        封装登录信息参数为map格式
         */
        Map<String, Object> loginResultMap = new HashMap<>(4);
        loginResultMap.put("loginAct", loginAct);
        loginResultMap.put("loginPwd", loginPwd);
        /*
        调用Service层方法来查询用户
         */
        User user = userService.queryUserByLoginActAndPwd(loginResultMap);
        /*
        创建生成返回信息json的对象
         */
        ReturnJsonObject returnJsonObject = new ReturnJsonObject();
        /*
        根据查询结果生成响应信息，进行判断
        也可以将判断逻辑写入service，然后调用方法 例如 isSuccessLogin 返回状态码即可
        此处视作处理前台响应信息，写入Controller
         */
        if(user == null){
            /*
            登录失败，用户名或者密码错误
             */
            System.out.println("UserController: login: 登录失败，用户名或密码错误，未查询到用户对象");

            returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_FILED);
            returnJsonObject.setMessage("用户名或密码错误");
        }else {
            /*
            查询到账户，进一步判断是否合法，用户期限，用户状态，用户ip
            比较过期时间与当前时间
            比较方法，均转换为相同格式的字符串进行比较，字符串比较器是按照逐个字符比较，依据字典顺序判断
            调用工具类的方法
            获取当前系统时间并转换格式
             */
            String nowTimeStr = DateUtils.formatDateTime(new Date());

            if (nowTimeStr.compareTo(user.getExpireTime()) > 0){
                /*
                大于0表示已过期，登录失败
                 */
                System.out.println("UserController: login: 登录失败，账号已过期" +
                                   "当前时间为" + nowTimeStr +
                                   "用户期限为" + user.getExpireTime());

                returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_FILED);
                returnJsonObject.setMessage("账号已过期");
            }else if(Contants.ACT_STATE_LOCKED.equals(user.getLockState())) {
                /*
                状态码为 0 表示已锁定，登陆失败
                 */
                System.out.println("UserController: login: 登录失败，账号已锁定");

                returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_FILED);
                returnJsonObject.setMessage("账号已锁定");
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                /*
                从请求request对象获取用户ip地址
                上线时关闭ip验证功能，方便展示
                 */
                System.out.println("UserController: login: 登录失败，ip受限" +
                                   "用户ip为：" + request.getRemoteAddr());

                returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_FILED);
                returnJsonObject.setMessage("ip受限");
            }else {
                try {
                    /*模拟执行时间*/
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                /*
                登陆成功
                 */
                returnJsonObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnJsonObject.setMessage("登录成功");
                System.out.println("UserController: login: 登录成功");
                /*
                登录成功后，将user对象存储到session中
                key设置成常量，方便维护
                 */
                session.setAttribute(Contants.SESSION_USER, user);
                /*判断是否记住用户，需要记住用户则写cookie，cookie中保存账号信息loginAct loginPwd，有效时间为十天*/
                if (Contants.TRUE.equals(isRemAct)){
                    /*将cookie写入响应信息，打回浏览器持久化*/
                    Cookie actCookie = new Cookie("loginAct", user.getLoginAct());
                    actCookie.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(actCookie);

                    Cookie pwdCookie = new Cookie("loginPwd", user.getLoginPwd());
                    pwdCookie.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(pwdCookie);
                }else {
                    /*将未过期cookie删除*/
                    Cookie actCookie = new Cookie("loginAct", "1");
                    actCookie.setMaxAge(0);
                    response.addCookie(actCookie);

                    Cookie pwdCookie = new Cookie("loginPwd", "1");
                    pwdCookie.setMaxAge(0);
                    response.addCookie(pwdCookie);
                }
            }
        }
        /*
        返回对象，框架会将其自动转换为json格式
         */
        return returnJsonObject;
    }

    /**
     * 登出
     * @param response 响应对象
     * @param session 会话对象
     * @return 重定向到首页
     */
    @RequestMapping("/settings/qx/user/logout.action")
    public String logout(HttpServletResponse response, HttpSession session){
        /*清空cookie*/
        Cookie actCookie = new Cookie("loginAct", "1");
        actCookie.setMaxAge(0);
        response.addCookie(actCookie);

        Cookie pwdCookie = new Cookie("loginPwd", "1");
        pwdCookie.setMaxAge(0);
        response.addCookie(pwdCookie);
        /*销毁session*/
        session.invalidate();
        /*重定向到首页*/
        /*由mvc框架完成重定向，框架底层会执行 response.sendRedirect("/crm")*/

        System.out.println("访问UserController，路径为 /settings/qx/user/logout.action");
        System.out.println("UserController: logout：账号登出");

        return "redirect:/";
    }
}
