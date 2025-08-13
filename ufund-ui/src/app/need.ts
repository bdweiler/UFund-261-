export interface Need {
    id: number;
    type: string;
    cost: number;
    quantity: number;
    category: Category;
}

export enum Category {
    PRODUCE = "PRODUCE",
    PERISHABLES = "PERISHABLES",
    NON_PERISHABLES = "NON_PERISHABLES",
    SEMI_PERISHABLES = "SEMI_PERISHABLES",
    BEVERAGES = "BEVERAGES"
}
