package org.example.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AlipayConfig;
import org.example.domain.common.R;
import org.example.domain.vo.AlipayTradeVO;
import org.example.enums.AliPayStatusEnum;
import org.example.service.AlipayService;
import org.example.utils.AlipayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/alipay")
public class AlipayController {

    @Resource
    private AlipayService alipayService;

    @Resource
    private AlipayUtils alipayUtils;

    @Resource
    private AlipayConfig alipayConfig;

    @ApiOperation("PC网页支付")
    @GetMapping(value = "/toPayAsPC")
    public R<Object> toPayAsPc(@ApiParam(value = "订单号", required = true) @RequestParam("orderId") String orderId, @ApiParam(value = "订单金额", required = true) @RequestParam("amount") BigDecimal amount, @ApiParam(value = "商品名称") @RequestParam("body") String body, HttpServletRequest request) throws Exception {

        // 参数校验
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return R.error("订单金额必须大于0");
        }

        String payUrl = "";
        AlipayTradeVO trade = new AlipayTradeVO();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        trade.setBody(body);
        trade.setTradeNo(orderId);
        trade.setTotalAmount(amount);
        trade.setSubject(body);
        payUrl = alipayService.payPc(trade);
        // payUrl = alipayService.payH5(trade);

        if (StringUtils.hasText(payUrl)) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("out_trade_no", trade.getOutTradeNo());
            resultMap.put("code_url", payUrl);
            return R.ok(resultMap);
        } else {
            return R.error("无效的订单,无法支付");
        }
    }


    @ApiIgnore
    @GetMapping("/return")
    @ApiOperation("支付之后跳转的链接")
    public ResponseEntity<String> returnPage(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html;charset=" + alipayConfig.getCharset());
        // 内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipayConfig)) {
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            log.info("商户订单号: {}, 第三方交易号: {}", outTradeNo, tradeNo);


            // 根据业务需要返回数据，这里统一返回OK
            return new ResponseEntity<>("payment successful", HttpStatus.OK);
        } else {
            // 根据业务需要返回数据
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiIgnore
    @RequestMapping("/notify")
    @ApiOperation("支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理")
    public ResponseEntity<Object> notify(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipayConfig)) {
            // 交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 付款金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 验证
            if (tradeStatus.equals(AliPayStatusEnum.TRADE_SUCCESS.getValue()) || tradeStatus.equals(AliPayStatusEnum.TRADE_FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
                log.info("===============支付结果通知==================");
                log.info("交易状态: {}", tradeStatus);
                log.info("商户订单号: {}", outTradeNo);
                log.info("支付宝交易号: {}", tradeNo);
                log.info("付款金额: {}", totalAmount);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @ApiOperation("支付状态查询")
    @GetMapping(value = "/toPayTradeQuery")
    public Map<String, String> toPayTradeQuery(@RequestParam("outOrderNo") String outOrderNo) throws Exception {
        return alipayService.toPayTradeQuery(outOrderNo);
    }


}

