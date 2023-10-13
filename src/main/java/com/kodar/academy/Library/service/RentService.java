package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.rent.RentCreateDTO;
import com.kodar.academy.Library.model.dto.rent.RentResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.Rent;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.enums.Role;
import com.kodar.academy.Library.model.exceptions.*;
import com.kodar.academy.Library.model.mapper.RentMapper;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.repository.RentRepository;
import com.kodar.academy.Library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentService {

    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(RentService.class);

    @Autowired
    public RentService(RentRepository rentRepository, UserRepository userRepository,
                           BookRepository bookRepository) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<RentResponseDTO> getAllRents() {
        logger.info("getAllRents called");
        List<RentResponseDTO> rents = rentRepository.findAll().stream()
                .map(RentMapper::mapToResponse)
                .toList();
        return rents;
    }

    public RentResponseDTO getRentById(int id) {
        logger.info("getRentById called with params:" + id);
        Rent rent = rentRepository.findById(id).orElse(null);
        if(rent != null) {
            return RentMapper.mapToResponse(rent);
        }
        else throw new RentNotFoundException(id);
    }

    @Transactional
    public RentResponseDTO createRent(int bookId, RentCreateDTO rentCreateDTO) {
        logger.info("createRent called for book with id: " + bookId + " with params:" + rentCreateDTO);
        Book book = bookRepository.findById(bookId).orElse(null);
        if(book == null) {
            throw new BookNotFoundException(bookId);
        }
        if(!book.getIsActive()) {
            throw new BookNotActiveException(bookId);
        }
        if(book.getAvailableQuantity() < 1) {
            throw new InsufficientBookAvailableQuantityException();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        User rentForUser;
        if(rentCreateDTO != null) {
            if(rentCreateDTO.getUserId() != null) {
                rentForUser = userRepository.findById(rentCreateDTO.getUserId()).orElse(null);
                if(rentForUser == null) {
                    throw new UserNotFoundException(rentCreateDTO.getUserId());
                }
            }
            else rentForUser = authUser;
            if (authUser.getId() != rentForUser.getId() && !authUser.getRole().equals(Role.ADMIN)) {
                throw new UserNotEligibleToRentException(authUser.getUsername(), rentForUser.getUsername());
            }
        }
        else rentForUser = authUser;
        if(rentForUser.getHasProlongedRents()) {
            throw new UserProlongedRentsException(rentForUser.getUsername());
        }
        int counter = 0;
        for(Rent r : rentForUser.getRents()) {
            if(r.getBook().getId() == bookId && r.getReturnDate() == null) {
                throw new DuplicateRentException();
            }
            if(r.getReturnDate() == null) {
                counter++;
            }
        }
        if(counter > 2) {
            throw new RentCapException(rentForUser.getUsername());
        }
        Rent rent = new Rent();
        rent.setRentDate(LocalDate.now());
        if(rentCreateDTO == null || rentCreateDTO.getExpectedReturnDate() == null) {
            rent.setExpectedReturnDate(LocalDate.now().plusMonths(1));
        }
        else {
            rent.setExpectedReturnDate(rentCreateDTO.getExpectedReturnDate());
        }
        rent.setBook(book);
        rent.setUser(rentForUser);
        rentRepository.save(rent);
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);
        return RentMapper.mapToResponse(rent);
    }

    @Transactional
    public RentResponseDTO returnRent(int id) {
        logger.info("returnRent called with params:" + id);
        Rent rent = rentRepository.findById(id).orElse(null);
        if(rent == null) {
            throw new RentNotFoundException(id);
        }
        if(rent.getReturnDate() == null) {
            rent.setReturnDate(LocalDate.now());
            rentRepository.save(rent);
            Book book = bookRepository.findById(rent.getBook().getId()).orElseThrow();
            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            bookRepository.save(book);
            User user = userRepository.findById(rent.getUser().getId()).orElseThrow();
            boolean hasProlonged = false;
            if(user.getHasProlongedRents()) {
                for(Rent r : user.getRents()) {
                    if(r.getReturnDate() == null && LocalDate.now().isAfter(r.getExpectedReturnDate())) {
                        hasProlonged = true;
                        break;
                    }
                }
            }
            if(!hasProlonged) {
                user.setHasProlongedRents(false);
                userRepository.save(user);
            }
            return RentMapper.mapToResponse(rent);
        }
        else throw new BookAlreadyReturnedException();
    }

    public String checkAuth(int id) {
        Rent rent = rentRepository.findById(id).orElse(null);
        return rent == null ? null : rent.getUser().getUsername();
    }
}
