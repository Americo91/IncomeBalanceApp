package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import static com.astoppello.incomebalanceapp.services.YearBalanceService.YEAR_BALANCE_NOT_FOUND;

/**
 * Created by @author stopp on 20/12/2020
 */
@Service
public class MonthBalanceServiceImpl implements MonthBalanceService {

    private final MonthBalanceRepository monthBalanceRepository;
    private final MonthBalanceMapper monthBalanceMapper;
    private final YearBalanceRepository yearBalanceRepository;

    public MonthBalanceServiceImpl(
            MonthBalanceRepository monthBalanceRepository,
            MonthBalanceMapper monthBalanceMapper,
            YearBalanceRepository yearBalanceRepository) {
        this.monthBalanceRepository = monthBalanceRepository;
        this.monthBalanceMapper = monthBalanceMapper;
        this.yearBalanceRepository = yearBalanceRepository;
    }

    @Override
    public List<MonthBalanceDTO> findByMonth(String month) {
        return monthBalanceRepository.findAll().stream()
                                     .filter(monthBalance -> month.equals(monthBalance.getMonth()))
                                     .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
                                     .collect(Collectors.toList());
    }

    @Override
    public MonthBalanceDTO createNewMonthBalanceById(Long yearBalanceId, MonthBalanceDTO monthBalanceDTO) {
        MonthBalance monthBalance = monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO);
        yearBalanceRepository.findById(yearBalanceId).ifPresent(monthBalance::setYearBalance);
        return createAndReturnDto(monthBalance);
    }

    private MonthBalanceDTO createAndReturnDto(MonthBalance monthBalance) {
        MonthBalance savedMonthBalance = monthBalanceRepository.save(monthBalance);
        if (savedMonthBalance.getYearBalance() != null) {
            savedMonthBalance.getYearBalance().addMonthBalance(savedMonthBalance);
            yearBalanceRepository.save(savedMonthBalance.getYearBalance());
        }
        return monthBalanceMapper.monthBalanceToMonthBalanceDto(savedMonthBalance);
    }

    @Override
    public List<MonthBalanceDTO> findAllById(Long yearBalanceId) {
        return CollectionUtils
                .emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
                .stream()
                .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
                .collect(Collectors.toList());
    }

    @Override
    public MonthBalanceDTO findMonthOfYearById(Long yearBalanceId, Long monthBalanceId) {
        return CollectionUtils
                .emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
                .stream()
                .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
                .findFirst()
                .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
                .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + monthBalanceId));
    }


    @Override
    public List<MonthBalanceDTO> findAll() {
        return monthBalanceRepository
                .findAll()
                .stream()
                .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
                .collect(Collectors.toList());
    }

    @Override
    public MonthBalanceDTO findById(Long id) {
        return monthBalanceRepository
                .findById(id)
                .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
                .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + id));
    }

    @Override
    public MonthBalanceDTO createNewMonthBalance(MonthBalanceDTO monthBalanceDTO) {
        MonthBalance monthBalance = monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO);
        yearBalanceRepository.findById(monthBalanceDTO.getYearBalanceId()).ifPresent(monthBalance::setYearBalance);
        return createAndReturnDto(monthBalance);
    }

    private YearBalance getYearBalanceById(Long yearBalanceId) {
        return yearBalanceRepository
                .findById(yearBalanceId)
                .orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + yearBalanceId));
    }
}
