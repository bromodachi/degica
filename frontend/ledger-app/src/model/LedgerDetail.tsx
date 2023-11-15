import {LedgerEntry} from "./LedgerEntry";

export class LedgerDetail {
    name: string
    totalAmount: number
    totalAmountInCurrency: string
    currencyRates: Map<string, Map<string, number>>
    ledgerDetails: LedgerEntry[]
    totalEntries: number

    constructor(name: string, totalAmount: number, totalAmountInCurrency: string, currencyRates: Map<string, Map<string, number>>, ledgerDetails: LedgerEntry[], totalEntries: number = 0) {
        this.name = name
        this.totalAmount = totalAmount;
        this.totalAmountInCurrency = totalAmountInCurrency;
        this.currencyRates = currencyRates;
        this.ledgerDetails = ledgerDetails;
        this.totalEntries = totalEntries
    }
}