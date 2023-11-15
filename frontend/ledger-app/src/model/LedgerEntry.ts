export class LedgerEntry {
    id: number
    amount: string
    currency: string
    isCredit: boolean
    description: string
    datetime: string

    constructor(id: number, amount: string, currency: string, isCredit: boolean, description: string, datetime: string) {
        this.id = id
        this.amount = amount;
        this.currency = currency;
        this.isCredit = isCredit;
        this.description = description;
        this.datetime = datetime;
    }

    get symbol(): string {
        if (this.currency === "JPY") { return "¥"; }
        else if (this.currency === "USD") {return "$"; }
        else if (this.currency === "KRW") {return "₩"; }
        else {
            return "$";
        }
    }

    get isRefund(): string {
        if (this.isCredit) { return "-"; }
        else { return  ""; }
    }
}