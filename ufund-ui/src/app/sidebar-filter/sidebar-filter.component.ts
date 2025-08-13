import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-sidebar-filter',
  templateUrl: './sidebar-filter.component.html',
  styleUrls: ['./sidebar-filter.component.css']
})
export class SidebarFilterComponent {
  @Input() isVisible: boolean = false;
  @Output() categoryFilterChange = new EventEmitter<{ category: string, isChecked: boolean }>();
  @Output() filtersCleared = new EventEmitter<void>();
  @Output() costSortChange = new EventEmitter<string>();

  onCheckboxChange(event: Event, category: string): void {
    const isChecked = (event.target as HTMLInputElement).checked;
    this.categoryFilterChange.emit({ category, isChecked });
  }

  clearFilters(): void {
    const checkboxes = document.querySelectorAll<HTMLInputElement>('.checkbox-container input[type="checkbox"]');
    checkboxes.forEach(checkbox => checkbox.checked = false);

    // Uncheck radio buttons when clearing filters
    const radioButtons = document.querySelectorAll('input[type="radio"][name="cost"]') as NodeListOf<HTMLInputElement>;
    radioButtons.forEach(radio => radio.checked = false);


    this.filtersCleared.emit(); // Notify NeedsComponent to clear selectedCategories and fetch all needs
  }

  removeCost(): void {
    const radioButtons = document.querySelectorAll('input[type="radio"][name="cost"]') as NodeListOf<HTMLInputElement>;
    radioButtons.forEach(radio => radio.checked = false);
  }

  removeQuantity(): void {
    const radioButtons = document.querySelectorAll('input[type="radio"][name="quantity"]') as NodeListOf<HTMLInputElement>;
    radioButtons.forEach(radio => radio.checked = false);
  }

  //notify parent to change sort
  onSortChange(order: string): void {
    if(order === 'low-highQ' || order === 'high-lowQ') { this.removeCost(); }
    else if (order === 'low-highC' || order === 'high-lowC') { this.removeQuantity(); }
    this.costSortChange.emit(order);
  }


}
