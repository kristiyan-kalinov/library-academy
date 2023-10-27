package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.user.UserBalanceDTO;
import com.kodar.academy.Library.model.dto.user.UserCPDTO;
import com.kodar.academy.Library.model.dto.user.UserEditDTO;
import com.kodar.academy.Library.model.dto.user.UserExtendedResponseDTO;
import com.kodar.academy.Library.model.dto.user.UserRegisterDTO;
import com.kodar.academy.Library.model.dto.user.UserResponseDTO;
import com.kodar.academy.Library.model.dto.user.UserSubscriptionDTO;
import com.kodar.academy.Library.model.entity.Rent;
import com.kodar.academy.Library.model.entity.Subscription;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.exceptions.DowngradeCapException;
import com.kodar.academy.Library.model.exceptions.InsufficientBalanceException;
import com.kodar.academy.Library.model.exceptions.UserNotFoundException;
import com.kodar.academy.Library.model.mapper.UserMapper;
import com.kodar.academy.Library.repository.BookAuditLogRepository;
import com.kodar.academy.Library.repository.SubscriptionRepository;
import com.kodar.academy.Library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookAuditLogRepository bookAuditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           BookAuditLogRepository bookAuditLogRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.bookAuditLogRepository = bookAuditLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.subscriptionRepository = subscriptionRepository;
    }

    public UserExtendedResponseDTO getUserById(int id) {
        logger.info("getUserById called with params: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            return UserMapper.mapToExtendedResponse(user);
        }
        else throw new UserNotFoundException(id);
    }

    public List<UserResponseDTO> getAllUsers() {
        logger.info("getAllUsers called");
        List<UserResponseDTO> users = new ArrayList<>();
        userRepository.findAll().stream()
                .map(UserMapper::mapToResponse)
                .forEach(users::add);

        return users;

    }

    @Transactional
    public UserResponseDTO createUser(UserRegisterDTO userRegisterDTO) {
        logger.info("createUser called with params: " + userRegisterDTO.toString());
        User user = UserMapper.mapToUser(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setBalance(BigDecimal.valueOf(0));
        userRepository.save(user);

        return UserMapper.mapToResponse(user);

    }

    @Transactional
    public UserResponseDTO editUser(int id, UserEditDTO userEditDTO) {
        logger.info("editUser called for user with id: " + id + " and params: " + userEditDTO.toString());
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        user.setUsername(userEditDTO.getUsername());
        user.setFirstName(userEditDTO.getFirstName());
        user.setLastName(userEditDTO.getLastName());
        user.setDisplayName(userEditDTO.getDisplayName());
        userRepository.save(user);
        return UserMapper.mapToResponse(user);

    }

    @Transactional
    public void deleteUser(int id) {
        logger.info("deleteUser called with params: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            bookAuditLogRepository.setUserIdToNull(user.getUsername());
            userRepository.deleteById(id);
        }
        else throw new UserNotFoundException(id);
    }

    public void changePassword(int id, UserCPDTO userCPDTO) {
        logger.info("changePassword called for user with id: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        user.setPassword(passwordEncoder.encode(userCPDTO.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void subscribe(int id, UserSubscriptionDTO userSubscriptionDTO) {
        logger.info("subscribe called for user with id: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        Subscription userSubscription = user.getSubscription();
        Subscription newSubscription = subscriptionRepository.findById(userSubscriptionDTO.getSubscription()).orElse(null);
        if(newSubscription != null) {
            if(userSubscription == null) {
                if(user.getBalance().compareTo(newSubscription.getCost()) < 0) {
                    throw new InsufficientBalanceException(id);
                }
                user.setBalance(user.getBalance().subtract(amountToPay(newSubscription))
                        .setScale(2, RoundingMode.DOWN));
            }
            else {
                if(userSubscription.getCost().compareTo(newSubscription.getCost()) < 0) {
                    user.setBalance(user.getBalance().add(amountToPay(userSubscription)).subtract(amountToPay(newSubscription)));
                    if(user.getBalance().compareTo(newSubscription.getCost()) < 0) {
                        throw new InsufficientBalanceException(id);
                    }
                }
                else if(userSubscription.getCost().compareTo(newSubscription.getCost()) > 0) {
                    if(user.getRents().stream().filter(rent -> rent.getReturnDate() == null).count()
                            > newSubscription.getMaxRentBooks()) {
                        throw new DowngradeCapException(newSubscription.getTier().toString(), newSubscription.getMaxRentBooks());
                    }
                }
            }
            user.setSubscription(newSubscription);
            user.setCancelledSubscription(false);
            userRepository.save(user);
        }
    }

    private static BigDecimal amountToPay(Subscription subscription) {
        return subscription.getCost()
                .divide(BigDecimal.valueOf(LocalDate.now().lengthOfMonth()), RoundingMode.UP)
                .multiply(BigDecimal.valueOf(LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth()));
    }

    @Transactional
    public void unsubscribe(int id) {
        logger.info("unsubscribe called for user with id: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        user.setCancelledSubscription(true);
        userRepository.save(user);
    }

    @Transactional
    public void addBalance(int id, UserBalanceDTO userBalanceDTO) {
        logger.info("addBalance called for user with id: " + id);
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        user.setBalance(user.getBalance().add(userBalanceDTO.getBalance())
                .setScale(2, RoundingMode.DOWN));
        userRepository.save(user);
    }

    @Transactional
    public void checkSubscriptions() {
        logger.info("checkSubscriptions called");
        List<User> users = userRepository.findAllBySubscriptionNotNull();
        for(User u : users) {
            if(u.getCancelledSubscription()) {
                u.setSubscription(null);
                u.setCancelledSubscription(false);
            }
            else {
                if(u.getBalance().compareTo(u.getSubscription().getCost()) < 0) {
                    u.setSubscription(null);
                    u.setCancelledSubscription(false);
                }
                else {
                    u.setBalance(u.getBalance().subtract(u.getSubscription().getCost())
                            .setScale(2, RoundingMode.DOWN));
                }
            }
        }
    }

    @Transactional
    public void tax() {
        logger.info("tax called");
        List<User> users = userRepository.findAllProlonged();
        for(User u : users) {
            for(Rent r : u.getRents()) {
                if(r.getReturnDate() == null && r.getExpectedReturnDate().isBefore(LocalDate.now())) {
                    u.setBalance(u.getBalance().subtract(BigDecimal.valueOf(Constants.TAX_PER_DAY))
                            .setScale(2, RoundingMode.DOWN));
                }
            }
        }
    }

    @Transactional
    public void checkHasProlonged() {
        logger.info("checkHasProlonged called");
        List<User> users = userRepository.findAll();
        for(User u : users) {
            boolean flag = false;
            for(Rent r : u.getRents()) {
                if(r.getReturnDate() == null && r.getExpectedReturnDate().isBefore(LocalDate.now())) {
                    flag = true;
                    break;
                }
            }
            u.setHasProlongedRents(flag);
        }
    }

    public String checkAuth(int id) {
        User user = userRepository.findById(id).orElse(null);
        return user == null ? null : user.getUsername();
    }
}
