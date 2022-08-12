package ru.vk.competition.minbenchmark.service.impl;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.competition.minbenchmark.dto.TableMetaDto;
import ru.vk.competition.minbenchmark.repository.TablesDao;
import ru.vk.competition.minbenchmark.service.TablesService;

@Service
public class TablesServiceImpl implements TablesService {

    @Autowired
    private TablesDao tablesDao;

    @Override
    public void createTable(TableMetaDto tableMeta) throws IllegalArgumentException {
        try {
            tablesDao.createTable(tableMeta);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public TableMetaDto findTableMeta(String tableName) {
        try {
            return tablesDao.findTableMeta(tableName);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void deleteTable(String tableName) throws IllegalArgumentException {
        try {
            tablesDao.deleteTable(tableName);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
