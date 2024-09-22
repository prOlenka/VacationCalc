package vacationcalc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class VacationPayRequest {

    private double salary;
    private int vacationDays;
    private LocalDate exactVacationDayStart;
}
