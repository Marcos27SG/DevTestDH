import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Note } from './../../app.component';
import { ActivatedRoute } from '@angular/router';
import { Tag } from 'src/app/models/tag.model';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
    selectedNavItem = 'all';
      selectedNote: Note | null = null;
   @Input() tags: Tag[] = [];
  @Input() selectedTag = '';
  @Output() tagSelected = new EventEmitter<string>();

  constructor(private route: ActivatedRoute){}

  onTagClick(tagName: string): void {
    this.tagSelected.emit(tagName);
  
    
  }

   selectNavItem(item: string) {
    this.selectedNavItem = item;
    this.selectedTag = '';
    this.selectedNote = null;
  }

}
