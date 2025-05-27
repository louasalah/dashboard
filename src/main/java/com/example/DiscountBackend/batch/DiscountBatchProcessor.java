package com.example.DiscountBackend.batch;

import com.example.DiscountBackend.entities.LinkProdDisc;
import com.example.DiscountBackend.Repository.LinkDiscDefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DiscountBatchProcessor {

    @Autowired
    private LinkDiscDefRepository linkProdDiscRepository;

    @Scheduled(cron = "0 * * * * *") // Chaque minute
    @Transactional
    public void processDiscounts() {
        LocalDate currentDate = LocalDate.now();
        List<LinkProdDisc> allDiscounts = linkProdDiscRepository.findAll();

        Map<Long, List<LinkProdDisc>> discountsByProduct = allDiscounts.stream()
                .collect(Collectors.groupingBy(d -> d.getProduct().getIdproduct()));

        discountsByProduct.forEach((productId, discounts) -> processProductDiscounts(discounts, currentDate));

        linkProdDiscRepository.saveAll(allDiscounts);
    }

    private void processProductDiscounts(List<LinkProdDisc> discounts, LocalDate currentDate) {
        // 1. Désactiver toutes les remises
        discounts.forEach(d -> d.setActive(false));

        // 2. Filtrer celles qui sont valides aujourd'hui
        List<LinkProdDisc> validDiscounts = discounts.stream()
                .filter(d -> isDiscountValid(d, currentDate))
                .sorted((a, b) -> getPriorityValue(b.getPriority()) - getPriorityValue(a.getPriority()))
                .collect(Collectors.toList());

        // 3. Activer la plus prioritaire
        if (!validDiscounts.isEmpty()) {
            LinkProdDisc best = validDiscounts.get(0);
            best.setActive(true);

            double originalPrice = best.getPrice();
            double discountValue = best.getDiscountDef().getValeur();

            if ("percentage".equalsIgnoreCase(best.getDiscountDef().getType())) {
                best.setDiscountedPrice(originalPrice - (originalPrice * discountValue / 100));
            } else if ("fixed".equalsIgnoreCase(best.getDiscountDef().getType())) {
                best.setDiscountedPrice(originalPrice - discountValue);
            }
        }
    }

    private boolean isDiscountValid(LinkProdDisc discount, LocalDate currentDate) {
        return discount.getValideFrom() != null && discount.getValideTo() != null
                && ( !currentDate.isBefore(discount.getValideFrom()) )
                && ( !currentDate.isAfter(discount.getValideTo()) );
    }

    private int getPriorityValue(String priority) {
        if (priority == null) return 0;

        return switch (priority.toUpperCase()) {
            case "HIGH" -> 3;
            case "MEDIUM" -> 2;
            case "LOW" -> 1;
            default -> 0;
        };
    }
}
