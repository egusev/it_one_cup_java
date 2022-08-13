package ru.vk.competition.minbenchmark.repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vk.competition.minbenchmark.dto.ColumnMetaDto;
import ru.vk.competition.minbenchmark.dto.TableMetaDto;
import ru.vk.competition.minbenchmark.utils.TypeSerializer;

@Slf4j
@Repository
public class TablesDao {

    @Autowired
    private DataSource dataSource;

    public void createTable(@NotNull TableMetaDto tableMetaDto) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        StringBuilder query = new StringBuilder("CREATE TABLE ");
        query.append(tableMetaDto.getTableName());
        query.append(" (");
        for (ColumnMetaDto columnMetaDto : tableMetaDto.getColumnInfos()) {
            query.append(columnMetaDto.getTitle());
            query.append(' ');
            query.append(columnMetaDto.getType());
            query.append(',');
        }
        query.append("PRIMARY KEY (");
        query.append(tableMetaDto.getPrimaryKey());
        query.append("))");

        try {
            jdbcTemplate.execute(query.toString());
            log.info("Created table {} with {} columns and PK {}",
                     tableMetaDto.getTableName(),
                     tableMetaDto.getColumnsAmount(),
                     tableMetaDto.getPrimaryKey());
        } catch (BadSqlGrammarException bge ) {
            log.error("Could not create table {} with params {} ",
                      tableMetaDto.getTableName(),
                      tableMetaDto);
            log.debug("Could not create table {} with params {} ",
                      tableMetaDto.getTableName(),
                      tableMetaDto,
                      bge);
            throw new SQLException(bge);
        }
    }

    public boolean isTableExist(@NotNull String tableName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"})) {
                while (rs.next()) {
                    String dbTableName = rs.getString("TABLE_NAME");
                    return tableName.equalsIgnoreCase(dbTableName);
                };
                return false;
            }
        }
    }

    @Null
    public TableMetaDto findTableMeta(@NotNull String tableName) throws SQLException {

        String sqlTableName = tableName.toUpperCase();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null, null, sqlTableName, new String[]{"TABLE"})) {
                if (!resultSet.next()) {
                    return null;
                }
            }

            TableMetaDto tableMetaDto = new TableMetaDto();
            tableMetaDto.setTableName(tableName);

            try (ResultSet rs = metaData.getPrimaryKeys(null, null, sqlTableName)) {
                if (rs.next()) {
                    String pk = rs.getString("COLUMN_NAME");
                    tableMetaDto.setPrimaryKey(pk.toLowerCase());
                    tableMetaDto.setColumnInfos(new ArrayList<>());
                }
            }

            try (ResultSet rs = metaData.getColumns(null, null, sqlTableName, null)) {
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    int columnSize = rs.getInt("COLUMN_SIZE");
                    int datatype = rs.getInt("DATA_TYPE");
                    tableMetaDto.getColumnInfos().add(new ColumnMetaDto(columnName, TypeSerializer.serialize(datatype, columnSize)));
                }
            }
            tableMetaDto.setColumnsAmount(tableMetaDto.getColumnInfos().size());
            return tableMetaDto;
        }
    }

    public void deleteTable(@NotNull String tableName) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.execute("DROP TABLE " + tableName);
        }
        catch(BadSqlGrammarException bge) {
            log.error("Can't delete table {}", tableName);
            log.debug("Can't delete table {}", tableName, bge);
            throw new SQLException(bge);
        }
    }
}