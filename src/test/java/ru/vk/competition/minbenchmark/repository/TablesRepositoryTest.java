package ru.vk.competition.minbenchmark.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.vk.competition.minbenchmark.MinbenchmarkApplication;
import ru.vk.competition.minbenchmark.dto.ColumnMetaDTO;
import ru.vk.competition.minbenchmark.dto.TableMetaDto;

@SpringBootTest(classes = {MinbenchmarkApplication.class})
@ActiveProfiles("dev")
class TablesRepositoryTest {

    @Autowired
    private TablesDao tablesDao;

    @Test
    void testCreateTable_test() throws SQLException {
        tablesDao.createTable(new TableMetaDto("test", 1, "id", Arrays.asList(new ColumnMetaDTO("id", "int4"))));
    }

    @Test
    void testCreateTable_Customer() throws SQLException {
        tablesDao.createTable(new TableMetaDto("Customer", 12, "CustomerId", Arrays.asList(new ColumnMetaDTO("CustomerId", "int4"),
                                                                                           new ColumnMetaDTO("FirstName", "VARCHAR(40)"),
                                                                                           new ColumnMetaDTO("LastName", "VARCHAR(20)"),
                                                                                           new ColumnMetaDTO("Company", "VARCHAR(80)"),
                                                                                           new ColumnMetaDTO("Address", "VARCHAR(70)"),
                                                                                           new ColumnMetaDTO("City", "VARCHAR(40)"),
                                                                                           new ColumnMetaDTO("Country", "VARCHAR(40)"),
                                                                                           new ColumnMetaDTO("PostalCode", "VARCHAR(10)"),
                                                                                           new ColumnMetaDTO("Phone", "VARCHAR(24)"),
                                                                                           new ColumnMetaDTO("Fax", "VARCHAR(24)"),
                                                                                           new ColumnMetaDTO("Email", "VARCHAR(60)"),
                                                                                           new ColumnMetaDTO("SupportRepId", "int4")
        )));
    }

    @Test
    void testCreateTable_Artist() throws SQLException {
        tablesDao.createTable(new TableMetaDto("Artist", 3, "id", Arrays.asList(new ColumnMetaDTO("id", "int4"),
                                                                                new ColumnMetaDTO("name", "varchar"),
                                                                                new ColumnMetaDTO("age", "int4")
        )));
    }

    @Test
    void testCreateTable_CustomerLaLa() throws SQLException {
        tablesDao.createTable(new TableMetaDto("CustomerЛАЛА", 12, "CustomerId", Arrays.asList(new ColumnMetaDTO("CustomerId", "int4"),
                                                                                               new ColumnMetaDTO("FirstName", "VARCHAR(40)"),
                                                                                               new ColumnMetaDTO("LastName", "VARCHAR(20)"),
                                                                                               new ColumnMetaDTO("Company", "VARCHAR(80)"),
                                                                                               new ColumnMetaDTO("Address", "VARCHAR(70)"),
                                                                                               new ColumnMetaDTO("City", "VARCHAR(40)"),
                                                                                               new ColumnMetaDTO("Country", "VARCHAR(40)"),
                                                                                               new ColumnMetaDTO("PostalCode", "VARCHAR(10)"),
                                                                                               new ColumnMetaDTO("Phone", "VARCHAR(24)"),
                                                                                               new ColumnMetaDTO("Fax", "VARCHAR(24)"),
                                                                                               new ColumnMetaDTO("Email", "VARCHAR(60)"),
                                                                                               new ColumnMetaDTO("SupportRepId", "int4")
        )));
    }

    @Test
    void testCreateTable_Artists() throws SQLException {
        assertThrows(SQLException.class,
                     () -> tablesDao.createTable(new TableMetaDto("Artists", 3, "zoo", Arrays.asList(new ColumnMetaDTO("id", "int4"),
                                                                                                     new ColumnMetaDTO("name", "varchar"),
                                                                                                     new ColumnMetaDTO("age", "int4")
                     ))));
    }

    @Test
    void testIsTableExist() throws SQLException {
        // Given
        Set<String> tableNames = IntStream.range(0, 9)
                                          .mapToObj(i -> "table_" + RandomString.make())
                                          .collect(Collectors.toSet());

        Set<String> tableNames2 = IntStream.range(0, 9)
                                           .mapToObj(i -> "table_" + RandomString.make())
                                           .collect(Collectors.toSet());

        List<TableMetaDto> tablesMeta = tableNames.stream()
                                                  .map(name -> new TableMetaDto(name, 1, "id", Arrays.asList(new ColumnMetaDTO("id", "int4"))))
                                                  .collect(Collectors.toList());

        for (TableMetaDto tableMeta : tablesMeta) {
            tablesDao.createTable(tableMeta);
        }

        // When
        assertFalse(tablesDao.isTableExist("table_"));

        // When
        for (String tableName : tableNames) {
            assertTrue(tablesDao.isTableExist(tableName));
        }

        // When
        for (String tableName : tableNames2) {
            assertFalse(tablesDao.isTableExist(tableName));
        }
    }

    @Test
    void testFindTableMeta() throws SQLException {
        // Given
        String tableName = "table_" + RandomString.make();
        tablesDao.createTable(new TableMetaDto(tableName + "1",
                                               12,
                                               "CustomerId",
                                               Arrays.asList(new ColumnMetaDTO("CustomerId", "int"),
                                                             new ColumnMetaDTO("FirstName", "VARCHAR(40)"),
                                                             new ColumnMetaDTO("LastName", "VARCHAR(20)"),
                                                             new ColumnMetaDTO("Company", "VARCHAR(80)"),
                                                             new ColumnMetaDTO("Address", "VARCHAR(70)"),
                                                             new ColumnMetaDTO("City", "VARCHAR(40)"),
                                                             new ColumnMetaDTO("Country", "VARCHAR(40)"),
                                                             new ColumnMetaDTO("PostalCode", "VARCHAR(10)"),
                                                             new ColumnMetaDTO("Phone", "VARCHAR(24)"),
                                                             new ColumnMetaDTO("Fax", "VARCHAR(24)"),
                                                             new ColumnMetaDTO("Email", "VARCHAR(60)"),
                                                             new ColumnMetaDTO("SupportRepId", "int4"))));

        tablesDao.createTable(new TableMetaDto(tableName + "2",
                                               12,
                                               "CustomerId",
                                               Arrays.asList(new ColumnMetaDTO("CustomerId", "int4"),
                                                             new ColumnMetaDTO("FirstName", "VARCHAR(40)"),
                                                             new ColumnMetaDTO("LastName", "VARCHAR(20)"),
                                                             new ColumnMetaDTO("Company", "VARCHAR(80)"),
                                                             new ColumnMetaDTO("Address", "VARCHAR(70)"),
                                                             new ColumnMetaDTO("Active", "tinyint"),
                                                             new ColumnMetaDTO("age", "smallint"),
                                                             new ColumnMetaDTO("score", "bigint"),
                                                             new ColumnMetaDTO("AMOUNT", "numeric"),
                                                             new ColumnMetaDTO("birthday", "date"),
                                                             new ColumnMetaDTO("arrived", "timestamp"),
                                                             new ColumnMetaDTO("daystart", "time"))));

        // When
        TableMetaDto result_ = tablesDao.findTableMeta(tableName);
        TableMetaDto result1 = tablesDao.findTableMeta(tableName + "1");
        TableMetaDto result2 = tablesDao.findTableMeta(tableName + "2");
        TableMetaDto result3 = tablesDao.findTableMeta(tableName + "3");

        // Then
        assertThat(result_).isNull();

        assertThat(result1).extracting("tableName", "columnsAmount", "primaryKey")
                           .containsExactly(tableName + "1", 12, "customerid");
        assertThat(result1.getColumnInfos()).extracting("title", "type")
                                            .containsExactly(tuple("CUSTOMERID", "INTEGER"),
                                                             tuple("FIRSTNAME", "VARCHAR(40)"),
                                                             tuple("LASTNAME", "VARCHAR(20)"),
                                                             tuple("COMPANY", "VARCHAR(80)"),
                                                             tuple("ADDRESS", "VARCHAR(70)"),
                                                             tuple("CITY", "VARCHAR(40)"),
                                                             tuple("COUNTRY", "VARCHAR(40)"),
                                                             tuple("POSTALCODE", "VARCHAR(10)"),
                                                             tuple("PHONE", "VARCHAR(24)"),
                                                             tuple("FAX", "VARCHAR(24)"),
                                                             tuple("EMAIL", "VARCHAR(60)"),
                                                             tuple("SUPPORTREPID", "INTEGER"));

        assertThat(result2).extracting("tableName", "columnsAmount", "primaryKey")
                                   .containsExactly(tableName + "2", 12, "customerid");
        assertThat(result2.getColumnInfos()).extracting("title", "type")
                                            .containsExactly(tuple("CUSTOMERID", "INTEGER"),
                                                             tuple("FIRSTNAME", "VARCHAR(40)"),
                                                             tuple("LASTNAME", "VARCHAR(20)"),
                                                             tuple("COMPANY", "VARCHAR(80)"),
                                                             tuple("ADDRESS", "VARCHAR(70)"),
                                                             tuple("ACTIVE", "TINYINT"),
                                                             tuple("AGE", "SMALLINT"),
                                                             tuple("SCORE", "BIGINT"),
                                                             tuple("AMOUNT", "NUMERIC"),
                                                             tuple("BIRTHDAY", "DATE"),
                                                             tuple("ARRIVED", "TIMESTAMP"),
                                                             tuple("DAYSTART", "TIME"));

        assertThat(result3).isNull();
    }

    @Test
    public void testDeleteTable_success() throws SQLException {
        // Given
        Set<String> tableNames = IntStream.range(0, 9)
                                          .mapToObj(i -> "table_" + RandomString.make())
                                          .collect(Collectors.toSet());

        List<TableMetaDto> tablesMeta = tableNames.stream()
                                                  .map(name -> new TableMetaDto(name, 1, "id", Arrays.asList(new ColumnMetaDTO("id", "int4"))))
                                                  .collect(Collectors.toList());

        for (TableMetaDto tableMeta : tablesMeta) {
            tablesDao.createTable(tableMeta);
        }

        for (String tableName : tableNames) {
            assertTrue(tablesDao.isTableExist(tableName));
        }

        // When
        for (String tableName : tableNames) {
            tablesDao.deleteTable(tableName);
        }

        // Then
        for (String tableName : tableNames) {
            assertFalse(tablesDao.isTableExist(tableName));
        }
    }

    @Test
    public void testDeleteTable_fail() throws SQLException {
        // Given
        assertThrows(SQLException.class,
                     () -> tablesDao.deleteTable(RandomString.make()));
    }
}