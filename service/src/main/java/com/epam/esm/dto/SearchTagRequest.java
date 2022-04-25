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
    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "rCode21")
    private String page;
    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "rCode22")
    private String pageSize;
}
