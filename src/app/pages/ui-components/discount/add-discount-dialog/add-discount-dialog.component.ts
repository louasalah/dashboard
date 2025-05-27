import { Component } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { Discount } from '../dicount.data';
import { DiscountService } from '../discount.service';

@Component({
  selector: 'app-add-discount-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule,
   
  ],
  templateUrl: './add-discount-dialog.component.html',
  styleUrls: ['./add-discount-dialog.component.scss']
})
export class AddDiscountDialogComponent {
  discount: Discount = {
    idDisc: 0, // Sera défini par le backend
    refDisc: '',
    type: 'pourcentage', // Valeur par défaut
    valeur: 0,
    idLink: 0
  };

  discountTypes = ['pourcentage', 'Fixe', 'Other'];
  
  // Variable pour suivre les erreurs de validation
  validationError: string | null = null;

  constructor(
    public dialogRef: MatDialogRef<AddDiscountDialogComponent>,
    private discountService:DiscountService
  ) {}

  onCancel(): void {
    this.dialogRef.close();
  }

  // Méthode pour valider la remise
  validateDiscount(): boolean {
    // Vérifier si le type est Percentage et la valeur > 100
    if (this.discount.type === 'pourcentage' && this.discount.valeur > 100) {
      this.validationError = 'Le pourcentage ne peut pas dépasser 100%';
      return false;
    }
    
    // Réinitialiser l'erreur si la validation réussit
    this.validationError = null;
    return true;
  }

  // Méthode pour gérer le changement de type
  onTypeChange(): void {
    // Réinitialiser l'erreur quand le type change
    this.validationError = null;
    
    // Si le type passe à Percentage et la valeur est > 100, réinitialiser à 100
    if (this.discount.type === 'pourcentage' && this.discount.valeur > 100) {
      this.discount.valeur = 100;
    }
  }

  onSave(): void {
    if (this.validateDiscount()) {
     this.discountService.createDiscount(this.discount).subscribe({
    next: (res) => {
      this.dialogRef.close(res); // Ferme le dialogue si OK
    },
    error: (err) => {
      // Gestion de l'erreur si la référence existe déjà
      if (err.error?.message?.includes('existe déjà')) {
        this.validationError = 'La référence existe déjà.';
      } else {
        this.validationError = 'Erreur lors de l\'ajout de la remise.';
      }
    }
  });
}
  }}