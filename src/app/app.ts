import { Component, HostListener, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Auth } from '@angular/fire/auth';
import { Firestore, doc, getDoc } from '@angular/fire/firestore';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class AppComponent implements OnInit {
  isScrolled = false;
  menuOpen = false;
  isAdmin = false;

  constructor(private auth: Auth, private firestore: Firestore) {}

  async ngOnInit() {
    this.auth.onAuthStateChanged(async (user) => {
      if (user) {
        const ref = doc(this.firestore, 'users', user.uid);
        const snap = await getDoc(ref);
        if (snap.exists()) {
          this.isAdmin = snap.data()['rol'] === 'admin';
        }
      } else {
        this.isAdmin = false;
      }
    });
  }

  @HostListener('window:scroll')
  onScroll() {
    this.isScrolled = window.scrollY > 50;
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  cerrarMenu() {
    this.menuOpen = false;
  }
}