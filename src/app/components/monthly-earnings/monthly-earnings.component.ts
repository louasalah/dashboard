import { Component, OnInit } from '@angular/core';
import { TablerIconsModule } from 'angular-tabler-icons';
import { MaterialModule } from 'src/app/material.module';
import { StatisticsService, PositionFrequency } from 'src/app/services/statistics.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-monthly-earnings',
  standalone: true,
  imports: [MaterialModule, TablerIconsModule, CommonModule],
  templateUrl: './monthly-earnings.component.html',
})
export class AppMonthlyEarningsComponent implements OnInit {
  positionData: PositionFrequency | null = null;
  isLoading = true;
  error = false;

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    this.isLoading = true;
    this.statisticsService.getMostFrequentPosition().subscribe({
      next: (data) => {
        this.positionData = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching position data:', err);
        this.error = true;
        this.isLoading = false;
      }
    });
  }

  getFormattedCoordinates(): string {
    if (!this.positionData) return '';
    return `${this.positionData.latitude.toFixed(2)}, ${this.positionData.longitude.toFixed(2)}`;
  }
}