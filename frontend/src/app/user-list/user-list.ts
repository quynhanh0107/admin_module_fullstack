// gọi api getUsers() và in ra bảng HTML

import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService, User } from '../services/user.service';


@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-list.html',
})
export class UserList implements OnInit{
  private userService = inject(UserService);
  private cdr = inject(ChangeDetectorRef);

  users: User[] = [];
  roles: any[] = [];

  userErrorMessage: string = '';
  roleErrorMessage: string = '';

  // các biến quản lý role popup
  isPopOpen: boolean = false;
  selectedUser: User | null = null;
  selectedRoleName: string = '';

  ngOnInit(): void {
      this.fetchUsers();
      this.fetchRoles();
  }

  fetchUsers(): void {
    this.userService.getUser().subscribe({
      next: (res:any) => {
        this.users = res.content || res.data || res;
        this.userErrorMessage = ''; // clear lỗi cũ
        this.cdr.detectChanges(); // Ép Angular cập nhật HTML ngay lập tức
      },
      error: (err) => {
        console.error('Lỗi lấy danh sách user:', err);
        this.userErrorMessage = 'Không thể tải danh sách người dùng.';
        this.cdr.detectChanges();
      }
    });
  }

  fetchRoles(): void {
    this.userService.getRoles().subscribe({
      next: (res:any) => {
        this.roles = res.content || res.data || res;
        this.roleErrorMessage = '';
      },
      error: (err) => {
        console.error('Lỗi lấy danh sách quyền:', err);
        this.roleErrorMessage = "Không thể tải danh sách các quyền";
        this.cdr.detectChanges();
      }
    });
  }

  //idea: bấm vào nút "Cấp vai trò" và hiện ra Popup chứa danh sách các roles
  // ở định dạng dropdown để chọn
  // logic đóng mở pop up
  openAssignRolePopup(user: User): void {
    this.selectedUser = user;
    this.selectedRoleName = ''; // reset lại dropdown
    this.isPopOpen = true;
  }

  closePopup(): void {
    this.isPopOpen = false;
    this.selectedUser = null;
  }

  // gọi api lưu vai trò
  saveAssignRole(): void {
    if (!this.selectedUser || !this.selectedRoleName) return;

    this.userService.assignRole(this.selectedUser.username, this.selectedRoleName).subscribe({
      next: () => {
        alert(`Đã cấp quyền ${this.selectedRoleName} cho tài khoản ${this.selectedUser?.username} thành công!`);
        this.closePopup();
        this.fetchUsers(); // load lại danh sách để cập nhật giao diện
      },
      error: (err) => {
        console.error('Lỗi khi cấp quyền', err);
        alert('Cấp quyền thất bại. Vui lòng thử lại!');
      }
    });
  }

  // các biến quản lý thêm tài khoản mới
  isCreatedPopOpen: boolean = false;
  newUser = { username: '', password: ''};

  openCreateUserPop(): void {
    this.newUser = { username: '', password: ''};
    this.isCreatedPopOpen = true;
  }

  closeCreateUserPop(): void {
    this.isCreatedPopOpen = false;
  }

  // hàm gọi api tạo tài khoản
  confirmCreateUser(): void {
    if (!this.newUser.username || !this.newUser.password) {
      alert('Vui lòng nhập đầy đủ Tài khoản và Mật khẩu!');
      return;
    }

    this.userService.registerUser(this.newUser).subscribe({
      next: () => {
        alert('Tạo tài khoản thành công!');
        this.closeCreateUserPop();
        this.fetchUsers();
      },
      error: (err) => {
        console.error('Lỗi khi tạo user:', err);
        alert('Tạo tài khoản thất bại (Có thể do trùng tên). Vui lòng kiểm tra lại!');
      }
    });
  }

  // hàm thu hồi quyền từ 1 user
  removeRoleFromUser(user: User, role: any): void {
    const roleName = role.name || role;
    
    // hiện thông báo xác nhận trước khi xóa
    const isConfirm = confirm(`Bạn có chắc chắn muốn thu hồi quyền [${roleName}] của tài khoản [${user.username}]?`);
    
    if (isConfirm) {
      this.userService.revokeRole(user.username, roleName).subscribe({
        next: () => {
          alert('Thu hồi quyền thành công!');
          this.fetchUsers(); // Load lại danh sách để cập nhật HTML
        },
        error: (err) => {
          console.error('Lỗi khi thu hồi quyền:', err);
          alert('Thu hồi quyền thất bại. Vui lòng thử lại!');
        }
      });
    }
  }

  // hàm xóa hoàn toàn 1 người dùng
  deleteUser(user: User): void {
    const isConfirm = confirm(`CẢNH BÁO: Bạn có chắc chắn muốn xóa hoàn toàn tài khoản [${user.username}] không? Hành động này không thể hoàn tác!`);
    
    if (isConfirm) {
      this.userService.deleteUser(user.username).subscribe({
        next: () => {
          alert('Đã xóa tài khoản thành công!');
          this.fetchUsers(); // Load lại danh sách để cập nhật HTML
        },
        error: (err) => {
          console.error('Lỗi khi xóa tài khoản:', err);
          alert('Xóa tài khoản thất bại (Có thể do người dùng này đang có dữ liệu liên kết như điểm số). Vui lòng kiểm tra lại!');
        }
      });
    }
  }
}

