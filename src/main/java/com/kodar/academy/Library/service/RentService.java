package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.dto.rent.RentCreateDTO;
import com.kodar.academy.Library.model.dto.rent.RentResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.Rent;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.enums.Role;
import com.kodar.academy.Library.model.enums.SubscriptionType;
import com.kodar.academy.Library.model.exceptions.BookAlreadyReturnedException;
import com.kodar.academy.Library.model.exceptions.BookNotActiveException;
import com.kodar.academy.Library.model.exceptions.BookNotFoundException;
import com.kodar.academy.Library.model.exceptions.DuplicateRentException;
import com.kodar.academy.Library.model.exceptions.InsufficientBookAvailableQuantityException;
import com.kodar.academy.Library.model.exceptions.NoSubscriptionException;
import com.kodar.academy.Library.model.exceptions.RentCapException;
import com.kodar.academy.Library.model.exceptions.RentNotFoundException;
import com.kodar.academy.Library.model.exceptions.UserNotEligibleToRentException;
import com.kodar.academy.Library.model.exceptions.UserNotFoundException;
import com.kodar.academy.Library.model.exceptions.UserProlongedRentsException;
import com.kodar.academy.Library.model.mapper.RentMapper;
import com.kodar.academy.Library.repository.BookRepository;
import com.kodar.academy.Library.repository.RentRepository;
import com.kodar.academy.Library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
            if (authUser.getId() != rentForUser.getId()) {
                if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.toString()))) {
                    throw new UserNotEligibleToRentException(authUser.getUsername(), rentForUser.getUsername());
                }
            }
        }
        else rentForUser = authUser;
        if(rentForUser.getSubscription() == null) {
            throw new NoSubscriptionException(rentForUser.getId());
        }
        if(rentForUser.getHasProlongedRents()) {
            throw new UserProlongedRentsException(rentForUser.getUsername());
        }
        int counter = 0;
        for(Rent r : rentForUser.getRents()) {
            if(r.getReturnDate() == null) {
                if(r.getBook().getId() == bookId) {
                    throw new DuplicateRentException();
                }
                counter++;
            }
        }
        if(counter >= rentForUser.getSubscription().getMaxRentBooks()) {
            throw new RentCapException(rentForUser.getUsername());
        }
        Rent rent = new Rent();
        rent.setRentDate(LocalDate.now());
        if(rentForUser.getSubscription().getTier() == SubscriptionType.BRONZE) {
            rent.setExpectedReturnDate(LocalDate.now().plusDays(rentForUser.getSubscription().getMaxRentDays()));
        }
        else if(rentForUser.getSubscription().getTier() == SubscriptionType.SILVER) {
            rent.setExpectedReturnDate(LocalDate.now().plusDays(rentForUser.getSubscription().getMaxRentDays()));
        }
        else if(rentForUser.getSubscription().getTier() == SubscriptionType.GOLD) {
            rent.setExpectedReturnDate(LocalDate.now().plusDays(rentForUser.getSubscription().getMaxRentDays()));
        }
        rent.setBook(book);
        rent.setUser(rentForUser);
        rentRepository.save(rent);
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);
        rentForUser.getRents().add(rent);
        userRepository.save(rentForUser);
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
