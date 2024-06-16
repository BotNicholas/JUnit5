package objects;

import java.sql.Date;

public class Author {

    private Integer id;
    private Date birthDate;
    private String contactDetails;
    private String firstname;
    private Character gender;
    private String initials;
    private String lastname;
    private String otherDetails;

    public Author() {
    }

    public Author(Integer id, Date birthDate, String contactDetails, String firstname, char gender, String initials, String lastname, String otherDetails) {
        this.id = id;
        this.birthDate = birthDate;
        this.contactDetails = contactDetails;
        this.firstname = firstname;
        this.gender = gender;
        this.initials = initials;
        this.lastname = lastname;
        this.otherDetails = otherDetails;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", birthDate=" + birthDate +
                ", contactDetails='" + contactDetails + '\'' +
                ", firstname='" + firstname + '\'' +
                ", gender=" + gender +
                ", initials='" + initials + '\'' +
                ", lastname='" + lastname + '\'' +
                ", otherDetails='" + otherDetails + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Author author = (Author) obj;
        return (this.id.equals(author.getId()));
    }
}
