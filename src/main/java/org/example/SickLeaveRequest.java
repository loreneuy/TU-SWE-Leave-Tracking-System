package org.example;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SickLeaveRequest extends LeaveRequest {

    private boolean medicalCertificateProvided;

    public SickLeaveRequest(int requestId, Employee employee, String startDate, String endDate) {
        super(requestId, employee, startDate, endDate, "Sick Leave");
    }

    public boolean isMedicalCertificateProvided() {
        return medicalCertificateProvided;
    }

    public void setMedicalCertificateProvided(boolean medicalCertificateProvided) {
        this.medicalCertificateProvided = medicalCertificateProvided;
    }

    @Override
    public int calculateLeaveDays() {
        LocalDate startDate = LocalDate.parse(this.startDate);
        LocalDate endDate = LocalDate.parse(this.endDate).plusDays(1);

        Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

        return (int) startDate.datesUntil(endDate)
                .filter(d -> !weekend.contains(d.getDayOfWeek()))
                .count();
    }

    @Override
    public boolean processRequest() {
        if (calculateLeaveDays() > employee.getLeaveBalance()) {
            System.out.println("ERROR: Employee does not have enough leave credits.");
            return false;
        }

        if (calculateLeaveDays() > 2 && !medicalCertificateProvided) {
            System.out.println("VALIDATION FAILED: Sick leave longer than 2 days requires a medical certificate.");
            return false;
        }

        System.out.println("Processing sick leave request...\n");
        return true;
    }

    @Override
    public boolean approve(String approverName) {
        super.approve(approverName);

        employee.requestLeave(calculateLeaveDays());

        return true;
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format("Medical Certificate: %s\n",
                        this.medicalCertificateProvided);
    }
}
