import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { jwtDecode } from 'jwt-decode';

interface JwtPayload{
  sub : string,
  id: number,
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  // private authService = inject(AuthService); // Uncomment when you create the service

  loginForm!: FormGroup;
  isLoading = false;
  hidePassword = true;

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid && !this.isLoading) {
      this.isLoading = true;
      const { email, password } = this.loginForm.value;

      
      this.authService.login(email, password).subscribe({
        next: (response: any) => {
          if (response?.token) {
            localStorage.setItem('authToken', response.token);
            const decoded = jwtDecode<JwtPayload>(response.token);
            localStorage.setItem('userId', decoded.id.toString());
          }
          this.showMessage('Login successful!');
          this.router.navigate([`/home/${localStorage.getItem('userId')}`]);
          this.isLoading = false;
        },
        error: (err: any) => {
          this.showMessage(err.error?.message || 'Login failed. Please try again.');
          this.isLoading = false;
        }
      });
      
    } else {
      this.markFormGroupTouched();
    }
  }

  onGoogleSignIn(): void {
    // Google sign-in will do nothing for now as requested
    console.log('Google sign-in clicked - no action implemented yet');
  }

  navigateToSignUp(): void {
    this.router.navigate(['/signup']); // Make sure you have this route configured
  }

  navigateToForgotPassword(): void {
    this.router.navigate(['/auth/forgot-password']); // Configure this route as needed
  }

  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  private showMessage(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'bottom'
    });
  }

  getErrorMessage(fieldName: string): string {
    const control = this.loginForm.get(fieldName);
    if (control?.hasError('required')) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
    }
    if (control?.hasError('email')) {
      return 'Please enter a valid email address';
    }
    if (control?.hasError('minlength')) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} must be at least 6 characters`;
    }
    return '';
  }
}
