package com.epam.esm.dto;


import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {
    @Positive(message = "messageCode10")
    private Integer tagId;
    @Size(min = 1, message = "rCode17")
    private String tagName;
}
