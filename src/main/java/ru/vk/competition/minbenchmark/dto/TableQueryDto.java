package ru.vk.competition.minbenchmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TableQueryDto extends QueryDto {

    public TableQueryDto(int queryId, String query, String tableName) {
        super(queryId, query);
        this.tableName = tableName;
    }

    private String tableName;

}
