import { Routes } from '@angular/router';
import { Login } from './login/login';
import { authGuard } from './auth.guard';
import { Layout } from './layout/layout';
import { CourseList } from './course-list/course-list';
import { CourseCreateComponent } from './course-create/course-create';

export const routes: Routes = [
    { path: 'login', component: Login},
    //khu vực cần đăng nhập thì ms truy cập dc
    {
        path: '',
        component: Layout,
        canActivate: [authGuard],
        // các path khai báo dưới đây sẽ được render vào <router-outlet> của Layout
        children: [
            { path: 'courses', component: CourseList },
            { path: 'courses/create', component: CourseCreateComponent},
            //{ path: 'nhap-diem', canActivate: [authGuard] },

            //nếu truy cập thẳng vào "localhost:4200/", tự động chuyển hướng sang danh sách khóa học
            { path: '', redirectTo: 'courses', pathMatch: 'full'}
        ]
    },
    //xử lý đường dẫn ko tồn tại
    { path: '**', redirectTo: '/login', pathMatch: 'full' }
];
