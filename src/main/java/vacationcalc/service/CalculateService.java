package vacationcalc.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vacationcalc.dto.VacationPayResponse;
import vacationcalc.exception.CalculatorException;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CalculateService {

    private static final double AVERAGE_MONTH_DAYS = 29.3;

    private final HolidayService holidayService;

    public VacationPayResponse calculateVacationPay(double averageSalary, int vacationDays, LocalDate startDate) {
        if (averageSalary <= 0) {
            throw new CalculatorException("Зарплата должна быть больше 0 ");
        }
        if (vacationDays <= 0) {
            throw new CalculatorException("Дней отпуска должно быть больше 0");
        }
        double vacationPay;
        if (startDate != null) {
            vacationPay = calculateVacationPayWithDates(averageSalary, vacationDays, startDate);
        } else {
            vacationPay = calculateSimpleVacationPay(averageSalary, vacationDays);
        }
        return new VacationPayResponse(vacationPay);
    }

    private double calculateSimpleVacationPay(double averageSalary, int vacationDays) {
        if (averageSalary <= 0) {
            throw new CalculatorException("Средняя заработная плата должна быть больше нуля");
        }
        if (vacationDays <= 0) {
            throw new CalculatorException("Количество дней отпуска должно быть больше нуля");
        }
        return (averageSalary / AVERAGE_MONTH_DAYS) * vacationDays;
    }

    private double calculateVacationPayWithDates(double averageSalary, int vacationDays, LocalDate startDate) {
        if (averageSalary <= 0) {
            throw new CalculatorException("Средняя заработная плата должна быть больше нуля");
        }
        if (vacationDays <= 0) {
            throw new CalculatorException("Количество дней отпуска должно быть больше нуля");
        }
        if (startDate == null) {
            throw new CalculatorException("Дата начала отпуска не должна быть нулевой при расчете отпуска с точными датами");
        }
        int actualVacationDays = 0;
        int totalDays = 0;
        LocalDate currentDate = startDate;
        while (totalDays < vacationDays) {
            if (!holidayService.isHolidayOrWeekend(currentDate)) {
                actualVacationDays++;
            }
            currentDate = currentDate.plusDays(1);
            totalDays++;
        }
        return (averageSalary / AVERAGE_MONTH_DAYS) * actualVacationDays;
    }


}
