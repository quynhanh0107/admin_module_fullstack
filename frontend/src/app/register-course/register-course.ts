import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-register-course',
  imports: [CommonModule],
  templateUrl: './register-course.html',
  styleUrl: './register-course.css',
})
export class RegisterCourse implements OnInit{
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);

  private baseUrl = environment.apiUrl;

  availableCourses: any[] = [];
  currentUserId: string = '';
  enrolledCourseIds: string[] = [];

  ngOnInit(): void {
    // extract studentId
    this.currentUserId = localStorage.getItem('userId') || '';
    
    // gọi api lấy các môn đã đăng ký trước đó
    if (this.currentUserId) {
      this.http.get<any[]>(`${this.baseUrl}/enrollments/student/${this.currentUserId}/scores`).subscribe({
        next: (res) => {
          this.enrolledCourseIds = res.map(item => item.course?.id);
          this.cdr.detectChanges();
        }
      });
    }

    this.http.get<any[]>(`${this.baseUrl}/courses`).subscribe({
      next: (res) => {
        this.availableCourses = res;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Lỗi tải danh sách khóa học:', err);
        alert('Không thể kết nối đến máy chủ để lấy danh sách lớp học!');
      }
    });
  }

  isEnrolled(courseId: string): boolean {
    return this.enrolledCourseIds.includes(courseId);
  }

  // gọi api học sinh đăng ký khóa học
  enrollCourse(courseId: string): void {
    if (!this.currentUserId) {
      alert('Lỗi: Không tìm thấy thông tin tài khoản học sinh. Vui lòng đăng nhập lại!');
      return;
    }

    const confirmMsg = confirm('Bạn có chắc chắn muốn đăng ký môn học này?');
    if (!confirmMsg) return;

    const payload = {
      studentId: this.currentUserId,
      courseId: courseId
    };

    this.http.post(`${this.baseUrl}/enrollments`, payload).subscribe({
      next: (res) => {
        alert('Đăng ký môn thành công');
        this.enrolledCourseIds.push(courseId);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Lỗi đăng ký:', err);
        const errorMsg = err.error?.message || err.error || 'Có thể bạn đã đăng ký môn này rồi.';
        alert('Đăng ký thất bại! Lỗi: ' + errorMsg);
      }
    });

  }
}
