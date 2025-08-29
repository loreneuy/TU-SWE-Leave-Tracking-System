package org.example;

/**
 * Hello world!
 *
 */
final public class LeaveTracker
{
    public static void main( String[] args )
    {
        Employee Alice = new Employee(1, "Alice", "Sales");
        Employee Bob = new Employee(2, "Bob", "IT");

        LeaveRequest lr1 = new LeaveRequest(1, Alice, "2025-09-01", "2025-09-03");
        LeaveRequest lr2 = new LeaveRequest(2, Bob, "2025-09-02", "2025-09-04");

        System.out.printf("Name: %s\nLeave Balance: %d\n",
                Alice.getName(),
                Alice.getLeaveBalance());

        System.out.println("Alice is requesting a 3 day leave.");
        Alice.requestLeave(3);

        System.out.printf("Updated Leave Balance: %d\n",
                Alice.getLeaveBalance());

        System.out.println("Alice is requesting a 20 day leave.");
        Alice.requestLeave(20);

        System.out.printf("\nLeave Request Details:\nEmployee Name: %s\nStart Date: %s\nEnd Date: %s\nStatus: %s\n",
                lr1.getEmployee().getName(),
                lr1.getStartDate(),
                lr1.getEndDate(),
                lr1.getStatus());
    }
}
