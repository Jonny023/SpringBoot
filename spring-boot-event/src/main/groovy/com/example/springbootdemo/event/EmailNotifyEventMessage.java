package com.example.springbootdemo.event;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author Jonny
 * @date 创建时间：2023/9/5 10:48
 * @description 邮件消息内容
 */
@Data
@Builder
@ToString
public class EmailNotifyEventMessage {

    private Long msgId;
    private String title;
    private String content;
}
