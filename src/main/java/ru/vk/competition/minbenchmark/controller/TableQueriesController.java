package ru.vk.competition.minbenchmark.controller;

import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;
import ru.vk.competition.minbenchmark.service.TableQueriesService;
import ru.vk.competition.minbenchmark.service.TablesService;

@Slf4j
@RestController
@RequestMapping("/api/table-query")
@RequiredArgsConstructor
public class TableQueriesController {

    @Autowired
    private final TableQueriesService tableQueriesService;

    @Autowired
    private final TablesService tablesService;

    /**
     * Создание нового запроса таблицы
     */
    @RequestMapping(value = "add-new-query-to-table", method = RequestMethod.POST)
    public ResponseEntity createQuery(@RequestBody TableQueryDto tableQueryDto) {

        if (!validate(tableQueryDto)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        if (tablesService.findTableMeta(tableQueryDto.getTableName()) == null) {
            // table doesn't exist
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            tableQueriesService.create(tableQueryDto);
        } catch (IllegalArgumentException iae) {
            log.info(iae.getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Изменение запроса таблицы
     */
    @RequestMapping(value = "modify-query-in-table", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody TableQueryDto tableQueryDto) {

        if (!validate(tableQueryDto)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        if (tablesService.findTableMeta(tableQueryDto.getTableName()) == null) {
            // table doesn't exist
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            tableQueriesService.update(tableQueryDto);
        } catch (IllegalArgumentException iae) {
            log.info(iae.getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Удаление запроса таблицы
     */
    @RequestMapping(value = "delete-table-query-by-id/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") int id) {
        try {
            tableQueriesService.delete(id);
        } catch (IllegalArgumentException iae) {
            log.info(iae.getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    /**
     * Получение запроса таблицы по id
     */
    @RequestMapping(value = "get-table-query-by-id/{id}", method = RequestMethod.GET)
    public ResponseEntity<TableQueryDto> get(@PathVariable("id") int queryId) {
        TableQueryDto tableQueryDto = tableQueriesService.getById(queryId);
        if (tableQueryDto == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(tableQueryDto, HttpStatus.OK);
        }
    }

    /**
     * Получение всех запросов таблицы
     */
    @RequestMapping(value = "get-all-queries-by-table-name/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<TableQueryDto>> get(@PathVariable("name") String tableName) {

        if (tablesService.findTableMeta(tableName) == null) {
            // table doesn't exist
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        return new ResponseEntity<>(tableQueriesService.getByTableName(tableName), HttpStatus.OK);
    }

    /**
     * Получение всех запросов
     */
    @RequestMapping(value = "get-all-table-queries", method = RequestMethod.GET)
    public List<TableQueryDto> getAll() {
        return tableQueriesService.getAll();
    }

    /**
     * Запуск запроса таблицы
     */
    @RequestMapping(value = "execute-table-query-by-id/{id}", method = RequestMethod.GET)
    public ResponseEntity execute(@PathVariable("id") int id) {
        try {
            tableQueriesService.execute(id);
        }
        catch (IllegalArgumentException iae) {
            log.info(iae.getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    private boolean validate(@Null TableQueryDto tableQueryDto) {
        if (tableQueryDto == null) {
            return false;
        }
        if (StringUtils.isBlank(tableQueryDto.getQuery())) {
            return false;
        }
        if (StringUtils.isBlank(tableQueryDto.getTableName())) {
            return false;
        }
        return true;
    }
}
