package com.cvsnewsletter.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KudosRequest {
    @NotBlank(message = "Sender OHR ID is mandatory")
    private String ohrId;

    @NotBlank(message = "Designation is mandatory")
    private String designation;

    @NotBlank(message = "Recipient OHR ID is mandatory")
    private String recipientOhrId;

    @NotBlank(message = "Recipient name is mandatory")
    private String recipientName;

    @NotBlank(message = "Message is mandatory")
    private String message;

}

