package ru.vk.competition.minbenchmark.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class ColumnMetaDto {

    @NonNull
    private String title;
    @NonNull
    private String type;
    @JsonInclude(Include.NON_NULL)
    private String size;

}
