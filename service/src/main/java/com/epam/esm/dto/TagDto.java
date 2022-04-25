package com.epam.esm.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class TagDto extends RepresentationModel<TagDto> {
    @Positive(message = "messageCode10")
    private Integer tagId;
    @Size(min = 1, message = "rCode17")
    private String tagName;
}
