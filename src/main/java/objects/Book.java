package objects;

import java.sql.Date;

public class Book {
    private Integer id;
    private Author author;
    private BookCategory category;
    private String comments;
    private Date dateAcquired;
    private String isbn;
    private Date publicationDate;
    private Double recommendedPrice;
    private String title;

    public Book() {
    }

    public Book(Integer id, Author author, BookCategory category, String comments, Date dateAcquired, String isbn, Date publicationDate, Double recommendedPrice, String title) {
        this.id = id;
        this.author = author;
        this.category = category;
        this.comments = comments;
        this.dateAcquired = dateAcquired;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.recommendedPrice = recommendedPrice;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(Date dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Double getRecommendedPrice() {
        return recommendedPrice;
    }

    public void setRecommendedPrice(Double recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRank() {
        return title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author=" + author +
                ", category=" + category +
                ", comments='" + comments + '\'' +
                ", dateAcquired=" + dateAcquired +
                ", isbn='" + isbn + '\'' +
                ", publicationDate=" + publicationDate +
                ", recommendedPrice=" + recommendedPrice +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Book book = (Book) obj;
        return this.id.equals(book.id);
    }
}