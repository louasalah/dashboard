import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Discount } from '../dicount.data';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';


@Component({
  standalone: true,
  selector: 'app-edit-discount-dialog',
  imports: [
    MatFormField,
    ReactiveFormsModule,
    MatInput,
    MatDialogContent,
    MatDialogTitle,
    MatDialogActions,
    MatButton,
    MatLabel,
    MatSelectModule,
  ],
  templateUrl: './edit-discount-dialog.component.html',
  styleUrl: './edit-discount-dialog.component.scss'
})
export class EditDiscountDialogComponent {
form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<EditDiscountDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Discount
  ) {
    this.form = this.fb.group({
      idDisc: [{value:data.idDisc,disabled:true}],
      refDisc: [{value:data.refDisc,disabled:true}],
      type: [data.type],
      valeur: [data.valeur],
  
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
