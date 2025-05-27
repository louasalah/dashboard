import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { MaterialModule } from 'src/app/material.module';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import {LoginRequest, LoginService} from "../login.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-side-login',
  standalone: true,
  imports: [RouterModule, MaterialModule, FormsModule, ReactiveFormsModule, NgIf],
  templateUrl: './side-login.component.html',
})
export class AppSideLoginComponent {

  constructor( private router: Router,private authService: LoginService) {}

  form = new FormGroup({
    uname: new FormControl('', [Validators.required, Validators.minLength(6)]),
    password: new FormControl('', [Validators.required]),
  });
  loginData: LoginRequest = {
    email: '',
    password: ''
  };
  get f() {
    return this.form.controls;
  }
  errorMessage: string = ''; // add this at the top of your component

  onLogin() {
    this.errorMessage = ''; // clear previous errors

    if (!this.loginData.email || !this.loginData.password) {
      this.errorMessage = 'Email and password are required';
      return;
    }
    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        console.log('Logged in! Token:', response.token);
        localStorage.setItem('token', response.token);
        // optionally navigate to dashboard
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Login error:', err);
        this.errorMessage = 'Invalid email or password. Please try again.';
      }
    });
  }
  submit() {
    // console.log(this.form.value);
    this.router.navigate(['/']);
  }
}
