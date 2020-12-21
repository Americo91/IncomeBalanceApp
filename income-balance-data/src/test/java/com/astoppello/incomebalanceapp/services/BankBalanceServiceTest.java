package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperImpl;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {BankBalanceMapperImpl.class, BankMapperImpl.class})
class BankBalanceServiceTest {

    private static final Long ID = 1L;
    public static final BigDecimal EXPENSES = BigDecimal.valueOf(100);
    public static final BigDecimal SALARY = BigDecimal.valueOf(200);
    public static final String REVOLUT = "Revolut";
    BankBalance bankBalance;
    @Autowired
    BankBalanceMapper bankBalanceMapper;

    @Mock
    BankBalanceRepository repository;
    BankBalanceService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new BankBalanceServiceImpl(repository, bankBalanceMapper);
        bankBalance =
                BankBalance.builder()
                           .bank(Bank.builder()
                                     .id(ID)
                                     .name(REVOLUT)
                                     .build())
                           .expenses(EXPENSES)
                           .salary(SALARY)
                           .id(ID)
                           .build();
    }

    @Test
    void findAll() {
        List<BankBalance> list = List.of(bankBalance, BankBalance.builder().build());
        when(repository.findAll()).thenReturn(list);
        assertEquals(list.size(), service.findAll().size());
        verify(repository).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(bankBalance));
        BankBalanceDTO bankBalanceDTO = service.findById(ID);
        assertNotNull(bankBalanceDTO);
        assertEquals(ID, bankBalanceDTO.getId());
        assertEquals(EXPENSES, bankBalanceDTO.getExpenses());
        assertEquals(SALARY, bankBalanceDTO.getSalary());
        assertEquals(ID, bankBalanceDTO.getBank().getId());
        assertEquals(REVOLUT, bankBalanceDTO.getBank().getName());

    }
}