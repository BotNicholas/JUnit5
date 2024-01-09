package services;

import DAO.BookDAO;
import objects.Author;
import objects.Book;
import objects.BookCategory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private final BookDAO dao;
    public BookService(BookDAO dao){
        this.dao =dao;
    }

    public List<Book> findAll(){
        return dao.findAll();
    }

    public List<Book> findBooksCheaperThan(Double price){
        List<Book> books = dao.findAll();
        return books.stream().filter(b->b.getRecommendedPrice()<price).collect(Collectors.toList());
    }

    public Double getTotalBooksPrice(){
//        return dao.findAll().stream().map(b->b.getRecommendedPrice()).reduce(0d, (p1, p2) -> p1+p2);
        return dao.findAll().stream().reduce(0d, (Double p, Book b)->p+b.getRecommendedPrice(), (Double p1, Double p2)->p1+p2);

        /**
         * Optional<T> reduce(BinaryOperator<T> accumulator) - работает чисто с одним и тем же типом данных
         * T reduce(T identity, BinaryOperator<T> accumulator) - так же, как и первый, только сдесь мы передаем "начальное" значение для функции во втором параметре, а также если функция не отработает, вернёт его
         * U reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner) - тут первый параметр - это начальное значение,
         * второй - BiFunction - это функция для промежуточных вычислений. То есть она будет брать предыдущее значение (Double) и что-то делать (складывать его) с новым объектом (Book. Вернее ценой этого Book. Book Она принимает, но работает с Double). То есть это, по сути, функция преобразования одного объекта в другой.
         * Третий параметр - функция соединения (reduce-a), то есть та функция, которая соберет все предыдущие значения...
         */
    }

    public List<Book> findAllBooksForAuthor(Author author){
        return dao.findAll().stream().filter((b)->b.getAuthor().equals(author)).collect(Collectors.toList());
    }

    public int countBooksByCategory(BookCategory category){
        return dao.findAll().stream().filter(b->b.getCategory().equals(category)).collect(Collectors.toList()).size();
    }
}
