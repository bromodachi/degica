import React, {Component} from "react";
import axios from 'axios';
import axiosRetry from 'axios-retry';
import {LedgerEntry} from "../model/LedgerEntry";
import LedgerDetailTable from "./LedgerDetailTable";
import Button from '@mui/material/Button';
import {LedgerDetail} from "../model/LedgerDetail";
import Backdrop from '@mui/material/Backdrop';
import CircularProgress from '@mui/material/CircularProgress';

export class LedgerDetailView extends Component {

    constLedger(): LedgerDetail { return new LedgerDetail("My Ledger", 0, "USD", new Map(), [])}

    state = {
        ledgerDetail: this.constLedger(),
        already_called: false,
        isLoading: false
    }

    async getData() {
        try {
            this.setState({
                isLoading: true
            });
            axiosRetry(axios, { retries: 3});
            let response = await axios.get("http://localhost:8080/invoices");
            const ledgerJson = response.data
            const data = ledgerJson.ledgerDetail.map((value: any) => {
                return new LedgerEntry(
                    0,
                    value.amount,
                    value.currency,
                    value.is_credit,
                    value.description,
                    value.created_at
                );
            });
            const ledgerInfo = new LedgerDetail(
                "My Ledger",
                ledgerJson.totalAmount,
                ledgerJson.totalAmountInCurrency,
                ledgerJson.currencyRates,
                data
            )
            this.setState({
                ledgerDetail: ledgerInfo,
                isLoading: false
            })
        } catch (error) {
            alert("We couldn't load the invoices. Please reload the page to try again")
            console.error(error)
            this.setState({
                invoices: [],
                isLoading: false
            });
        }
    }

    componentDidMount() {
        if (!this.state.already_called) {
            this.getData().then(r =>
                this.setState(
                    {
                        already_called: true
                    }
                )
            );
        }
    }

    getTotalAmount(): String {
        return "" + this.state.ledgerDetail.totalAmountInCurrency + '$' +  this.state.ledgerDetail.totalAmount
    }

    render() {
        return (
            <div>
                <Backdrop
                    sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
                    open={this.state.isLoading}
                >
                    <CircularProgress color="inherit" />
                </Backdrop>
                <p className="simple-header">{this.state.ledgerDetail.name}</p>
                <p className="simple-entries">Total Amount: {this.getTotalAmount()}</p>
                <p className="simple-entries">Total Entries: {this.state.ledgerDetail.ledgerDetails.length}</p>
                <Button variant="contained" href="http://localhost:8080/invoices/download">Download CSV</Button>
                <LedgerDetailTable ledgerDetails={this.state.ledgerDetail.ledgerDetails}></LedgerDetailTable>
            </div>
        );
    }

}