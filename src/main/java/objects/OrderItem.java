package objects;

public class OrderItem {
    private Integer itemNumber;
    private Order order;
    private Book book;
    private Double agreedPrice;
    private String comment;

    public OrderItem() {
    }

    public OrderItem(Integer itemNumber, Order order, Book book, Double agreedPrice, String comment) {
        this.itemNumber = itemNumber;
        this.order = order;
        this.book = book;
        this.agreedPrice = agreedPrice;
        this.comment = comment;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Double getAgreedPrice() {
        return agreedPrice;
    }

    public void setAgreedPrice(Double agreedPrice) {
        this.agreedPrice = agreedPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
