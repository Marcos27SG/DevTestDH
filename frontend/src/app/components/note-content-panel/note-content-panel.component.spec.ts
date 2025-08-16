import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoteContentPanelComponent } from './note-content-panel.component';

describe('NoteContentPanelComponent', () => {
  let component: NoteContentPanelComponent;
  let fixture: ComponentFixture<NoteContentPanelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NoteContentPanelComponent]
    });
    fixture = TestBed.createComponent(NoteContentPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
