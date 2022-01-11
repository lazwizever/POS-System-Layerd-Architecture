package lk.ijse.pos_system.entity;

public class Customer {
    private String CustId;
    private String CustTitle;
    private String CustName;
    private String CustAddress;
    private String City;
    private String Province;
    private String PostalCode;

    public Customer() {
    }

    public Customer(String custId, String custTitle, String custName, String custAddress, String city, String province, String postalCode) {
        setCustId(custId);
        setCustTitle(custTitle);
        setCustName(custName);
        setCustAddress(custAddress);
        setCity(city);
        setProvince(province);
        setPostalCode(postalCode);
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getCustTitle() {
        return CustTitle;
    }

    public void setCustTitle(String custTitle) {
        CustTitle = custTitle;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }
}
