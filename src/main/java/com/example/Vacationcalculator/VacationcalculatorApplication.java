package com.example.Vacationcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@SpringBootApplication
@RestController
public class VacationcalculatorApplication {

    // Официальные праздники России в 2025 году
    private static final Set<LocalDate> HOLIDAYS_2025 = Set.of(
            // Январь
            LocalDate.of(2025, 1, 1),  // Новый год
            LocalDate.of(2025, 1, 2),
            LocalDate.of(2025, 1, 3),
            LocalDate.of(2025, 1, 6),
            LocalDate.of(2025, 1, 7),  // Рождество
            LocalDate.of(2025, 1, 8),

            // Февраль
            LocalDate.of(2025, 2, 24), // День защитника Отечества (перенесён с 23.02)

            // Март
            LocalDate.of(2025, 3, 10), // 8 Марта (перенесён с субботы 8.03 на понедельник 10.03)

            // Май
            LocalDate.of(2025, 5, 1),  // Праздник Весны и Труда
            LocalDate.of(2025, 5, 9),  // День Победы

            // Июнь
            LocalDate.of(2025, 6, 12), // День России

            // Ноябрь
            LocalDate.of(2025, 11, 4)  // День народного единства
    );

    public static void main(String[] args) {
        SpringApplication.run(VacationcalculatorApplication.class, args);
    }

    @GetMapping("/calculate")
    public VacationResponse calculate(
            @RequestParam double averageSalary,
            @RequestParam int vacationDays,
            @RequestParam(required = false) String startDate) {

        if (startDate == null) {
            // Простой расчёт без учёта праздников
            double amount = calculateSimple(averageSalary, vacationDays);
            return new VacationResponse(amount, "Расчёт без учёта праздников");
        } else {
            // Расчёт с учётом праздников и выходных
            LocalDate start = LocalDate.parse(startDate);
            int workingDays = countWorkingDays(start, vacationDays);
            double amount = calculateSimple(averageSalary, workingDays);
            return new VacationResponse(amount,
                    String.format("Учтено %d праздников/выходных",
                            vacationDays - workingDays));
        }
    }

    // Основная формула расчёта
    private double calculateSimple(double averageSalary, int days) {
        return (averageSalary / 29.3) * days; // 29.3 - среднее количество дней в месяце
    }

    // Подсчёт рабочих дней (исключая праздники и выходные)
    private int countWorkingDays(LocalDate start, int days) {
        int workingDays = 0;
        LocalDate current = start;

        for (int i = 0; i < days; i++) {
            if (isWorkingDay(current)) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        return workingDays;
    }

    // Проверка, является ли день рабочим
    private boolean isWorkingDay(LocalDate date) {
        return !isWeekend(date) && !isHoliday(date);
    }

    // Проверка на выходные (суббота/воскресенье)
    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    // Проверка на праздник
    private boolean isHoliday(LocalDate date) {
        return HOLIDAYS_2025.contains(date);
    }

    // DTO для ответа
    record VacationResponse(double vacationPay, String message) {}
}