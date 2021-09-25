package com.digitaldubai.bookstore;

import com.digitaldubai.bookstore.error.BookNotFoundException;
import com.digitaldubai.bookstore.error.BookUnSupportedFieldPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    private BookRepository repository;

    // Find
    @GetMapping("/books")
    List<Book> findAll() {
        return repository.findAll();
    }

    // Save
    @PostMapping("/books")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    Book newBook(@RequestBody Book newBook) {
        return repository.save(newBook);
    }

    // Find
    @GetMapping("/books/{id}")
    Book findOne(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // Save or update
    @PutMapping("/books/{id}")
    Book saveOrUpdate(@RequestBody Book newBook, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {
                    x.setName(newBook.getName());
                    x.setAuthor(newBook.getAuthor());
                    x.setPrice(newBook.getPrice());
                    x.setDescription(newBook.getDescription());
                    x.setIsbn(newBook.getIsbn());
                    x.setType(newBook.getType());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repository.save(newBook);
                });
    }

    // update author only
    @PatchMapping("/books/{id}")
    Book patch(@RequestBody Map<String, String> update, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {

                    String author = update.get("author");
                    if (!StringUtils.isEmpty(author)) {
                        x.setAuthor(author);

                        // better create a custom method to update a value = :newValue where id = :id
                        return repository.save(x);
                    } else {
                        throw new BookUnSupportedFieldPatchException(update.keySet());
                    }

                })
                .orElseGet(() -> {
                    throw new BookNotFoundException(id);
                });

    }

    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }



    //
    @PostMapping("/checkout")
      String checkOut(@RequestBody CheckoutForm checkoutForm) {
        double sum = 0,discount = 0;
        if(!isEmptyString(checkoutForm.getPromotionCode())) {
            //if promotion code is discount
            if (checkoutForm.getPromotionCode().equalsIgnoreCase("DISCOUNT")) {
                for (Book book : checkoutForm.getBooks()) {
                    sum += book.getPrice();
                    if (book.getType().equalsIgnoreCase("Comics")) {
                        discount= 100-0;
                    } else if (book.getType().equalsIgnoreCase("Fiction")) {
                        discount= 100-20;
                    }
                    sum=(discount*sum)/100;
                }
            }
        }else{
            sum = checkoutForm.getBooks().stream().filter(a->a!=null ).mapToDouble(Book::getPrice).sum();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        return df.format(sum);
    }

    boolean isEmptyString(String str){
        return str==null || str.isEmpty();
    }

}
