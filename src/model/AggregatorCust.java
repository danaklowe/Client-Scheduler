
package model;

/**
 *
 * @author Dana K Lowe
 */

// aggregating class to hold customer name, combined address string, phone number, etc.

public class AggregatorCust {
    private String name;
    private boolean active;
    private String address;
    private String address2;
    private String city;
    private String country;
    private String postalCode;
    private String phone;
    private String combinedAddress;
    private int customerId;
    private int addressId;
    private int cityId;
    private int countryId;


    public AggregatorCust(String name, boolean active, String address, String address2, String city, String country, String postalCode, String phone, int customerId, int addressId, int cityId, int countryId) {
        this.name = name;
        this.active = active;
        this.address = address;
        if (address2 == null) {
            this.address2 ="";
        } else {
            this.address2 = address2;
        }
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;
        
        this.combinedAddress = this.address + " " + this.address2 + ", " + this.city + ", " + this.country + " " + this.postalCode;
        
        this.customerId = customerId;
        this.addressId = addressId;
        this.cityId = cityId;
        this.countryId = countryId;
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCombinedAddress() {
        return combinedAddress;
    }

    public void setCombinedAddress(String combinedAddress) {
        this.combinedAddress = combinedAddress;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    
    
    
}
