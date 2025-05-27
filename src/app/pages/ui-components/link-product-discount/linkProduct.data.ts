export interface ProductDiscountLink {
  idLink: number;
  active: boolean;
  duration: number;
  valideFrom: string;
  valideTo: string;
  priority: string
  jours: number;

  product: string;

  discountedPrice: number;
  discountId: number;
  //idproduct: number;
  referenceProduct:string;
  refDiscount:string;

}
export interface LinkProdDiscRequest {
  jours: number;
  active: boolean;
 
}
// export interface ProductDiscountLink {
//   idLink: number;
//   active: boolean;
//   duration: number;
//   valideFrom: string;
//   valideTo: string;
//   priority: string
//   jours: number;
//   quantiteDiscount: number;
//   product: string;
//   discountedPrice: number;
//   discountDef: number;
//   idProduct: number;
// }
// export interface ProductDiscountLink {
//   idLink: number;
//   active: boolean;
//   duration: number;
//   valideFrom: Date;
//   valideTo: Date;
//   priority: string;
//   jours: number;
//   quantiteDiscount: number;
//   discountedPrice: number;
//   discountDef: number;
//   idProduct: number;
//
//
// }
