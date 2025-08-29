package org.example;

public class MaternityLeaveRequest extends LeaveRequest {

    private String expectedDeliveryDate;

    public MaternityLeaveRequest(int requestId, Employee employee, String startDate, String endDate) {
        super(requestId, employee, startDate, endDate, "Maternity Leave");
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    @Override
    public int calculateLeaveDays() {
        return 90;
    }

    @Override
    public boolean processRequest() {
        System.out.println("Processing maternity leave request...\n");
        return true;
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format("Expected Delivery Date: %s\n",
                        this.expectedDeliveryDate);
    }
}
