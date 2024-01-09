package objects;

import java.util.List;

public class Customer {
    private Integer id;
    private String address;
    private String email;
    private String idnp;
    private String name;
    private String phone;

    private List<Order> orders;

    public Customer() {
    }

    public Customer(Integer id, String address, String email, String idnp, String name, String phone) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.idnp = idnp;
        this.name = name;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdnp() {
        return idnp;
    }

    public void setIdnp(String idnp) {
        this.idnp = idnp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
