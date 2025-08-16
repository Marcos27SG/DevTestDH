import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Note } from 'src/app/app.component';
import { MatDialog } from '@angular/material/dialog';
import { DeleteNoteDialogComponent } from '../delete-note-dialog/delete-note-dialog.component';
import { NotesService } from 'src/app/services/notes.service';
@Component({
  selector: 'app-right-actions-panel',
  templateUrl: './right-actions-panel.component.html',
  styleUrls: ['./right-actions-panel.component.scss']
})
export class RightActionsPanelComponent {
 @Input() note: Note | null = null;
  @Output() archiveNote = new EventEmitter<void>();
  @Output() deleteNote = new EventEmitter<void>();

  constructor(private dialog: MatDialog, private noteService: NotesService){}

  onArchiveClick(): void {
    if (this.note) {
      this.archiveNote.emit();
    }
  }

  onDeleteClick(): void {
    if (this.note) {
      //this.deleteNote.emit();
         const dialogRef = this.dialog.open(DeleteNoteDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // User confirmed delete
        this.deleteNote.emit();
      }
    });
    }
  }
    

}
