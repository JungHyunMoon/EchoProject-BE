package com.sportsecho.hotdeal.scheduler;

import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.product.entity.Product;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotdealScheduler {

    private final HotdealRepository hotdealRepository;

    // 매분마다 시행
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void deleteClosedHotdeal() {
        LocalDateTime now = LocalDateTime.now();
        List<Hotdeal> expiredHotdeals = hotdealRepository.findAllByDueDayBefore(now);

        if (!expiredHotdeals.isEmpty()) {
            log.info("마감시간이 지난 HOTDEAL {}개를 삭제하겠습니다.", expiredHotdeals.size());
            hotdealRepository.deleteAll(expiredHotdeals);
            hotdealRepository.flush();
            List<Hotdeal> expiredHotdealsForCheck = hotdealRepository.findAllByDueDayBefore(now);
            log.info(String.valueOf(expiredHotdealsForCheck.size()));
        }

        List<Hotdeal> hotdealsWithZeroQuantity = hotdealRepository.findAllByDealQuantity(0);

        if (!hotdealsWithZeroQuantity.isEmpty()) {
            log.info("한정수령이 모두 판매된 Hotdeal {}개를 삭제하겠습니다.", hotdealsWithZeroQuantity.size());
            hotdealRepository.deleteAll(hotdealsWithZeroQuantity);
            hotdealRepository.flush();
        }
    }

}
