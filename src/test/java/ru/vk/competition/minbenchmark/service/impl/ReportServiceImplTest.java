package ru.vk.competition.minbenchmark.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vk.competition.minbenchmark.MinbenchmarkApplication;
import ru.vk.competition.minbenchmark.dto.ColumnMetaDto;
import ru.vk.competition.minbenchmark.dto.ReportDto;
import ru.vk.competition.minbenchmark.dto.TableMetaDto;
import ru.vk.competition.minbenchmark.dto.TableReportDto;
import ru.vk.competition.minbenchmark.repository.QueriesDao;

@SpringBootTest(classes = {MinbenchmarkApplication.class})
class ReportServiceImplTest {

    @Autowired
    private QueriesDao queriesDao;

    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private TablesServiceImpl tablesService;

    @Test
    public void testCalculateCounts() throws SQLException {
        // Given
        TableMetaDto tableMetaDto = new TableMetaDto("Test", 4, "id", Arrays.asList(new ColumnMetaDto("id", "integer"),
                                                                                    new ColumnMetaDto("amount", "int4"),
                                                                                    new ColumnMetaDto("title", "varchar(100)"),
                                                                                    new ColumnMetaDto("status", "varchar(10)")));
        tablesService.createTable(tableMetaDto);

        queriesDao.execute("insert into test(id, amount, title, status) values (1, 10, 'test1', 'ok')");
        queriesDao.execute("insert into test(id, amount, title, status) values (2, null, 'test2', null)");
        queriesDao.execute("insert into test(id, amount, title, status) values (3, 30, null, null)");
        queriesDao.execute("insert into test(id, amount, title, status) values (4, null, 'test4', null)");

        TableReportDto request1 = new TableReportDto("Test", Arrays.asList(new ColumnMetaDto("id", "int4", null),
                                                                           new ColumnMetaDto("amount", "int4", null),
                                                                           new ColumnMetaDto("title", "varchar(100)", null),
                                                                           new ColumnMetaDto("status", "varchar(10)", null)));

        // When
        Map<String, String> result1 = reportService.calculateCounts(request1);

        // Then
        assertThat(result1).isNotNull()
                           .containsExactlyInAnyOrderEntriesOf(Map.of("ID", "4",
                                                                      "AMOUNT", "2",
                                                                      "TITLE", "3",
                                                                      "STATUS", "1"));

        queriesDao.execute("drop table test");
    }

    @Test
    public void testExecuteReport() throws SQLException {
        // Given
        TableMetaDto table1 = new TableMetaDto("Test", 4, "id", Arrays.asList(new ColumnMetaDto("id", "integer"),
                                                                              new ColumnMetaDto("amount", "int4"),
                                                                              new ColumnMetaDto("title", "varchar(100)"),
                                                                              new ColumnMetaDto("status", "varchar(10)")));

        TableMetaDto table2 = new TableMetaDto("BASA", 4, "id", Arrays.asList(new ColumnMetaDto("id", "integer")));

        tablesService.createTable(table1);
        tablesService.createTable(table2);

        queriesDao.execute("insert into test(id, amount, title, status) values (1, 10, 'test1', 'ok')");
        queriesDao.execute("insert into test(id, amount, title, status) values (2, null, 'test2', null)");
        queriesDao.execute("insert into test(id, amount, title, status) values (3, 30, null, null)");
        queriesDao.execute("insert into test(id, amount, title, status) values (4, null, 'test4', null)");

        TableReportDto request1 = new TableReportDto("Test", Arrays.asList(new ColumnMetaDto("id", "int4", null),
                                                                           new ColumnMetaDto("amount", "int4", null),
                                                                           new ColumnMetaDto("title", "varchar(100)", null),
                                                                           new ColumnMetaDto("status", "varchar(10)", null)));

        TableReportDto request2 = new TableReportDto("BASA", Arrays.asList(new ColumnMetaDto("id", "int4", null)));

        reportService.createReport(new ReportDto(1, 2, Arrays.asList(request1, request2)));

        assertThat(request1.getColumns()).extracting("title", "size")
                                         .containsExactlyInAnyOrder(tuple("id", "4"),
                                                                    tuple("amount", "2"),
                                                                    tuple("title", "3"),
                                                                    tuple("status", "1"));

        assertThat(request2.getColumns()).extracting("title", "size")
                                         .containsExactlyInAnyOrder(tuple("id", "0"));

        queriesDao.execute("drop table test");
        queriesDao.execute("drop table basa");
    }

    @Test
    public void testExecuteReport_fail() {
        assertThrows(
            IllegalArgumentException.class,
            () -> reportService.createReport(new ReportDto(1, 2, Arrays.asList(new TableReportDto(RandomString.make(),
                                                                                                  Arrays.asList(new ColumnMetaDto("id", "int4", null))))))
        );
    }
}