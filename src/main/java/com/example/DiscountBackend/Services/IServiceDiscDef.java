package com.example.DiscountBackend.Services;

import com.example.DiscountBackend.entities.DiscountDef;

import java.util.List;

public interface IServiceDiscDef {
    void addDiscount(DiscountDef discountDef);  

    List<DiscountDef> getDiscountDef();

    DiscountDef getDiscDefById(Long id);

    DiscountDef updateDiscountDef(DiscountDef discountDef);

    void deleteDiscountDef(Long id);
    
    List<DiscountDef> getApplicableDiscounts(Double price);
    
    boolean existsByRefDisc(String refDisc);

}
