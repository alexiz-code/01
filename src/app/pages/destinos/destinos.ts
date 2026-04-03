import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CommonModule, } from '@angular/common';

interface Slide {
  bg: string;
  thumb: string;
  label: string;
  title: string;
  desc: string;
  name: string;
}

@Component({
  selector: 'app-destinos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './destinos.html',
  styleUrl: './destinos.css',
})
export class Destinos implements OnInit, OnDestroy {

  currentIndex = 0;
  private autoTimer: any;

  slides: Slide[] = [
    {
      bg: 'https://www.ytuqueplanes.com/imagenes/fotos/novedades/sierra-pampa-quinua.JPG',
      thumb: 'https://www.ytuqueplanes.com/imagenes/fotos/novedades/sierra-pampa-quinua.JPG',
      label: 'histórica',
      title: 'Pampa de <br>Quinua',
      desc: 'Ubicada cerca de Ayacucho, es un hermoso paisaje andino de gran valor histórico y natural. Este lugar fue escenario de la Batalla de Ayacucho, un hecho clave para la independencia del Perú y de Sudamérica.',
      name: 'Historia Viva'
    },
    {
      bg: 'https://images.pexels.com/photos/259967/pexels-photo-259967.jpeg',
      thumb: 'https://images.pexels.com/photos/259967/pexels-photo-259967.jpeg',
      label: 'Mística',
      title: 'Machu <br>Picchu',
      desc: 'Una antigua ciudad inca rodeada de imponentes montañas y paisajes espectaculares. Considerada una de las maravillas del mundo, combina historia, arquitectura y un entorno natural único que la convierten en uno de los destinos más fascinantes del planeta.',
      name: 'Maravilla'
    },
    {
      bg: 'https://images.pexels.com/photos/21014/pexels-photo.jpg',
      thumb: 'https://images.pexels.com/photos/21014/pexels-photo.jpg',
      label: 'Histórico',
      title: 'Cusco',
      desc: 'Una ciudad andina llena de historia, antigua capital del Imperio Inca y punto de partida hacia increíbles destinos culturales.',
      name: 'Milenario'
    },
    {
      bg: 'https://images.pexels.com/photos/753626/pexels-photo-753626.jpeg',
      thumb: 'https://images.pexels.com/photos/753626/pexels-photo-753626.jpeg',
      label: 'Místico',
      title: 'Lago<br>Titicaca',
      desc: 'El lago navegable más alto del mundo, rodeado de paisajes únicos y tradiciones ancestrales.',
      name: 'Sagrado'
    },
    {
      bg: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4qHUBk0VGAdXrmJiwT98ksH9f9KWWm7Sicw&s',
      thumb: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4qHUBk0VGAdXrmJiwT98ksH9f9KWWm7Sicw&s',
      label: 'Tradicional',
      title: 'Sarhua',
      desc: 'Un pueblo andino conocido por su arte tradicional, especialmente las tablas de Sarhua que narran historias culturales.',
      name: 'Cultural'
    }
  ];

  ngOnInit(): void {
    this.resetAuto();
  }

  ngOnDestroy(): void {
    clearInterval(this.autoTimer);
  }

  goTo(index: number): void {
    this.currentIndex = (index + this.slides.length) % this.slides.length;
    this.resetAuto();
  }

  private resetAuto(): void {
    clearInterval(this.autoTimer);
    this.autoTimer = setInterval(() => this.goTo(this.currentIndex + 1), 5000);
  }

  get statusText(): string {
    const cur = String(this.currentIndex + 1).padStart(2, '0');
    const tot = String(this.slides.length).padStart(2, '0');
    return `${cur} / ${tot}`;
  }

  @HostListener('document:keydown', ['$event'])
  onKeydown(e: KeyboardEvent): void {
    if (e.key === 'ArrowLeft') this.goTo(this.currentIndex - 1);
    if (e.key === 'ArrowRight') this.goTo(this.currentIndex + 1);
  }

  private startX = 0;

  @HostListener('touchstart', ['$event'])
  onTouchStart(e: TouchEvent): void {
    this.startX = e.touches[0].clientX;
  }

  @HostListener('touchend', ['$event'])
  onTouchEnd(e: TouchEvent): void {
    const diff = this.startX - e.changedTouches[0].clientX;
    if (Math.abs(diff) > 50) {
      this.goTo(diff > 0 ? this.currentIndex + 1 : this.currentIndex - 1);
    }
  }
}