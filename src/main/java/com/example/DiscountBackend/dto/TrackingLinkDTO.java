package com.example.DiscountBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingLinkDTO {

    private Long linkId;
    private boolean active;
    private int duration;
    private LocalDate valideFrom; // ✅ Corrigé
    private LocalDate valideTo;   // ✅ Corrigé
    private String priority;

    private Long productId;
    private String productName;
    private Long discountId;
    private Double discountedPrice;

    private Long trackingId;
    private Long idProduct;
    private String pageName;

    private Long timeSpent;
    private Integer clicks;
    private String sessionId;

    private Double latitude;
    private Double longitude;

    private Date entryTimeFormatted;
    private String categorieNAme;
}
