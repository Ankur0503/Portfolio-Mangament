import { Component } from '@angular/core';

@Component({
  selector: 'app-hero',
  templateUrl: './hero.component.html',
  styleUrls: ['./hero.component.css']
})
export class HeroComponent {
  features = [
    { image: 'assets/explore.png', text: 'Explore investment opportunities.' },
    { image: 'assets/decisions.png', text: 'Amplify your impact with smart decisions.' },
    { image: 'assets/investements.png', text: 'Manage your finances with ease.' },
  ];

  currentSlide = 0;
  autoScrollInterval: any;

  ngOnInit() {
    this.startAutoScroll();
  }

  ngOnDestroy() {
    clearInterval(this.autoScrollInterval);
  }

  startAutoScroll() {
    this.autoScrollInterval = setInterval(() => {
      this.currentSlide = (this.currentSlide + 1) % this.features.length;
    }, 2000);
  }
}
