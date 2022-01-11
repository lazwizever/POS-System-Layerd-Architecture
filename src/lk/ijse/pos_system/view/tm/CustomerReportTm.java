package lk.ijse.pos_system.view.tm;

public class CustomerReportTm {
    private String customerId;
    private String customerName;
    private double income;

    public CustomerReportTm() {
    }

    public CustomerReportTm(String customerId, String customerName, double income) {
        this.setCustomerId(customerId);
        this.setCustomerName(customerName);
        this.setIncome(income);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
