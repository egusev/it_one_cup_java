package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.competition.minbenchmark.dto.ReportDto;
import ru.vk.competition.minbenchmark.service.ReportService;

@Slf4j
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "create-report", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody ReportDto reportDto) {
        log.info("POST /api/report/create-report {}", reportDto.getReportId());

        if (!validate(reportDto)) {
            log.info("invalid report request");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            reportService.createReport(reportDto);
            log.info("report created");
        } catch (IllegalArgumentException iae) {
            log.info(ExceptionUtils.getRootCause(iae).getMessage());
            log.debug(iae.getMessage(), iae);
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "get-report-by-id/{id}", method = RequestMethod.GET)
    public ResponseEntity<ReportDto> get(@PathVariable("id") int id) {
        log.info("POST /api/report/get-report-by-id/{}", id);

        ReportDto reportDto = reportService.getReport(id);
        log.info("found report {}", reportDto != null);

        return new ResponseEntity(reportDto, reportDto == null ? HttpStatus.NOT_ACCEPTABLE : HttpStatus.CREATED);
    }

    boolean validate(ReportDto reportDto) {
        if (reportDto.getTables() == null) {
            return false;
        } else if (reportDto.getTables().size() != reportDto.getTableAmount()) {
            return false;
        }

        return true;
    }

}
