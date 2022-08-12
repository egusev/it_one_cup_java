package ru.vk.competition.minbenchmark.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableMetaDto {

    private String tableName;
    private int columnsAmount;
    private String primaryKey;
    private List<ColumnMetaDTO> columnInfos;
}
