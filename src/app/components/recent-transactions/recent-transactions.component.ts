import { Component, OnInit } from '@angular/core';
import { TablerIconsModule } from 'angular-tabler-icons';
import { MaterialModule } from 'src/app/material.module';
import { CommonModule } from '@angular/common';
import { StatisticsService, CategoryFrequency } from 'src/app/services/statistics.service';

@Component({
  selector: 'app-recent-transactions',
  standalone: true,
  imports: [MaterialModule, TablerIconsModule, CommonModule],
  templateUrl: './recent-transactions.component.html',
})
export class AppRecentTransactionsComponent implements OnInit {
  categories: CategoryFrequency[] = [];
  isLoading = true;
  error = false;

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    this.isLoading = true;
    this.statisticsService.getCategoryFrequency().subscribe({
      next: (data) => {
        this.categories = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching category frequency:', err);
        this.error = true;
        this.isLoading = false;
      }
    });
  }

  // Returns a unique color for each category (for visualization)
  getCategoryColor(index: number): string {
    const colors = ['#5D87FF', '#49BEFF', '#13DEB9', '#F4CE14', '#FF5722', '#7352FF'];
    return colors[index % colors.length];
  }

  // Calculate percentage of the category compared to total counts
  calculatePercentage(count: number): number {
    const total = this.categories.reduce((sum, category) => sum + category.count, 0);
    return total > 0 ? Math.round((count / total) * 100) : 0;
  }
}