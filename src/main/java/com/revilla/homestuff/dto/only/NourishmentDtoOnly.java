package com.revilla.homestuff.dto.only;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revilla.homestuff.dto.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * NourishmentDto
 * @author Kirenai
 */
@Data
@ToString(exclude = {"category"})
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NourishmentDtoOnly {

    private Long nourishmentId;

    private String name;

    private String imagePath;

    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isAvailable;

    private CategoryDto category;

}
