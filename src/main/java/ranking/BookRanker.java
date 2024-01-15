package ranking;

import objects.Book;

import java.util.Date;

public class BookRanker implements Ranker<Book> {
    @Override
    public int calculateRank(Book book) {
        return (int) (Math.round(book.getRecommendedPrice()) + getBookAge(book));
    }

    public long getBookAge(Book book) {
        Date now = new Date();
        return now.getTime() - book.getPublicationDate().getTime();
    }
}
