package com.dqy.englishstudyapi.util;

import com.dqy.englishstudyapi.vo.RequestResultVO;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Data
@ToString
@Component
public class RequestUtil {
    @Value("${dqy.wordUrl}")
    String wordUrl;
    // 创建 WebClient 对象

    /*
     * 向目的URL发送post请求
     *
     * */
        public void sendPostRequest(String url, MultiValueMap<String,String> params) {
            WebClient webClient =getWebClient();

            // 发送请求
            Mono<String> mono = webClient
                    .post() // POST 请求
                    .uri(wordUrl)  // 请求路径
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject(params))
                    .retrieve() // 获取响应体
                    .bodyToMono(String.class); //响应数据类型转换

            // 输出结果
            System.out.println(mono.block());
            return;

    }

    public WebClient  getWebClient(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json;charset=utf-8");
        headers.set("Server","YDWS");
        headers.set("Transfer-Encoding","chunked");
        headers.set("Content-Encoding","gzip");
        headers.set("extra","g4qpbLr-DqPvODnd_UhflFasOYxUtnyhHcj_p0X_lII=");
        Consumer<HttpHeaders> headersConsumer = httpHeaders -> {
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                httpHeaders.put(entry.getKey(), entry.getValue());
            }
        };
            WebClient webClient = WebClient.builder()
                .defaultHeaders(headersConsumer)
                .defaultCookie("ACCESS_TOKEN", "test_token")
                .build();

            return webClient;
    }
}
