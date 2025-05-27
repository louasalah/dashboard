import { Component, OnInit } from '@angular/core';
import { MainTableComponent } from "../main-table/main-table.component";
import { ProductDiscountLink } from "./linkProduct.data";
import { LinkProductService } from './link-product.service';
import { MatDialog } from "@angular/material/dialog";
import { EditLinkedProductComponent } from "./edit-linked-product/edit-linked-product.component";
import { AddLinkedProductComponent } from "./add-linked-product/add-linked-product.component"; // Import the new component
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-link-product-discount',
  standalone: true,
  imports: [MainTableComponent,
        MatIconModule,
  ],
  templateUrl: './link-product-discount.component.html',
  styleUrls: ['./link-product-discount.component.scss']
})
export class LinkProductDiscountComponent implements OnInit {
  discountLinks: ProductDiscountLink[] = [];  // Start with an empty array
  discountLinkTableConfig = {
    title: 'Product Discount Links',
    dataSource: this.discountLinks,  // Bind to dynamic discountLinks array
    showMenu: true,
    addButton: true, // Add a button to create new links
    onAddClick: () => this.addNewLink(), // Add handler for the add button
    menuItems: [
      { icon: 'edit', label: 'Edit', action: (link: ProductDiscountLink) => this.editLink(link) },
      { icon: 'delete', label: 'Delete', action: (link: ProductDiscountLink) => this.deleteLink(link) },
    ],
    columns: [
      {
        name: 'link',
        header: 'Remise',
        headerClass: 'font-bold',
        cellClass: 'flex items-center',
        cell: (link: ProductDiscountLink) => `
   
               
                     <div class="f-s-14 f-w-600">ID: ${link.idLink}</div>
                   </div>
                 </div>
               `
             },
      {
                     name: 'referenceProduct',
                     header: 'RefProduit',
                     cell: (link: ProductDiscountLink) => `${link.referenceProduct}`
      },
      {
        name: 'refDiscount',
        header: 'RefRemise',
        cell: (link: ProductDiscountLink) => `${link. refDiscount}`
},
     
      {
        name: 'price',
        header: 'Prix réduit',
        cell: (link: ProductDiscountLink) => `${link.discountedPrice.toFixed(2)} DT`
      },
      {
        name: 'validity',
        header: 'Période de validité',
        cell: (link: ProductDiscountLink) => `
          <div class="flex flex-col">
            <div>From: ${link.valideFrom}</div>
            <div>To: ${link.valideTo}</div>
            <div class="text-sm">${link.duration} days</div>
          </div>
        `
      },
      {
        name: 'status',
        header: 'Statut',
        cell: (link: ProductDiscountLink) => {
          if (link.active) {
            return `<span class="bg-light-success text-success rounded f-w-600 p-6 p-y-4 f-s-12">Active</span>`;
          } else {
            return `<span class="bg-light-error text-error rounded f-w-600 p-6 p-y-4 f-s-12">Inactive</span>`;
          }
        }
      },
      {
        name: 'priority',
        header: 'Priorité',
        cell: (link: ProductDiscountLink) => {
          const priorityClass = {
            'high': 'bg-light-error text-error',
            'medium': 'bg-light-warning text-warning',
            'low': 'bg-light-success text-success'
          }[link.priority] || 'bg-gray-100 text-gray-800';

          return `<span class="${priorityClass} rounded f-w-600 p-6 p-y-4 f-s-12">
            ${link.priority.charAt(0).toUpperCase() + link.priority.slice(1)}
          </span>`;
        }
      }
    ]
  };

  // Properties needed for adding a new link
  selectedProductId: number | null = null;
  selectedProduct: any = null;
  selectedDiscountId: number | null = null;
  selectedDiscount: any = null;
errorMessage: any;

  constructor(
    private linkProductService: LinkProductService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getLinkedProducts();  // Fetch data when the component initializes
  }

  getLinkedProducts(): void {
    this.linkProductService.getLinkedProducts().subscribe(
      (data: ProductDiscountLink[]) => {
        console.log('Fetched Product Discount Links:', data);  // Log data to check if all rows are being returned
        this.discountLinks = data;  // Assign the fetched data to the discountLinks array
        this.discountLinkTableConfig.dataSource = this.discountLinks;  // Update the table with new data
      },
      error => {
        console.error('Error fetching product discount links:', error);
      }
    );
  }

  editLink(link: ProductDiscountLink) {
    const dialogRef = this.dialog.open(EditLinkedProductComponent, {
      width: '400px',
      data: { valideTo: link.valideTo,active: link.active }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const request = {
          jours: result.jours,
          active: result.active 
          
        };
        this.linkProductService.prolongerLien(link.idLink, request).subscribe({
          next: (message) => {
            console.log('Link updated successfully:', message);
            this.getLinkedProducts(); // refresh list after update
          },
          error: (error) => {
            console.error('Error updating link:', error);
          }
        });
      }
    });
  }

  deleteLink(link: ProductDiscountLink) {
    this.linkProductService.deleteLink(link.idLink).subscribe({
      next: () => {
        this.discountLinks = this.discountLinks.filter(p => p.idLink !== link.idLink);
        this.discountLinkTableConfig.dataSource = [...this.discountLinks];
        console.log('Lien supprimé avec succès :', link);
      },
      error: (error) => {
        console.error('Échec de la suppression du lien', error);
      }
    });
  }
  // New method to add a link
  addNewLink() {
    const dialogRef = this.dialog.open(AddLinkedProductComponent, {
      width: '600px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Add link dialog result:', result);
        
        const linkData = {
          idproduct: result.productId,
          idDisc: result.discountId,
          active: result.active,
          duration: result.duration,
          valideFrom: result.valideFrom,
          valideTo: result.valideTo,
          priority: result.priority,
        };
        
        this.linkProductService.addLinkProdDisc(linkData).subscribe({
          next: (response) => {
            console.log('Link added successfully:', response);
            this.getLinkedProducts(); // refresh list after adding
          },
          error: (error) => {
            console.error('Error adding product link:', error);
          }
        });
      }
    });
  }
}