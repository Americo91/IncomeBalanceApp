package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperImpl;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {BankBalanceMapperImpl.class, BankMapperImpl.class})
class BankBalanceServiceTest {

    private final String expenses = "100";
    final BigDecimal EXPENSES = new BigDecimal(expenses);
    private final String incomes = "200";
    final BigDecimal INCOMES = new BigDecimal(incomes);
    final String REVOLUT = "Revolut";
    private static final Long ID = 1L;
    BankBalance bankBalance;
    @Autowired
    BankBalanceMapper bankBalanceMapper;
    BankBalanceService service;
    @Mock
    YearBalanceRepository yearBalanceRepository;
    @Mock
    BankBalanceRepository bankBalanceRepository;
    @Mock
    MonthBalanceRepository monthBalanceRepository;
    private YearBalance yearBalance;
    private MonthBalance monthBalance;
    @Mock
    BankRepository bankRepository;
    private Bank bank;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service =
                new BankBalanceServiceImpl(
                        bankBalanceRepository,
                        bankBalanceMapper,
                        monthBalanceRepository,
                        yearBalanceRepository, bankRepository);
        monthBalance = MonthBalance.builder()
                                   .month(Month.SEPTEMBER)
                                   .id(ID)
                                   .build();
        bank = Bank.builder()
                   .id(ID)
                   .name(REVOLUT)
                   .build();
        bankBalance =
                BankBalance.builder()
                           .bank(bank)
                           .expenses(EXPENSES)
                           .incomes(INCOMES)
                           .id(ID)
                           .build();
        yearBalance =
                YearBalance.builder()
                           .id(ID)
                           .build()
                           .addMonthBalance(monthBalance.addBankBalance(bankBalance));
        bankBalance.setYearBalance(yearBalance);
        bankBalance.setMonthBalance(monthBalance);
    }

    @Test
    void findAllById() {
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
        assertEquals(1, service.findAllByIds(ID, ID)
                               .size());
        verify(yearBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void findAllByYearBalanceId() {
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
        assertEquals(1, service.findAllByYearBalanceId(ID)
                               .size());
        verify(yearBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void findAll() {
        when(bankBalanceRepository.findAll()).thenReturn(List.of(bankBalance));
        assertEquals(1, service.findAll()
                               .size());
        verify(bankBalanceRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(bankBalanceRepository.findById(anyLong())).thenReturn(Optional.of(bankBalance));
        BankBalanceDTO bankBalanceDTO = service.findById(ID);
        assertNotNull(bankBalanceDTO);
        assertEquals(ID, bankBalanceDTO.getId());
        assertEquals(expenses, bankBalanceDTO.getExpenses());
        assertEquals(incomes, bankBalanceDTO.getIncomes());
        assertEquals(ID, bankBalanceDTO.getBank()
                                       .getId());
        assertEquals(REVOLUT, bankBalanceDTO.getBank()
                                            .getName());
        verify(bankBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByBankName() {
        when(bankBalanceRepository.findAll()).thenReturn(List.of(bankBalance));
        BankBalanceDTO bankBalanceDTO = service.findByBankName(REVOLUT)
                                               .get(0);
        assertNotNull(bankBalanceDTO);
        assertEquals(ID, bankBalanceDTO.getId());
        assertEquals(expenses, bankBalanceDTO.getExpenses());
        assertEquals(incomes, bankBalanceDTO.getIncomes());
        assertEquals(ID, bankBalanceDTO.getBank()
                                       .getId());
        assertEquals(REVOLUT, bankBalanceDTO.getBank()
                                            .getName());
        verify(bankBalanceRepository, times(1)).findAll();
    }

    @Test
    void createNewBankBalanceById() {
        BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
        when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);
        when(monthBalanceRepository.save(any())).thenReturn(monthBalance);
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
        when(bankRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bank));

        BankBalanceDTO savedBankBalanced = service.createNewBankBalanceById(ID, ID, bankBalanceDTO);
        assertNotNull(savedBankBalanced);
        assertEquals(ID, savedBankBalanced.getId());
        assertEquals(expenses, savedBankBalanced.getExpenses());
        assertEquals(incomes, savedBankBalanced.getIncomes());
        assertEquals(ID, savedBankBalanced.getBank()
                                          .getId());
        assertEquals(REVOLUT, savedBankBalanced.getBank()
                                               .getName());
        verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
        assertNotNull(savedBankBalanced.getMonthBalanceId());
        verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
        assertNotNull(savedBankBalanced.getYearBalanceId());
        verify(yearBalanceRepository, times(1)).findById(anyLong());
        verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
        verify(bankRepository, times(1)).findById(anyLong());
    }

    @Test
    void createNewBankBalance() {
        BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
        when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);
        when(bankRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bank));
        when(monthBalanceRepository.save(any())).thenReturn(monthBalance);
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));

        BankBalanceDTO savedBankBalanceDto = service.createNewBankBalance(bankBalanceDTO);
        assertNotNull(savedBankBalanceDto);
        assertEquals(ID, savedBankBalanceDto.getId());
        assertEquals(expenses, savedBankBalanceDto.getExpenses());
        assertEquals(incomes, savedBankBalanceDto.getIncomes());
        assertEquals(ID, savedBankBalanceDto.getBank()
                                            .getId());
        assertEquals(REVOLUT, savedBankBalanceDto.getBank()
                                                 .getName());
        verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
        assertNotNull(savedBankBalanceDto.getMonthBalanceId());
        verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
        verify(bankRepository, times(1)).findById(anyLong());
        verify(yearBalanceRepository, times(1)).save(any());
        verify(yearBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveBankBalance() {
        BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
        when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);
        when(monthBalanceRepository.save(any())).thenReturn(monthBalance);
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));

        BankBalanceDTO savedBankBalanceDto = service.saveBankBalance(ID, bankBalanceDTO);
        assertNotNull(savedBankBalanceDto);
        assertEquals(savedBankBalanceDto.getId(), ID);
        verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
        assertNotNull(savedBankBalanceDto.getMonthBalanceId());
        verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
        verify(yearBalanceRepository, times(1)).findById(anyLong());
        verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
        verify(bankRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateBankBalance() {
        BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
        String result = "100";
        bankBalanceDTO.setResult(result);

        when(bankBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bankBalance));
        when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);
        when(monthBalanceRepository.save(any())).thenReturn(monthBalance);
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));

        BankBalanceDTO savedBankBalanceDto = service.updateBankBalance(ID, bankBalanceDTO);
        assertNotNull(savedBankBalanceDto);
        assertEquals(ID, savedBankBalanceDto.getId());
        assertEquals(expenses, savedBankBalanceDto.getExpenses());
        assertEquals(incomes, savedBankBalanceDto.getIncomes());
        assertEquals(ID, savedBankBalanceDto.getBank()
                                            .getId());
        assertEquals(REVOLUT, savedBankBalanceDto.getBank()
                                                 .getName());
        assertEquals(result, savedBankBalanceDto.getResult());
        verify(bankBalanceRepository, times(1)).findById(anyLong());
        verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
        verify(monthBalanceRepository, times(2)).save(any(MonthBalance.class));
        verify(yearBalanceRepository, times(1)).findById(anyLong());
        verify(yearBalanceRepository, times(2)).save(any(YearBalance.class));
        verify(bankRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteBankBalance() {
        service.deleteBankBalance(ID);
        verify(bankBalanceRepository, times(1)).findById(anyLong());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(ID));
        verify(bankBalanceRepository, times(1)).deleteById(anyLong());
    }

    private BankBalanceDTO createBankBalanceDto() {
        BankBalanceDTO bankBalanceDTO = new BankBalanceDTO();
        bankBalanceDTO.setExpenses(expenses);
        bankBalanceDTO.setIncomes(incomes);
        bankBalanceDTO.setId(ID);
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName(REVOLUT);
        bankDTO.setId(ID);
        bankBalanceDTO.setBank(bankDTO);
        bankBalanceDTO.setMonthBalanceId(monthBalance.getId());
        bankBalanceDTO.setYearBalanceId(yearBalance.getId());
        return bankBalanceDTO;
    }

}
