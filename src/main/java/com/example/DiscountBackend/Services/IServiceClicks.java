package com.example.DiscountBackend.Services;

public interface IServiceClicks {
	Integer getClickCount(Long idproduct);
	void incrementClickCount(Long idproduct);
}
