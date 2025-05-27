package com.example.DiscountBackend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponTracking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String couponCode;
    private String productReference;
    private String status;
    private Long idLink;
    @Column(nullable = false)
    private LocalDateTime sentDate;
}
