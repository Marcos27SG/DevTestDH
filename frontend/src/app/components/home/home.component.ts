



export interface Note {
  id: number;
  title: string;
  content: string;
  tags: Tag[];
  lastEdited: Date;
  archived: boolean;
}



import { Component, OnInit } from '@angular/core';
import { Tag } from 'src/app/models/tag.model';
import { NotesService } from 'src/app/services/notes.service';
import { TagService } from 'src/app/services/tag.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
   selectedNote: Note | null = null;
  selectedTag = 'All Notes';
  searchQuery = '';
  availableTags: Tag[] = [];
  userNotes: Note[] = [];
  
    constructor(private tagService: TagService, private notesService: NotesService){

    }
    ngOnInit(): void {
      this.loadUserTags()
      this.loadUserNotes()
    }
    loadUserNotes():void{
      const  userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('User ID not found in localStorage');
      return;
    }
    this.notesService.getNotes(parseInt(userId)).subscribe({
      next: (notes: Note[]) => {
        this.userNotes = notes;
      },
      error: (err: any) => {
        console.error('Error loading tags:', err);
      }
    });
    }

    loadUserTags(): void {
     const userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('User ID not found in localStorage');
      return;
    }

    this.tagService.getUserTags(userId).subscribe({
      next: (tags: Tag[]) => {
        this.availableTags = tags;
        console.log('User tags loaded:', this.availableTags);
      },
      error: (err: any) => {
        console.error('Error loading tags:', err);
      }
    });
  }


  onTagSelected(tag: string): void {
    this.selectedTag = tag;
  }



  onSearchChanged(query: string): void {
    this.searchQuery = query;
  }

  onArchiveNote(): void {
    if (this.selectedNote) {
      this.selectedNote.archived = true;
      this.selectedNote = null;
    }
  }

  onDeleteNote(): void {
    if (this.selectedNote) {

    
    const userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('User ID not found in localStorage');
      return;
    }
   
      
      this.notesService.deleteNote(this.selectedNote.id, Number(userId));


    
  
      const index = this.userNotes.findIndex(n => n.id === this.selectedNote!.id);
      if (index > -1) {
        this.userNotes.splice(index, 1);
        this.selectedNote = this.userNotes.length > 0 ? this.userNotes[0] : null;
      }
    }
  }

  getFilteredNotes(): Note[] {
    let filtered = this.userNotes;

    if (this.selectedTag !== 'All Notes' && this.selectedTag !== 'Archived Notes') {
      filtered = filtered.filter(note => note.tags.some(tag => tag.title === this.selectedTag));
    }

    if (this.selectedTag === 'Archived Notes') {
      filtered = filtered.filter(note => note.archived);
    } else {
      filtered = filtered.filter(note => !note.archived);
    }

    if (this.searchQuery) {
      filtered = filtered.filter(note => 
        note.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        note.content.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    }

    return filtered;
  }
   onNoteSelected(note: Note) {
    this.selectedNote = note;
  }


  onNoteSaved(updatedNote: Note) {
 
    const index = this.userNotes.findIndex(n => n.id === updatedNote.id);
    if (index !== -1) {
      this.userNotes[index] = updatedNote;
      this.selectedNote = updatedNote;
      console.log('Note saved:', updatedNote);


    }
  }

  onNewNoteCreated(newNote: Note) {
    // Add the new note to the beginning of the list
    this.userNotes.unshift(newNote);
    
    // Select the newly created note for immediate editing
    this.selectedNote = newNote;
    
    console.log('New note created and selected:', newNote);
  }

  onNoteDiscarded(noteId: number) {
    // Remove the discarded note from the list
    this.userNotes = this.userNotes.filter(n => n.id !== noteId);
    
    // Select another note or clear selection
    if (this.selectedNote?.id === noteId) {
      this.selectedNote = this.userNotes.length > 0 ? this.userNotes[0] : null;
    }
  }
}
