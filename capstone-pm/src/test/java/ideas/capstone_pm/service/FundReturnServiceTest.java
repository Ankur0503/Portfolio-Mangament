package ideas.capstone_pm.service;

import ideas.capstone_pm.entity.FundReturn;
import ideas.capstone_pm.repository.FundReturnRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundReturnServiceTest {

    @Mock
    FundReturnRepository fundReturnRepository;

    @InjectMocks
    FundReturnService fundReturnService;

    FundReturn fundReturn;

    @BeforeEach
    public void setUp() {
        fundReturn = new FundReturn();
        fundReturn.setFundReturnId(1);
        fundReturn.setFundReturn1Month(12.34);
        fundReturn.setFundReturn1Year(24.56);
        fundReturn.setFundReturn3Year(36.78);
    }

    @Test
    public void shouldAddFundReturn() {
        when(fundReturnRepository.save(any(FundReturn.class))).thenReturn(fundReturn);

        FundReturn savedFundReturn = fundReturnService.addFundReturn(fundReturn);

        verify(fundReturnRepository, times(1)).save(any(FundReturn.class));

        assertNotNull(savedFundReturn);
        assertEquals(fundReturn.getFundReturnId(), savedFundReturn.getFundReturnId());
        assertEquals(fundReturn.getFundReturn1Month(), savedFundReturn.getFundReturn1Month());
    }

    @Test
    public void shouldGetAllFundReturns() {
        when(fundReturnRepository.findAll()).thenReturn(Collections.singletonList(fundReturn));

        List<FundReturn> fundReturns = fundReturnService.getAllFundReturns();

        verify(fundReturnRepository, times(1)).findAll();

        assertNotNull(fundReturns);
        assertEquals(1, fundReturns.size());
        assertEquals(fundReturn.getFundReturnId(), fundReturns.get(0).getFundReturnId());
    }
}
