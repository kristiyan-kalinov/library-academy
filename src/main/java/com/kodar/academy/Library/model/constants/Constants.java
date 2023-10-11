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
    public static final String SUCCESSFUL_BOOK_DELETE = "Book successfully deleted";
    public static final String INSUFFICIENT_BOOK_TOTAL_QUANTITY = "Insufficient total quantity";
    public static final String INSUFFICIENT_BOOK_AVAILABLE_QUANTITY = "Book out of stock";
    public static final String SUCCESSFUL_USER_DELETE = "User successfully deleted";
    public static final String SUCCESSFUL_PASSWORD_CHANGE = "Password changed successfully";
    public static final String USER_NOT_FOUND = "User with id %d not found";
    public static final String USER_NOT_ELIGIBLE_TO_RENT = "User: %s can't rent a book for %s";
    public static final String RENT_NOT_FOUND = "Rent with id %d not found";
    public static final String BOOK_ALREADY_RETURNED = "Book is already returned";
    public static final String USER_PROLONGED_RENTS = "User: %s has prolonged rents";
    public static final String DUPLICATE_RENT = "Can't rent the same book twice";
    public static final String RENT_CAP_MSG = "User: %s reached max amount of rented books at once";
}
