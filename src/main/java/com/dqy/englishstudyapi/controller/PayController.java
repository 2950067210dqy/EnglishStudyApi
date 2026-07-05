package com.dqy.englishstudyapi.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqy.englishstudyapi.entity.Pay.PaySession;
import com.dqy.englishstudyapi.service.ScoreService;
import com.dqy.englishstudyapi.service.ZborderService;
import com.dqy.englishstudyapi.tablebean.Zborder;
import com.dqy.englishstudyapi.timetask.ZbOrderTimeTask;
import com.dqy.englishstudyapi.timetask.ZbOrderTimer;
import com.dqy.englishstudyapi.util.TimeUtil;
import com.dqy.englishstudyapi.vo.SubReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("pay")

public class PayController {

    //appid
    private final String APP_ID = "2021000122608927";
    //应用私钥
    private final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCTaCaD4nViLN2p4gCuyYkNxPVlGDesxAnkFe0NAgNnCNG5P1kRhXuuX2Yt4vzgPwZXh5HmOreZiC55VkQr6eopd4j/edBp54KYDzUBIdOyBNCLbu/11oiy4Bqnjp7YpFOMo2V4M7NwjM1I5dxO1FUVvVsMY4UIAySBmx21jhe0nANUkrq60oVkfrIQjiY1dDLRAm4Op58yCGQCRAJYx5OJv85xJTUNSkDGjxbTkcS2yusmdbxezWAzKVr34NpcSTnIEboJBoI1kQNrN/sN3EjqbifybzjoAZWqaJ8k0S/IapTSqHJwvlRmiqAMrKJb0jVoxl/p/I4V3qXxDlaaDmyPAgMBAAECggEAb7LDRLrMkjlHOt0MAWasGSf/f7yrK5pdmF1ZBLta0q+nJEWxe4p3nzmhFvJjBpYbeyY604f46jl8ZeuNuu0EyKlQwa6P1EBfAFS9qH72iHYHhTDqWpJXgZw59nAnRCi5Ot/yGvSqa4X2ZN/xYjaZxuB25tteyiR/fEqLZ7Elm3BT1TBO5YhfTuB2EpHjqAXiCMqZ0IjWp7q0F0Bszv9V+aAwvQWdHdp2s+aFYwM/28tnxhloo1YO5cKs/qqDaEGZpouKQ0hoW6YOnPtFM3Jwqj5Y1O2KZXq1rAGrouOhiIhyhNXOMDCL2tDS8+eotw1SD+qhM7WT6fbjcV030bYgUQKBgQDxj/VPkjrgJ3Umzy66EiiiaUiSoXyGR9ASIFEaKQ3PnhvsJ84Rrvyed6rFKWdqiaHigTMfrIGjqR0caZEASTiCgx/6Zny06/Txth1kOs2yApxy8B6qZcBZV9HK7vBHKFg3xVjzDwDWZAEgFaRh/jpaMWkfQNySEfY86p1t0vJdmQKBgQCcN48szhsrcO2eryhtoMt5PEBDohCpaRDmn7CKtaPkm7OVg0Ei2GaCSfUaH114Ku1E/fCbxml6tiKaRrOup8+1EUExSCiCXCukvJrb3bwYHwSwa3KuR1aeGmn2TBmkU5UqOQSHbb9ZCBXixXQ0BV8Fu6rr7qAHusvUQIIW/LNkZwKBgD3pdKkxRG/BDTCmN0avcsS1kUkrpHvSGg0bRU4cKZMF5ggv54v2un5vE1C3HO9cWSZxkMXUc+GbbX+juutE2PvyWZbW/61rMHtm/r96ps+yBaEb0/A+Zf9AF+AYP3MtbvbUWMjrFn7cYMF1ao1HGHOXaGb/O8kgW2kfhLvqLf45AoGAcPkRFuL67SCfB4o+n4GQhN2Y0BCNIib0bQSBZN7VZsdfSToByhRblc+sbLbQHs5UhvdqoQ0NTQwY3B9TZHTBln2i+uPnP9cuTNWE3Ipvs7YC6x7ZccXvkykhBliklp1Hm6BecR5iKh3ZzgTj85cK0BeXFUad/tkM/TOloMAhb/MCgYEAx8FRK0V1oWNzzV4n1ksOh/DCj3KsNmIzCrmWvG10GL+ESti0yNHbFFzo5aR+3KWR/aJr/lyqehm7Vmc5KS0iuZZ7y0+lndDugZswEugpkq3x8XlxKjVnDu6LcNu0fjPEB0AMMfE24hrO7ry4AVFxnT2LfzZ4ffXpjaOdj3/WpBA=";
    private final String CHARSET = "UTF-8";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKaRKBXpS2UXbT1OaIyag9F5uQbxU/XdY8tOMzyYD7OJGVwyAIlB3VsNyuwEgwRDdZS68ONMmwnTQ9sC3Q4MPdyLtEUQcePnV+p+emXdP+9+YylRwQ5xoB+SPhRgI951GLwOvQ0y+pb0t05yM7AIiJWiyMmgWpSSX660Gl0JInWTtImjUWhGB2CvmA8G9jckk2UQ8UBogeaok/TubKfY2+y9LgD7iwkUb2T29WdVp79598Ul1B8C7tY9/dsP/lLCuzxG2gqgV5+RtQLgo+Jf4G70Sj970SJIEaQpqle480mXL2JU+QX3Z4pzaZBgdAAZDDw1TmkePMqaZ0F9OD3fgQIDAQAB";
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL ="https://openapi.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    @Value("${dqy.address}")
    private String url;
    private final String NOTIFY_URL = "pay/return";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL =  "pay/return";

    @Autowired
    ZborderService zborderService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    TimeUtil timeUtil;
   
    ZbOrderTimer zbOrderTimer = new ZbOrderTimer();
    ZbOrderTimeTask zbOrderTimeTask;
    @RequestMapping("/return")
    public String getReturn(HttpSession session, HttpServletRequest request
                        ) throws AlipayApiException {
        //把dona_id项目id 放在session中
        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }

        System.out.println(params);//查看参数都有哪些
//        Map<String, String> params = new HashMap<>();
//        Map<String, String[]> requestParams = request.getParameterMap();
//        for (String name : requestParams.keySet()) {
//            params.put(name, request.getParameter(name));
//            // System.out.println(name + " = " + request.getParameter(name));
//        }

        String tradeNo = params.get("out_trade_no");
        String[] tradenos = tradeNo.split("@");

        String gmtPayment = params.get("gmt_payment");
        String alipayTradeNo = params.get("trade_no");
        Map<String,Object> paramss= new HashMap<>();
        paramss.put("uid",Integer.valueOf(tradenos[1]));
        paramss.put("zborderid",tradeNo);
        Zborder zborder = zborderService.getOne(new QueryWrapper<Zborder>().allEq(paramss));
        zborder.setZbcode(alipayTradeNo);
        zborder.setStatus(1);
        SubReturnVo subReturnVo = scoreService.reCharge(zborder);
        // 支付宝验签

            // 验签通过
            System.out.println("交易名称: " + params.get("subject"));
            System.out.println("交易状态: " + params.get("trade_status"));
            System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
            System.out.println("商户订单号: " + params.get("out_trade_no"));

            System.out.println("交易金额: " + params.get("total_amount"));
            System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
            System.out.println("买家付款时间: " + params.get("gmt_payment"));
            System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

            // 更新订单未已支付
//        ordersMapper.updateState(tradeNo, "已支付", gmtPayment, alipayTradeNo);
        String HTML = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />\n" +
                "    <title>网络网页</title>\n" +
                "    <style type=\"text/css\">\n" +
                "      .btn {\n" +
                "        display: block;\n" +
                "        margin: 20px auto;\n" +
                "        padding: 5px;\n" +
                "        background-color: #007aff;\n" +
                "        border: 0;\n" +
                "        color: #ffffff;\n" +
                "        height: 40px;\n" +
                "        width: 200px;\n" +
                "      }\n" +
                "\n" +
                "      .btn-red {\n" +
                "        background-color: #dd524d;\n" +
                "      }\n" +
                "\n" +
                "      .btn-yellow {\n" +
                "        background-color: #f0ad4e;\n" +
                "      }\n" +
                "\n" +
                "      .desc {\n" +
                "        padding: 10px;\n" +
                "        color: #999999;\n" +
                "      }\n" +
                "\n" +
                "      .post-message-section {\n" +
                "        visibility: hidden;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <p class=\"desc\">充值"+(subReturnVo.isResult()?"成功":"失败")+"点击下列按钮，返回页面。</p>\n" +
                "    <div class=\"btn-list\">\n" +
                "      <button class=\"btn\" type=\"button\" data-action=\"redirectTo\">返回</button>\n" +
                "    </div>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      var userAgent = navigator.userAgent;\n" +
                "      if (userAgent.indexOf('AlipayClient') > -1) {\n" +
                "        // 支付宝小程序的 JS-SDK 防止 404 需要动态加载，如果不需要兼容支付宝小程序，则无需引用此 JS 文件。\n" +
                "        document.writeln('<script src=\"https://appx/web-view.min.js\"' + '>' + '<' + '/' + 'script>');\n" +
                "      } else if (/QQ/i.test(userAgent) && /miniProgram/i.test(userAgent)) {\n" +
                "        // QQ 小程序\n" +
                "        document.write(\n" +
                "          '<script type=\"text/javascript\" src=\"https://qqq.gtimg.cn/miniprogram/webview_jssdk/qqjssdk-1.0.0.js\"><\\/script>'\n" +
                "        );\n" +
                "      } else if (/miniProgram/i.test(userAgent) && /micromessenger/i.test(userAgent)) {\n" +
                "        // 微信小程序 JS-SDK 如果不需要兼容微信小程序，则无需引用此 JS 文件。\n" +
                "        document.write('<script type=\"text/javascript\" src=\"https://res.wx.qq.com/open/js/jweixin-1.4.0.js\"><\\/script>');\n" +
                "      } else if (/toutiaomicroapp/i.test(userAgent)) {\n" +
                "        // 头条小程序 JS-SDK 如果不需要兼容头条小程序，则无需引用此 JS 文件。\n" +
                "        document.write(\n" +
                "          '<script type=\"text/javascript\" src=\"https://s3.pstatp.com/toutiao/tmajssdk/jssdk-1.0.1.js\"><\\/script>');\n" +
                "      } else if (/swan/i.test(userAgent)) {\n" +
                "        // 百度小程序 JS-SDK 如果不需要兼容百度小程序，则无需引用此 JS 文件。\n" +
                "        document.write(\n" +
                "          '<script type=\"text/javascript\" src=\"https://b.bdstatic.com/searchbox/icms/searchbox/js/swan-2.0.18.js\"><\\/script>'\n" +
                "        );\n" +
                "      } else if (/quickapp/i.test(userAgent)) {\n" +
                "        // quickapp\n" +
                "        document.write('<script type=\"text/javascript\" src=\"https://quickapp/jssdk.webview.min.js\"><\\/script>');\n" +
                "      }\n" +
                "      if (!/toutiaomicroapp/i.test(userAgent)) {\n" +
                "        document.querySelector('.post-message-section').style.visibility = 'visible';\n" +
                "      }\n" +
                "    </script>\n" +
                "    <!-- uni 的 SDK -->\n" +
                "    <!-- 需要把 uni.webview.1.5.4.js 下载到自己的服务器 -->\n" +
                "    <script type=\"text/javascript\" src=\"https://unpkg.com/@dcloudio/uni-webview-js@0.0.3/index.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      // 待触发 `UniAppJSBridgeReady` 事件后，即可调用 uni 的 API。\n" +
                "      document.addEventListener('UniAppJSBridgeReady', function() {\n" +
                "        uni.postMessage({\n" +
                "            data: {\n" +
                "                action: 'message'\n" +
                "            }\n" +
                "        });\n" +
                "        uni.getEnv(function(res) {\n" +
                "            console.log('当前环境：' + JSON.stringify(res));\n" +
                "        });\n" +
                "\n" +
                "        document.querySelector('.btn-list').addEventListener('click', function(evt) {\n" +
                "          var target = evt.target;\n" +
                "          if (target.tagName === 'BUTTON') {\n" +
                "            var action = target.getAttribute('data-action');\n" +
                "            switch (action) {\n" +
                "              case 'switchTab':\n" +
                "                uni.switchTab({\n" +
                "                  url: '/pages/paymentScore/paymentScore'\n" +
                "                });\n" +
                "                break;\n" +
                "              case 'reLaunch':\n" +
                "                uni.reLaunch({\n" +
                "                  url: '/pages/paymentScore/paymentScore'\n" +
                "                });\n" +
                "                break;\n" +
                "              case 'navigateBack':\n" +
                "                uni.navigateBack({\n" +
                "                  delta: 1\n" +
                "                });\n" +
                "                break;\n" +
                "              default:\n" +
                "                uni[action]({\n" +
                "                  url: '/pages/paymentScore/paymentScore'\n" +
                "                });\n" +
                "                break;\n" +
                "            }\n" +
                "          }\n" +
                "        });\n" +
                "        document.getElementById('postMessage').addEventListener('click', function() {\n" +
                "          uni.postMessage({\n" +
                "            data: {\n" +
                "              action: 'message'\n" +
                "            }\n" +
                "          });\n" +
                "        });\n" +
                "      });\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";
//        if (subReturnVo.isResult()){
//            HTML+="<h1 style='color:green;'>充值成功，点击左上角返回按钮</h1>";
//        }else {
//            HTML+="<h1 style='color:red;'>充值失败，点击左上角返回按钮</h1>";
//        }
        return HTML;
    }


    //必须加ResponseBody注解，否则spring会寻找thymeleaf页面
    @RequestMapping("/alipay")
    public String alipay(HttpSession session, Model model,
                         @RequestParam("money") Integer money,
                         @RequestParam("uid") Integer uid,
                         @RequestParam("dsc")Integer dsc,
                         @RequestParam(value = "orderid",required = false)String orderId
                        ) throws AlipayApiException {


        String OrderNum ="";
        //生成订单号（支付宝的要求？）
        if (orderId==null){
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String user = UUID.randomUUID().toString().replace("-","").toUpperCase();
            OrderNum = time+user+"@"+uid+"@"+dsc;


            //订单service
            Zborder zborder = new Zborder();
            zborder.setDsc(dsc);
            zborder.setZborderid(OrderNum);
            zborder.setDscabb(dsc+"积分充值");
            zborder.setMoney(BigDecimal.valueOf(money));
            zborder.setUid(uid);
            zborder.setZbcode("");
            zborder.setStatus(0);
            zborder.setCreatetime(timeUtil.getCurrentTimeLocalDateTime());
            boolean result = zborderService.save( zborder);
            if (result){
                // 初始化时间轮
                // 注册此定时任务（延迟时间为1天，也就是说1天后订单过期
                zbOrderTimeTask = new ZbOrderTimeTask(zborder.getId());
                zbOrderTimer.newTimeout(zbOrderTimeTask,1, TimeUnit.DAYS);
            }

        }else{
            OrderNum = orderId;
        }





        //调用封装好的方法（给支付宝接口发送请求）
        return sendRequestToAlipay(OrderNum,money,uid,dsc,dsc+"积分充值");
    }
    /*
参数1：订单号
参数2：订单金额
参数3：订单名称
 */
    //支付宝官方提供的接口
    private String sendRequestToAlipay(String outTradeNo,Integer totalAmount,Integer uid,Integer dsc,String subject) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,APP_ID,APP_PRIVATE_KEY,FORMAT,CHARSET,ALIPAY_PUBLIC_KEY,SIGN_TYPE);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(url+RETURN_URL);
      alipayRequest.setNotifyUrl(url+NOTIFY_URL);

        //商品描述（可空）
        String body=uid+"@"+dsc;
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","
                + "\"total_amount\":\"" + totalAmount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        return result;
    }
}