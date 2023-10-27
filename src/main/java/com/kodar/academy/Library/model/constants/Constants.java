package com.kodar.academy.Library.model.constants;

public class Constants {

    //event listener
    public static final String CREATE_ACTION = "CREATE";
    public static final String UPDATE_TITLE_ACTION = "UPDATE title";
    public static final String UPDATE_PUBLISHER_ACTION = "UPDATE publisher";
    public static final String UPDATE_STATUS_ACTION = "UPDATE status";
    public static final String UPDATE_REASON_ACTION = "UPDATE deactivation reason";
    public static final String UPDATE_TOTAL_QUANTITY_ACTION = "UPDATE total quantity";
    public static final String UPDATE_AVAILABLE_QUANTITY_ACTION = "UPDATE available quantity";
    //entity fields
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String YEAR = "year";
    public static final String ID = "id";
    public static final String GENRES = "genres";
    public static final String AUTHORS = "authors";
    public static final String IS_ACTIVE = "isActive";
    //exception messages
    public static final String DELETE_ACTIVE_BOOK_MSG = "Active books can't be deleted";
    public static final String BOOK_NOT_FOUND = "Book with id %d not found";
    public static final String BOOK_NOT_ACTIVE = "Book with id %d is not active";
    public static final String INVALID_DEACT_REASON = "Invalid deactivation reason";
    public static final String INSUFFICIENT_BOOK_TOTAL_QUANTITY = "Insufficient total quantity";
    public static final String INSUFFICIENT_BOOK_AVAILABLE_QUANTITY = "Book out of stock";
    public static final String USER_NOT_FOUND = "User with id %d not found";
    public static final String USER_NOT_ELIGIBLE_TO_RENT = "User: %s can't rent a book for %s";
    public static final String RENT_NOT_FOUND = "Rent with id %d not found";
    public static final String BOOK_ALREADY_RETURNED = "Book is already returned";
    public static final String USER_PROLONGED_RENTS = "User: %s has prolonged rents";
    public static final String DUPLICATE_RENT = "Can't rent the same book twice";
    public static final String RENT_CAP_MSG = "User: %s reached max amount of rented books at once";
    public static final String NO_SUBSCRIPTION = "User with id %d has no subscription";
    public static final String INSUFFICIENT_BALANCE = "User with id %d has insufficient balance to subscribe";
    public static final String CAP_DOWNGRADE_EXCEPTION_MSG = "Can't downgrade plan to %s because you have more than %d rented books";
    //controller messages
    public static final String SUCCESSFUL_BOOK_DELETE = "Book successfully deleted";
    public static final String SUCCESSFUL_USER_DELETE = "User successfully deleted";
    public static final String SUCCESSFUL_PASSWORD_CHANGE = "Password changed successfully";
    public static final String SUCCESSFUL_SUBSCRIPTION = "User successfully subscribed";
    public static final String SUCCESSFUL_UNSUBSCRIBE = "User successfully unsubscribed";
    public static final String ADD_BALANCE = "User with id: %d added %f balance";
    //validation messages
    public static final String TITLE_REQUIRED = "Title is required";
    public static final String TITLE_LENGTH = "Title must be between 1 and 255 characters";
    public static final String PUBLISHER_REQUIRED = "Publisher is required";
    public static final String PUBLISHER_LENGTH = "Publisher must be between 2 and 64 characters";
    public static final String TOTAL_QUANTITY_MIN_VALUE = "Total quantity can't be set to less than 0";
    public static final String ISBN_REQUIRED = "ISBN is required";
    public static final String FNAME_REQUIRED = "First name is required";
    public static final String FNAME_LENGTH = "First name must be between 1 and 64 characters";
    public static final String FNAME_LETTERS = "First name must contain only letters";
    public static final String LNAME_REQUIRED = "Last name is required";
    public static final String LNAME_LENGTH = "Last name must be between 1 and 64 characters";
    public static final String LNAME_LETTERS = "Last name must contain only letters";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_LENGTH = "Password must be between 8 and 32 characters";
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String USERNAME_LENGTH = "Username must be between 3 and 32 characters";
    public static final String DISPLAY_NAME_REQUIRED = "Display name is required";
    public static final String DISPLAY_NAME_LENGTH = "Display name must be between 3 and 32 characters";
    public static final String DUPLICATE_USERNAME = "Username already taken";
    public static final String PAST_DATE_EXPECTED = "Past date expected";
    public static final String MIN_BALANCE = "Can't add negative balance";
    public static final String LESS_THAN_1_INVALID = "cant be less than 1";
    public static final String MORE_THAN_3_INVALID = "cant be more than 3";
    //service constants
    public static final double TAX_PER_DAY = 0.2;
}
