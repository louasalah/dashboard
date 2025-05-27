import { Component, OnInit } from '@angular/core';
import { MainTableComponent } from "../main-table/main-table.component";
import { DiscountService } from './discount.service';
import { EditDiscountDialogComponent } from './edit-discount-dialog/edit-discount-dialog.component';

import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Discount } from './dicount.data';
import { AddDiscountDialogComponent } from './add-discount-dialog/add-discount-dialog.component';

@Component({
  selector: 'app-discount',
  standalone: true,
  imports: [MainTableComponent, MatButtonModule, MatIconModule],
  templateUrl: './discount.component.html',
  styleUrls: ['./discount.component.scss']
})
export class DiscountComponent implements OnInit {
  discounts: Discount[] = [];  // Start with an empty array

  // Table configuration
  discountTableConfig = {
    title: 'Discount Management',
    dataSource: this.discounts,  // Bind to dynamic discounts array
    showMenu: true,
    menuItems: [
      { icon: 'edit', label: 'Edit', action: (discount: Discount) => this.editDiscount(discount) },
      { icon: 'delete', label: 'Delete', action: (discount: Discount) => this.deleteDiscount(discount)},

    ],
    columns: [
      {
        name: 'reference',
        header: 'RefRemise',
        headerClass: 'font-bold',
        cell: (discount: Discount) => `
          <div class="flex flex-col">
            <div class="f-s-14 f-w-600">${discount.refDisc}</div>
            <div class="text-sm text-gray-500">ID: ${discount.idDisc}</div>
          </div>
        `,
        cellClass: ''
      },
      {
        name: 'type',
        header: 'Type',
        cell: (discount: Discount) => `
          <span class="rounded f-w-600 p-6 p-y-4 f-s-12
            ${discount.type === 'Percentage' ? 'bg-light-info text-info' :
            discount.type === 'Fixed Amount' ? 'bg-light-warning text-warning' :
            'bg-light-success text-success'}">
            ${discount.type}
          </span>
        `,
        cellClass: ''
      },
      {
        name: 'value',
        header: 'Valeur remise',
        cell: (discount: Discount) => `
          <div class="f-s-14 f-w-600">
          ${discount.valeur}
          </div>
        `,
        cellClass: ''
      },
    ]
  };

  constructor(private discountService: DiscountService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.getDiscounts();  // Fetch data when the component initializes
  }

  getDiscounts(): void {
    this.discountService.getDiscountProduct().subscribe(
      (data: Discount[]) => {
        console.log('Fetched Discounts:', data);  // Log data to check if all rows are being returned
        this.discounts = data;  // Assign the fetched data to the discounts array
        this.discountTableConfig.dataSource = this.discounts;  // Update the table with new data
      },
      error => {
        console.error('Error fetching discounts:', error);
      }
    );
  }

  createDiscount() {
    const dialogRef = this.dialog.open(AddDiscountDialogComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.discountService.createDiscount(result).subscribe(
          newDiscount => {
            this.discounts.push(newDiscount);
            this.discountTableConfig.dataSource = [...this.discounts]; // Refresh table binding
            console.log('New discount created:', newDiscount);
          },
          error => {
            console.error('Creation failed', error);
          }
        );
      }
    });
  }

  editDiscount(discount: Discount) {
    const dialogRef = this.dialog.open(EditDiscountDialogComponent, {
      width: '500px',
      data: { ...discount }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.discountService.updateDiscount(result.idDisc, result).subscribe(
          updated => {
            const index = this.discounts.findIndex(p => p.idDisc === updated.idDisc);
            if (index !== -1) {
              this.discounts[index] = updated;
              this.discountTableConfig.dataSource = [...this.discounts]; // Refresh table binding
            }
          },
          error => {
            console.error('Update failed', error);
          }
        );
      }
    });
  }

  deleteDiscount(discount: Discount) {
    this.discountService.deleteProduct(discount.idDisc).subscribe({
      next: () => {
        // Retirer l'élément supprimé de la liste
        this.discounts = this.discounts.filter(p => p.idDisc !== discount.idDisc);
        // Mettre à jour la source de données de la table
        this.discountTableConfig.dataSource = [...this.discounts];
        console.log('Deleted discount:', discount);
      },
      error: (error) => {
        console.error('Delete failed', error);
      }
    });
  }
}