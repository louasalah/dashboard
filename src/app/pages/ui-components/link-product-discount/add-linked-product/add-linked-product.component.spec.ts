import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLinkedProductComponent } from './add-linked-product.component';

describe('AddLinkedProductComponent', () => {
  let component: AddLinkedProductComponent;
  let fixture: ComponentFixture<AddLinkedProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddLinkedProductComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddLinkedProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
