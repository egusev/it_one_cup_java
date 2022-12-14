package ru.vk.competition.minbenchmark.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlSeeAlso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlSeeAlso(List.class)
public class TableMetaDto {

    private String tableName;
    private int columnsAmount;
    private String primaryKey;
    private List<ColumnMetaDto> columnInfos;
}
