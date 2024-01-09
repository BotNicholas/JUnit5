package objects;

public class Contact {
    private Integer id;
    private String firstname;
    private String lastname;
    private RefContactType contactType;
    private String cellPhone;
    private String workPhone;
    private String otherDetails;

    public Contact() {
    }

    public Contact(Integer id, String firstname, String lastname, RefContactType contactType, String cellPhone, String workPhone, String otherDetails) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contactType = contactType;
        this.cellPhone = cellPhone;
        this.workPhone = workPhone;
        this.otherDetails = otherDetails;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public RefContactType getContactType() {
        return contactType;
    }

    public void setContactType(RefContactType contactType) {
        this.contactType = contactType;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }
}
