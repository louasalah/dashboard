import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {
  MAT_DIALOG_DATA, MatDialog,
  MatDialogActions, MatDialogContainer,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";

@Component({
  standalone:true,
  selector: 'app-edit-linked-product',
  imports: [
    MatDialogActions,
    MatFormField,
    MatDialogContent,
    ReactiveFormsModule,
    MatButton,
    MatInput,
    MatDialogTitle,
    MatLabel,

  ],
  templateUrl: './edit-linked-product.component.html',
  styleUrl: './edit-linked-product.component.scss'
})
export class EditLinkedProductComponent {
  editForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EditLinkedProductComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { valideTo: number; active: boolean }
  ) {
    this.editForm = this.fb.group({
      jours: [data.valideTo],
      active: [data.active]
    });
  }

  save() {
    if (this.editForm.valid) {
      this.dialogRef.close(this.editForm.value); // return updated values
    }
  }

  cancel() {
    this.dialogRef.close(); // no changes
  }
}
