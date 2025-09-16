package models;

public class Supplier {
    private int supplierID;
    private String supplierName;
    private String contactName;
    private String phone;
    private String email;
    private  String address;
    // getters/setters
    public int getSupplierID() { return supplierID; }
    public void setSupplierID(int supplierID) { this.supplierID = supplierID; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) { this.address = address; }
}
