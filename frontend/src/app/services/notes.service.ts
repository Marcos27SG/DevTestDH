import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotesService {
  private apiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) { }

  // In your note.service.ts
  createNote(noteData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/notes`, noteData);
  }

  updateNote(noteId: number, userId: number ,noteData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/notes/${noteId}/user/${userId}`, noteData);
  }

  getNotes(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/notes/user/${userId}`);
  }

  deleteNote(noteId: number , userId: number): Observable<void> {

    return this.http.delete<void>(`${this.apiUrl}/notes/${noteId}/user/${userId}`);
  }
}
