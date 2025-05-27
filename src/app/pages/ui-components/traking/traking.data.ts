// export interface TrackingLinkDTO {
//   linkId: number;
//   active: boolean;
//   duration: number;
//   valideFrom: string;  // String is used for date because it's easier to parse
//   valideTo: string;
//   priority: string;
//   productId: number;
//   productName: string;
//   discountId: number;
//   discountedPrice: number;
//   trackingId: number;
//   idProduct: number;
//   pageName: string;
//   timeSpent: number;
//   clicks: number;
//   sessionId: string;
//   latitude: number;
//   longitude: number;
//   entryTimeFormatted: string;  // You can format this in the frontend if needed
//   categorieNAme: string;
// }
export interface TrackingLinkDTO {
  id: number;                  // Corresponds to @Id in Java class
  idproduct: number; 
   idLink: number;
  referenceProduct:string;          // Corresponds to Long idproduct in Java class
  pagename: string;            // Corresponds to String pagename in Java class
  timespent: number;           // Corresponds to Long timespent in Java class
  clicks: number;              // Corresponds to Integer clicks in Java class
  entryTimeFormatted: string;  // Corresponds to @Temporal Date entryTimeFormatted in Java class (converted to string)
  latitude: number;            // Corresponds to Double latitude in Java class
  longitude: number;           // Corresponds to Double longitude in Java class
  sessionId: string;  
id_category: number;
  categorie: {
    id: number;
    nom: string;
  };  
       // Corresponds to String sessionId in Java class
}

export interface TrackingData {
  pagename: string;
  timespent: number;
  clicks: number;
  longitude: number;
  latitude: number;
  sessionId: string;
}
