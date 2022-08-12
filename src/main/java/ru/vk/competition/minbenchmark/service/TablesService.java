package ru.vk.competition.minbenchmark.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import ru.vk.competition.minbenchmark.dto.TableMetaDto;

public interface TablesService {

    void createTable(@Validated TableMetaDto tableMeta) throws IllegalArgumentException;

    /**
     * Returns meta data for a specific table. Returns null if a table is absent.
     * @param tableName table name
     */
    @Nullable
    TableMetaDto findTableMeta(@NonNull String tableName);

    /**
     * Deletes a specific table.
     * @exception IllegalArgumentException in the case the specified table doesn't exist
     * @param tableName table name
     */
    void deleteTable(@NonNull String tableName) throws IllegalArgumentException;

}
