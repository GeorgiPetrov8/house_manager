package org.example.Schedual;

import org.example.Enum.StatusType;
import org.example.entity.Apartments;
import org.example.entity.Fees;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FeeScheduler {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void scheduleMonthlyFeeCreation(Apartments apartment) {
        executorService.scheduleAtFixedRate(() -> {createNewFee(apartment);}, 0, 30, TimeUnit.DAYS);
        System.out.println("Scheduled new fee");
    }

    private void createNewFee(Apartments apartment) {
        Fees newFee = new Fees(apartment, new BigDecimal(10.0), new BigDecimal(5.0), new BigDecimal(3.0), StatusType.NOT_PAID);
    }

    public void shutdownScheduler() {
        executorService.shutdown();
    }
}
