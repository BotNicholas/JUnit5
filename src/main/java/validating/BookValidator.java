package validating;

import com.mysql.cj.util.StringUtils;
import objects.Book;

import java.sql.Date;

public class BookValidator implements Validator<Book> {
    private final AuthorValidator authorValidator;
    private final BookCategoryValidator bookCategoryValidator;

    public BookValidator() {
        this.authorValidator = new AuthorValidator();
        this.bookCategoryValidator = new BookCategoryValidator();
    }

    @Override
    public Boolean isValid(Book book) {
        return book != null
                && isIdValid(book.getId())
                && authorValidator.isValid(book.getAuthor())
                && bookCategoryValidator.isValid(book.getCategory())
                && isDateAcquiredValid(book.getDateAcquired())
                && isIsbnValid(book.getIsbn())
                && isPublicationDateValid(book.getPublicationDate())
                && isRecommendedPriceValid(book.getRecommendedPrice())
                && isTitleValid(book.getTitle());
    }

    private boolean isIdValid(Integer id) {
        return id > 0;
    }

    private boolean isDateAcquiredValid(Date dateAcquired) {
        java.util.Date now = new java.util.Date();
        return dateAcquired.compareTo(now) <= 0;
    }

    private boolean isIsbnValid(String isbn) {
        return !StringUtils.isEmptyOrWhitespaceOnly(isbn) && isbn.length() == 17 && isbn.matches("\\d{3}-\\d-\\d{2}-\\d{6}-\\d");
    }

    private boolean isPublicationDateValid(Date publicationDate) {
        java.util.Date now = new java.util.Date();
        return publicationDate.compareTo(now) <= 0;
    }

    //price should be <= 999.99, because if more than it is too expensive
    private boolean isRecommendedPriceValid(Double recommendedPrice) {
        return recommendedPrice > 0 && recommendedPrice <= Constants.MAX_BOOK_PRICE;
    }

    private boolean isTitleValid(String title) {
        return !StringUtils.isEmptyOrWhitespaceOnly(title);
    }
}
