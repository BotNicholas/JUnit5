package services;

import DAO.BookDao;
import objects.Author;
import objects.Book;
import objects.BookCategory;
import ranking.BookRanker;

import java.util.List;
import java.util.stream.Collectors;

public class BookService {

    private final BookDao dao;

    public BookService(BookDao dao) {
        this.dao = dao;
    }

    public List<Book> findAll() {
        return dao.findAll();
    }

    public List<Book> findBooksCheaperThan(Double price) {
        List<Book> books = dao.findAll();
        return books.stream().filter(b -> b.getRecommendedPrice() < price).collect(Collectors.toList());
    }

    public Double getTotalBooksPrice() {
        return dao.findAll().stream().reduce(0d, (Double p, Book b) -> p + b.getRecommendedPrice(), (Double p1, Double p2) -> p1 + p2);
    }

    public List<Book> findAllBooksForAuthor(Author author) {
        return dao.findAll().stream().filter((b) -> b.getAuthor().equals(author)).collect(Collectors.toList());
    }

    public int countBooksByCategory(BookCategory category) {
        return dao.findAll().stream().filter(b -> b.getCategory().equals(category)).collect(Collectors.toList()).size();
    }

    public List<Book> getSortedBooksByRank() {
        BookRanker ranker = new BookRanker();
        return findAll().stream().sorted((book1, book2) -> ranker.calculateRank(book1) - ranker.calculateRank(book2)).collect(Collectors.toList());
    }
}
