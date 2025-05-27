import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Discount} from "./dicount.data";


@Injectable({
  providedIn: 'root'
})

export class DiscountService {
  private apiUrl = 'http://localhost:8080';  // Directly using your API endpoint

  constructor(private http: HttpClient) { }

  // Get all coupons
  getDiscountProduct(): Observable<Discount[]> {
    return this.http.get<Discount[]>(`${this.apiUrl}/apie/discounts`);
  }
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/apie/discounts/${id}`);
  }
  updateDiscount(id: number, discount: Discount): Observable<Discount> {
      return this.http.put<Discount>(`${this.apiUrl}/apie/discounts/${id}`, discount);
    }

      // Créer un nouveau discount
  createDiscount(discount: Discount): Observable<Discount> {
    return this.http.post<Discount>(`${this.apiUrl}/apie/discounts/addDisc`, discount);
  }


}
