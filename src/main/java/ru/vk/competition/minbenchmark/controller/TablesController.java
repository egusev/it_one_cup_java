package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity createTable(@Validated TableMetaDto tableMetaDto) {
        try {
            tablesService.createTable(tableMetaDto);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        catch (IllegalArgumentException iae) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "get-table-by-name/{name}", method = RequestMethod.GET)
    public ResponseEntity<TableMetaDto> getTable(@PathVariable("name") String name) {
        TableMetaDto tableMeta = tablesService.findTableMeta(name);
        return new ResponseEntity<>(tableMeta, HttpStatus.OK);
    }

    @RequestMapping(value = "drop-table-by-name/{name}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTable(@PathVariable("name") String name) {
        try {
            tablesService.deleteTable(name);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        catch (IllegalArgumentException iae) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

    }

}
