package com.dqy.englishstudyapi.configure;

import com.dqy.englishstudyapi.Interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
 
    @Autowired
    private TokenInterceptor tokenInterceptor;
 
    /**
     * 解决跨域请求
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
//               .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true);
    }
 
    /**
     * 异步请求配置
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3)));
        configurer.setDefaultTimeout(30000);
    }
 
    /**
     * 配置拦截器、拦截路径
     * 每次请求到拦截的路径，就会去执行拦截器中的方法
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = new ArrayList<>();
        //排除拦截，除了注册登录(此时还没token)，其他都拦截
        excludePath.add("/user/loginByUserName");  //登录
        excludePath.add("/user/loginByPhone");  //登录
        excludePath.add("/user/wxlogin");  //登录
        excludePath.add("/user/qqlogin");  //登录
        excludePath.add("/user/selectByPhone");
        excludePath.add("/user/selectByEmail");
        excludePath.add("/user/selectByUsername");
        excludePath.add("/user/register");     //注册
        excludePath.add("/user/updatePwd");

        excludePath.add("/adminUser/loginByUserName"); //登录
        excludePath.add("/adminUser/loginByPhone");  //登录
        excludePath.add("/adminUser/wxlogin");  //登录
        excludePath.add("/adminUser/qqlogin");  //登录
        excludePath.add("/adminUser/selectByPhone");  //登录
        excludePath.add("/adminUser/selectByEmail");  //登录
        excludePath.add("/adminUser/register");     //注册
        excludePath.add("/adminUser/updatePwd");



        excludePath.add("/file/uploadImageRegister");
        excludePath.add("/word/getOneOpen");
        excludePath.add("/wordFull/getOneOpen");
        //获取验证码
        excludePath.add("/code/getcode");
        excludePath.add("/pay/**");
        excludePath.add("/code/getcodeEmail");
        excludePath.add("/code/verifyCode");
        excludePath.add("/code/verifyCodeRegist");
        excludePath.add("/code/verifyCodeEmail");
        excludePath.add("/code/verifyCodeEmailRegist");
        excludePath.add("/liju/getByWord");
        excludePath.add("/baidu/trans");
        excludePath.add("/doc.html");     //swagger
        excludePath.add("/swagger-ui.html");     //swagger
        excludePath.add("/swagger-resources/**");     //swagger
        excludePath.add("/v2/api-docs");     //swagger
        excludePath.add("/webjars/**");     //swagger
        excludePath.add("/static/**");  //静态资源
        excludePath.add("/assets/**");  //静态资源
        excludePath.add("/error");
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        WebMvcConfigurer.super.addInterceptors(registry);
 
    }
}