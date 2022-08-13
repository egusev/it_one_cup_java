package ru.vk.competition.minbenchmark.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.competition.minbenchmark.dto.QueryDto;
import ru.vk.competition.minbenchmark.repository.QueriesDao;
import ru.vk.competition.minbenchmark.service.SingleQueriesService;

@Slf4j
@Service
public class SingleQueriesServiceImpl implements SingleQueriesService {

    @Autowired
    private QueriesDao queriesDao;

    private final Map<Integer, QueryDto> idToTableQueryMap = new HashMap<>();

    @Override
    public void create(@NotNull QueryDto queryDto) throws IllegalArgumentException {
        int id = queryDto.getQueryId();

        if (getById(id) != null) {
            throw new IllegalArgumentException("Query with id " + id + " already exists");
        }

        add(queryDto);
    }

    @Override
    public void update(@NotNull QueryDto queryDto) throws IllegalArgumentException {
        int id = queryDto.getQueryId();

        if (getById(id) == null) {
            throw new IllegalArgumentException("Query with id " + id + " doesn't exist");
        }

        idToTableQueryMap.put(id, queryDto);
    }

    @Override
    public @NotNull List<QueryDto> getAll() {
        return new ArrayList<>(idToTableQueryMap.values());
    }

    @Override
    public @Null QueryDto getById(int id) {
        return idToTableQueryMap.get(id);
    }

    @Override
    public void delete(int id) throws IllegalArgumentException {
        if (!remove(id)) {
            throw new IllegalArgumentException("Query with id " + id + " doesn't exist");
        }
    }

    @Override
    public void execute(int id) throws IllegalArgumentException {
        QueryDto queryDto = idToTableQueryMap.get(id);

        if (queryDto == null) {
            throw new IllegalArgumentException("Query with id " + id + " doesn't exist");
        }

        try {
            queriesDao.execute(queryDto.getQuery());
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    void add(QueryDto tableQueryDto) {
        idToTableQueryMap.put(tableQueryDto.getQueryId(), tableQueryDto);
    }

    boolean remove(int id) {
        return idToTableQueryMap.remove(id) != null;
    }
}
