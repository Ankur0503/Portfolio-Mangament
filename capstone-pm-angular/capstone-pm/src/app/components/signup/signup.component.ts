import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validateEmail } from '../../validators/email-validator';
import { validatePassword } from '../../validators/password-validator';
import { validatePhone } from '../../validators/phone-validator';
import { HttpClient } from '@angular/common/http';
import { User } from 'src/app/models/user-model/user';
import { AuthService } from 'src/app/services/auth-service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  name: FormControl
  email: FormControl
  password: FormControl
  phone: FormControl

  signupForm: FormGroup

  constructor(public http: HttpClient, public authService: AuthService, public router: Router) {
    this.name = new FormControl('', [Validators.required])
    this.email = new FormControl('', [Validators.required, validateEmail()])
    this.password = new FormControl('', [Validators.required, Validators.minLength(8), validatePassword()])
    this.phone = new FormControl('', [Validators.required, validatePhone()])

    this.signupForm = new FormGroup({
      name: this.name,
      email: this.email,
      password: this.password,
      phone: this.phone
    })
  }

  signUpOnSubmit() {
    let user: User = {
      userName: this.signupForm.value.name,
      userEmail: this.signupForm.value.email,
      userPassword: this.signupForm.value.password,
      userPhone: this.signupForm.value.phone,
      userId: 0
    }
    this.authService.register(user).subscribe(response => {
      this.router.navigateByUrl("/signin")
    },
    err => {
      console.error('SignUp Failed', err)
    })
  }
}