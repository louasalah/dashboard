import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { MainTableComponent } from "../main-table/main-table.component";
import { GetCouponTracking } from "./coupon.data";
import { CouponService } from './coupon.service';
import { MatDialog } from "@angular/material/dialog";
import { MatSelectModule } from "@angular/material/select";
import { MatOptionModule } from "@angular/material/core";
import { FormsModule } from "@angular/forms";
import { EditCouponDialogComponent } from "./edit-coupon-dialog/edit-coupon-dialog.component";
import { ProductDiscountLink } from '../link-product-discount/linkProduct.data';
import { LinkProductService } from '../link-product-discount/link-product.service';
import { DiscountService } from '../discount/discount.service';
import { Discount } from '../discount/dicount.data';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-coupon',
  standalone: true,
  imports: [MainTableComponent, MatSelectModule, FormsModule, MatOptionModule],
  templateUrl: './coupon.component.html',
  styleUrls: ['./coupon.component.scss']
})
export class CouponComponent implements OnInit {
  coupons: GetCouponTracking[] = [];
  editingCouponId: number | null = null;
  discountLinks: ProductDiscountLink[] = [];
  discounts: Discount[] = [];

  editedStatus: string = '';

  couponTableConfig = {
    title: 'Coupon Management',
    dataSource: this.coupons,
    showMenu: true,
    menuItems: [
      { icon: 'edit', label: 'Edit', action: (coupon: GetCouponTracking) => this.startEditing(coupon) }
    ],
    columns: [
      {
        name: 'couponCode',
        header: 'Code Coupon ',
        headerClass: 'font-bold',
        cell: (coupon: GetCouponTracking) => `
          <div class="f-s-14 f-w-600">${coupon.couponCode}</div>
          <div class="f-s-14 f-w-600">ID: ${coupon.id}</div>
        `
      },
      {
        name: 'productReference',
        header: 'RefProduit',
        cell: (coupon: GetCouponTracking) => coupon.productReference
      },
   
      {
        name: 'email',
        header: 'Email',
        cell: (coupon: GetCouponTracking) => `${coupon.email}`
      },
      {
        name: 'sentDate',
        header: 'Date denvoie',
        cell: (coupon: GetCouponTracking) => `${coupon.sentDate}`
      },
      {
        name: 'status',
        header: 'Statut',
        cell: (coupon: GetCouponTracking) => `${coupon.status}`
      },
      {
        name: 'idLink',
        header: 'ID Link ',
        cell: (coupon: GetCouponTracking) => `${coupon.idLink}`
      }
    ]
  };

  constructor(
    private couponService: CouponService,
    private discountService: DiscountService,
    private linkProductService: LinkProductService,
    private dialog: MatDialog,
    private el: ElementRef,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    forkJoin({
      discounts: this.discountService.getDiscountProduct(),
      discountLinks: this.linkProductService.getLinkedProducts()
    }).subscribe(({ discounts, discountLinks }) => {
      this.discounts = discounts;
      this.discountLinks = discountLinks;
      this.getCoupons();  // Appelé seulement après que discounts et discountLinks soient remplis
    });
  }
  getCoupons(): void {
    this.couponService.getCoupons().subscribe(
      (data: GetCouponTracking[]) => {
        this.coupons = data.map(coupon => {

          console.log('tes',coupon.idLink)
          console.log('tes',this.discountLinks[0].idLink)

          const discountLink = this.discountLinks.find(link => link.idLink === coupon.idLink);
          console.log('tes',discountLink)


          const matchedDiscount = discountLink
            ? this.discounts.find(discount => discount.idDisc === discountLink.discountId)
            : null;
            console.log('tes',matchedDiscount)
          return {
            ...coupon,
            discountValue: matchedDiscount ? matchedDiscount.valeur : coupon.discountValue
          };
        });

        console.log('tes',this.coupons)
        this.couponTableConfig.dataSource = [...this.coupons];
      },
      error => {
        console.error('Error fetching coupons:', error);
      }
    );
  }

  getDiscounts(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.discountService.getDiscountProduct().subscribe(
        (data: Discount[]) => {
          this.discounts = data;
          resolve();
        },
        error => {
          console.error('Error fetching discounts:', error);
          reject();
        }
      );
    });
  }

  getLinkedProducts(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.linkProductService.getLinkedProducts().subscribe(
        (data: ProductDiscountLink[]) => {
          this.discountLinks = data;
          resolve();
        },
        error => {
          console.error('Error fetching product discount links:', error);
          reject();
        }
      );
    });
  }
  onStatusChange(event: Event, couponId: number): void {
    const selectElement = event.target as HTMLSelectElement;
    const newStatus = selectElement.value;
  
    console.log("Updated status:", newStatus, "for coupon ID:", couponId);
  
    this.couponService.updateCouponStatus(couponId, newStatus).subscribe(
      () => {
        console.log('Coupon status updated successfully');
        this.getCoupons(); // Refresh data
      },
      error => {
        console.error('Error updating coupon status:', error);
      }
    );
  }
  

  deleteCoupon(coupon: GetCouponTracking) {
    console.log('Deleting coupon:', coupon);
  }
  startEditing(coupon: GetCouponTracking) {
    this.editingCouponId = coupon.id;
  
    const dialogRef = this.dialog.open(EditCouponDialogComponent, {
      width: '500px',
      data: { coupon }
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const couponsID=coupon.id
        this.couponService.updateCouponStatus( couponsID, result).subscribe(
          () => {
            console.log('Coupon status updated successfully');
            this.getCoupons(); // Refresh data after update
          },
          error => {
            console.error('Error updating coupon status:', error);
          }
        );
      }
      this.editingCouponId = null;
    });
  }
  
}
