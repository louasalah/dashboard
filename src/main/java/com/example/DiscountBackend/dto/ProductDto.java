package com.example.DiscountBackend.dto;

import java.util.List;

import com.example.DiscountBackend.entities.Categorie;
import com.example.DiscountBackend.entities.LinkProdDisc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long idproduct;

    private String referenceProduct;

    private String description;

    private String comment;

    private Double price;

    private Integer quantiteDiscount;

    private String image;

    private Categorie categorie;

    private List<LinkProdDisc> linkProdDiscs;

}
