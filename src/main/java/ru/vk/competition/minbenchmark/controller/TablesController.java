package ru.vk.competition.minbenchmark.controller;

import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.competition.minbenchmark.dto.ColumnMetaDto;
import ru.vk.competition.minbenchmark.dto.TableMetaDto;
import ru.vk.competition.minbenchmark.service.TableQueriesService;
import ru.vk.competition.minbenchmark.service.TablesService;
import ru.vk.competition.minbenchmark.utils.Loggable;

@Slf4j
@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
public class TablesController {

    private static final Map<String, String> SQL_TYPE_FIXES = Map.of("VARCHAR","CHARACTER VARYING",
                                                                     "VARCHAR\\(\\d+\\)", "CHARACTER VARYING");

    private final TablesService tablesService;

    private final TableQueriesService tableQueriesService;

    @Loggable
    @RequestMapping(value = "create-table", method = RequestMethod.POST)
    public ResponseEntity createTable(@Validated @RequestBody TableMetaDto tableMetaDto) {
        log.info("{}: {}", tableMetaDto.getTableName(), tableMetaDto);

        if (!validate(tableMetaDto)) {
            log.info("invalid meta");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            for (ColumnMetaDto column : tableMetaDto.getColumnInfos()) {
                log.info("{}:{}", column.getTitle(), column.getType());
                if (column.getTitle().equalsIgnoreCase("integer")) {
                    column.setTitle("int4");
                }
            }
            tablesService.createTable(tableMetaDto);
            log.info("table created");
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IllegalArgumentException iae) {
            log.info("failed create table: {}", ExceptionUtils.getRootCause(iae).getMessage());
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Loggable
    @RequestMapping(value = "get-table-by-name/{name}", method = RequestMethod.GET)
    public ResponseEntity<TableMetaDto> getTable(@PathVariable("name") String name) {

        TableMetaDto tableMeta = tablesService.findTableMeta(name);
        log.info("found {}", tableMeta != null);

        if (tableMeta != null) {
            for (ColumnMetaDto column : tableMeta.getColumnInfos()) {
                column.setType(typeConverter(column.getType()));
                log.info("{}:{}", column.getTitle(), column.getType());
            }
        }

        return new ResponseEntity<>(tableMeta, HttpStatus.OK);
    }

    @Loggable
    @RequestMapping(value = "drop-table/{name}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTable(@PathVariable("name") String name) {

        try {
            tablesService.deleteTable(name);
            int deletedQueries = tableQueriesService.delete(name);
            log.info("table {} deleted with {} queries", name, deletedQueries);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IllegalArgumentException iae) {
            log.info("couldn't delete table {}: {}", name, ExceptionUtils.getRootCause(iae).getMessage());
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    private boolean validate(TableMetaDto tableMetaDto) {
        if (StringUtils.isBlank(tableMetaDto.getTableName())) {
            return false;
        }
        if (StringUtils.isBlank(tableMetaDto.getPrimaryKey())) {
            return false;
        }
        if (tableMetaDto.getColumnInfos() == null) {
            return false;
        } else if (tableMetaDto.getColumnInfos().size() != tableMetaDto.getColumnsAmount()) {
            return false;
        }

        return true;
    }

    static String typeConverter(String orig) {
        for (Entry<String, String> entry : SQL_TYPE_FIXES.entrySet()) {
            if (orig.matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return orig;
    }
}
