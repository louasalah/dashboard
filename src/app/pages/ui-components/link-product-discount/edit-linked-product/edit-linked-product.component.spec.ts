import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditLinkedProductComponent } from './edit-linked-product.component';

describe('EditLinkedProductComponent', () => {
  let component: EditLinkedProductComponent;
  let fixture: ComponentFixture<EditLinkedProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditLinkedProductComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditLinkedProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
