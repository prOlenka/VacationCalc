package vacationcalc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vacationcalc.dto.VacationPayResponse;
import vacationcalc.service.CalculateService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CalculateControllerImpl.class)
class CalculateControllerTest {

	private MockMvc mockMvc;

	@Mock
	private CalculateService calculateService;

	@InjectMocks
	private CalculateControllerImpl calculateController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(calculateController).build();
	}

	// проверяет успешный ответ контроллера с корректными параметрами.
	@Test
	void calculateVacationPay_ShouldReturnValidResponse() throws Exception {
		double salary = 60000;
		int vacationDays = 10;
		VacationPayResponse response = new VacationPayResponse(20477.14); // пример ожидаемого результата

		Mockito.when(calculateService.calculateVacationPay(salary, vacationDays, null)).thenReturn(response);

		mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
						.param("salary", String.valueOf(salary))
						.param("vacationDays", String.valueOf(vacationDays))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(20477.14));
	}

	//проверяет, что запрос с некорректной зарплатой возвращает статус 400 (Bad Request) и сообщение об ошибке.
	@Test
	void calculateVacationPay_WithInvalidSalary_ShouldReturnBadRequest() throws Exception {
		double invalidSalary = -1000;
		int vacationDays = 10;

		mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
						.param("salary", String.valueOf(invalidSalary))
						.param("vacationDays", String.valueOf(vacationDays))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Salary must be greater than zero."));
	}

	//проверяет, что некорректные дни отпуска также возвращают ошибку.
	@Test
	void calculateVacationPay_WithInvalidVacationDays_ShouldReturnBadRequest() throws Exception {
		double salary = 60000;
		int invalidVacationDays = -5;

		mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
						.param("salary", String.valueOf(salary))
						.param("vacationDays", String.valueOf(invalidVacationDays))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string("Vacation days must be greater than zero."));
	}

	//проверяет расчет с датой начала отпуска.
	@Test
	void calculateVacationPay_WithStartDate_ShouldReturnValidResponse() throws Exception {
		double salary = 60000;
		int vacationDays = 10;
		LocalDate startDate = LocalDate.of(2024, 9, 1);
		VacationPayResponse response = new VacationPayResponse(20000.00); // пример результата

		Mockito.when(calculateService.calculateVacationPay(salary, vacationDays, startDate)).thenReturn(response);

		mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
						.param("salary", String.valueOf(salary))
						.param("vacationDays", String.valueOf(vacationDays))
						.param("startDate", startDate.toString())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(20000.00));
	}
}

