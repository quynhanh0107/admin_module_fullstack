import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { Course, CourseService } from '../services/course.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 

@Component({
  selector: 'app-course-list',
  standalone: true, 
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './course-list.html',
  styleUrl: './course-list.css',
})
export class CourseList implements OnInit {
  courses: Course[] = [];
  errorMessage: string = '';

  // Các biến quản lý popup thêm khóa học
  isCreatePopOpen: boolean = false;
  newCourse: any = { courseCode: '', name: '', credits: 2, courseType: 'BẮT BUỘC' };

  // các biến quản lý trạng thái sửa
  isEditMode: boolean = false;
  editingCourseId: string = '';

  private courseService = inject(CourseService);
  private cdr = inject(ChangeDetectorRef); // Công cụ đồng bộ giao diện

  ngOnInit(): void {
    this.fetchCourses();
  }

  fetchCourses(): void {
    this.courseService.getCourses().subscribe({
      next: (response: any) => {
        // Cơ chế bóc tách an toàn
        if (response && response.content) {
          this.courses = response.content; 
        } else if (response && response.data) {
          this.courses = response.data;
        } else {
          this.courses = response || []; 
        }
        
        this.errorMessage = '';
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.error('Lỗi khi lấy dữ liệu', err);
        this.errorMessage = 'Không thể tải danh sách khóa học. Vui lòng thử lại!';
        this.cdr.detectChanges();
      }
    });
  }

  openCreateCoursePop(): void {
    this.isEditMode = false;
    this.editingCourseId = '';
    // Reset lại form trống trước khi mở
    this.newCourse = { courseCode: '', name: '', credits: 2, courseType: 'BẮT BUỘC' };
    this.isCreatePopOpen = true;
  }

  // mở popup ở chế độ sửa
  openEditCoursePop(course: Course): void {
    this.isEditMode = true;
    this.editingCourseId = course.courseCode;

    this.newCourse = {...course}; // copy dữ liệu khóa học vào form
    this.isCreatePopOpen = true;
  }

  closeCreateCoursePop(): void {
    this.isCreatePopOpen = false;
  }

  // Gọi API lưu khóa học
  confirmCreateCourse(): void {
    // Validate cơ bản
    if (!this.newCourse.courseCode || !this.newCourse.name || !this.newCourse.credits) {
      alert('Vui lòng nhập đầy đủ thông tin khóa học!');
      return;
    }

    // nếu ở chế độ sửa
    if (this.isEditMode) {
      this.courseService.updateCourse(this.editingCourseId, this.newCourse).subscribe({
        next: () => {
          alert('Cập nhật khóa học thành công');
          this.closeCreateCoursePop();
          this.fetchCourses();
        },
        error: (err) => {
          console.error('Lỗi cập nhật:', err);
          alert('Cập nhật thất bại. Vui lòng thử lại!');
        }
      });
    } else { // nếu ở chế độ thêm mới
        this.courseService.createCourses(this.newCourse).subscribe({
        next: () => {
          alert('Thêm khóa học thành công!');
          this.closeCreateCoursePop(); // Đóng popup
          this.fetchCourses(); // Load lại bảng danh sách
        },
        error: (err) => {
          console.error('Lỗi tạo khóa học:', err);
          alert('Thêm khóa học thất bại (Có thể do trùng mã). Vui lòng kiểm tra lại!');
        }
      });
    }
  }

  // hàm xử lý nút Xóa
  deleteCourse(course: Course): void {
    if (confirm(`Bạn có chắc chắn muốn xóa khóa học [${course.name}] không?`)) {
      this.courseService.deleteCourse(course.courseCode).subscribe({
        next: () => {
          alert('Xóa khóa học thành công!');
          this.fetchCourses(); // Tải lại bảng HTML
        },
        error: (err) => {
          console.error('Lỗi khi xóa:', err);
          alert('Không thể xóa khóa học này. Vui lòng thử lại!');
        }
      });
    }
  }
}