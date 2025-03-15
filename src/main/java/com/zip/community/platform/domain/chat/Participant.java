package com.zip.community.platform.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    private Long id;
    private String name;

    public static Participant of(Long id, String name) {
        return Participant.builder()
                .id(id)
                .name(name)
                .build();
    }
}
