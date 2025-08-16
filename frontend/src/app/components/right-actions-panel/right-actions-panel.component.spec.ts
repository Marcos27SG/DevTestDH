import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RightActionsPanelComponent } from './right-actions-panel.component';

describe('RightActionsPanelComponent', () => {
  let component: RightActionsPanelComponent;
  let fixture: ComponentFixture<RightActionsPanelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RightActionsPanelComponent]
    });
    fixture = TestBed.createComponent(RightActionsPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
