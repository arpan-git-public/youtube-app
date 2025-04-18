import { NgModule } from '@angular/core';
import { AuthModule } from 'angular-auth-oidc-client';


@NgModule({
    imports: [AuthModule.forRoot({
        config: {
            authority: 'http://dev-rp1uvpzel4sj0e61.us.auth0.com',
            redirectUrl: window.location.origin,
            postLogoutRedirectUri: window.location.origin,
            clientId: 'khrkJ6pmM78Bfx2Yx4wdHAYkrnKUG2xd',
            scope: 'openid profile offline_access email',
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            secureRoutes: ['http://localhost:8080'],
            customParamsAuthRequest: {
              audience: 'http://localhost:8080'
            }
        }
      })],
    exports: [AuthModule],
})
export class AuthConfigModule {}
