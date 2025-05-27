import { Routes } from '@angular/router';

// ui
import { AppTablesComponent } from './tables/tables.component';
import {ProductsComponent} from "./products/products.component";
import {MainTableComponent} from "./main-table/main-table.component";
import {CouponComponent} from "./coupon/coupon.component";
import {DiscountComponent} from "./discount/discount.component";
import {LinkProductDiscountComponent} from "./link-product-discount/link-product-discount.component";
import {TrakingComponent} from "./traking/traking.component";

export const UiComponentsRoutes: Routes = [
  {
    path: '',
    children: [


      {
        path: 'linkedProductDiscount',
        component: LinkProductDiscountComponent,
      },
      {
        path: 'discount',
        component: DiscountComponent,
      },
      {
        path: 'main-table',
        component: MainTableComponent,
      },

      {
        path: 'trakingTable',
        component: TrakingComponent,
      },
      {
        path: 'tables',
        component: AppTablesComponent,
      },    {
        path: 'products',
        component: ProductsComponent,
      },{
        path: 'Coupon',
        component: CouponComponent,
      },
    ],
  },
];
