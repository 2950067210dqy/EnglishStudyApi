package com.dqy.englishstudyapi.util;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.*;
import com.dqy.englishstudyapi.entity.frontEntity.EmailVerifyCode;
import com.dqy.englishstudyapi.entity.frontEntity.PhoneVerifyCode;
import com.dqy.englishstudyapi.vo.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class SmsUtil {

    @Resource
    private JavaMailSenderImpl mailSender;

    @Value("${dqy.aliyunSmsUrl}")
    String url;
    public Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public ReturnVO sendCode(PhoneVerifyCode phoneVerifyCode) throws Exception {
        ReturnVO returnVO = new ReturnVO();
        // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
       
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest();
//        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            sendSmsRequest
                .setSignName("沁柚英语学习平台")
                
                .setPhoneNumbers(phoneVerifyCode.getPhone())
                .setTemplateParam("{\"code\":\""+phoneVerifyCode.getCode()+"\"}");
        try {
            // 复制代码运行请自行打印 API 的返回值
            returnVO.setCode(200);

            returnVO.setMessage("发送成功");
            returnVO.setData(client.sendSms(sendSmsRequest).getBody());
            return  returnVO;

        } catch (TeaException error) {
            // 如有需要，请打印 error
            returnVO.setCode(500);

            returnVO.setMessage( com.aliyun.teautil.Common.assertAsString(error.message));
            return  returnVO;
        } catch (Exception _error) {
            // 如有需要，请打印 error
            returnVO.setCode(500);
            TeaException error = new TeaException(_error.getMessage(), _error);
            returnVO.setMessage( com.aliyun.teautil.Common.assertAsString(com.aliyun.teautil.Common.assertAsString(error.message)));
            return  returnVO;
        }
    }

    public ReturnVO sendCodeEmail(EmailVerifyCode emailVerifyCode) {
        ReturnVO returnVO = new ReturnVO();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);

            //标题
            helper.setSubject("您的验证码为：" + emailVerifyCode.getCode());
            helper.setText("【沁柚英语学习平台】(author:dqy) 您的验证码为:" + "<h2>" + emailVerifyCode.getCode() + "</h2>" + "有效期5分钟，千万不能告诉别人哦！", true);
            //邮件接收者
            helper.setTo(emailVerifyCode.getEmail());
            //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
            helper.setFrom("2950067210@qq.com");

            //内容

            mailSender.send(mimeMessage);

            returnVO.setMessage("发送成功");
            returnVO.setCode(200);
        }catch (MessagingException e) {
            e.printStackTrace();
            returnVO.setMessage("失败："+e.getMessage());
            returnVO.setCode(500);
        }
        return returnVO;

    }
}
