import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditDiscountDialogComponent } from './edit-discount-dialog.component';

describe('EditDiscountDialogComponent', () => {
  let component: EditDiscountDialogComponent;
  let fixture: ComponentFixture<EditDiscountDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditDiscountDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditDiscountDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
