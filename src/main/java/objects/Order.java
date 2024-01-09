package objects;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Order{
    private Integer id;
    private Customer customer;
    private Date orderDate;
    private Double orderValue;

    private List<OrderItem> items;


    public Order() {
    }

    public Order(Integer id, Customer customer, Date orderDate, Double orderValue) {
        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
        this.orderValue = orderValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Double orderValue) {
        this.orderValue = orderValue;
    }
}
