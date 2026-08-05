// course-assignment.ts
import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseAssignmentService } from '../services/course-assignment.service';
import { CourseService } from '../services/course.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-course-assignment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './course-assignment.html'
})
export class CourseAssignment implements OnInit {
  private assignmentService = inject(CourseAssignmentService);
  private courseService = inject(CourseService);
  private userService = inject(UserService);

  courses: any[] = [];
  teachers: any[] = [];

  
  assignmentData = {
    courseId: '',
    teacherId: '',
    roleType: 'GIANG_VIEN_CHINH'
  };

  ngOnInit(): void {
    // Tạm dùng Mock data. Thực tế bạn sẽ gọi API lấy danh sách:
    // this.courseService.getCourses().subscribe(res => this.courses = res);
    // this.userService.getTeachers().subscribe(res => this.teachers = res);

    this.courseService.getCourses().subscribe({
      next: (res) => {
        this.courses = res;
      },
      error: (err) => {
        console.error('Lỗi khi tải danh sách khóa học:', err);
        alert('Không thể tải danh sách khóa học từ máy chủ!');
      }
    });

    // gọi API lấy danh sách GV
    this.userService.getUser().subscribe({
      next: (res) => {
        this.teachers = res;
      },
      error: (err) => {
        console.error('Lỗi khi tải danh sách giáo viên:', err);
        alert('Không thể tải danh sách giáo viên từ máy chủ!');
      }
    });
  }

  resetForm(): void {
    this.assignmentData = {
      courseId: '',
      teacherId: '',
      roleType: 'GIANG_VIEN_CHINH'
    };
  }

  // Hàm xử lý khi bấm nút Phân công
  submitAssignment(): void {
    if (!this.assignmentData.courseId || !this.assignmentData.teacherId) {
      alert('Vui lòng chọn đầy đủ Khóa học và Giáo viên!');
      return;
    }

    this.assignmentService.assignCourse(this.assignmentData).subscribe({
      next: (res) => {
        alert('Phân công giảng dạy thành công!');
        
        // Reset form sau khi thành công
        this.assignmentData.courseId = '';
        this.assignmentData.teacherId = '';
        this.assignmentData.roleType = 'GIANG_VIEN_CHINH';
      },
      error: (err) => {
        console.error('Lỗi phân công:', err);
        // Bắt chính xác câu chữ lỗi từ Backend (nếu có)
        const errorMsg = err.error?.message || err.error || 'Phân công thất bại. Có thể môn học đã được phân công cho giáo viên này.';
        alert('Lỗi: ' + errorMsg);
      }
    });
  }
}