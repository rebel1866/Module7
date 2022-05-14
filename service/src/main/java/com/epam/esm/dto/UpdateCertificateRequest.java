package com.epam.esm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
public class UpdateCertificateRequest {
    @Size(min = 1,message = "rCode15")
    private String certificateName;
    @Size(min = 1,message = "rCode16")
    private String description;
    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "rCode12")
    private String price;
    @Pattern(regexp = "^[1-9]+[0-9]*$", message = "rCode1")
    private String duration;
    @Valid
    private List<TagDto> tags;
}
