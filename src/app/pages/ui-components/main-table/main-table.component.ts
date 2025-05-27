// main-table.component.ts
import {Component, Input, ViewChild, ViewContainerRef} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from "../../../material.module";
import { MatMenuModule } from "@angular/material/menu";
import { MatIconModule } from "@angular/material/icon";
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

export interface TableColumn {
  name: string;
  header: string;
  cell: (element: any) => string; // Changed from string | SafeHtml to just string
  headerClass?: string;
  cellClass?: string;
}

export interface TableConfig {
  columns: TableColumn[];
  dataSource: any[];
  title?: string;
  showMenu?: boolean;
  menuItems?: { icon: string; label: string; action: (element: any) => void }[];
}

@Component({
  selector: 'app-main-table',
  standalone: true,
  imports: [
    CommonModule,
    MaterialModule,
    MatMenuModule,
    MatIconModule,
  ],
  templateUrl: './main-table.component.html',
  styleUrls: ['./main-table.component.scss']
})
export class MainTableComponent {
  @ViewChild('cellContainer', { read: ViewContainerRef }) cellContainer!: ViewContainerRef;

  @Input() config: TableConfig = {
    columns: [],
    dataSource: [],
    showMenu: false
  };

  constructor(private sanitizer: DomSanitizer) {}

  get displayedColumns(): string[] {
    if (!this.config?.columns) return [];

    const columns = this.config.columns.map(c => c.name);
    if (this.config.showMenu) {
      return [...columns, 'actions'];
    }
    return columns;
  }

  sanitizeHtml(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}
