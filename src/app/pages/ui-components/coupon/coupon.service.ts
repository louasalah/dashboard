import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {GetCouponTracking} from "./coupon.data";


@Injectable({
  providedIn: 'root'
})

  export class CouponService {
  private apiUrl = 'http://localhost:8080';  // Directly using your API endpoint

  constructor(private http: HttpClient) { }

  // Get all coupons
  getCoupons(): Observable<GetCouponTracking[]> {
    return this.http.get<GetCouponTracking[]>(`${this.apiUrl}/api/coupons/trackinglogs`);
  }
  sendCoupon(email: string, productReference: string, status: string, idLink: number): Observable<any> {
    const payload = { email, productReference, status, idLink };
    return this.http.post(`${this.apiUrl}/api/coupons/sendcoupon`, payload);
  }
  updateCouponStatus(id: number, status: string = 'used'): Observable<any> {
    return this.http.put(`${this.apiUrl}/api/coupons/updateStatus/${id}`, null, {
      params: { status }
    });
  }

}
