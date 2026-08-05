// kết nối với api từ backend

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
    id: string;
    username: string;
    roles?: any[];
}

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:8080/api/users';

    // gọi api lấy danh sách user
    getUser(): Observable<User[]> {
        return this.http.get<User[]>(this.apiUrl);
    }

    // gọi api từ backend để lấy danh sách role
    getRoles(): Observable<any> {
        return this.http.get('http://localhost:8080/api/roles');
    }

    // gọi api lấy danh sách các quyền
    getActions(): Observable<any> {
        return this.http.get('http://localhost:8080/api/actions');
    }

    updateRoleActions(roleName: string, actionCodes: string[]): Observable<any> {
        const payload = {
            roleName: roleName,
            actionCodes: actionCodes
        };
        return this.http.post("http://localhost:8080/api/roles/assign-action", payload);
    }

    // gọi api đăng ký tài khoản
    registerUser(payload: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, payload);
    }

    // gọi api gán role cho user
    assignRole(username: string, roleName: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/assign-role`, {username, roleName});
    }

        // gọi api create role (trong RoleController.java)
    createRole(payload: { roleName: string }): Observable<any> {
        return this.http.post('http://localhost:8080/api/roles', payload);
    }

    // gọi api xóa quyền của 1 user cụ thể
    revokeRole(username: string, roleName: string): Observable<any> {
        return this.http.delete<any>(`${this.apiUrl}/${username}/roles/${roleName}`);
    }

    // 2. API Xóa hoàn toàn 1 quyền khỏi hệ thống (Trang Quản lý Quyền)
    deleteSystemRole(roleName: string): Observable<any> {
        return this.http.delete<any>(`http://localhost:8080/api/roles/${roleName}`);
    }

    // gọi api xóa người dùng
    deleteUser(username: string): Observable<any> {
        // Gọi DELETE tới http://localhost:8080/api/users/{username}
        return this.http.delete<any>(`${this.apiUrl}/${username}`);
    }
}