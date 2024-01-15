package validating;

import com.mysql.cj.util.StringUtils;
import objects.BookCategory;

public class BookCategoryValidator implements Validator<BookCategory> {
    @Override
    public Boolean isValid(BookCategory category) {
        return category != null
                && isIdValid(category.getCode())
                && isDescriptionValid(category.getDescription());
    }

    private boolean isIdValid(int id) {
        return id > 0;
    }

    private boolean isDescriptionValid(String description) {
        return !StringUtils.isEmptyOrWhitespaceOnly(description);
    }


}
