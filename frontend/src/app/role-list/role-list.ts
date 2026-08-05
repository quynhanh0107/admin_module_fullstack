import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService, User } from '../services/user.service';

@Component({
  selector: 'app-role-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './role-list.html',
  styleUrl: './role-list.css',
})
export class RoleList {
  private roleService = inject(UserService);
  private cdr = inject(ChangeDetectorRef);

  roles: any[] = [];
  allActions: any[] = [];
  errorMessage: string = '';

  isPopOpen: boolean = false;
  selectedRole: any = null;
  selectedActionCodes: string[] = [];

  ngOnInit(): void {
    this.fetchRoles();
    this.fetchActions();
  }

  fetchRoles(): void {
    this.roleService.getRoles().subscribe({
      next: (res: any) => {
        this.roles = res.content || res.data || res;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Không thể tải danh sách Vai trò.';
        this.cdr.detectChanges();
      }
    });
  }

  fetchActions(): void {
    this.roleService.getActions().subscribe({
      next: (res: any) => this.allActions = res.content || res.data || res,
      error: (err) => console.error('Lỗi tải actions', err)
    });
  }

  openConfigPopup(role: any): void {
    this.selectedRole = role;
    
    // nếu role có sẵn trong DB, cho các mã code vào mảng để checkbox sáng lên
    if (role.actions && role.actions.length > 0) {
      this.selectedActionCodes = role.actions.map((a: any) => a.code);
    } else {
      this.selectedActionCodes = []; // reset lại từ đầu cho các role mới
    }
    this.isPopOpen = true;
  
  }
  closeModal(): void {
    this.isPopOpen = false;
    this.selectedRole = null;
    this.selectedActionCodes = [];
  }

  toggleAction(actionCode: string, event: any): void {
    const isChecked = event.target.checked;

    if(isChecked) {
      this.selectedActionCodes.push(actionCode);
    } else {
      // nếu bỏ tick -> lọc bỏ mã code đó khỏi mảng
      this.selectedActionCodes = this.selectedActionCodes.filter(code => code !== actionCode);
    }
  }

  // lưu xuống DB
  saveRoleActions(): void {
    if (!this.selectedRole) return;

    this.roleService.updateRoleActions(this.selectedRole.name, this.selectedActionCodes).subscribe({
      next: () => {
        alert('Cập nhật quyền thành công!');
        this.closeModal();
        this.fetchRoles(); // Load lại bảng để thấy quyền mới
      },
      error: (err) => {
        console.error('Lỗi lưu quyền:', err);
        alert('Lỗi cập nhật quyền. Xem Console!');
      }
    });
  }

  // Tạo Modal cho +Thêm vai trò
  isCreateRolePopOpen: boolean = false;
  newRoleName: string = '';

  openCreateRoleModal(): void {
    this.newRoleName = ''; // Xóa trắng ô input trước khi mở
    this.isCreateRolePopOpen = true;
  }

  closeCreateRoleModal(): void {
    this.isCreateRolePopOpen = false;
  }

  // --- 3. HÀM GỌI API LƯU ROLE VÀO DATABASE ---
  confirmCreateRole(): void {
    if (!this.newRoleName.trim()) {
      alert('Vui lòng nhập tên vai trò!');
      return;
    }

    const payload = { roleName: this.newRoleName.trim() };

    this.roleService.createRole(payload).subscribe({
      next: () => {
        alert('Tạo vai trò thành công!');
        this.closeCreateRoleModal();
        this.fetchRoles(); // Load lại bảng để thấy Role mới ngay lập tức
      },
      error: (err) => {
        console.error('Lỗi khi tạo Role:', err);
        alert('Tạo thất bại (Có thể do trùng tên). Vui lòng thử lại!');
      }
    });
  }
}
