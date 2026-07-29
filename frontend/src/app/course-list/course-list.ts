import { Component, OnInit} from '@angular/core';
import { Course, CourseService } from '../services/course.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-course-list',
  imports: [CommonModule, RouterModule],
  templateUrl: './course-list.html',
  styleUrl: './course-list.css',
})
export class CourseList implements OnInit {
  courses: Course[] = [];
  errorMessage: string='';

  constructor(private courseService: CourseService) {

  }

  ngOnInit(): void {
    console.log("1. trang danh sách đã mở");
    this.fetchCourses();
  }

  fetchCourses(): void {
    this.courseService.getCourses().subscribe({
      next: (response:any) => {
        console.log("2. dữ liệu angular nhận được");
        //this.courses = data; // để render trên html
        // Cơ chế bóc tách an toàn
        if (response && response.content) {
          this.courses = response.content; 
        } else if (response && response.data) {
          this.courses = response.data;
        } else {
          this.courses = response || []; 
        }

        console.log("3. Số lượng khóa học sau khi gán:", this.courses.length);
      },
      error: (err) => {
        console.error('Lỗi khi lấy dữ liệu', err);
        this.errorMessage = 'Không thể tải danh sách khóa học. Vui lòng thử lại!';

      }
    });
  }
}
