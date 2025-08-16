import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent {

   @Input() title = '';
  @Output() searchChanged = new EventEmitter<string>();

  onSearchChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchChanged.emit(target.value);
  }

}
