// course-assignment.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CourseAssignmentService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/assignments`;

  // Hàm gọi API phân công
  assignCourse(payload: { courseId: string, teacherId: string, roleType: string }): Observable<any> {
    return this.http.post<any>(this.apiUrl, payload);
  }
}