package vacationcalc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vacationcalc.dto.VacationPayRequest;
import vacationcalc.dto.VacationPayResponse;
import vacationcalc.service.CalculateService;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
public class CalculateControllerImpl implements CalculateController {
    private final CalculateService calculateService;


    @Override
    public ResponseEntity<VacationPayResponse> calculateVacationPay(double salary, int vacationDays, LocalDate startDate) {
        var request = new VacationPayRequest(salary, vacationDays, startDate);
        VacationPayResponse response = calculateService.calculateVacationPay(request.getSalary(),
                request.getVacationDays(), request.getExactVacationDayStart());
        return ResponseEntity.ok(response);
    }
}
