import { Component, OnInit } from '@angular/core';
import { RouterLinkActive, RouterLinkWithHref, RouterOutlet, Router } from "@angular/router";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterLinkActive, RouterLinkWithHref, RouterOutlet],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout implements OnInit{
  username: string = 'Người dùng';
  userActions: string[] = [];

  constructor(private router: Router) {}

  ngOnInit(): void {
    // lấy tên và quyền từ localStorage sau khi Login thành công
      this.username = localStorage.getItem('username') || 'Người dùng';

      const storedActions = localStorage.getItem('actions');
      if (storedActions) {
        this.userActions = JSON.parse(storedActions);
      }
  }

  isAdmin(): boolean {
    return this.userActions.includes('ROLE_ADMIN');
  }

  //hàm ktra xem user có quyền này ko để HTML quyết định ẩn hay hiện
  hasAction(actionCode: string): boolean {
    // Admin -> return true (thấy hết)
    // khác -> check trong mảng
    if(this.userActions.includes('ROLE_ADMIN')) return true;

    return this.userActions.includes(actionCode);
  }

  onLogout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('actions');
    this.router.navigate(['/login']);
  }
}
