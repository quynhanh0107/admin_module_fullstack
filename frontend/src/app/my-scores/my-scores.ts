import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-my-scores',
  imports: [CommonModule],
  templateUrl: './my-scores.html',
  styleUrl: './my-scores.css',
})
export class MyScores implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);
  private baseUrl = environment.apiUrl;

  currentUserId: string = '';
  myScores: any[] = [];
  averageScore: number = 0;

  ngOnInit(): void {


    // Lấy ID học sinh từ Local Storage
    this.currentUserId = localStorage.getItem('userId') || '';

    if (!this.currentUserId) {
      alert('Không tìm thấy thông tin tài khoản. Vui lòng đăng nhập lại!');
      return;
    }

    // gọi API lấy danh sách môn đã đăng ký kèm điểm số của học sinh
    this.http.get<any[]>(`${this.baseUrl}/enrollments/student/${this.currentUserId}/scores`).subscribe({
      next: (res) => {
        this.myScores = res; // Gán dữ liệu thật vào mảng
        this.calculateGPA(); // Tính toán lại điểm GPA ngay lập tức
        this.cdr.detectChanges(); // Ép Angular vẽ lại bảng điểm
      },
      error: (err) => {
        console.error('Lỗi tải bảng điểm:', err);
        alert('Không thể kết nối đến máy chủ để tải bảng điểm!');
      }
    });
  }

  // Hàm tính điểm trung bình (Chỉ tính các môn ĐÃ CÓ ĐIỂM)
  calculateGPA(): void {
    let totalScore = 0;
    let totalCredits = 0;

    this.myScores.forEach(item => {
      // Kiểm tra kỹ tránh trường hợp điểm là null hoặc chưa nhập
      if (item.score !== null && item.score !== undefined) {
        const credits = item.course?.credits || 0;
        totalScore += item.score * credits;
        totalCredits += credits;
      }
    });

    this.averageScore = totalCredits > 0 ? (totalScore / totalCredits) : 0;
  }
}


