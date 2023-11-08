package com.kodar.academy.Library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kodar.academy.Library.model.constants.Constants;
import com.kodar.academy.Library.model.dto.rent.RentResponseDTO;
import com.kodar.academy.Library.model.entity.Book;
import com.kodar.academy.Library.model.entity.Rent;
import com.kodar.academy.Library.model.entity.User;
import com.kodar.academy.Library.model.enums.Role;
import com.kodar.academy.Library.model.exceptions.ErrorMessage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RentControllerIntegrationTest extends BaseTest{

    //getAllRents
    @Test
    @Transactional
    void getAllRents_Success() throws Exception {
        User user = genAdmin();
        User rentUser = genRentUser();
        Book book = genBook(1);
        Rent rent = genRent(rentUser, book);

        MockHttpServletResponse result = this.mockMvc.perform(get("/rents")
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<RentResponseDTO> response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.size() > 0);
        Assertions.assertTrue(response.stream().map(r -> r.getBookTitle() + r.getRentedBy()).toList()
                .contains(rent.getBook().getTitle() + rent.getUser().getUsername()));
    }

    @Test
    @Transactional
    void getAllRents_ForbiddenWhenNotAdmin() throws Exception {
        User user = genUser();

        MockHttpServletResponse result = this.mockMvc.perform(get("/rents")
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    //getRentById
    @Test
    @Transactional
    void getRentById_AsAdmin_Success() throws Exception {
        User user = genAdmin();
        User rentUser = genRentUser();
        Book book = genBook(1);
        Rent rent = genRent(rentUser, book);

        MockHttpServletResponse result = this.mockMvc.perform(get("/rents/" + rent.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        RentResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(rent.getUser().getUsername(), response.getRentedBy());
        Assertions.assertEquals(rent.getBook().getTitle(), response.getBookTitle());
        Assertions.assertEquals(rent.getRentDate(), response.getRentDate());
        Assertions.assertEquals(rent.getReturnDate(), response.getReturnDate());
        Assertions.assertEquals(rent.getExpectedReturnDate(), response.getExpectedReturnDate());
    }

    @Test
    @Transactional
    void getRentById_AsUser_Success() throws Exception {
        User user = genUser();
        Book book = genBook(1);
        Rent rent = genRent(user, book);

        MockHttpServletResponse result = this.mockMvc.perform(get("/rents/" + rent.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        RentResponseDTO response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(rent.getUser().getUsername(), response.getRentedBy());
        Assertions.assertEquals(rent.getBook().getTitle(), response.getBookTitle());
        Assertions.assertEquals(rent.getRentDate(), response.getRentDate());
        Assertions.assertEquals(rent.getReturnDate(), response.getReturnDate());
        Assertions.assertEquals(rent.getExpectedReturnDate(), response.getExpectedReturnDate());
    }

    @Test
    @Transactional
    void getRentById_Forbidden() throws Exception {
        User user = genUser();
        User rentUser = genRentUser();
        Book book = genBook(1);
        Rent rent = genRent(rentUser, book);

        MockHttpServletResponse result = this.mockMvc.perform(get("/rents/" + rent.getId())
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.USER.toString()))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @Transactional
    void getRentById_RentNotFoundException() throws Exception {
        User user = genAdmin();

        MockHttpServletResponse result = this.mockMvc.perform(get("/rents/" + -10)
                        .with(user(user.getUsername()).authorities(new SimpleGrantedAuthority(Role.ADMIN.toString()))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();

        ErrorMessage response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertEquals(String.format(Constants.RENT_NOT_FOUND, -10), response.getMessages().get(0));
    }
}
