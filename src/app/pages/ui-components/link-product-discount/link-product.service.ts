import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {LinkProdDiscRequest, ProductDiscountLink} from "./linkProduct.data";


@Injectable({
  providedIn: 'root'
})

export class LinkProductService {
  private apiUrl = 'http://localhost:8080';  // Directly using your API endpoint

  constructor(private http: HttpClient) { }

  // Get all coupons
  getLinkedProducts(): Observable<ProductDiscountLink[]> {
    return this.http.get<ProductDiscountLink[]>(`${this.apiUrl}/aapiee/getlinks`);
  }
  prolongerLien(id: number, request: LinkProdDiscRequest): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/aapiee/prolonger/${id}`, request);
  }

  private baseUrl = 'http://localhost:8080/aapiee'; // Base API URL



  // Get all linked products


  // Add new product-discount link
  addLinkProdDisc(linkData: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/add`, linkData);
  }

  // Get product by ID (might be needed for the selection)
  getProductById(productId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/products/${productId}`);
  }

  // Get discount definition by ID (might be needed for the selection)
  getDiscountDefById(discountId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/discounts/${discountId}`);
  }

  deleteLink(idLink: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/aapiee/links/${idLink}`);
  }
}

