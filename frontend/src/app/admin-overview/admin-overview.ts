import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-admin-overview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-overview.html'
})
export class AdminOverview implements OnInit {
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);

  private baseUrl = `${environment.apiUrl}/enrollments`;

  allData: any[] = [];
  
  // Các biến lưu con số thống kê
  totalEnrollments: number = 0;
  totalGraded: number = 0;
  totalPending: number = 0;

  ngOnInit(): void {
    // gọi api lấy toàn bộ dữ liệu hệ thống
    this.http.get<any[]>(`${this.baseUrl}/all`).subscribe({
      next: (res) => {
        this.allData = res;
        this.calculateStats(); 
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Lỗi tải dữ liệu:', err);
      }
    });
  }

  calculateStats(): void {
    this.totalEnrollments = this.allData.length;
    
    // Đếm số bài đã có điểm và chưa có điểm
    this.totalGraded = this.allData.filter(item => item.grade !== null && item.grade !== undefined).length;
    this.totalPending = this.totalEnrollments - this.totalGraded;
  }
}