// các hàm gọi api lấy danh sách lớp, lấy danh sách học sinh, và lưu điểm
import { Injectable, inject } from "@angular/core";
import { HttpClient } from '@angular/common/http'
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ScoreService {
    private http = inject(HttpClient);
    private baseApi = "http://localhost:8080/api/enrollments";

    getTeacherCourses(username: string): Observable<any> {
        return this.http.get<any>(`${this.baseApi}/teacher/${username}/courses`);
    }

    getStudentsbyCourse(courseId: string) {
        return this.http.get<any>(`${this.baseApi}/course/${courseId}/students`);
    }

    // gửi điểm + courseid của học sinh vào server
    saveScores(courseId: string, scoreData: any[]): Observable<any> {
        const payload = {
            courseId: courseId,
            scores: scoreData
        };
        return this.http.post<any>(`${this.baseApi}/scores`, payload)
    }
}