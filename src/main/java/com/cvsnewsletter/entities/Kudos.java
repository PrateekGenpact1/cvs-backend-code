package com.cvsnewsletter.entities;

import com.cvsnewsletter.entities.enums.DesignationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "kudos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kudos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ohrId;
    private String name;

    @Enumerated(EnumType.STRING)
    private DesignationType designation;

    private String recipientOhrId;
    private String recipientName;
    private String message;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private Boolean isApproved = false;
}
