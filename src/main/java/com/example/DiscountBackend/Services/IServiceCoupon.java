package com.example.DiscountBackend.Services;

import com.example.DiscountBackend.entities.Coupon;

import jakarta.mail.MessagingException;

public interface IServiceCoupon {
	 void autoSendCoupons();

	void sendCouponEmail(String email, String couponCode) throws MessagingException;
	 
}
