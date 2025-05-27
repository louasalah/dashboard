package com.example.DiscountBackend.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DiscountBackend.entities.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
