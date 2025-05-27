import { Component, OnInit } from '@angular/core';
import { TablerIconsModule } from 'angular-tabler-icons';
import { MaterialModule } from 'src/app/material.module';
import { CommonModule } from '@angular/common';
import { StatisticsService, GlobalStatistics } from 'src/app/services/statistics.service';

interface ProductView {
  id: number;
  views: number;
 
}


@Component({
  selector: 'app-product-performance',
  standalone: true,
  imports: [MaterialModule, TablerIconsModule, CommonModule],
  templateUrl: './product-performance.component.html',
})
export class AppProductPerformanceComponent implements OnInit {
 stats: GlobalStatistics | null = null;
  products: ProductView[] = [];

  isLoading = true;
  error = false;

  displayedColumns: string[] = ['id', 'views'];

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    this.isLoading = true;
    this.statisticsService.getGlobalStatistics().subscribe({
      next: (data) => {
        this.stats = data;
        this.processProductData();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching product statistics:', err);
        this.error = true;
        this.isLoading = false;
      }
    });
  }

  processProductData(): void {
    if (!this.stats) return;
    
    this.products = this.stats.mostViewedProducts.map(([id, views]) => ({
      id,
      views
    }));

}
}