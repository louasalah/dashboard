import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkProductDiscountComponent } from './link-product-discount.component';

describe('LinkProductDiscountComponent', () => {
  let component: LinkProductDiscountComponent;
  let fixture: ComponentFixture<LinkProductDiscountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinkProductDiscountComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LinkProductDiscountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
