package com.dqy.englishstudyapi.util;

import com.dqy.englishstudyapi.entity.frontEntity.EmailVerifyCode;
import com.dqy.englishstudyapi.entity.frontEntity.PhoneVerifyCode;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
public class SessionUtil {
     public  static  String PHONECODE_SESSION="PhoneCode";
    public  static  String EMAILCODE_SESSION="EmailCode";
    /**
     * 设置缓存
     *
     * @param phoneVerifyCode
     * @param request
     */
    public void setCodeSession(PhoneVerifyCode phoneVerifyCode, HttpServletRequest request) {
        //获取缓存内存
        HttpSession session = request.getSession();

        Map<String, List<String>> checkCode = (Map<String, List<String>>) session .getAttribute(PHONECODE_SESSION);
        List<String> codes = new ArrayList<>();
        if (null != checkCode) {
            //如果原来就存有session，直接拿出原来的验证码的list
            codes = checkCode.get(phoneVerifyCode.getPhone());
        } else {
            checkCode = new HashMap<>();
        }

        //将新的验证码放入list中
        codes.add(phoneVerifyCode.getCode());
        checkCode.put(phoneVerifyCode.getPhone(), codes);

        //自定义类设置进缓存
        session.setAttribute(PHONECODE_SESSION, checkCode);

    }
    public void setCodeSessionEmail(EmailVerifyCode emailVerifyCode, HttpServletRequest request) {
        //获取缓存内存
        HttpSession session = request.getSession();

        Map<String, List<String>> checkCode = (Map<String, List<String>>) session .getAttribute(EMAILCODE_SESSION);
        List<String> codes = new ArrayList<>();
        if (null != checkCode) {
            //如果原来就存有session，直接拿出原来的验证码的list
            codes = checkCode.get(emailVerifyCode.getEmail());
        } else {
            checkCode = new HashMap<>();
        }

        //将新的验证码放入list中
        codes.add(emailVerifyCode.getCode());
        checkCode.put(emailVerifyCode.getEmail(), codes);

        //自定义类设置进缓存
        session.setAttribute(EMAILCODE_SESSION, checkCode);
    }
    /**
     * 指定有效时间内移除验证码
     *
     * @param session        缓存
     * @param phoneVerifyCode 封装的手机号码
     */
    public void removeCodeSession(final HttpSession session,  final PhoneVerifyCode phoneVerifyCode) {

        //从Session提取手机号对应的验证码
        Map<String, List<String>> checkCode = (Map<String, List<String>>) session.getAttribute(PHONECODE_SESSION);
        List<String> codes = checkCode.get(phoneVerifyCode.getPhone());

        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("code-thread-runner-%d").build();
        ScheduledExecutorService schedu = new ScheduledThreadPoolExecutor(1, threadFactory);
//        ScheduledExecutorService schedu = Executors.newScheduledThreadPool(1);
        Runnable removeCode = new Runnable() {
            @Override
            public void run() {
                try {
                    // 移除session中list存的验证码元素
                    codes.remove(phoneVerifyCode.getCode());

                    System.out.println("删除session中[" +phoneVerifyCode.getPhone() + "]存的验证码:" + phoneVerifyCode.getCode());
                } catch (Exception e) {
                    PhoneVerifyCode  newPhoneVerifyCode = (PhoneVerifyCode) session.getAttribute(PHONECODE_SESSION);
                    System.out.println(newPhoneVerifyCode.getPhone() + "移除出错。");
                }
            }
        };
        //功能:创建并执行在给定延迟后启用的一次性操作。对removeCode任务暂停五分钟后执行
        schedu.schedule(removeCode, 5 * 60, TimeUnit.SECONDS);
        //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。
        schedu.shutdown();

    }


}
