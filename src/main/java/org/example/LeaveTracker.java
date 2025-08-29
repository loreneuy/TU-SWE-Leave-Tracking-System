package org.example;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
final public class LeaveTracker
{
    private static Scanner kb = new Scanner(System.in);
    private static ArrayList<Employee> employees = new ArrayList<Employee>();
    private static ArrayList<LeaveRequest> leaveRequests = new ArrayList<LeaveRequest>();
    private static int nextLeaveToProcessIdx = 0;

    public static void main( String[] args )
    {
        employees.add(new Employee(1, "Alice", "Sales"));
        employees.add(new Employee(2, "Bob", "IT"));
        employees.add(new Employee(3, "Carlos", "Marketing"));
        employees.add(new Employee(3, "Dana", "Sales"));

        int menuChoice;

        do {
            menuChoice = mainMenu();

            if (menuChoice == 1) {
                int employeeIdx = leaveRequestMenu();
                leaveMenu(employeeIdx);

            } else if (menuChoice == 2) {
                processLeaveMenu();
            } else if (menuChoice == 3) {
                for (LeaveRequest lr : leaveRequests) {
                    System.out.print(lr.toString());
                    lr.printStatusHistory();
                }
            }

        } while (menuChoice != 4);

        System.out.println("Exiting system. Goodbye!");

    }

    public static int mainMenu() {
        int input = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.printf("\nWelcome to the HR Leave Management System!\n" +
                    "--- Main Menu---\n" +
                    "1. Create New Leave Request\n" +
                    "2. Process a Pending Request\n" +
                    "3. View All Request Histories\n" +
                    "4. Exit\n" +
                    "Choose an option (1-4): ");

            try {
                input = Integer.parseInt(kb.nextLine());
                if (input < 1 || input > 4) {
                    System.out.println("Invalid input.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        return input;
    }

    public static int leaveRequestMenu() {
        int input = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("\n--- Create New Leave Request---\nSelect an employee:");

            for (int i = 0; i < employees.size(); i++) {
                System.out.printf("%d. %s\n", i+1, employees.get(i).getName());
            }

            System.out.print("Enter employee number: ");

            try {
                input = Integer.parseInt(kb.nextLine());
                if (input < 1 || input > employees.size()) {
                    System.out.println("Invalid input.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        return input - 1;
    }

    public static void leaveMenu(int employeeIdx) {
        int input = 0;
        boolean validInput = false;
        Employee emp = employees.get(employeeIdx);

        System.out.print("\n" + emp.toString());

        while (!validInput) {
            System.out.printf("\nSelect leave type:\n" +
                    "1. Sick Leave\n" +
                    "2. Vacation Leave\n" +
                    "3. Maternity Leave\n" +
                    "Enter leave type number (1-3): ");

            try {
                input = Integer.parseInt(kb.nextLine());
                if (input < 1 || input > 3) {
                    System.out.println("Invalid input.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        boolean validDates = false;
        String startDate = "";
        String endDate = "";
        while (!validDates) {
            startDate = getDate("start");
            endDate = getDate("end");

            if (LocalDate.parse(endDate).isAfter(LocalDate.parse(startDate)) || LocalDate.parse(endDate).equals(LocalDate.parse(startDate)))
                validDates = true;
            else
                System.out.println("Invalid input: End date cannot be before start date.");
        }

        if (input == 1) {
            boolean hasMedCert = false;
            validInput = false;

            while (!validInput) {
                System.out.print("Is a medical certificate provided? (true/false): ");
                String temp = kb.nextLine();
                temp = temp.trim();

                if (!temp.equalsIgnoreCase("true") && !temp.equalsIgnoreCase("false")) {
                    System.out.println("Invalid input.");
                } else {
                    hasMedCert = Boolean.parseBoolean(temp);
                    validInput = true;
                }
            }

            SickLeaveRequest sl = new SickLeaveRequest(leaveRequests.size() + 1, emp, startDate, endDate);
            sl.setMedicalCertificateProvided(hasMedCert);

            leaveRequests.add(sl);
            System.out.println("Successfully created Sick Leave Request for " + emp.getName());

        } else if (input == 2) {
            boolean isPTO = false;
            validInput = false;

            while (!validInput) {
                System.out.print("Is this paid time off? (true/false): ");
                String temp = kb.nextLine();
                temp = temp.trim();

                if (!temp.equalsIgnoreCase("true") && !temp.equalsIgnoreCase("false")) {
                    System.out.println("Invalid input.");
                } else {
                    isPTO = Boolean.parseBoolean(temp);
                    validInput = true;
                }
            }

            VacationLeaveRequest vl = new VacationLeaveRequest(leaveRequests.size() + 1, emp, startDate, endDate);
            vl.setPaidTimeOff(isPTO);

            leaveRequests.add(vl);

            System.out.println("Successfully created Vacation Leave Request for " + emp.getName());

        } else if (input == 3) {
            String edd = getDate("expected delivery");

            MaternityLeaveRequest ml = new MaternityLeaveRequest(leaveRequests.size() + 1, emp, startDate, endDate);
            ml.setExpectedDeliveryDate(edd);

            leaveRequests.add(ml);

            System.out.println("Successfully created Maternity Leave Request for " + emp.getName());
        }
    }

    public static void processLeaveMenu() {
        if (nextLeaveToProcessIdx >= leaveRequests.size()) {
            System.out.println("\nNo new pending requests.");
            return;
        }

        LeaveRequest lr = leaveRequests.get(nextLeaveToProcessIdx);
        nextLeaveToProcessIdx++;

        System.out.print("\n" + lr.toString());

        if (lr.processRequest()) {
            int input = 0;
            boolean validInput = false;

            while (!validInput) {
                System.out.print("1. Approve\n2. Deny\nChoose an option (1-2): ");

                try {
                    input = Integer.parseInt(kb.nextLine());
                    if (input < 1 || input > 2) {
                        System.out.println("Invalid input.");
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }

            if (input == 1) {
                lr.approve("HR");
            } else {
                System.out.print("Enter denial reason: ");
                String reason = kb.nextLine();
                lr.deny("HR", reason);
            }
        } else {
            lr.deny("System", "System validation failed.");
        }
    }

    public static String getDate(String type) {
        String date = "";
        boolean validDate = false;

        Pattern pattern = Pattern.compile("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$");
        Matcher matcher;

        while (!validDate) {
            System.out.printf("Enter %s date (YYYY-MM-DD): ", type);

            date = kb.nextLine();
            matcher = pattern.matcher(date);

            validDate = matcher.find();

            if (!validDate)
                System.out.println("Invalid input.");
        }

        return date;
    }
}
