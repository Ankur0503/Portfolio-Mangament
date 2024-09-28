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
    // Start auto-scrolling when the component is initialized
    this.startAutoScroll();
  }

  ngOnDestroy() {
    // Clear the interval when the component is destroyed to avoid memory leaks
    clearInterval(this.autoScrollInterval);
  }

  // Automatically scroll to the next slide every 2 seconds
  startAutoScroll() {
    this.autoScrollInterval = setInterval(() => {
      this.currentSlide = (this.currentSlide + 1) % this.features.length; // Loop through slides
    }, 2000); // 2000ms = 2 seconds
  }
}
