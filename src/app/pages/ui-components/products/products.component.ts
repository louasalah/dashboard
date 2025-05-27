import { Component, OnInit } from '@angular/core';
import { MainTableComponent } from "../main-table/main-table.component";
import { Product } from "./product.data";
import { ProductService } from './product.service';
import {MatDialog} from "@angular/material/dialog";
import {EditProductDialogComponent} from "./edit-product-dialog/edit-product-dialog.component";
import {CreateProductDialogComponent} from "./create-product-dialog/create-product-dialog.component";
import {MatButton} from "@angular/material/button";  // Import ProductService
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [MainTableComponent, MatButton,    CommonModule,
],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  errorMessage: string | null = null; // Start with an empty array
  productTableConfig = {
    dataSource: this.products,  // Bind to dynamic products array
    showMenu: true,
    menuItems: [
      { icon: 'edit', label: 'Edit', action: (product: Product) => this.editProduct(product) },
      { icon: 'delete', label: 'Delete', action: (product: Product) => this.deleteProduct(product)},

    ],
    columns: [
      {
        name: 'product',
        header: 'Produit',
        headerClass: 'font-bold',
        cellClass: 'flex items-center',
        cell: (product: Product) => `
          <div class="flex items-center gap-4">
            <img src="${product.image}" width="60" class="rounded" alt="pas image"/>
            <div>
              <div class="f-s-14 f-w-600">${product.description}</div>
              <div class="f-s-14 f-w-600">Ref: ${product.referenceProduct}</div>
              <div class="f-s-14 f-w-600">ID: ${product.idproduct}</div>
            </div>
          </div>
        `
      },
      {
        name: 'category',
        header: 'Catégorie',
         cell: (product: Product) => product.categorie?.nom || 'pas catégorie'
      },
      {
        
         name: 'price',
         header: 'Prix',
         cell: (product: Product) =>
    typeof product.price === 'number' ? `${product.price.toFixed(2)} DT` : 'Prix non défini'


      },
      {
        name: 'discount',
        header: 'Quantiteremise',
        cell: (product: Product) => `${product.quantiteDiscount}`
      },
      // {
      //   name: 'status',
      //   header: 'Status',
      //   cell: (product: Product) => {
      //     if (product.status === 'in-stock') {
      //       return `<span class="bg-light-success text-success rounded f-w-600 p-6 p-y-4 f-s-12">In Stock</span>`;
      //     } else if (product.status === 'out-of-stock') {
      //       return `<span class="bg-light-error text-error rounded f-w-600 p-6 p-y-4 f-s-12">Out of Stock</span>`;
      //     } else {
      //       return `<span class="bg-light-warning text-warning rounded f-w-600 p-6 p-y-4 f-s-12">Low Stock</span>`;
      //     }
      //   }
      // },
      {
        name: 'comment',
        header: 'Commentaire',
        cell: (product: Product) => product.comment || 'pas commentaire'
      }
    ]
  };

  constructor(private productService: ProductService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.getProducts();  // Fetch data when the component initializes
  }

  getProducts(): void {
    this.productService.getProducts().subscribe(
      (data: Product[]) => {
        this.products = data;  // Assign the fetched data to the products array
        console.log( this.products )

        this.productTableConfig.dataSource = this.products;  // Update the table with new data
      },
      error => {
        console.error('Error fetching products:', error);
      }
    );
  }

  editProduct(product: Product) {
    const dialogRef = this.dialog.open(EditProductDialogComponent, {
      width: '500px',
      data: { ...product }
    });

    dialogRef.afterClosed().subscribe(result => {
      
      if (result) {
        this.productService.updateProduct(result.idproduct, result).subscribe(
          updated => {
            const index = this.products.findIndex(p => p.idproduct === updated.idproduct);
            if (index !== -1) {
              this.products[index] = updated;
              this.productTableConfig.dataSource = [...this.products]; // Refresh table binding
            }
          },
          error => {
            console.error('Update failed', error);
          }
        );
      }
    });
  }
  openCreateProductDialog() {
    const dialogRef = this.dialog.open(CreateProductDialogComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result ) {
        this.productService.createProduct(result).subscribe(
          newProduct => {
            this.products.push(newProduct);
            this.getProducts();
            this.productTableConfig.dataSource = [...this.products]; // Refresh table
            this.errorMessage = null;
          },
         error => {
          console.error('Create failed', error);
          this.errorMessage = error.error?.message || 'Référence existe déja.';
        }
      );
    }
  });
}
  deleteProduct(product: Product) {
    this.productService.deleteProduct(product.idproduct).subscribe({
    next: () => {
    this.products = this.products.filter(p => p.idproduct !== product.idproduct);
    this.productTableConfig.dataSource = [...this.products]; // Met à jour la table
    console.log('Deleted product:', product);
    },
    error: (error) => {
    console.error('Delete failed', error);
    }
    });
    }
  
}
