package com.epam.esm.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CertificateDto {
    private Integer giftCertificateId;
    @NotBlank(message = "rCode6")
    private String certificateName;
    @NotBlank(message = "rCode7")
    private String description;
    @Positive(message = "rCode12")
    @NotNull(message = "rCode8")
    private Integer price;
    @NotNull(message = "rCode9")
    @Positive(message = "rCode10")
    private Integer duration;
    @Valid
    @NotNull(message = "rCode11")
    private List<TagDto> tags;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creationDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastUpdateTime;
}