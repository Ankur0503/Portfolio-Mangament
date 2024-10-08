import { inject, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { SigninComponent } from './components/signin/signin.component';
import { SignupComponent } from './components/signup/signup.component';
import { ExploreComponent } from './components/explore/explore.component';
import { InvestmentsComponent } from './components/investments/investments.component';
import { FundDetailsComponent } from './components/fund-details/fund-details.component';
import { AuthService } from './services/auth-service/auth.service';
import { CartComponent } from './components/cart/cart.component';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';

export const guard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  return inject(AuthService).canActivate()
}

const routes: Routes = [
  {
    path: "",
    component: HomeComponent
  },
  {
    path: "signin",
    component: SigninComponent
  },
  {
    path: "signup",
    component: SignupComponent
  },
  {
    path: "funds/filter",
    component: ExploreComponent
  },
  {
    path: "funds/user/investments",
    component: InvestmentsComponent,
    canActivate:[guard]
  },
  {
    path: "funds/:fundName",
    component: FundDetailsComponent,
    canActivate: [guard]
  },
  {
    path: "user/cart",
    component: CartComponent,
    canActivate: [guard]
  },
  {
    path: "user/profile",
    component: ProfileComponent,
    canActivate: [guard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
