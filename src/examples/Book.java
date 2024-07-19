package examples;

import java.time.LocalDate;
import java.util.Objects;

public class Book {
    private Author author;
    private LocalDate publishDate;

    public Book() {
    }

    public Book(Author author, LocalDate publishDate) {
        this.author = author;
        this.publishDate = publishDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(getAuthor(), book.getAuthor()) && Objects.equals(getPublishDate(), book.getPublishDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthor(), getPublishDate());
    }

    @Override
    public String toString() {
        return "Book{" +
                "author=" + author +
                ", publishDate=" + publishDate +
                '}';
    }
}
