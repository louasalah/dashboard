package com.example.DiscountBackend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DiscountBackend.Services.IServiceDiscDef;
import com.example.DiscountBackend.entities.DiscountDef;
import com.example.DiscountBackend.dto.ResponseMessage;


@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:55338" })
@RestController
@RequestMapping("/apie")
public class DiscDefController {
	 @Autowired
	    private IServiceDiscDef ServDiscDef;

	  @GetMapping("/discounts")
	    public List<DiscountDef> getDiscountDef() {
	        return ServDiscDef.getDiscountDef();
	    }
	  @GetMapping("/discounts/{id}")
	    public DiscountDef getDiscountDefById(@PathVariable Long id) {
	        return ServDiscDef.getDiscDefById(id);
	    }

	    @PutMapping("/discounts/{id}")
	    public ResponseEntity<DiscountDef> updateDiscountDef(@PathVariable Long id, @RequestBody DiscountDef discountDef) {
	       
	            DiscountDef existingDiscount = ServDiscDef.getDiscDefById(id);
	            existingDiscount.setRefDisc(discountDef.getRefDisc());
	            existingDiscount.setType(discountDef.getType());
	            existingDiscount.setValeur(discountDef.getValeur());
	     

	            DiscountDef updatedDiscount = ServDiscDef.updateDiscountDef(existingDiscount);

	            return ResponseEntity.ok(updatedDiscount);
	    }

	    @DeleteMapping("/discounts/{id}")
	    public void deleteDiscountDef(@PathVariable Long id) {
	    	ServDiscDef.deleteDiscountDef(id);
	    }
	    @GetMapping("/discounts/applicable/{price}")
	    public ResponseEntity<List<DiscountDef>> getApplicableDiscounts(@PathVariable Double price) {
	        List<DiscountDef> discounts = ServDiscDef.getApplicableDiscounts(price);
	        return ResponseEntity.ok(discounts);
	    }

	    @PostMapping("/discounts/addDisc")
	    public ResponseEntity<ResponseMessage> addDiscountDef(@RequestBody DiscountDef discountDef) {
	        if (ServDiscDef.existsByRefDisc(discountDef.getRefDisc())) {
	            return ResponseEntity.badRequest().body(new ResponseMessage("La référence de remise existe déjà."));
	        }

	        try {
	            DiscountDef newDiscount = new DiscountDef();
	            newDiscount.setRefDisc(discountDef.getRefDisc());
	            newDiscount.setType(discountDef.getType());
	            newDiscount.setValeur(discountDef.getValeur());
	       

	            ServDiscDef.addDiscount(newDiscount);

	            return ResponseEntity.status(200).body(new ResponseMessage("Remise ajoutée avec succès."));
	            
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(new ResponseMessage("Erreur interne du serveur. Veuillez réessayer plus tard."));
	        }
	    }
	    





}

