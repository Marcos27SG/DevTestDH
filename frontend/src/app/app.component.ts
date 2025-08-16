import { Component } from '@angular/core';
import { Tag } from './models/tag.model';


export interface Note {
  id: number;
  title: string;
  content: string;
  tags: Tag[];
  lastEdited: Date;
  archived: boolean;
  isNew?: Boolean;
}



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
}

