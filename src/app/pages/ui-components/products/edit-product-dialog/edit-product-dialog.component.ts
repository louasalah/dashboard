import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {Product} from "../product.data";
import {MainTableComponent} from "../../main-table/main-table.component";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";

@Component({
  standalone: true,
  styleUrls: ['./edit-product-dialog.component.scss'],

  selector: 'app-edit-product-dialog',
  imports: [
    MatFormField,
    ReactiveFormsModule,
    MatInput,
    MatDialogContent,
    MatDialogTitle,
    MatDialogActions,
    MatButton,
    MatLabel
  ],
  templateUrl: './edit-product-dialog.component.html'
})
export class EditProductDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<EditProductDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Product
  ) {
    this.form = this.fb.group({
      idproduct: [{value:data.idproduct,disabled:true}],
      referenceProduct: [{value:data.referenceProduct,disabled:true}],
      description: [{value:data.description,disabled:true}],
      comment: [{value:data.comment,disabled:true}],
      price: [{value:data.price,disabled:true}],
      quantiteDiscount: [data.quantiteDiscount],
      image: [{value:data.image,disabled:true}],
      // Optional: categorie, linkProdDiscs...
    });
  }

  submit() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.getRawValue());
    }
  }

  cancel() {
    this.dialogRef.close(this.form.getRawValue());
  }
}
