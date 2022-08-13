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
public class TableReportDto {

    private String tableName;
    private List<ColumnMetaDto> columns;
}
