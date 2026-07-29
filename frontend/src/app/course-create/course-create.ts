import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CourseService } from '../services/course.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-course-create',
  templateUrl: './course-create.html',
  styleUrls: ['./course-create.css'],
  imports: [CommonModule, FormsModule, RouterModule]
})
export class CourseCreateComponent {
  //object hứng dữ liệu từ giao diện
  course = {
    courseCode: '',
    name: '',
    credits: 3, // default: 3 tín chỉ
    courseType: 'BẮT BUỘC'
  };
  errorMessage = '';

  constructor(private courseService: CourseService, private router: Router) {}

  onSubmit(): void {
    this.courseService.createCourses(this.course).subscribe({
      next: (res) => {
        alert('Thêm khóa học thành công!');
        this.router.navigate(['/courses']); // trở về trang danh sách
      },
      error: (err) => {
        console.error('Lỗi thêm khóa học', err);
        // Bắt lỗi từ backend như trùng mã môn học hoặc Lỗi 403 thiếu quyền)
        this.errorMessage = err.error || 'Có lỗi xảy ra, không thể tạo khóa học!';
      }
    });
  }
}