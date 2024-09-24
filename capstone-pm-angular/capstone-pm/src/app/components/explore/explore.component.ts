import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FundResponse } from 'src/app/models/fund-model/funds-response';
import { FundService } from 'src/app/services/fund-service/fund.service';
import { SearchService } from 'src/app/services/search-service/search.service';

@Component({
  selector: 'app-explore',
  templateUrl: './explore.component.html',
  styleUrls: ['./explore.component.css']
})
export class ExploreComponent {
  fundResponse: FundResponse
  fundResponses: any[]
  filteredFundResponses: any[]
  fundAMCs: string[]
  fundTypes: string[]
  fundRisks: string[]
  fundAUM: number
  searchedFundName: string = ''

  selectedAMCs: Set<string> = new Set()
  selectedCategories: Set<string> = new Set()
  selectedRisks: Set<string> = new Set()

  constructor(public fundService: FundService, public router: Router, public searchService: SearchService) {
    this.fundResponse = new FundResponse()
    this.fundResponses = []
    this.fundAMCs = []
    this.fundTypes = []
    this.fundRisks = []
    this.filteredFundResponses = []
    this.fundAUM = -1;
  }

  ngOnInit() {
    this.fundService.retrieveAllFunds().subscribe(
      response => {
        Object.assign(this.fundResponses, response.data)
      }
    )
    this.fundService.retrieveAllFilters().subscribe(
      response => {
        Object.assign(this.fundAMCs, response.data.fundAMCs)
        Object.assign(this.fundTypes, response.data.fundTypes)
        Object.assign(this.fundRisks, response.data.fundRisks)
      }
    )

    this.searchService.currentFundName.subscribe(fundName => {
      this.searchedFundName = fundName;
      this.filterFundBasedOnNames()
    })
  }

  filterFundBasedOnNames() {
    if (this.searchedFundName) {
      this.filteredFundResponses = this.fundResponses.filter(fund =>
        fund.fundName.toLowerCase().includes(this.searchedFundName.toLowerCase())
      );
    } else {
      this.filteredFundResponses = this.fundResponses;
    }
  }

  onClickClearFilters() {
    this.selectedAMCs.clear()
    this.selectedCategories.clear()
    this.selectedRisks.clear()
    this.fundAUM = -1
    this.uncheckAllCheckboxes()
    this.fetchFilteredFunds()
  }

  private uncheckAllCheckboxes() {
    const checkboxes = document.querySelectorAll('.form-check-input') as NodeListOf<HTMLInputElement>;
    checkboxes.forEach(checkbox => checkbox.checked = false);
  }

  onCardClick(currentFund: FundResponse) {
    this.router.navigate(['/funds', currentFund.fundName], {queryParams: {fundId: currentFund.fundId}})
  }

  onCheckBoxChange(event: Event, filterName: string, type: string) {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    if(type === 'AUM') {
      this.fundAUM = input.checked ? Number(filterName) : -1
    }
    else {
      this.updateSelection(type, filterName, input.checked);
    }

    this.fetchFilteredFunds();
  }

  private updateSelection(type: string, filterName: string, isChecked: boolean) {
      if (type === 'AMC') {
          this.updateSet(this.selectedAMCs, filterName, isChecked);
      } else if (type === 'Risk') {
          this.updateSet(this.selectedRisks, filterName, isChecked);
      }
  }

  private updateSet(selectionSet: Set<string>, filterName: string, isChecked: boolean) {
      if (isChecked) {
          selectionSet.add(filterName);
      } else {
          selectionSet.delete(filterName);
      }
  }

  private fetchFilteredFunds() {
      this.fundService.retrieveFundsByFilters(this.selectedAMCs, this.selectedRisks, this.fundAUM)
          .subscribe(response => {
              this.filteredFundResponses = response.data || [];
          });
  }

  isChecked(value: string): boolean {
    return this.fundAUM === Number(value);
  }
}
