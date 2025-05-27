import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  private apiUrl = 'http://localhost:8080/api/statistics';

  constructor(private http: HttpClient) { }

  getGlobalStatistics(): Observable<GlobalStatistics> {
    return this.http.get<GlobalStatistics>(`${this.apiUrl}/global`);
  }

  getTriggeredProducts(): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/triggered-products`);
  }

  getProductStats(id: number): Observable<ProductStats> {
    return this.http.get<ProductStats>(`${this.apiUrl}/product/${id}`);
  }

  getCategoryFrequency(): Observable<CategoryFrequency[]> {
    return this.http.get<CategoryFrequency[]>(`${this.apiUrl}/categories/frequency`);
  }

  getMostFrequentPosition(): Observable<PositionFrequency> {
    return this.http.get<PositionFrequency>(`${this.apiUrl}/positions/frequent`);
  }

  getPeakHour(): Observable<PeakHour> {
    return this.http.get<PeakHour>(`${this.apiUrl}/peak-hour`);
  }

  runDiscountBatch(): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/run-discount-batch`, { responseType: 'text' as 'json' });
  }
}

export interface GlobalStatistics {
  totalCouponsSent: number;
  productsReachedMaxViews: number[];
  totalCouponsUsed: number;
  totalProducts: number;
  mostViewedProducts: [number, number][];
}

export interface ProductStats {
  [key: string]: any;
}

export interface CategoryFrequency {
  categorie: string;
  count: number;
}

export interface PositionFrequency {
  count: number;
  latitude: number;
  longitude: number;
}

export interface PeakHour {
  [key: string]: any;
}