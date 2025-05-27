export interface Product {
  idproduct: number;
  
  referenceProduct: string;
  description: string;
  comment: string;
  price: number;
  quantiteDiscount: number;
  image: string;
  status: 'in-stock' | 'out-of-stock' | 'low-stock';
  id_category: number;
  categorie: {
    id: number;
    nom: string;
  };
}
