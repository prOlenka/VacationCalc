package vacationcalc.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vacationcalc.dto.VacationPayResponse;

import javax.validation.Valid;
import java.time.LocalDate;

@RequestMapping
public interface CalculateController {

    @GetMapping("/calculate")
    ResponseEntity<VacationPayResponse> calculateVacationPay(@Valid @RequestParam("salary") double salary,
                                                             @Valid @RequestParam("vacationDays") int vacationDays,
                                                             @Valid @RequestParam(value = "startDate", required = false)
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate);
}
