import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Note } from 'src/app/app.component';
import { Tag } from 'src/app/models/tag.model';
import { AuthService } from 'src/app/services/auth.service';
import { NotesService } from 'src/app/services/notes.service';

@Component({
  selector: 'app-note-content-panel',
  templateUrl: './note-content-panel.component.html',
  styleUrls: ['./note-content-panel.component.scss']
})
export class NoteContentPanelComponent {

   @Input() note: Note | null = null;
   @Input() availableTags : Tag[] = [];
   @Output() noteSaved = new EventEmitter<Note>();
   @Output() noteDiscarded = new EventEmitter<number>();

  constructor(private authService : AuthService, private notesService : NotesService){

  }

  // Form controls for editing
  titleControl = new FormControl('');
  contentControl = new FormControl('');
  tagsControl = new FormControl<Tag[]>([]);
  
  isEditing = false;
  originalNote: Note | null = null;
  
    ngOnInit() {
    this.updateFormControls();
  }

  ngOnChanges() {
    this.updateFormControls();
    // Auto-enable editing for new notes
    this.isEditing = Boolean(this.note?.isNew);
  }

   private updateFormControls() {
    if (this.note) {
      this.titleControl.setValue(this.note.title);
      this.contentControl.setValue(this.note.content);
      this.tagsControl.setValue(this.note.tags);
      // Store original note for cancel functionality
      this.originalNote = { ...this.note };
    }
  }

  startEditing() {
    this.isEditing = true;
    this.originalNote = { ...this.note! };
  }

  private getTagIds(): number[] {
  const selectedTags = this.tagsControl.value || [];
  // If tags are objects with id property
  console.log(selectedTags.map(tag => tag.id));

  return selectedTags.map(tag => tag.id);

}


  saveNote() {

    var userId
    if (!this.note) return;

      userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('User ID not found in localStorage');
      return;
    }
     const notePayload = {
      title: this.titleControl.value || 'Untitled Note',
      content: this.contentControl.value || '',
      userId:userId, // You'll need to implement this method
      tagIds: this.getTagIds(), // Extract tag IDs from selected tags
      isFavorite: false
  };




  if (this.note.isNew) {
    // Create new note
    this.notesService.createNote(notePayload).subscribe({
      next: (createdNote: any) => {
        console.log('Note created successfully:', createdNote);
        this.isEditing = false;
        this.noteSaved.emit(createdNote); // Emit the created note
        // Optionally navigate or show success message
      },
      error: (error: any) => {
        console.error('Error creating note:', error);
        // Handle error (show toast, etc.)
      }
    });
  } else {
    // Update existing note
    this.notesService.updateNote(this.note.id, parseInt(userId) , notePayload).subscribe({
      next: (updatedNote: any) => {
        console.log('Note updated successfully:', updatedNote);
        this.isEditing = false;
        this.noteSaved.emit(updatedNote); // Emit the updated note
      },
      error: (error: any) => {
        console.error('Error updating note:', error);
        // Handle error (show toast, etc.)
      }
    });
  }

  }

  cancelEdit() {
    if (this.note?.isNew) {
      // If it's a new note, discard it completely
      this.noteDiscarded.emit(this.note.id);
    } else {
      // Revert to original values
      if (this.originalNote) {
        this.titleControl.setValue(this.originalNote.title);
        this.contentControl.setValue(this.originalNote.content);
        this.tagsControl.setValue(this.originalNote.tags);
      }
    }
    this.isEditing = false;
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
   formatContent(content: string): string {
    if (!content) return '';
    
    // Simple markdown-like transformations
    let html = content;
    
    // Convert line breaks to <br> tags
    html = html.replace(/\n/g, '<br>');
    
    // Convert **bold** to <strong>
    html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    
    // Convert *italic* to <em>
    html = html.replace(/\*(.*?)\*/g, '<em>$1</em>');
    
    // Convert numbered lists
    html = html.replace(/^(\d+)\.\s(.+)$/gm, '<div class="list-item numbered">$1. $2</div>');
    
    // Convert bullet points
    html = html.replace(/^-\s(.+)$/gm, '<div class="list-item bullet">• $1</div>');
    
    // Convert headers
    html = html.replace(/^###\s(.+)$/gm, '<h3>$1</h3>');
    html = html.replace(/^##\s(.+)$/gm, '<h2>$1</h2>');
    html = html.replace(/^#\s(.+)$/gm, '<h1>$1</h1>');
    
    // Convert inline code
    html = html.replace(/`([^`]+)`/g, '<code>$1</code>');
    
    return html;
  }
}
