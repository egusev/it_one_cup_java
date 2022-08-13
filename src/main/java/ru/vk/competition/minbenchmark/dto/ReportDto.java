package ru.vk.competition.minbenchmark.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private int reportId;
    private int tableAmount;
    private List<TableReportDto> tables;

}
