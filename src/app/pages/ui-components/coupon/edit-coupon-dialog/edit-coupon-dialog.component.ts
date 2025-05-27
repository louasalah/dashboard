import { Component, Inject } from '@angular/core';
import { FormsModule } from "@angular/forms";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import { MatCheckbox, MatCheckboxChange } from "@angular/material/checkbox";
import { MatButton } from "@angular/material/button";

type CouponStatus = 'active' | 'used' | 'expired';

interface DialogData {
  currentStatus: CouponStatus;
}

interface StatusOptions {
  active: boolean;
  used: boolean;
  expired: boolean;
}

@Component({
  selector: 'app-edit-coupon-dialog',
  standalone: true,
  imports: [
    FormsModule,
    MatDialogContent,
    MatCheckbox,
    MatDialogActions,
    MatButton,
    MatDialogTitle
  ],
  templateUrl: './edit-coupon-dialog.component.html',
  styleUrls: ['./edit-coupon-dialog.component.scss']
})
export class EditCouponDialogComponent {
  statuses: StatusOptions = {
    active: false,
    used: false,
    expired: false
  };
  selectedStatus: CouponStatus | '' = '';

  constructor(
    public dialogRef: MatDialogRef<EditCouponDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    // Initialize with current status
    this.statuses[data.currentStatus] = true;
    this.selectedStatus = data.currentStatus;
  }

  onStatusChange(event: MatCheckboxChange, status: CouponStatus): void {
    if (event.checked) {
      this.selectedStatus = status;
    } else {
      this.selectedStatus = '';
    }
  }

  onSave(): void {
    if (this.selectedStatus) {
      this.dialogRef.close(this.selectedStatus);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}