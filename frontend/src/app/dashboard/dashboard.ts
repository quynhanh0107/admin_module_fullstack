import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  username: string = 'Người dùng';

  ngOnInit(): void {
      const storedName = localStorage.getItem('username');
      if (storedName) {
        this.username = storedName;
      }
  }
}
