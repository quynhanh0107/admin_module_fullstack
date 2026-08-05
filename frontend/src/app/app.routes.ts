import { Routes } from '@angular/router';
import { Login } from './login/login';
import { authGuard } from './auth.guard';
import { Layout } from './layout/layout';
import { CourseList } from './course-list/course-list';
import { CourseCreateComponent } from './course-create/course-create';
import { UserList } from './user-list/user-list';
import { RoleList } from './role-list/role-list';
import { Dashboard } from './dashboard/dashboard';
import { EnterScore } from './enter-score/enter-score';
import { CourseAssignment } from './course-assignment/course-assignment';
import { RegisterCourse } from './register-course/register-course';
import { MyScores } from './my-scores/my-scores';
import { AdminOverview } from './admin-overview/admin-overview';

export const routes: Routes = [
    // nếu gõ localhost:4200 (ko thêm đuôi), tự động ném về /login
    { path: '', redirectTo: 'login', pathMatch:'full'},
    
    { path: 'login', component: Login},


    //khu vực cần đăng nhập thì ms truy cập dc
    {
        path: '',
        component: Layout,
        canActivate: [authGuard],
        // các path khai báo dưới đây sẽ được render vào <router-outlet> của Layout
        children: [
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
            // Trang tổng quan đầu tiên sau khi đăng nhập thành công
            { path: 'dashboard', component: Dashboard },
            { path: 'courses', component: CourseList },
            { path: 'courses/create', component: CourseCreateComponent},

            // nhập điểm (dành cho giáo viên)
            { path: 'enter-score', component: EnterScore },

            // đăng ký khóa học (dành cho học sinh)
            { path: 'register-course', component: RegisterCourse },
            { path: 'my-scores', component: MyScores },
            
            // Các trang Quản trị (Dành cho Admin)
            { path: 'users', component: UserList },
            { path: 'course-assignment', component: CourseAssignment},
            { path: 'roles', component: RoleList },
            { path: 'admin-overview', component: AdminOverview}
        ]
    },
    //xử lý đường dẫn ko tồn tại
    { path: '**', redirectTo: 'login', pathMatch: 'full' }
];
