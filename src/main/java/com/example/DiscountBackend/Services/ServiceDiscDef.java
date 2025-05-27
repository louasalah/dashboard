package com.example.DiscountBackend.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DiscountBackend.Repository.DiscDefRepository;
import com.example.DiscountBackend.entities.DiscountDef;

@Service
public class ServiceDiscDef implements IServiceDiscDef{
	 @Autowired
	    private DiscDefRepository discRep;
	 
	    @Override
	    public void addDiscount(DiscountDef discountDef) {
	         discRep.save(discountDef); 
	    }
 
	 @Override
	    public List<DiscountDef> getDiscountDef() {
	        return discRep.findAll();
	    }
	 @Override
    public DiscountDef getDiscDefById(Long id) {
    	 return discRep.findById(id).get();
    }
    
    @Override
    public DiscountDef updateDiscountDef(DiscountDef DiscountDef) {
        return discRep.save(DiscountDef);
    }
    @Override
    public void deleteDiscountDef(Long id) {
    	discRep.deleteById(id);
    }
    @Override
    public List<DiscountDef> getApplicableDiscounts(Double price) {
        return discRep.findApplicableDiscounts(price);
    }

	@Override
	public boolean existsByRefDisc(String refDisc) {
		return discRep.existsByRefDisc(refDisc);

	}
}
