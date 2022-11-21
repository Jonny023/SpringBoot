## 微信服务号消息订阅

> 微信公众号、订阅号、小程序服务号消息推送，认证主体为个人只能推送到订阅号里面，无法展示在消息列表中，微信小程序消息订阅为一次性订阅，每次登录小程序
> 后需要用户主动触发是否订阅，无法自动控制订阅，每订阅一次只能发一次消息，若用户未订阅则无法发送消息

#### 依赖

```xml
<!-- 微信公众号 -->
<dependency>
    <groupId>com.github.binarywang</groupId>
    <artifactId>weixin-java-mp</artifactId>
    <version>4.4.4.B</version>
</dependency>
```

#### token校验（非必选项）

> 访问地址：http://localhost:port/api/wx/checkToken，微信填写服务地址需要校验公网域名，填入公网/api/wx/checkToken完整地址
>
> 此配置消息推送回调校验，增加业务处理，没有业务需要则不用配置

```java
package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/wx")
public class WeChatController {
    @Autowired
    private WxMpService wxMpService;

    /**
     * 接入token验证
     * @param signature 微信加密签名，signature结合了开发者填写的 token 参数和请求中的 timestamp 参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return
     */
    @RequestMapping(value = "/checkToken", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkToken(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        log.info("signature:{}, timestamp:{}, nonce:{}, echostr: {}", signature, timestamp, nonce, echostr);
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息不合法
            return "消息不合法";
        }
        // 消息合法
        return echostr;
    }
}

```





### 微信消息推送踩坑

* `thing.DATA`消息key限制长度20字符，若用它作为备注需要控制字符长度
* 服务器回调为非必选，回调是为了结合业务处理