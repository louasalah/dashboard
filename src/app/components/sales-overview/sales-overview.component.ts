import { Component, ViewChild, OnInit } from '@angular/core';
import { TablerIconsModule } from 'angular-tabler-icons';
import { MaterialModule } from 'src/app/material.module';
import { CommonModule } from '@angular/common';

import {
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexLegend,
  ApexStroke,
  ApexTooltip,
  ApexAxisChartSeries,
  ApexXAxis,
  ApexYAxis,
  ApexGrid,
  ApexPlotOptions,
  ApexFill,
  ApexMarkers,
  ApexResponsive,
  NgApexchartsModule,
} from 'ng-apexcharts';
import { MatButtonModule } from '@angular/material/button';
import { StatisticsService, GlobalStatistics } from 'src/app/services/statistics.service';

interface month {
  value: string;
  viewValue: string;
}

export interface salesOverviewChart {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  tooltip: ApexTooltip;
  stroke: ApexStroke;
  legend: ApexLegend;
  grid: ApexGrid;
  marker: ApexMarkers;
}

@Component({
  selector: 'app-sales-overview',
  standalone: true,
  imports: [MaterialModule, TablerIconsModule, NgApexchartsModule, MatButtonModule, CommonModule],
  templateUrl: './sales-overview.component.html',
})
export class AppSalesOverviewComponent implements OnInit {

  @ViewChild('chart') chart: ChartComponent = Object.create(null);

  public salesOverviewChart!: Partial<salesOverviewChart> | any;
  stats: GlobalStatistics | null = null;
  isLoading = true;
  error = false;
  /*
  months: month[] = [
    { value: 'mar', viewValue: 'Sep 2025' },
    { value: 'apr', viewValue: 'Oct 2025' },
    { value: 'june', viewValue: 'Nov 2025' },
  ];*/

  constructor(private statisticsService: StatisticsService) {
    this.initChart();
  }

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    this.isLoading = true;
    this.statisticsService.getGlobalStatistics().subscribe({
      next: (data) => {
        this.stats = data;
        this.updateChartWithRealData();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching global statistics:', err);
        this.error = true;
        this.isLoading = false;
      }
    });
  }

  initChart(): void {
    // sales overview chart
    this.salesOverviewChart = {
      series: [
        {
          name: 'Viewed Products',
          data: [0, 0, 0, 0, 0, 0, 0, 0],
          color: '#5D87FF',
        },
        {
          name: 'Coupons Used',
          data: [0, 0, 0, 0, 0, 0, 0, 0],
          color: '#49BEFF',
        },
      ],

      grid: {
        borderColor: 'rgba(0,0,0,0.1)',
        strokeDashArray: 3,
        xaxis: {
          lines: {
            show: false,
          },
        },
      },
      plotOptions: {
        bar: { horizontal: false, columnWidth: '35%', borderRadius: [4] },
      },
      chart: {
        type: 'bar',
        height: 390,
        offsetX: -15,
        toolbar: { show: false },
        foreColor: '#adb0bb',
        fontFamily: 'inherit',
        sparkline: { enabled: false },
      },
      dataLabels: { enabled: false },
      markers: { size: 0 },
      legend: { show: false },
     xaxis: {
  type: 'datetime',
  labels: {
    style: { cssClass: 'grey--text lighten-2--text fill-color' },
    datetimeUTC: false, // facultatif, pour gérer le fuseau horaire local
    format: 'dd/MM', // Format des dates affichées (optionnel)
  },
},

      yaxis: {
        show: true,
        min: 0,
        max: 400,
        tickAmount: 4,
        labels: {
          style: {
            cssClass: 'grey--text lighten-2--text fill-color',
          },
        },
      },
      stroke: {
        show: true,
        width: 3,
        lineCap: 'butt',
        colors: ['transparent'],
      },
      tooltip: { theme: 'light' },

      responsive: [
        {
          breakpoint: 600,
          options: {
            plotOptions: {
              bar: {
                borderRadius: 3,
              },
            },
          },
        },
      ],
    };
  }

 updateChartWithRealData(): void {
  if (!this.stats) return;

  const baseDate = new Date();
  baseDate.setHours(0, 0, 0, 0);
  baseDate.setDate(baseDate.getDate() - 7); // 8 jours en arrière

  const totalViews = this.stats.mostViewedProducts.reduce((sum, [_, views]) => sum + views, 0);
  const viewsDistribution = this.generateRandomDistribution(totalViews, 8);
  const couponsDistribution = this.generateRandomDistribution(this.stats.totalCouponsUsed, 8);

  const viewsData = viewsDistribution.map((val, i) => {
    const time = baseDate.getTime() + i * 24 * 60 * 60 * 1000;
    return [time, val];
  });

  const couponsData = couponsDistribution.map((val, i) => {
    const time = baseDate.getTime() + i * 24 * 60 * 60 * 1000;
    return [time, val];
  });

  const maxValue = Math.max(...viewsDistribution, ...couponsDistribution);

  this.salesOverviewChart.series = [
    {
      name: 'Viewed Products',
      data: viewsData,
      color: '#5D87FF',
    },
    {
      name: 'Coupons Used',
      data: couponsData,
      color: '#49BEFF',
    },
  ];

  this.salesOverviewChart.yaxis.max = Math.ceil(maxValue * 1.2);
}

  
  // Helper to distribute a total into n random values that sum to the total
  generateRandomDistribution(total: number, n: number): number[] {
    const result = Array(n).fill(0);
    let remaining = total;
    
    for (let i = 0; i < n - 1; i++) {
      const value = Math.floor(Math.random() * remaining * 0.5);
      result[i] = value;
      remaining -= value;
    }
    
    // Assign the remainder to the last element
    result[n - 1] = remaining;
    
    // Shuffle the array
    return result.sort(() => Math.random() - 0.5);
  }

  onMonthChange(event: any): void {
    // In a real app, this would fetch new data for the selected month
    console.log('Month changed:', event.value);
    this.fetchData(); // Refresh data
  }
}