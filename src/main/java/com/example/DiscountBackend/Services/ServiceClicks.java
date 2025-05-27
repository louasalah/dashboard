package com.example.DiscountBackend.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DiscountBackend.Repository.ClickRepository;
import com.example.DiscountBackend.entities.Click;

@Service
public class ServiceClicks {
    @Autowired
    private ClickRepository clickRepository;

    // Récupérer le nombre de clics pour un produit
    public Integer getClickCount(Long idproduct) {
        Optional<Click> clickOpt = clickRepository.findByIdproduct(idproduct);
        return clickOpt.map(Click::getClicks).orElse(0);  // Si aucune , retourner 0
    }

    // Incrémenter le nombre de clics pour un produit
    public void incrementClickCount(Long idproduct) {
        Optional<Click> clickOpt = clickRepository.findByIdproduct(idproduct);
        Click click = clickOpt.orElseGet(() -> {
            Click newClick = new Click();
            newClick.setIdproduct(idproduct);
            newClick.setClicks(0);
            return newClick;
        });

        click.setClicks(click.getClicks() + 1);
        clickRepository.save(click);
    }

}
