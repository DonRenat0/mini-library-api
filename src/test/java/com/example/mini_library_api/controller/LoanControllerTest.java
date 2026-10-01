package com.example.mini_library_api.controller;

import com.example.mini_library_api.dto.LoanRequest;
import com.example.mini_library_api.dto.LoanResponse;
import com.example.mini_library_api.exception.BookNotAvailableException;
import com.example.mini_library_api.service.LoanService;



import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private LoanService loanService;

    @Test
    void createLoan_devuelve201_cuandoTodoVaBien() throws Exception {
        LoanResponse response = new LoanResponse(1L, "Clean Code", "Ana", LocalDate.now(), LocalDate.now().plusDays(14), null);
        when(loanService.createLoan(10L, 20L)).thenReturn(response);

        LoanRequest request = new LoanRequest(10L, 20L);

        mockMvc.perform(post("/api/loans")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookTitle").value("Clean Code"))
                .andExpect(jsonPath("$.memberName").value("Ana"));
    }

    @Test
    void createLoan_devuelve409_cuandoNoHayCopiasDisponibles() throws Exception {
        when(loanService.createLoan(10L, 20L))
                .thenThrow(new BookNotAvailableException("No quedan copias disponibles de \"Clean Code\""));

        LoanRequest request = new LoanRequest(10L, 20L);

        mockMvc.perform(post("/api/loans")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("No quedan copias disponibles de \"Clean Code\""));
    }

    @Test
    void createLoan_devuelve400_cuandoFaltaBookId() throws Exception {
        String jsonSinBookId = "{\"memberId\": 20}";

        mockMvc.perform(post("/api/loans")
                        .contentType("application/json")
                        .content(jsonSinBookId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnLoan_devuelve200_conFechaDeDevolucion() throws Exception {
        LoanResponse response = new LoanResponse(1L, "Clean Code", "Ana", LocalDate.now().minusDays(5), LocalDate.now().plusDays(9), LocalDate.now());
        when(loanService.returnLoan(1L)).thenReturn(response);

        mockMvc.perform(patch("/api/loans/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returnedDate").isNotEmpty());
    }
}