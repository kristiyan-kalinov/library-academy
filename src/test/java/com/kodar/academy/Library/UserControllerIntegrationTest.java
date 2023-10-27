package com.kodar.academy.Library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.user.UserBalanceDTO;
import com.kodar.academy.Library.model.dto.user.UserSubscriptionDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.Rent;
import com.kodar.academy.Library.model.entity.Subscription;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.enums.Role;
import com.kodar.academy.Library.model.exceptions.ErrorMessage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserControllerIntegrationTest extends BaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //subscribe
    @Test
    @Transactional
    void subscribe_GoldWithNoOlderSubscription_Success() throws Exception{
        User user = genUser();
        Assertions.assertNull(user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        Subscription expectedSubscription = subscriptionRepository.findById(3).orElse(null);
        userSubscriptionDTO.setSubscription(3);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(amountToPay(expectedSubscription))
                .setScale(2, RoundingMode.DOWN), user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void subscribe_SilverWithNoOlderSubscription_Success() throws Exception{
        User user = genUser();
        Assertions.assertNull(user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        Subscription expectedSubscription = subscriptionRepository.findById(2).orElse(null);
        userSubscriptionDTO.setSubscription(2);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(amountToPay(expectedSubscription))
                .setScale(2, RoundingMode.DOWN), user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void subscribe_BronzeWithNoOlderSubscription_Success() throws Exception{
        User user = genUser();
        Assertions.assertNull(user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        Subscription expectedSubscription = subscriptionRepository.findById(1).orElse(null);
        userSubscriptionDTO.setSubscription(1);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(amountToPay(expectedSubscription))
                .setScale(2, RoundingMode.DOWN), user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void subscribe_UpgradeToSilverFromBronze_Success() throws Exception{
        User user = genBronzeUser();
        Subscription oldSubscription = subscriptionRepository.findById(1).orElse(null);
        Assertions.assertEquals(oldSubscription, user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(2);
        Subscription expectedSubscription = subscriptionRepository.findById(2).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(amountToPay(expectedSubscription)).add(amountToPay(oldSubscription))
                        .setScale(2, RoundingMode.DOWN),
                user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void subscribe_UpgradeToGoldFromBronze_Success() throws Exception{
        User user = genBronzeUser();
        Subscription oldSubscription = subscriptionRepository.findById(1).orElse(null);
        Assertions.assertEquals(oldSubscription, user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(3);
        Subscription expectedSubscription = subscriptionRepository.findById(3).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(amountToPay(expectedSubscription)).add(amountToPay(oldSubscription))
                        .setScale(2, RoundingMode.DOWN),
                user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void subscribe_UpgradeToGoldFromSilver_Success() throws Exception{
        User user = genSilverUser();
        Subscription oldSubscription = subscriptionRepository.findById(2).orElse(null);
        Assertions.assertEquals(oldSubscription, user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(3);
        Subscription expectedSubscription = subscriptionRepository.findById(3).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(amountToPay(expectedSubscription)).add(amountToPay(oldSubscription))
                        .setScale(2, RoundingMode.DOWN),
                user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void subscribe_DowngradeToSilverFromGold_Success() throws Exception{
        User user = genGoldUser();
        Subscription oldSubscription = subscriptionRepository.findById(3).orElse(null);
        Assertions.assertEquals(oldSubscription, user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(2);
        Subscription expectedSubscription = subscriptionRepository.findById(2).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance, user.getBalance());
    }

    @Test
    @Transactional
    void subscribe_DowngradeToBronzeFromGold_Success() throws Exception{
        User user = genGoldUser();
        Subscription oldSubscription = subscriptionRepository.findById(3).orElse(null);
        Assertions.assertEquals(oldSubscription, user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(1);
        Subscription expectedSubscription = subscriptionRepository.findById(1).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance, user.getBalance());
    }

    @Test
    @Transactional
    void subscribe_DowngradeToBronzeFromSilver_Success() throws Exception{
        User user = genSilverUser();
        Subscription oldSubscription = subscriptionRepository.findById(2).orElse(null);
        Assertions.assertEquals(oldSubscription, user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(1);
        Subscription expectedSubscription = subscriptionRepository.findById(1).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.SUCCESSFUL_SUBSCRIPTION, response);
        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance, user.getBalance());
    }

    @Test
    @Transactional
    void subscribe_GoldWithNoOlderSubscription_InsufficientBalanceException() throws Exception{
        User user = genUser();
        user.setBalance(BigDecimal.valueOf(0));
        Assertions.assertNull(user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(3);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.INSUFFICIENT_BALANCE, user.getId()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void subscribe_SilverWithNoOlderSubscription_InsufficientBalanceException() throws Exception{
        User user = genUser();
        user.setBalance(BigDecimal.valueOf(0));
        Assertions.assertNull(user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(2);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.INSUFFICIENT_BALANCE, user.getId()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void subscribe_BronzeWithNoOlderSubscription_InsufficientBalanceException() throws Exception{
        User user = genUser();
        user.setBalance(BigDecimal.valueOf(0));
        Assertions.assertNull(user.getSubscription());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(1);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.INSUFFICIENT_BALANCE, user.getId()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void subscribe_UpgradeToSilverFromBronze_InsufficientBalanceException() throws Exception{
        User user = genBronzeUser();
        user.setBalance(BigDecimal.valueOf(0));
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(2);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.INSUFFICIENT_BALANCE, user.getId()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void subscribe_UpgradeToGoldFromBronze_InsufficientBalanceException() throws Exception{
        User user = genBronzeUser();
        user.setBalance(BigDecimal.valueOf(0));
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(3);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.INSUFFICIENT_BALANCE, user.getId()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void subscribe_UpgradeToGoldFromSilver_InsufficientBalanceException() throws Exception{
        User user = genSilverUser();
        user.setBalance(BigDecimal.valueOf(0));
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(3);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.INSUFFICIENT_BALANCE, user.getId()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void subscribe_DowngradeToSilverFromGold_DowngradeCapException() throws Exception{
        User user = genGoldUser();
        for (int i = 1; i < 6; i++) {
            Book book = genBook(i);
            genRent(user, book);
        }
        Assertions.assertEquals(5, user.getRents().size());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(2);
        Subscription newSubscription = subscriptionRepository.findById(2).orElse(null);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.CAP_DOWNGRADE_EXCEPTION_MSG, newSubscription.getTier(),
                        newSubscription.getMaxRentBooks()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void subscribe_DowngradeToBronzeFromGold_DowngradeCapException() throws Exception{
        User user = genGoldUser();
        for (int i = 1; i < 6; i++) {
            Book book = genBook(i);
            genRent(user, book);
        }
        Assertions.assertEquals(5, user.getRents().size());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(1);
        Subscription newSubscription = subscriptionRepository.findById(1).orElse(null);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.CAP_DOWNGRADE_EXCEPTION_MSG, newSubscription.getTier(),
                        newSubscription.getMaxRentBooks()),
                response.getMessages().get(0));
    }

    @Test
    @Transactional
    void subscribe_DowngradeToBronzeFromSilver_DowngradeCapException() throws Exception{
        User user = genGoldUser();
        for (int i = 1; i < 5; i++) {
            Book book = genBook(i);
            genRent(user, book);
        }
        Assertions.assertEquals(4, user.getRents().size());
        UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
        userSubscriptionDTO.setSubscription(1);
        Subscription newSubscription = subscriptionRepository.findById(1).orElse(null);

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/subscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSubscriptionDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.CAP_DOWNGRADE_EXCEPTION_MSG, newSubscription.getTier(),
                        newSubscription.getMaxRentBooks()),
                response.getMessages().get(0));
    }

    //unsubscribe
    @Test
    @Transactional
    void unsubscribe_Success() throws Exception {
        User user = genGoldUser();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/unsubscribe/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertTrue(user.getCancelledSubscription());
        Assertions.assertEquals(Constants.SUCCESSFUL_UNSUBSCRIBE, response);
    }

    @Test
    @Transactional
    void unsubscribe_Forbidden() throws Exception {
        User user = genGoldUser();
        User bronzeUser = genBronzeUser();

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/unsubscribe/" + bronzeUser.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    //addBalance
    @Test
    @Transactional
    void addBalance_Success() throws Exception {
        User user = genGoldUser();
        user.setBalance(BigDecimal.valueOf(0));
        BigDecimal oldBalance = user.getBalance();
        UserBalanceDTO userBalanceDTO = new UserBalanceDTO();
        userBalanceDTO.setBalance(BigDecimal.valueOf(20));

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/add-balance/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBalanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        String response = result.getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.ADD_BALANCE, user.getId(), userBalanceDTO.getBalance()
                .setScale(2, RoundingMode.DOWN)), response);
        Assertions.assertEquals(oldBalance.add(userBalanceDTO.getBalance()).setScale(2, RoundingMode.DOWN),
                user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void addBalance_Forbidden() throws Exception{
        User user = genGoldUser();
        UserBalanceDTO userBalanceDTO = new UserBalanceDTO();
        userBalanceDTO.setBalance(BigDecimal.valueOf(20));

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/add-balance/" + -500)
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBalanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @Transactional
    void addBalance_ValidationException() throws Exception{
        User user = genGoldUser();
        UserBalanceDTO userBalanceDTO = new UserBalanceDTO();
        userBalanceDTO.setBalance(BigDecimal.valueOf(0));

        MockHttpServletResponse result = this.mockMvc.perform(put("/users/add-balance/" + user.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString())))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBalanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constants.MIN_BALANCE, response.getMessages().get(0));
    }

    @Test
    @Transactional
    void checkSubscriptions_ManualCancelSubscription() {
        User user = genGoldUser();
        user.setCancelledSubscription(true);
        BigDecimal oldBalance = user.getBalance();

        userService.checkSubscriptions();

        Assertions.assertNull(user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance, user.getBalance());
    }

    @Test
    @Transactional
    void checkSubscriptions_InsufficientBalanceCancelSubscription() {
        User user = genGoldUser();
        user.setBalance(BigDecimal.valueOf(0));
        BigDecimal oldBalance = user.getBalance();

        userService.checkSubscriptions();

        Assertions.assertNull(user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance, user.getBalance());
    }

    @Test
    @Transactional
    void checkSubscriptions_RenewGoldSubscription() {
        User user = genGoldUser();
        Subscription expectedSubscription = subscriptionRepository.findById(3).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        userService.checkSubscriptions();

        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(expectedSubscription.getCost())
                .setScale(2, RoundingMode.DOWN), user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void checkSubscriptions_RenewSilverSubscription() {
        User user = genSilverUser();
        Subscription expectedSubscription = subscriptionRepository.findById(2).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        userService.checkSubscriptions();

        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(expectedSubscription.getCost())
                .setScale(2, RoundingMode.DOWN), user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void checkSubscriptions_RenewBronzeSubscription() {
        User user = genBronzeUser();
        Subscription expectedSubscription = subscriptionRepository.findById(1).orElse(null);
        BigDecimal oldBalance = user.getBalance();

        userService.checkSubscriptions();

        Assertions.assertEquals(expectedSubscription, user.getSubscription());
        Assertions.assertFalse(user.getCancelledSubscription());
        Assertions.assertEquals(oldBalance.subtract(expectedSubscription.getCost())
                .setScale(2, RoundingMode.DOWN), user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void tax_ProlongedBooks() {
        User user = genBronzeUser();
        for (int i = 1; i < 3; i++) {
            Book book = genBook(i);
            Rent rent = genRent(user, book);
            rent.setExpectedReturnDate(LocalDate.now().minusDays(5));
        }
        Book book = genBook(3);
        genRent(user, book);
        BigDecimal oldBalance = user.getBalance();
        int counter = 0;
        for(Rent r : user.getRents()) {
            if(r.getExpectedReturnDate().isBefore(LocalDate.now())) {
                counter++;
            }
        }

        userService.tax();

        Assertions.assertEquals(oldBalance.subtract(BigDecimal.valueOf(counter*Constants.TAX_PER_DAY))
                        .setScale(2, RoundingMode.DOWN),
                user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void tax_NoProlongedBooks() {
        User user = genBronzeUser();
        for (int i = 1; i < 4; i++) {
            Book book = genBook(i);
            genRent(user, book);
        }
        BigDecimal oldBalance = user.getBalance();
        int counter = 0;
        for(Rent r : user.getRents()) {
            if(r.getExpectedReturnDate().isBefore(LocalDate.now())) {
                counter++;
            }
        }

        userService.tax();

        Assertions.assertEquals(oldBalance.subtract(BigDecimal.valueOf(counter*Constants.TAX_PER_DAY))
                        .setScale(2, RoundingMode.DOWN),
                user.getBalance().setScale(2, RoundingMode.DOWN));
    }

    @Test
    @Transactional
    void checkHasProlonged_ProlongedBooks() {
        User user = genGoldUser();
        Assertions.assertFalse(user.getHasProlongedRents());
        Book book = genBook(1);
        Rent rent = genRent(user, book);
        rent.setExpectedReturnDate(LocalDate.now().minusDays(5));

        userService.checkHasProlonged();

        Assertions.assertTrue(user.getHasProlongedRents());
    }

    @Test
    @Transactional
    void checkHasProlonged_NoProlongedBooks() {
        User user = genGoldUser();
        Assertions.assertFalse(user.getHasProlongedRents());
        Book book = genBook(1);
        genRent(user, book);

        userService.checkHasProlonged();

        Assertions.assertFalse(user.getHasProlongedRents());
    }
}
