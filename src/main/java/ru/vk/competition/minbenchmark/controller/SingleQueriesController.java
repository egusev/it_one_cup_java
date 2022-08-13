package ru.vk.competition.minbenchmark.controller;

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
import ru.vk.competition.minbenchmark.dto.QueryDto;
import ru.vk.competition.minbenchmark.service.SingleQueriesService;

@Slf4j
@RestController
@RequestMapping("/api/single-query")
@RequiredArgsConstructor
public class SingleQueriesController {

    @Autowired
    private final SingleQueriesService singleQueriesService;

    /**
     * Создание нового запроса таблицы
     */
    @RequestMapping(value = "add-new-query", method = RequestMethod.POST)
    public ResponseEntity createQuery(@RequestBody QueryDto queryDto) {

        if (!validate(queryDto)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        try {
            singleQueriesService.create(queryDto);
        } catch (IllegalArgumentException iae) {
            log.info(iae.getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Изменение запроса таблицы
     */
    @RequestMapping(value = "modify-query ", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody QueryDto queryDto) {

        if (!validate(queryDto)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            singleQueriesService.update(queryDto);
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
    @RequestMapping(value = "delete-single-query-by-id/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") int id) {
        try {
            singleQueriesService.delete(id);
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
    @RequestMapping(value = "get-single-query-by-id/{id}", method = RequestMethod.GET)
    public ResponseEntity<QueryDto> get(@PathVariable("id") int queryId) {
        QueryDto tableQueryDto = singleQueriesService.getById(queryId);
        if (tableQueryDto == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(tableQueryDto, HttpStatus.OK);
        }
    }

    /**
     * Получение всех запросов
     */
    @RequestMapping(value = "get-all-single-queries", method = RequestMethod.GET)
    public List<QueryDto> getAll() {
        return singleQueriesService.getAll();
    }

    /**
     * Запуск запроса таблицы
     */
    @RequestMapping(value = "execute-single-query-by-id/{id}", method = RequestMethod.GET)
    public ResponseEntity execute(@PathVariable("id") int id) {
        try {
            singleQueriesService.execute(id);
        } catch (IllegalArgumentException iae) {
            log.info(iae.getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    private boolean validate(@Null QueryDto queryDto) {
        if (queryDto == null) {
            return false;
        }
        if (StringUtils.isBlank(queryDto.getQuery())) {
            return false;
        }
        return true;
    }
}
