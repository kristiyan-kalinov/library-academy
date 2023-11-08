package com.kodar.academy.Library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kodar.academy.Library.model.dto.author.AuthorDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthorControllerIntegrationTest extends BaseTest{

    @Test
    @Transactional
    void getAllAuthors() throws Exception {

        MockHttpServletResponse result = this.mockMvc.perform(get("/authors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<AuthorDTO> response = objectMapper.readValue(result.getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
    }
}
