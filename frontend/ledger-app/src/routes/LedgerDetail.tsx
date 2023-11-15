import React, {Component} from "react";
import axios from 'axios';
import axiosRetry from 'axios-retry';
import {LedgerEntry} from "../model/LedgerEntry";
import LedgerDetailTable from "../components/LedgerDetailTable";
import Button from '@mui/material/Button';
import {LedgerDetail} from "../model/LedgerDetail";
import Backdrop from '@mui/material/Backdrop';
import CircularProgress from '@mui/material/CircularProgress';
import InputLabel from '@mui/material/InputLabel';
import dayjs from 'dayjs';

import {
    Box,
    FormControlLabel,
    FormGroup,
    MenuItem,
    Modal,
    Select,
    SelectChangeEvent,
    Switch,
    TextField
} from "@mui/material";
import {DateTimePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {DemoContainer} from "@mui/x-date-pickers/internals/demo";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import utc from "dayjs/plugin/utc";


dayjs.extend(utc);
export class LedgerDetailView extends Component <any, any> {

    constLedger(): LedgerDetail { return new LedgerDetail("My Ledger", 0, "USD", new Map(), [])}
    constIdsSeen(): number []  { return [] }

    setEntries(ledgerInfo: LedgerDetail, hasMore: boolean, idSeen: number []| null = null) {

        let idsSeenCopy: number[]
        if (idSeen !== null) {
            idsSeenCopy = idSeen;
        } else {
            idsSeenCopy = [...this.state.idsSeen];
        }
        if (
            ledgerInfo.ledgerDetails.length > 0 &&
            (idsSeenCopy[idsSeenCopy.length - 1]) != ledgerInfo.ledgerDetails[ledgerInfo.ledgerDetails.length - 1].id as number
        ) {
            idsSeenCopy.push(ledgerInfo.ledgerDetails[ledgerInfo.ledgerDetails.length - 1].id as number);
        }
        this.setState({
            isLoading: false,
            ledgerDetail: ledgerInfo,
            hasMore: hasMore,
            idsSeen: idsSeenCopy,
            idsSeenIsEmpty: idsSeenCopy.length <= 1,
        });
    }

    state = {
        ledgerDetail: this.constLedger(),
        already_called: false,
        isLoading: false,
        id: 0,
        idsSeen: this.constIdsSeen(),
        hasMore: false,
        idsSeenIsEmpty: true,
        modalOpen: false,
        // not good but lacking time...
        entryAmount: "",
        entryCurrency: "USD",
        entryIsCredit: false,
        entryDescription: "",
        entryDatetime: dayjs.utc('2024-11-01')
    }

    handleOpen()  { this.setState({modalOpen: true}); }
    handleClose () { this.setState({
        modalOpen: false,
        ledgerName: "",
        entryAmount: "",
        entryCurrency: "USD",
        entryIsCredit: false,
        entryDescription: "",
        entryDatetime: dayjs.utc('2024-11-01')
    }, ); }

    async getData(id: any, isPrevious: Boolean) {
        try {
            this.setState({
                isLoading: true
            });
            axiosRetry(axios, { retries: 3});
            let idsSeenCopy = [...this.state.idsSeen];
            if (this.state.idsSeen.length > 0) {
                if (isPrevious) {
                    idsSeenCopy = idsSeenCopy.slice(0, -2);
                }
            }
            let lastIdSeen
            if (idsSeenCopy.length == 0) {
                lastIdSeen = null
            }
            else {
                lastIdSeen = idsSeenCopy[idsSeenCopy.length - 1]
            }
            let response = await axios.get("http://localhost:8080/ledger/" + id + "/details",
                { params: { lastId : lastIdSeen, limit: 5}});
            const ledgerJson = response.data
            const data = ledgerJson.ledgerEntries.map((value: any) => {
                return new LedgerEntry(
                    value.id,
                    value.amount,
                    value.currency,
                    value.isCredit,
                    value.description,
                    value.datetime,
                );
            });
            const details = ledgerJson.ledger
            const ledgerInfo = new LedgerDetail(
                details.name,
                details.totalAmount,
                details.totalAmountInCurrency,
                details.currencyRates,
                data,
                ledgerJson.totalEntries
            )
            this.setEntries(ledgerInfo, ledgerJson.hasMore, idsSeenCopy)
        } catch (error) {
            // TODO: Look at the status
            // @ts-ignore
            if (error.response === undefined) {
                alert("Couldn't connect to the api server or an unknown error occurred");
            }
            else {
                // @ts-ignore
                alert(error.response.data.description)
            }
            console.error(error)
            this.setState({
                invoices: [],
                isLoading: false
            });
        }
    }

    componentDidMount() {
        const { id } = this.props.params;
        if (!this.state.already_called) {
            this.getData(id, false).then(r =>
                this.setState(
                    {
                        id: id,
                        already_called: true
                    }
                )
            );
        }
    }

    getTotalAmount(): String {
        return "" + this.state.ledgerDetail.totalAmountInCurrency + '$' +  this.state.ledgerDetail.totalAmount
    }

    setDescription(description: string) { this.setState({entryDescription: description})}
    setAmount(amount: string) { this.setState({entryAmount: amount})}

    handleSwitchChange(event: React.ChangeEvent<HTMLInputElement>) {
        this.setState({entryIsCredit: event.target.checked});
    }

    handleSelectChange(event: SelectChangeEvent) {
        this.setState({entryCurrency: event.target.value})
    }

    readonly style = {
        position: 'absolute' as 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: 400,
        bgcolor: 'background.paper',
        border: '2px solid #000',
        boxShadow: 24,
        pt: 2,
        px: 4,
        pb: 3,
    };

    addEntry() {
        console.log({
            amount: this.state.entryAmount,
            currency: this.state.entryCurrency,
            isCredit: this.state.entryIsCredit,
            description: this.state.entryDescription,
            datetime: this.state.entryDatetime.valueOf()
        });
        axios.post(
            "http://localhost:8080/ledger/" + this.state.id + "/details",
            {
                amount: this.state.entryAmount,
                currency: this.state.entryCurrency,
                isCredit: this.state.entryIsCredit,
                description: this.state.entryDescription,
                datetime: this.state.entryDatetime.valueOf()
            }
        ).then((response) => {
            this.setState({
                idsSeen: [],
                idsSeenIsEmpty: true
            }, () =>{
                // TODO: Improve.
                this.handleClose()
                this.getData(this.state.id,false)
            });
        }).catch((error) => {
            // @ts-ignore
            if (error.response === undefined) {
                alert("Couldn't connect to the api server or an unknown error occurred");
            }
            else {
                // @ts-ignore
                alert(error.response.data.description)
            }
            this.setState({
                isLoading: false,
            });
        });
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
                <Modal
                    open={this.state.modalOpen}

                    onClose={() => this.handleClose()}
                    aria-labelledby="modal-modal-title"
                    aria-describedby="modal-modal-description"
                >
                    <Box sx={{...this.style, width: 250, m: 2,  paddingTop: 2 }}>
                    <FormGroup>
                        <InputLabel id="demo-simple-select-error-label">Currency:</InputLabel>
                        <Select
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            value={this.state.entryCurrency}
                            onChange={(event: SelectChangeEvent) => {
                                this.setState({entryCurrency: event.target.value})
                            }}
                        >
                            <MenuItem value={"USD"}>USD</MenuItem>
                            <MenuItem value={"JPY"}>JPY</MenuItem>
                            <MenuItem value={"KRW"}>KRW</MenuItem>
                        </Select>
                        <InputLabel id="demo-simple-select-error-label">Amount:</InputLabel>
                        <TextField
                            id="outlined-number"
                            type="number"
                            InputLabelProps={{
                                shrink: true,
                            }}
                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                                this.setAmount(event.target.value);
                            }}
                        />
                        <TextField id="outlined-basic" sx={{marginTop: 2}} label="Description" variant="outlined" onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            this.setDescription(event.target.value);
                        }}/>
                        <FormControlLabel control={
                            <Switch
                            checked={this.state.entryIsCredit}
                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                                this.setState({entryIsCredit: event.target.checked});
                            }}
                            inputProps={{ 'aria-label': 'controlled' }}
                        />} label="Is credit?" />
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DemoContainer components={['DatePicker', 'DatePicker']}>
                                <DateTimePicker
                                    // TODO: Allow different timezones
                                    label="Dateime in UTC"
                                    value={this.state.entryDatetime}
                                    timezone="UTC"
                                    onChange={(newValue) => this.setState({entryDatetime: newValue})}
                                />
                            </DemoContainer>
                        </LocalizationProvider>
                        <Button onClick={() => {
                            this.handleClose()
                        }}>Cancel</Button>
                        <Button onClick={() => {
                            this.addEntry()
                        }}>Add Entry</Button>
                    </FormGroup>
                    </Box>
                </Modal>
                <p className="simple-header">{this.state.ledgerDetail.name}</p>
                <p className="simple-entries">Total Amount: {this.getTotalAmount()}</p>
                <p className="simple-entries">Total Entries: {this.state.ledgerDetail.totalEntries}</p>
                <Button  onClick={() => {
                    this.handleOpen()
                }}>Add Entry</Button>
                <Button variant="contained" href={"http://localhost:8080/" + "ledger/" + this.state.id + "/details/download"}  >Download CSV</Button>
                <LedgerDetailTable ledgerDetails={this.state.ledgerDetail.ledgerDetails}></LedgerDetailTable>
                <Button variant="text"  disabled={this.state.idsSeenIsEmpty} onClick={() => {
                    this.getData(this.state.id, true)
                }}>Previous</Button>
                <Button variant="text"  disabled={!this.state.hasMore} onClick={() => {
                    this.getData(this.state.id, false)
                }}>Next</Button>
            </div>
        );
    }

}