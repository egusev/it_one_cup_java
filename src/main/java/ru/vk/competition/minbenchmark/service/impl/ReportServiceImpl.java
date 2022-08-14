package ru.vk.competition.minbenchmark.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.competition.minbenchmark.dto.ColumnMetaDto;
import ru.vk.competition.minbenchmark.dto.ReportDto;
import ru.vk.competition.minbenchmark.dto.TableMetaDto;
import ru.vk.competition.minbenchmark.dto.TableReportDto;
import ru.vk.competition.minbenchmark.repository.QueriesDao;
import ru.vk.competition.minbenchmark.service.ReportService;
import ru.vk.competition.minbenchmark.service.TablesService;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private TablesService tablesService;

    @Autowired
    private QueriesDao queriesDao;

    private Map<Integer, ReportDto> idToReport = new HashMap<>();

    @Override
    @Synchronized
    public void createReport(ReportDto reportDto) throws IllegalArgumentException {
        if (idToReport.containsKey(reportDto.getReportId())) {
            throw new IllegalArgumentException("Report " + reportDto.getReportId() + " already exists");
        }
        validateRequest(reportDto);

        executeReport(reportDto);

        idToReport.put(reportDto.getReportId(), reportDto);
    }

    @Override
    public ReportDto getReport(int id) {
        return idToReport.get(id);
    }

    private void executeReport(ReportDto request) throws IllegalArgumentException {
        for (TableReportDto table : request.getTables()) {
            Map<String, String> result = calculateCounts(table);

            for (ColumnMetaDto column : table.getColumns()) {
                String value = result.get(column.getTitle().toUpperCase());
                column.setSize(value);
            }
        }
    }

    Map<String, String> calculateCounts(TableReportDto tableReportDto) throws IllegalArgumentException {
        StringBuilder query = new StringBuilder("SELECT ");
        String separator = "";
        for (ColumnMetaDto column : tableReportDto.getColumns()) {
            String name = column.getTitle();
            query.append(separator);
            query.append("COUNT(");
            query.append(name);
            query.append(") AS ");
            query.append(name);
            separator = ", ";
        }
        query.append(" FROM ");
        query.append(tableReportDto.getTableName());
        try {
            Map<String, String> ret = new HashMap<>();
            Map<String, Object> result = queriesDao.queryForMap(query.toString());
            for (Entry<String, Object> entry : result.entrySet()) {
                ret.put(entry.getKey().toUpperCase(), entry.getValue().toString());
            }
            return ret;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void validateRequest(ReportDto request) throws IllegalArgumentException {
        for (TableReportDto table : request.getTables()) {
            String tableName = table.getTableName();
            if (StringUtils.isBlank(tableName)) {
                throw new IllegalArgumentException("Missed table name in the request");
            }

            log.info("Process {}", tableName);

            TableMetaDto tableMeta = tablesService.findTableMeta(tableName);
            Map<String, ColumnMetaDto> nameToColumn = tableMeta.getColumnInfos()
                                                               .stream()
                                                               .collect(Collectors.toMap(ColumnMetaDto::getTitle, c -> c));

            for (ColumnMetaDto requestedColumn : table.getColumns()) {
                ColumnMetaDto column = nameToColumn.get(requestedColumn.getTitle().toUpperCase());

                if (column == null) {
                    throw new IllegalArgumentException("Column " + tableName + "." + requestedColumn.getTitle() + " is missed");
                }

                if (!column.getType().equalsIgnoreCase(convert(requestedColumn.getType()))) {
                    throw new IllegalArgumentException("Incorrect type " + requestedColumn.getType() + "/" + column.getType()
                                                       + " of " + tableName + "." + requestedColumn.getTitle());
                }
            }
        }
    }

    private String convert(String type) {
        String value = type.trim();

        switch (value) {
            case "int1":
                return "tinyint";
            case "int2":
                return "smallint";
            case "int4":
                return "integer";
            case "int8":
                return "bigint";
            default:
                return value;
        }
    }

}
