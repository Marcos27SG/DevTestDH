import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Note } from 'src/app/app.component';
import { Tag } from 'src/app/models/tag.model';

@Component({
  selector: 'app-notes-list-panel',
  templateUrl: './notes-list-panel.component.html',
  styleUrls: ['./notes-list-panel.component.scss']
})
export class NotesListPanelComponent {
   @Input() notes: Note[] = [];
  @Input() selectedNote: Note | null = null;
  @Output() noteSelected = new EventEmitter<Note>();
  @Output() newNoteCreated = new EventEmitter<Note>();
  onNoteClick(note: Note): void {
    this.noteSelected.emit(note);
  }

  onCreateNewNote(): void {

    const newNote: Note = {
      id: Date.now(),
      title: 'New Note',
      tags: [],
      content: '',
      lastEdited: new Date(),
      isNew: true,
      archived: false
    };

     this.notes.unshift(newNote);
    this.selectedNote = newNote;
    this.newNoteCreated.emit(newNote); // 

  }
formatDate(date: Date | string | null | undefined): string {
  if (!date) {
    return 'No date'; // or return an empty string ''
  }
  
  // Convert string to Date if necessary
  const dateObj = date instanceof Date ? date : new Date(date);
  
  // Check if the date is valid
  if (isNaN(dateObj.getTime())) {
    return 'Invalid date';
  }
  
  return dateObj.toLocaleDateString('en-US', { 
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });
}

  getPreviewText(content: string): string {
    return content.length > 100 ? content.substring(0, 100) + '...' : content;
  }

}
