package ru.vk.competition.minbenchmark.service;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;

public interface TableQueriesService {

    void create(@NotNull TableQueryDto tableQueryDto) throws IllegalArgumentException;

    void update(@NotNull TableQueryDto tableQueryDto) throws IllegalArgumentException;

    @NotNull
    List<TableQueryDto> getAll();

    @Null
    TableQueryDto getById(int id);

    @NotNull
    List<TableQueryDto> getByTableName(@NotNull String tableName);

    void delete(int id) throws IllegalArgumentException;

    int delete(String tableName);
    
    void execute(int id) throws IllegalArgumentException;

}
