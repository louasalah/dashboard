import { Component, OnInit } from '@angular/core';
import { TrackingData, TrackingLinkDTO } from './traking.data';
import { MainTableComponent, TableColumn } from '../main-table/main-table.component';
import { TrakingService } from './traking.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Product } from '../products/product.data';
import { ProductService } from '../products/product.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-traking',
  standalone: true,
  imports: [
    MainTableComponent,
    MatDialogModule,
  ],
  templateUrl: './traking.component.html',
  styleUrls: ['./traking.component.scss']
})
export class TrakingComponent implements OnInit {
  trackingLinks: TrackingLinkDTO[] = [];
  products: Product[] = [];

  columns: TableColumn[] = [
    {
      name: 'id',
      header: 'ID',
      cell: (element: TrackingLinkDTO) => `${element.id}`
    },
    {
      name: 'idproduct',
      header: 'refProduit ',
      cell: (element: TrackingLinkDTO) => `${element.referenceProduct}`
    },
      {
  name: 'categorie',
  header: 'categorie',
  cell: (element: TrackingLinkDTO) => element.categorie?.nom ?? 'N/A'
},
    {
      name: 'pagename',
      header: 'NomPage',
      cell: (element: TrackingLinkDTO) => `${element.pagename}`
    },
    {
      name: 'idLink',
        header: 'ID Link ',
      cell: (element: TrackingLinkDTO) => `${element.idLink}`
    },
    {
      name: 'timespent',
      header: 'Durée visite',
      cell: (element: TrackingLinkDTO) => `${element.timespent} seconds`
    },
    {
      name: 'clicks',
      header: 'Clics',
      cell: (element: TrackingLinkDTO) => `${element.clicks}`
    },
    {
      name: 'entryTimeFormatted',
      header: 'Date denvoie',
      cell: (element: TrackingLinkDTO) => `${element.entryTimeFormatted}`
    },
    {
      name: 'latitude',
      header: 'Latitude',
      cell: (element: TrackingLinkDTO) => `${element.latitude}`
    },
    {
      name: 'longitude',
      header: 'Longitude',
      cell: (element: TrackingLinkDTO) => `${element.longitude}`
    },
    {
      name: 'sessionId',
      header: 'ID Session ',
      cell: (element: TrackingLinkDTO) => `${element.sessionId}`
    },
  ];

  tableConfig = {
    columns: this.columns,
    dataSource: this.trackingLinks,
    title: 'Tracking Links',
  };

  constructor(
    private trackingLinkService: TrakingService,
    private productService: ProductService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    // Utiliser forkJoin pour attendre que les deux appels soient terminés
    forkJoin({
      products: this.productService.getProducts(),
      trackingLinks: this.trackingLinkService.getTracking()
    }).subscribe({
      next: (result) => {
        this.products = result.products;
        
        // Maintenant que nous avons les produits, nous pouvons associer les références
        this.trackingLinks = result.trackingLinks.map(tracking => {
          const product = this.products.find(p => p.idproduct === tracking.idproduct);
          return {
            ...tracking,
            referenceProduct: product ? product.referenceProduct : 'Unknown',
            categorie: product?.categorie ?? { id: 0, nom: 'Inconnue' }

          };
        });
        
        // Mettre à jour la source de données du tableau
        this.tableConfig.dataSource = this.trackingLinks;
        console.log('Products:', this.products);
        console.log('Tracking links with product references:', this.trackingLinks);
      },
      error: (error) => {
        console.error('Error fetching data:', error);
      }
    });
  }
}