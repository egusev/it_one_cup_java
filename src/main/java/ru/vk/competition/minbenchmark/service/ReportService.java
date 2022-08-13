package ru.vk.competition.minbenchmark.service;

import org.springframework.lang.Nullable;
import ru.vk.competition.minbenchmark.dto.ReportDto;

public interface ReportService {

    void createReport(ReportDto reportDto) throws IllegalArgumentException;

    @Nullable
    ReportDto getReport(int id);
}
