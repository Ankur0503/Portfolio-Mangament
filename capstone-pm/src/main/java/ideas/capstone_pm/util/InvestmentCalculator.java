package ideas.capstone_pm.util;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class InvestmentCalculator {
    public Double calculateCurrentValue(Double initialInvestment, Double fundAnnualReturn, Date initialDate) {
        Date currentDate = new Date();
        long timeDiff = currentDate.getTime() - initialDate.getTime();
        long daysElapsed = TimeUnit.MILLISECONDS.toDays(timeDiff);
        return initialInvestment * Math.pow((1 + (fundAnnualReturn / 100)), (daysElapsed / 365.0));
    }
}
