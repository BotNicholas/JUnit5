package validating;

import com.mysql.cj.util.StringUtils;
import objects.Author;

import java.util.Date;

public class AuthorValidator implements Validator<Author> {
    @Override
    public Boolean isValid(Author author) {
        return author != null
                && isIdValid(author.getId())
                && isBirthDateValid(author.getBirthDate())
                && isFirstnameValid(author.getFirstname())
                && isLastnameValid(author.getLastname())
                && isGenderValid(author.getGender())
                && areInitialsValid(author.getInitials());
    }

    private boolean isIdValid(int id) {
        return id > 0;
    }

    //Author have to be 18> years old!
    private boolean isBirthDateValid(Date birthday) {
        Date now = new Date();
        long currentAuthorAge = now.getTime() - birthday.getTime();
        return currentAuthorAge >= (Constants.YEAR_IN_MILLISECONDS * Constants.ADULTHOOD_AGE);
    }

    private boolean isFirstnameValid(String firstName) {
        return !StringUtils.isEmptyOrWhitespaceOnly(firstName);
    }

    private boolean isLastnameValid(String lastName) {
        return !StringUtils.isEmptyOrWhitespaceOnly(lastName);
    }

    private boolean isGenderValid(Character gender) {
        return gender.equals('M') || gender.equals('F') || gender.equals('U');
    }

    private boolean areInitialsValid(String initials) {
        return initials.length() == 2 && !StringUtils.isEmptyOrWhitespaceOnly(initials) && isUpperCase(initials);
    }

    private boolean isUpperCase(String str) {
        return str.toUpperCase().equals(str);
    }
}
