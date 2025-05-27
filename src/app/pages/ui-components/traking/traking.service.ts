import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TrackingData, TrackingLinkDTO} from "./traking.data";


@Injectable({
  providedIn: 'root'
})

export class TrakingService {
  private apiUrl = 'http://localhost:8080';  // Directly using your API endpoint

  constructor(private http: HttpClient) { }

  // Get all coupons
  getTracking(): Observable<TrackingLinkDTO[]> {
    return this.http.get<TrackingLinkDTO[]>(`${this.apiUrl}/Api/trackingData`);
  }
  trackClientData(idproduct: number, sessionId: string, trackingData: TrackingData): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/Api/tracking/${idproduct}/${sessionId}`, trackingData);
  }

}
