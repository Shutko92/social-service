package ru.skillbox.diplom.group42.social.service.dto.email.recovery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailsDto {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public EmailDetailsDto(String recipient, String msgBody, String subject){
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
    }
}