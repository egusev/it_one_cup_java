package ru.vk.competition.minbenchmark.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vk.competition.minbenchmark.MinbenchmarkApplication;
import ru.vk.competition.minbenchmark.dto.TableQueryDto;

@SpringBootTest(classes = {MinbenchmarkApplication.class})
class TableQueriesServiceImplTest {

    @Autowired
    private TableQueriesServiceImpl tableQueriesService;

    @Test
    public void testCRUD() {
        assertThat(tableQueriesService.getAll())
            .isNotNull()
            .isEmpty();

        assertThat(tableQueriesService.getById(1))
            .isNull();

        assertThat(tableQueriesService.getByTableName("table"))
            .isNotNull()
            .isEmpty();

        assertThrows(IllegalArgumentException.class, () -> tableQueriesService.delete(1));

        tableQueriesService.create(new TableQueryDto(1, "test", "table"));
        tableQueriesService.create(new TableQueryDto(2, "test", "table2"));
        assertThrows(IllegalArgumentException.class, () -> tableQueriesService.create(new TableQueryDto(2, "test", "table3")));
        tableQueriesService.create(new TableQueryDto(3, "test", "table2"));

        assertThat(tableQueriesService.getById(1)).extracting("queryId").isEqualTo(1);
        assertThat(tableQueriesService.getById(2)).extracting("queryId").isEqualTo(2);
        assertThat(tableQueriesService.getById(3)).extracting("queryId").isEqualTo(3);
        assertThat(tableQueriesService.getById(4)).isNull();

        assertThat(tableQueriesService.getByTableName("table")).extracting("queryId")
                                                               .containsExactlyInAnyOrder(1);
        assertThat(tableQueriesService.getByTableName("table2")).extracting("queryId")
                                                               .containsExactlyInAnyOrder(2, 3);
        assertThat(tableQueriesService.getByTableName("table3")).isNotNull()
                                                                .isEmpty();

        assertThat(tableQueriesService.getAll()).extracting("queryId")
                                                .containsExactlyInAnyOrder(1, 2, 3);

        assertThrows(IllegalArgumentException.class, () -> tableQueriesService.delete(4));

        tableQueriesService.delete(2);

        assertThat(tableQueriesService.getById(1)).extracting("queryId").isEqualTo(1);
        assertThat(tableQueriesService.getById(2)).isNull();
        assertThat(tableQueriesService.getById(3)).extracting("queryId").isEqualTo(3);
        assertThat(tableQueriesService.getById(4)).isNull();

        assertThat(tableQueriesService.getByTableName("table")).extracting("queryId")
                                                               .containsExactlyInAnyOrder(1);
        assertThat(tableQueriesService.getByTableName("table2")).extracting("queryId")
                                                               .containsExactlyInAnyOrder(3);
    }
}