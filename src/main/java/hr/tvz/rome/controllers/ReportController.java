package hr.tvz.rome.controllers;

import hr.tvz.rome.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/rest")
public class ReportController {

    @Autowired
    ReportService reportService;

    @RequestMapping(value = "/dashboard/byHour", method = RequestMethod.GET)
    public Object getByHourMontlyReport() {
        return reportService.getHoursReportByEvidenceType(null, LocalDate.now());
    }
    
    @RequestMapping(value = "/dashboard/presence", method = RequestMethod.GET)
    public Object  getPresenceReport() {
        return reportService.getPresenceReport();
    }
    
   
}
