package ru.vk.competition.minbenchmark.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;
import ru.vk.competition.minbenchmark.repository.QueriesDao;
import ru.vk.competition.minbenchmark.service.TableQueriesService;

@Service
public class TableQueriesServiceImpl implements TableQueriesService {

    @Autowired
    private QueriesDao queriesDao;

    private final Map<Integer, TableQueryDto> idToTableQueryMap = new HashMap<>();

    private final Map<String, Set<Integer>> tableNameToIds = new HashMap<>();

    @Override
    public void create(@NotNull TableQueryDto tableQueryDto) throws IllegalArgumentException {
        int id = tableQueryDto.getQueryId();

        if (getById(id) != null) {
            throw new IllegalArgumentException("Table query with id " + id + " already exists");
        }
        add(tableQueryDto);
    }

    @Override
    public void update(@NotNull TableQueryDto tableQueryDto) throws IllegalArgumentException {
        int id = tableQueryDto.getQueryId();

        if (getById(id) == null) {
            throw new IllegalArgumentException("Table query with id " + id + " doesn't exist");
        }

        idToTableQueryMap.put(id, tableQueryDto);
    }

    @Override
    public @NotNull List<TableQueryDto> getAll() {
        return new ArrayList<>(idToTableQueryMap.values());
    }

    @Override
    public @Null TableQueryDto getById(int id) {
        return idToTableQueryMap.get(id);
    }

    @Override
    public @NotNull List<TableQueryDto> getByTableName(@NotNull String tableName) {
        Set<Integer> ids = tableNameToIds.get(tableName);
        if (ids == null) {
            return Collections.emptyList();
        }

        return ids.stream()
                  .map(idToTableQueryMap::get)
                  .collect(Collectors.toList());
    }

    @Override
    public void delete(int id) throws IllegalArgumentException {
        if (!remove(id)) {
            throw new IllegalArgumentException("Table query with id " + id + " doesn't exist");
        }
    }

    @Override
    public void execute(int id) throws IllegalArgumentException {
        TableQueryDto tableQueryDto = idToTableQueryMap.get(id);

        if (tableQueryDto == null) {
            throw new IllegalArgumentException("Table query with id " + id + " doesn't exist");
        }

        try {
            queriesDao.execute(tableQueryDto.getQuery());
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    void add(TableQueryDto tableQueryDto) {
        idToTableQueryMap.put(tableQueryDto.getQueryId(), tableQueryDto);
        tableNameToIds.computeIfAbsent(tableQueryDto.getTableName(), i -> new HashSet<>())
                      .add(tableQueryDto.getQueryId());
    }

    boolean remove(int id) {
        TableQueryDto tableQueryDto = idToTableQueryMap.remove(id);
        return tableQueryDto != null && tableNameToIds.get(tableQueryDto.getTableName()).remove(id);
    }

}
