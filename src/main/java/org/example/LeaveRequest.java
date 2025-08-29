package org.example;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class LeaveRequest implements Approvable{
    protected int requestId;
    protected Employee employee;
    protected String startDate;
    protected String endDate;
    protected String status;
    protected String leaveType;
    private ArrayList<StatusChange> statusHistory = new ArrayList<StatusChange>();

    public LeaveRequest(int requestId, Employee employee, String startDate, String endDate, String leaveType) {
        this.requestId = requestId;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Pending";
        this.leaveType = leaveType;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    @Override
    public boolean approve(String approverName) {
        this.status = "Approved";
        System.out.printf("Leave request with ID %d has been approved by %s\n", this.requestId, approverName);
        statusHistory.add(new StatusChange(this.status, LocalDate.now().toString(), approverName));
        return true;
    }

    @Override
    public boolean deny(String approverName, String reason) {
        this.status = "Denied";
        System.out.printf("Leave request with ID %d has been denied by %s.\nReason for denial: %s\n", this.requestId, approverName, reason);
        statusHistory.add(new StatusChange(this.status, LocalDate.now().toString(), approverName));
        return false;
    }

    public abstract int calculateLeaveDays();

    public boolean processRequest() {
        System.out.println("Processing generic leave request...");
        return true;
    }

    public class StatusChange{
        private String newStatus;
        private String changeDate;
        private String changedBy;

        public StatusChange(String newStatus, String changeDate, String changedBy) {
            this.newStatus = newStatus;
            this.changeDate = changeDate;
            this.changedBy = changedBy;
        }

        public String getNewStatus() {
            return newStatus;
        }

        public String getChangeDate() {
            return changeDate;
        }

        public String getChangedBy() {
            return changedBy;
        }
    }

    public void printStatusHistory() {
        System.out.printf("--- Status History for Request %d---\n", requestId);
        for (StatusChange status : statusHistory) {
            System.out.printf("-> Status was set to %s by %s on %s.\n",
                    status.getNewStatus(),
                    status.getChangedBy(),
                    status.getChangeDate());
        }
        System.out.println();
    }

    public String toString() {
        return String.format("Request ID: %d\n" +
                "Employee Name: %s\n" +
                "Start Date: %s\n" +
                "End Date: %s\n" +
                "Status: %s\n" +
                "Leave Type: %s\n",
                this.requestId, this.employee.getName(), this.startDate, this.endDate, this.status, this.leaveType);
    }
}
