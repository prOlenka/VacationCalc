package vacationcalc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vacationcalc.dto.VacationPayResponse;
import vacationcalc.exception.CalculatorException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CalculateServiceTest {

    @InjectMocks
    private CalculateService calculateService;

    @Mock
    private HolidayService holidayService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //проверяет корректность расчета отпускных при передаче валидных данных.
    @Test
    void calculateSimpleVacationPay_ValidData_ShouldReturnCorrectPay() {
        double salary = 60000;
        int vacationDays = 10;

        VacationPayResponse response = calculateService.calculateVacationPay(salary, vacationDays, null);
        double expectedVacationPay = (salary / 29.3) * vacationDays;

        assertEquals(expectedVacationPay, response.getVacationPay());
    }

    //проверяет расчет с учетом праздничных дней и выходных.
    @Test
    void calculateVacationPay_WithStartDate_ShouldCalculateWithHolidays() {
        double salary = 60000;
        int vacationDays = 10;
        LocalDate startDate = LocalDate.of(2024, 9, 1);

        // Mocking holiday and weekend days
        when(holidayService.isHolidayOrWeekend(startDate)).thenReturn(false);
        when(holidayService.isHolidayOrWeekend(startDate.plusDays(1))).thenReturn(true); // Holiday or weekend on 2nd day

        VacationPayResponse response = calculateService.calculateVacationPay(salary, vacationDays, startDate);
        // 9 working days (assuming 1 holiday/weekend)
        double expectedVacationPay = (salary / 29.3) * 9;

        assertEquals(expectedVacationPay, response.getVacationPay());
    }

    //проверяет, что передача некорректной зарплаты вызывает исключение
    @Test
    void calculateVacationPay_InvalidSalary_ShouldThrowException() {
        double invalidSalary = -1000;
        int vacationDays = 10;

        Exception exception = assertThrows(CalculatorException.class, () -> {
            calculateService.calculateVacationPay(invalidSalary, vacationDays, null);
        });

        assertEquals("Salary must be greater than zero.", exception.getMessage());
    }

    //проверяет, что некорректные дни отпуска также вызывают исключение.
    @Test
    void calculateVacationPay_InvalidVacationDays_ShouldThrowException() {
        double salary = 60000;
        int invalidVacationDays = -5;

        Exception exception = assertThrows(CalculatorException.class, () -> {
            calculateService.calculateVacationPay(salary, invalidVacationDays, null);
        });

        assertEquals("Vacation days must be greater than zero.", exception.getMessage());
    }
}
