import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from './auth';

//CanActivateFn: a route guard that determines whether a route can be accessed based on some conditions
// e.g. authentication/permissions...
// return type: boolean (true: navigation continues/false: navigation cancelled)
export const authGuard: CanActivateFn = (route, state) => {
    const authService = inject(Auth);
    const router = inject(Router);
    const token = authService.getAccessToken();
    // check xem token co hop le
    if (token) {
        return true;
    } else {
        // state.url: đính kèm returnUrl khi bị văng ra
        // vd: user cố vào trang /dashboard nhưng chưa đăng nhập thành công
        // state.url sẽ đính kèm /dashboard khi văng user ra 
        // khi họ đăng nhập thành công thì sẽ chuyển tiếp tới /dashboard luôn
        router.navigate(['/login'], { queryParams: { returnUrl: state.url} });
        return false;
    }
}
