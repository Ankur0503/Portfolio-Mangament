import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private fundNameSource = new BehaviorSubject<string>('');
  currentFundName = this.fundNameSource.asObservable();

  constructor() { }

  updateFundName(fundName: string) {
    this.fundNameSource.next(fundName);
  }
}
