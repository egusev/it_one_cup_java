package ru.vk.competition.minbenchmark.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vk.competition.minbenchmark.dto.ColumnMetaDto;
import ru.vk.competition.minbenchmark.dto.TableReportDto;
import ru.vk.competition.minbenchmark.repository.QueriesDao;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplMockTest {

    @Mock
    private QueriesDao queriesDao;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    public void testCalculateCounts_OneColumn() throws SQLException {
        // Given
        TableReportDto reportRequest =  new TableReportDto("Test", Arrays.asList(new ColumnMetaDto("id", "int4", null)));

        when(queriesDao.queryForMap(any())).thenReturn(Collections.emptyMap());

        // When
        reportService.calculateCounts(reportRequest);

        // Then
        verify(queriesDao).queryForMap("SELECT COUNT(id) AS id FROM Test");
        verifyNoMoreInteractions(queriesDao);
    }

    @Test
    public void testCalculateCounts_ThreeColumn() throws SQLException {
        // Given
        TableReportDto reportRequest =  new TableReportDto("Test2", Arrays.asList(new ColumnMetaDto("id", "int4", null),
                                                                                  new ColumnMetaDto("test", "varchar(20)", null),
                                                                                  new ColumnMetaDto("NAME", "smallint", null)));

        when(queriesDao.queryForMap(any())).thenReturn(Collections.emptyMap());

        // When
        reportService.calculateCounts(reportRequest);

        // Then
        verify(queriesDao).queryForMap("SELECT COUNT(id) AS id, COUNT(test) AS test, COUNT(NAME) AS NAME FROM Test2");
        verifyNoMoreInteractions(queriesDao);

    }

}