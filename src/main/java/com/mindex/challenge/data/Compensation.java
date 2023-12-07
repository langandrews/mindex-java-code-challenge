package com.mindex.challenge.data;

import java.time.LocalDate;

public class Compensation {
    private Employee employee;
    private Double salary;
    private LocalDate effectiveDate;

    public Employee getEmployee() {
        return employee;
    }

    public Compensation setEmployee(Employee employee) {
        this.employee = employee;

        return this;
    }

    public Double getSalary() {
        return salary;
    }

    public Compensation setSalary(Double salary) {
        this.salary = salary;

        return this;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public Compensation setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;

        return this;
    }
}
