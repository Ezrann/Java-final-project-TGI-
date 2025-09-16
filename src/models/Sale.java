package models;
import java.util.Date;
public class Sale {
    private int saleID;
    private Date saleDate;
    private int employeeID;
    private Integer customerID;
    private double totalAmount;
    private String paymentMethod;
    // getters/setters
    public int getSaleID() { return saleID; }
    public void setSaleID(int saleID) { this.saleID = saleID; }
    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }
    public Integer getCustomerID() { return customerID; }
    public void setCustomerID(Integer customerID) { this.customerID = customerID; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
