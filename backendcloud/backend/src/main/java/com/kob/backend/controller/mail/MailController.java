package com.kob.backend.controller.mail;

import com.kob.common.core.domain.R;
import com.kob.common.utils.email.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;


/**
 * 邮件发送案例
 *
 * @author Michelle.Chung
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/mail")
public class MailController {

    /**
     * 发送邮件
     *

     */
    @GetMapping("/sendSimpleMessage")
    public R<Void> sendSimpleMessage( @RequestParam Map<String, String> data) {
        String to = data.get("to");
        String subject = data.get("subject");
        String text = data.get("text");
        MailUtils.sendText(to, subject, text);
        return R.ok();
    }

    /**
     * 发送邮件（带附件）
     *
     * @param to       接收人
     * @param subject  标题
     * @param text     内容
     * @param filePath 附件路径
     */
    @GetMapping("/sendMessageWithAttachment")
    public R<Void> sendMessageWithAttachment(String to, String subject, String text, String filePath) {
        MailUtils.sendText(to, subject, text, new File(filePath));
        return R.ok();
    }

}
