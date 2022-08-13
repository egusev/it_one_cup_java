package ru.vk.competition.minbenchmark.repository;

import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class QueriesDao {

    @Autowired
    private DataSource dataSource;

    public void execute(String query) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        try {
            jdbcTemplate.execute(query);
            log.info("Executed query {}", query);
        } catch (Exception e) {
            log.info(e.getMessage());
            log.debug(e.getMessage(), e);

            if (e instanceof SQLException) {
                throw e;
            } else {
                throw new SQLException(e);
            }
        }
    }

}
