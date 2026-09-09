// kết nối với api từ backend

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

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
    private baseUrl = environment.apiUrl;

    private usersUrl = `${this.baseUrl}/users`;
    private rolesUrl = `${this.baseUrl}/roles`;
    private actionsUrl = `${this.baseUrl}/actions`;

    // gọi api lấy danh sách user
    getUser(): Observable<User[]> {
        return this.http.get<User[]>(this.usersUrl);
    }

    // gọi api từ backend để lấy danh sách role
    getRoles(): Observable<any> {
        return this.http.get(this.rolesUrl);
    }

    // gọi api lấy danh sách các quyền
    getActions(): Observable<any> {
        return this.http.get(this.actionsUrl);
    }

    updateRoleActions(roleName: string, actionCodes: string[]): Observable<any> {
        const payload = {
            roleName: roleName,
            actionCodes: actionCodes
        };
        return this.http.post(`${this.rolesUrl}/assign-action`, payload);
    }

    // gọi api đăng ký tài khoản
    registerUser(payload: any): Observable<any> {
        return this.http.post(`${this.usersUrl}/register`, payload);
    }

    // gọi api gán role cho user
    assignRole(username: string, roleName: string): Observable<any> {
        return this.http.post(`${this.usersUrl}/assign-role`, {username, roleName});
    }

        // gọi api create role (trong RoleController.java)
    createRole(payload: { roleName: string }): Observable<any> {
        return this.http.post(this.rolesUrl, payload);
    }

    // gọi api xóa quyền của 1 user cụ thể
    revokeRole(username: string, roleName: string): Observable<any> {
        return this.http.delete<any>(`${this.usersUrl}/${username}/roles/${roleName}`);
    }

    // 2. API Xóa hoàn toàn 1 quyền khỏi hệ thống (Trang Quản lý Quyền)
    deleteSystemRole(roleName: string): Observable<any> {
        return this.http.delete<any>(`${this.rolesUrl}/${roleName}`);
    }

    // gọi api xóa người dùng
    deleteUser(username: string): Observable<any> {
        // Gọi DELETE tới http://localhost:8080/api/users/{username}
        return this.http.delete<any>(`${this.usersUrl}/${username}`);
    }
}