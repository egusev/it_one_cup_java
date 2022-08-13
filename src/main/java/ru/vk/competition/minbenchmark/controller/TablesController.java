package ru.vk.competition.minbenchmark.controller;

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
import ru.vk.competition.minbenchmark.dto.TableMetaDto;
import ru.vk.competition.minbenchmark.service.TablesService;

@Slf4j
@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
public class TablesController {

    private final TablesService tablesService;

    @RequestMapping(value = "create-table", method = RequestMethod.POST)
    public ResponseEntity createTable(@Validated @RequestBody TableMetaDto tableMetaDto) {
        log.info("POST /api/table/create-table {}", tableMetaDto.getTableName());

        if (!validate(tableMetaDto)) {
            log.info("invalid meta");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            tablesService.createTable(tableMetaDto);
            log.info("table created");
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IllegalArgumentException iae) {
            log.info("failed create table: {}", ExceptionUtils.getRootCause(iae).getMessage());
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "get-table-by-name/{name}", method = RequestMethod.GET)
    public ResponseEntity<TableMetaDto> getTable(@PathVariable("name") String name) {
        log.info("GET /api/table/get-table-by-name/{}", name);
        TableMetaDto tableMeta = tablesService.findTableMeta(name);
        log.info("found {}", tableMeta != null);
        return new ResponseEntity<>(tableMeta, HttpStatus.OK);
    }

    @RequestMapping(value = "drop-table/{name}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTable(@PathVariable("name") String name) {
        log.info("DELETE /api/table/drop-table/{}", name);
        try {
            tablesService.deleteTable(name);
            log.info("table {} deleted", name);
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
}
