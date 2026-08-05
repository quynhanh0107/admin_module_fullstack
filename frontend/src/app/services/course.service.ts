import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Course {
    id: string;
    courseCode: string;
    name: string;
    credits: number;
    courseType: string;
}

@Injectable({
    providedIn: 'root'
})

export class CourseService{
    private apiUrl = 'http://localhost:8080/api/courses';

    constructor(private http: HttpClient) {}

    // goi api lay toan bo danh sach khoa hoc
    getCourses(): Observable<Course[]> {
        return this.http.get<Course[]>(this.apiUrl);
    }

    createCourses(courseData: any): Observable<any> {
        return this.http.post<any>(this.apiUrl, courseData);
    }

    // sửa/cập nhật các khóa học (dùng PUT)
    updateCourse(id: string, courseData: any): Observable<any> {
        return this.http.put<any>(`${this.apiUrl}/${id}`, courseData);
    }

    deleteCourse(id: string): Observable<any> {
        return this.http.delete<any>(`${this.apiUrl}/${id}`);
    }
}