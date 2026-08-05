import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ScoreService } from '../services/score.service';

@Component({
  selector: 'app-enter-score',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './enter-score.html',
  styleUrl: './enter-score.css',
})
export class EnterScore implements OnInit {
  courses: any[] = [];
  selectedCourseId: string = '';
  students: any[] = [];

  private scoreService = inject(ScoreService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
      const username = localStorage.getItem('username');

      if (username) {
        this.scoreService.getTeacherCourses(username).subscribe({
        next: (res: any) => {
          this.courses = res;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Lỗi lấy danh sách khóa học:', err);
        }
      });
      } else {
        console.warn('Không tìm thấy username trong bộ nhớ!');
      }
      
  }

  // Chạy khi Giáo viên chọn 1 môn học từ Dropdown
  onCourseSelect(): void {
    if (!this.selectedCourseId) {
      this.students = [];
      return;
    }

    // gọi API lấy danh sách học sinh theo khóa học
    this.scoreService.getStudentsbyCourse(this.selectedCourseId).subscribe({
      next: (res: any[]) => {
        // Backend trả về mảng StudentCourse (có chứa object student bên trong).
        // -> bóc tách (map) ra thành cấu trúc cho UI dễ hiển thị.
        this.students = res.map(item => ({
          studentId: item.student.id, // Lấy ID của học sinh
          // Lấy tên học sinh (Nếu Entity User của bạn không có fullName thì dùng username)
          studentName: item.student.username, 
          score: item.grade
        }));
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Lỗi lấy danh sách học sinh:', err);
        alert('Không thể lấy danh sách học sinh cho lớp này.');
      }
    });
  }   

  // Chạy khi bấm Lưu Điểm
  saveScores(): void {
    // Validate điểm
    const invalidScores = this.students.filter(s => s.score !== null && s.score !== undefined && (s.score < 0 || s.score > 10));
    if (invalidScores.length > 0) {
      alert('Lỗi: Điểm số phải nằm trong khoảng từ 0 đến 10!');
      return;
    }

    // Map lại dữ liệu khớp đúng với cấu trúc Backend yêu cầu (cần thuộc tính 'grade')
    const scoreDataPayload = this.students.map(s => ({
      studentId: s.studentId,
      grade: s.score // Đổi tên biến 'score' của UI về 'grade' cho Backend
    }));

    // Gọi API để lưu
    this.scoreService.saveScores(this.selectedCourseId, scoreDataPayload).subscribe({
      next: (res) => {
        alert('Lưu điểm thành công!');
        this.onCourseSelect(); 
      },
      error: (err) => {
        console.error('Lỗi khi lưu điểm:', err);
        alert('Lưu điểm thất bại. Vui lòng kiểm tra lại!');
      }
    });
  }




}
