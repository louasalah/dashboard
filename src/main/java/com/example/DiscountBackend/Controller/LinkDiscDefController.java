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
import com.example.DiscountBackend.Services.ISderviceLinkDiscDef;
import com.example.DiscountBackend.dto.CreateLinkProductDTO;
import com.example.DiscountBackend.entities.LinkProdDisc;
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:55338" })

@RestController
@RequestMapping("/aapiee")
public class LinkDiscDefController {
	 @Autowired
	    private ISderviceLinkDiscDef servLPD;
	    @PostMapping("/add")
	    public LinkProdDisc addLink(@RequestBody CreateLinkProductDTO LinkData) {
	      return  servLPD.addLinkProdDisc(LinkData); 
	    }

	    @GetMapping("/getlinks")
	    public List<LinkProdDisc> getLink() {
	        List<LinkProdDisc> links = servLPD.getLinks();
	        return links;
	    }
	    @PutMapping("/update/{id}")
	    public ResponseEntity<String> updateLink(
	            @PathVariable Long id,
	            @RequestBody LinkProdDisc updatedData) {
	        return servLPD.updateLink(id, updatedData);
	    }
	    
	    
	    @PutMapping("/prolonger/{id}")
	    public ResponseEntity<String> prolongerLien(
	            @PathVariable Long id,
	            @RequestBody  LinkProdDisc request) {
	    	System.out.println("test"+request.toString());
	    	
	        try {
	            String message = servLPD.prolongerLien(id, request.getJours(), request.isActive());
	            return ResponseEntity.ok(message);
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(404).body(e.getMessage());
	        }
	    }
	    
	    @DeleteMapping("/links/{idLink}")
	    public ResponseEntity<Void> deleteProduct(@PathVariable Long idLink) {
	    	servLPD.deleteLink(idLink);
	        return ResponseEntity.noContent().build();
	    }

	    // Endpoint to get the most recent 50% discount or most recent discount if no 50% exists
	    @GetMapping("/remise-recente-preferee")
	    public ResponseEntity<?> getMostRecentDiscountPreferFiftyPercent() {
	        LinkProdDisc discount = servLPD.getMostRecentDiscountPreferFiftyPercent();
	        if (discount == null) {
	            return ResponseEntity.status(404).body("Aucune remise trouvée");
	        }
	        return ResponseEntity.ok(discount);
	    }

}
