package com.bsanju.stressmate.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {
    private String userId;
    private String message;
    private String response;
    private String sentiment;
    private LocalDateTime timestamp;
}
