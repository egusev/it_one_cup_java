package ru.vk.competition.minbenchmark.service;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import ru.vk.competition.minbenchmark.dto.QueryDto;

public interface SingleQueriesService {

    void create(@NotNull QueryDto queryDto) throws IllegalArgumentException;

    void update(@NotNull QueryDto queryDto) throws IllegalArgumentException;

    @NotNull
    List<QueryDto> getAll();

    @Null
    QueryDto getById(int id);

    void delete(int id) throws IllegalArgumentException;

    void execute(int id) throws IllegalArgumentException;

}
