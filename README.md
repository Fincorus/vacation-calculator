# Калькулятор отпускных

Микросервис для расчета отпускных на Spring Boot

## Функционал
- Расчет по средней зарплате и дням отпуска
- Учет праздников и выходных при указании даты

## Как запустить
```bash
mvn spring-boot:run
```

## API
`GET /calculate?averageSalary=50000&vacationDays=14[&startDate=2025-05-01]`
