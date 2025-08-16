import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Tag } from '../models/tag.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TagService {
  private apiUrl = 'http://localhost:8080/api/v1/tags';

  constructor(private http: HttpClient) { }

  
  getUserTags(userId: string): Observable<Tag[]> {
    return this.http.get<Tag[]>(`${this.apiUrl}/user/${userId}`);
  }

  
}
