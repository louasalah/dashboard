import { Component, OnInit, ViewChild } from '@angular/core';
import { TablerIconsModule } from 'angular-tabler-icons';
import { MaterialModule } from 'src/app/material.module';
import { NgApexchartsModule } from 'ng-apexcharts';
import { StatisticsService, GlobalStatistics } from 'src/app/services/statistics.service';
import { CommonModule } from '@angular/common';

import {
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexLegend,
  ApexTooltip,
  ApexNonAxisChartSeries,
  ApexResponsive,
} from 'ng-apexcharts';

export interface yearlyBreakupChart {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  labels: any;
  tooltip: ApexTooltip;
  legend: ApexLegend;
  colors: string[];
  dataLabels: ApexDataLabels;
}

@Component({
  selector: 'app-yearly-breakup',
  standalone: true,
  imports: [MaterialModule, TablerIconsModule, NgApexchartsModule, CommonModule],
  templateUrl: './yearly-breakup.component.html',
})
export class AppYearlyBreakupComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent = Object.create(null);
  public yearlyBreakupChart!: Partial<yearlyBreakupChart> | any;
  
  stats: GlobalStatistics | null = null;
  isLoading = true;
  error = false;

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.fetchData();
    this.initChart();
  }

  fetchData(): void {
    this.isLoading = true;
    this.statisticsService.getGlobalStatistics().subscribe({
      next: (data) => {
        this.stats = data;
        this.updateChartData();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching statistics:', err);
        this.error = true;
        this.isLoading = false;
      }
    });
  }

  initChart(): void {
    this.yearlyBreakupChart = {
      series: [60, 40],
      chart: {
        type: 'donut',
        height: 95,
        fontFamily: 'inherit',
        foreColor: '#adb0bb',
      },
      colors: ['#5D87FF', '#ECF2FF'],
      plotOptions: {
        pie: {
          donut: {
            size: '75%',
            background: 'none',
            labels: {
              show: true,
              name: {
                show: true,
                fontSize: '12px',
                color: undefined,
                offsetY: 0,
              },
              value: {
                show: false,
                color: '#98AFD0',
              },
              total: {
                show: false,
                label: 'Yearly Breakup',
                color: '#98AFD0',
              },
            },
          },
        },
      },
      dataLabels: {
        enabled: false,
      },
      legend: {
        show: false,
      },
      responsive: [
        {
          breakpoint: 991,
          options: {
            chart: {
              width: 120,
            },
          },
        },
      ],
      tooltip: {
        enabled: false,
      },
    };
  }

  updateChartData(): void {
    if (!this.stats) return;
    
    // Calculate usage percentage
    const totalCoupons = this.stats.totalCouponsSent;
    const usedCoupons = this.stats.totalCouponsUsed;
    
    if (totalCoupons > 0) {
      const usedPercentage = Math.round((usedCoupons / totalCoupons) * 100);
      const unusedPercentage = 100 - usedPercentage;
      
      this.yearlyBreakupChart.series = [usedPercentage, unusedPercentage];
      this.yearlyBreakupChart.labels = ['Used Coupons', 'Unused Coupons'];
    }
  }
}