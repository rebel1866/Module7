package com.epam.esm.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class SearchTagRequest {
    @Size(min = 1, message = "rCode17")
    private String tag;
    @Pattern(regexp = "tagId|tagName",
            message = "rCode18")
    private String sorting;
    @Pattern(regexp = "asc|desc", message = "rCode5")
    private String sortingOrder;
}
