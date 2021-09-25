package com.digitaldubai.bookstore;

import java.util.List;

public class CheckoutForm {

    List<Book> books;

    String promotionCode;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }
}
