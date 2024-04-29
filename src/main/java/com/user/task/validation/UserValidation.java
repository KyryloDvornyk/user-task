package com.user.task.validation;

import com.user.task.exception.DataProcessingException;
import com.user.task.model.User;

import java.time.LocalDate;
import java.util.regex.Pattern;

import static com.user.task.util.ValidationUtils.*;
import static java.util.Objects.*;

public class UserValidation {
    private final static String REGEX_EMAIL = "^(.+)@(\\S+)$";

    public static void fullUserValidation(User user) {
        nullValidation(user);
        emailValidation(user.getEmail());
        birthDateValidation(user.getBirthDate());
    }

    private static void nullValidation(User user) {
        if (isNull(user.getEmail()) || isNull(user.getBirthDate())
                || isNull(user.getFirstName()) || isNull(user.getLastName())) {
            throw new DataProcessingException("Any of email, birth date, first name or last name fields can't be empty");
        }
    }

    public static void emailValidation(String email) {
        if (isNot(Pattern.compile(REGEX_EMAIL).matcher(email).matches())) {
            throw new DataProcessingException("Email " + email + " is not valid");
        }
    }

    public static void birthDateValidation(LocalDate birthDate) {
        if (LocalDate.now().isBefore(birthDate)) {
            throw new DataProcessingException("Birth date must be earlier than current date");
        }
    }

    public static void dateRangeValidation(LocalDate fromDate, LocalDate toDate) {
        if (isNot(fromDate.isBefore(toDate))) {
            throw new DataProcessingException("From date can't be after To date");
        }
    }
}
