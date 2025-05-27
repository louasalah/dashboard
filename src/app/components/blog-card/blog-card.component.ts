import { Component, OnInit } from '@angular/core';
import { TablerIconsModule } from 'angular-tabler-icons';
import { MaterialModule } from 'src/app/material.module';
import { CommonModule } from '@angular/common';
import { StatisticsService } from 'src/app/services/statistics.service';


@Component({
  selector: 'app-blog-card',
  standalone: true,
  imports: [MaterialModule, TablerIconsModule, CommonModule],
  templateUrl: './blog-card.component.html',
})
export class AppBlogCardsComponent implements OnInit {
  peakHourData: any = null;
  isLoading = true;
  error = false;

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    this.isLoading = true;
    this.statisticsService.getPeakHour().subscribe({
      next: (data) => {
        this.peakHourData = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching peak hour data:', err);
        this.error = true;
        this.isLoading = false;
      }
    });
  }

  runBatch(): void {
    this.statisticsService.runDiscountBatch().subscribe({
      next: (response) => {
        console.log('Batch job executed:', response);
        // Show success notification
      },
      error: (err) => {
        console.error('Error running batch job:', err);
        // Show error notification
      }
    });
  }
}