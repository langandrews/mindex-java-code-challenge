package com.mindex.challenge.data;

public class ReportingStructure {
    private Employee employee;
    private Integer numberOfReports;

    public ReportingStructure() {
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public ReportingStructure setEmployee(Employee employee) {
        this.employee = employee;

        return this;
    }

    public Integer getNumberOfReports() {
        return this.numberOfReports;
    }

    public ReportingStructure setNumberOfReports(Integer numberOfReports) {
        this.numberOfReports = numberOfReports;

        return this;
    }
}
