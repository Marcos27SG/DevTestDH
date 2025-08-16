import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotesListPanelComponent } from './notes-list-panel.component';

describe('NotesListPanelComponent', () => {
  let component: NotesListPanelComponent;
  let fixture: ComponentFixture<NotesListPanelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NotesListPanelComponent]
    });
    fixture = TestBed.createComponent(NotesListPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
