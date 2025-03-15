package com.zip.community.platform.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LastMessage {

    private String content;
    private String sender;
    private LocalDateTime sentAt;

    public static LastMessage of(String content, String sender, LocalDateTime sentAt) {
        return LastMessage.builder()
                .content(content)
                .sender(sender)
                .sentAt(sentAt)
                .build();
    }
}
