package com.example.DiscountBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateLinkProductDTO {

    private int duration;
    private LocalDate valideFrom;   // ✅ changé en LocalDate
    private LocalDate valideTo;     // ✅ changé en LocalDate
    private String priority;

    private Long idproduct;
    private Long idDisc;

    private Double price;
    private Double discountedPrice;
}
