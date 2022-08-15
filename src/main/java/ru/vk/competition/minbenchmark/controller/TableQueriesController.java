package ru.vk.competition.minbenchmark.controller;

import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import ru.vk.competition.minbenchmark.utils.Loggable;

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
    @Loggable
    @RequestMapping(value = "add-new-query-to-table", method = RequestMethod.POST)
    public ResponseEntity createQuery(@RequestBody TableQueryDto tableQueryDto) {
        log.info("{}", tableQueryDto);

        if (!validate(tableQueryDto)) {
            log.info("invalid dto");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        if (tablesService.findTableMeta(tableQueryDto.getTableName()) == null) {
            log.info("table doesn't exist");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            tableQueriesService.create(tableQueryDto);
            log.info("table query created");
        } catch (IllegalArgumentException iae) {
            log.info(ExceptionUtils.getRootCause(iae).getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Изменение запроса таблицы
     */
    @Loggable
    @RequestMapping(value = "modify-query-in-table", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody TableQueryDto tableQueryDto) {
        log.info("{}", tableQueryDto);

        if (!validate(tableQueryDto)) {
            log.info("invalid dto");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        if (tablesService.findTableMeta(tableQueryDto.getTableName()) == null) {
            log.info("table doesn't exist");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            tableQueriesService.update(tableQueryDto);
            log.info("table query updated");
        } catch (IllegalArgumentException iae) {
            log.info(ExceptionUtils.getRootCause(iae).getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Удаление запроса таблицы
     */
    @Loggable
    @RequestMapping(value = "delete-table-query-by-id/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") int id) {

        try {
            tableQueriesService.delete(id);
            log.info("table query deleted");
        } catch (IllegalArgumentException iae) {
            log.info(ExceptionUtils.getRootCause(iae).getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    /**
     * Получение запроса таблицы по id
     */
    @Loggable
    @RequestMapping(value = "get-table-query-by-id/{id}", method = RequestMethod.GET)
    public ResponseEntity<TableQueryDto> get(@PathVariable("id") int queryId) {

        TableQueryDto tableQueryDto = tableQueriesService.getById(queryId);
        log.info("table query found {}", tableQueryDto != null);

        if (tableQueryDto == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(tableQueryDto, HttpStatus.OK);
        }
    }

    /**
     * Получение всех запросов таблицы
     */
    @Loggable
    @RequestMapping(value = "get-all-queries-by-table-name/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<TableQueryDto>> get(@PathVariable("name") String tableName) {

        if (tablesService.findTableMeta(tableName) == null) {
            log.info("table doesn't exist");
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        List<TableQueryDto> tableQueries = tableQueriesService.getByTableName(tableName);

        log.info("Found {} table queries", tableQueries.size());

        return new ResponseEntity<>(tableQueries, HttpStatus.OK);
    }

    /**
     * Получение всех запросов
     */
    @Loggable
    @RequestMapping(value = "get-all-table-queries", method = RequestMethod.GET)
    public List<TableQueryDto> getAll() {

        List<TableQueryDto> tableQueries = tableQueriesService.getAll();

        log.info("Found {} table queries", tableQueries.size());

        return tableQueries;
    }

    /**
     * Запуск запроса таблицы
     */
    @Loggable
    @RequestMapping(value = "execute-table-query-by-id/{id}", method = RequestMethod.GET)
    public ResponseEntity execute(@PathVariable("id") int id) {

        try {
            tableQueriesService.execute(id);
            log.info("Table query execution passed");
        } catch (IllegalArgumentException iae) {
            log.info(ExceptionUtils.getRootCause(iae).getMessage());
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
            log.info("Query is empty");
            return false;
        }
        if (tableQueryDto.getQuery().length() > 120) {
            log.info("Query is too long: {}", tableQueryDto.getQuery().length());
            return false;
        }
        if (StringUtils.isBlank(tableQueryDto.getTableName())) {
            log.info("Query table is empty");
            return false;
        }
        if (tableQueryDto.getTableName().length() > 50) {
            log.info("Query table is too long: {}", tableQueryDto.getTableName().length());
            return false;
        }
        return true;
    }
}
