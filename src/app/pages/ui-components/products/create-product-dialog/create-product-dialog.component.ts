import { Component, Inject, OnInit } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../product.service';

import { CommonModule } from '@angular/common';
import { Category } from '../category.data';



@Component({
  selector: 'app-create-product-dialog',
  standalone: true,
  styleUrls: ['./create-product-dialog.component.scss'],
  templateUrl: './create-product-dialog.component.html',
  imports: [
    MatLabel,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    MatDialogActions,
    MatDialogTitle,
    MatDialogContent,
    MatButton,
     MatOptionModule,
     MatSelectModule,
         FormsModule, 
        CommonModule
    // same modules as edit component
  ]
})
export class CreateProductDialogComponent implements OnInit  {
  form: FormGroup;
categories: Category[] = [];


  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CreateProductDialogComponent>,
    private productService: ProductService
  ) {
    this.form = this.fb.group({
      referenceProduct: [''],
      description: [''],
      comment: [''],
      price: [0],
      quantiteDiscount: [0],
      image: [''],
       categorie: [null] 
      
    });
  }
    ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.productService.getAllCategories().subscribe((data: Category[]) => {
      this.categories = data;
    });
  }

 validationError: string | null = null;

submit() {
  if (this.form.valid) {
    this.productService.createProduct(this.form.value).subscribe({
      next: (res) => {
        this.dialogRef.close(res); // fermer si succès
      },
      error: (err) => {
        // 🔍 Vérifie si le backend renvoie un message d'erreur
        if (err.error?.message?.includes('référence de Produit existe déjà')) {
          this.validationError = 'La référence de produit existe déjà.';
        } else {
          this.validationError = err.error?.message || 'Erreur lors de l\'ajout du produit.';
        }
      }
    });
  }
}


  cancel() {
    this.dialogRef.close();
  }
   // Méthode pour gérer le changement de type
  onCategoryChange(event: any): void {
    console.log("Selected Category: ", event.value);
  }
}
