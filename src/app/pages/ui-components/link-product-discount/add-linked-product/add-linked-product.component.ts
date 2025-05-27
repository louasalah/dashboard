import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { DiscountService } from '../../discount/discount.service';
import { ProductService } from '../../products/product.service';

export interface Product {
  idproduct: number;
  referenceProduct: string;
  description: string;
  comment: string;
  price: number;
  quantiteDiscount: number;
  image: string;
  status: 'in-stock' | 'out-of-stock' | 'low-stock';
  id_category: number;
  categorie: {
    id: number;
    nom: string;
  };
}

export interface Discount {
  idDisc: number;
  refDisc: string;
  type: string;
  valeur: number;
  idLink: number;
}

@Component({
  selector: 'app-add-linked-product',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,
    MatCheckboxModule
  ],
  templateUrl: './add-linked-product.component.html',
  styleUrls: ['./add-linked-product.component.scss']
})
export class AddLinkedProductComponent implements OnInit {
  products: Product[] = [];
  discounts: Discount[] = [];
  selectedProduct: Product | null = null;
  selectedDiscount: Discount | null = null;

  linkData = {
    productId: null as number | null,
    discountId: null as number | null,
    active: false,
    jour: 7, // Default 7 days
    valideFrom: new Date(),
    valideTo: new Date(new Date().setDate(new Date().getDate() + 7)), // Default 7 days from now
    priority: 'medium' // Default priority
  };

  constructor(
    private productService: ProductService,
    private discountService: DiscountService,
    public dialogRef: MatDialogRef<AddLinkedProductComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit(): void {
    // Use products and discounts passed from parent component
    if (this.data) {
      if (this.data.products) {
        this.products = this.data.products;
        console.log('Products loaded from parent:', this.products);
      }
      
      if (this.data.discounts) {
        this.discounts = this.data.discounts;
        console.log('Discounts loaded from parent:', this.discounts);
      }
    }
    this.getProducts()
    this.getDiscounts()
  }

  getProducts(): void {
    this.productService.getProducts().subscribe(
      (data: Product[]) => {
        this.products = data;  // Assign the fetched data to the products array
        console.log( this.products )

      },
      error => {
        console.error('Error fetching products:', error);
      }
    );
  }
  
    getDiscounts(): void {
      this.discountService.getDiscountProduct().subscribe(
        (data: Discount[]) => {
          console.log('Fetched Discounts:', data);  // Log data to check if all rows are being returned
          this.discounts = data;  // Assign the fetched data to the discounts array
        },
        // error => {
        // //   console.error('Error fetching discounts:', error);
        // }
      );
    }
  selectProduct(event: any): void {
    const productId = typeof event === 'object' ? event.value : event;
    this.linkData.productId = productId;
    this.selectedProduct = this.products.find(p => p.idproduct === productId) || null;
    console.log('Selected product:', this.selectedProduct);
  }

  selectDiscount(event: any): void {
    const discountId = typeof event === 'object' ? event.value : event;
    this.linkData.discountId = discountId;
    this.selectedDiscount = this.discounts.find(d => d.idDisc === discountId) || null;
    console.log('Selected discount:', this.selectedDiscount);
  }

  isFormValid(): boolean {
    return !!(
      this.linkData.productId &&
      this.linkData.discountId &&
      this.linkData.jour &&
      this.linkData.valideFrom &&
      this.linkData.valideTo &&
      this.linkData.priority
    );
  }

  onSubmit(): void {
    if (this.isFormValid()) {
      // Format dates to ISO string (or your preferred format)
      const formData = {
        ...this.linkData,
        valideFrom: this.formatDate(this.linkData.valideFrom),
        valideTo: this.formatDate(this.linkData.valideTo)
      };
      this.dialogRef.close(formData);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  // Helper method to format dates
  private formatDate(date: Date): string {
    return date.toISOString().split('T')[0]; // Returns YYYY-MM-DD format
  }
}