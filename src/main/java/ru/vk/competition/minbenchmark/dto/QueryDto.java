package ru.vk.competition.minbenchmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryDto {

    private int queryId;
    private String query;

}
