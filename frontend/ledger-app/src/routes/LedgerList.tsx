import TableContainer from "@mui/material/TableContainer";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import * as React from "react";
import {Component} from "react";
import {Ledger} from "../model/Ledger";
import axios from "axios";
import axiosRetry from "axios-retry";
import Backdrop from "@mui/material/Backdrop";
import CircularProgress from "@mui/material/CircularProgress";
import Button from "@mui/material/Button";
import Link from '@mui/material/Link';
import {Box, Modal, TextField} from "@mui/material";

export class LedgersList extends Component {

    state: {ledgers: Ledger[], isLoading: boolean, idsSeen: number[], hasMore: boolean, modalOpen: boolean, ledgerName: string, idsSeenIsEmpty: boolean} = {
        ledgers: [],
        isLoading: false,
        idsSeen: [],
        hasMore: false,
        modalOpen: false,
        ledgerName: "",
        idsSeenIsEmpty: true
    }

    setLedgers(ledgers: Ledger[], hasMore: boolean, idSeen: number []| null = null) {

        let idsSeenCopy: number[]
        if (idSeen !== null) {
            idsSeenCopy = idSeen;
        } else {
            idsSeenCopy = [...this.state.idsSeen];
        }
        if (
            (idsSeenCopy[idsSeenCopy.length - 1]) != ledgers[ledgers.length - 1].id as number
        ) {
            idsSeenCopy.push(ledgers[ledgers.length - 1].id as number);
        }
        this.setState({
            isLoading: false,
            ledgers: ledgers,
            hasMore: hasMore,
            idsSeen: idsSeenCopy,
            idsSeenIsEmpty: idsSeenCopy.length <= 1
        });
    }

    componentDidMount() {
        this.getData(false)
    }

    async getData(isPrevious: Boolean) {
        this.setState({
            isLoading: true
        });
        let idsSeenCopy = [...this.state.idsSeen];
        if (this.state.idsSeen.length > 0) {
            if (isPrevious) {
                idsSeenCopy = idsSeenCopy.slice(0, -2);
            }
        }
        axiosRetry(axios, { retries: 3});
        let lastIdSeen
        if (idsSeenCopy.length == 0) {
            lastIdSeen = null
        }
        else {
            lastIdSeen = idsSeenCopy[idsSeenCopy.length - 1]
        }
        axios.get("http://localhost:8080/ledger" , { params: { lastId : lastIdSeen, limit: 10}}).then((response) => {
            const json = response.data;
            const ledgers = json.ledgers.map((value: any) => {
                return new Ledger(value.id, value.name)
            })
            this.setLedgers(ledgers, json.hasMore, idsSeenCopy)
        }).catch((error) => {
            alert("We couldn't load the invoices. Please reload the page to try again")
            console.error(error)
            this.setState({
                isLoading: false
            });
        })
    }

    handleOpen()  { this.setState({modalOpen: true}); }
    handleClose () { this.setState({modalOpen: false, ledgerName: ""}); }

    setName(name: string) { this.setState({ledgerName: name})}

    addLedger() {
        this.setState({
            isLoading: true
        });
        axiosRetry(axios, { retries: 3});
        axios.post("http://localhost:8080/ledger", {ledgerName: this.state.ledgerName}).then((response) => {
            this.setState({
                idsSeen: [],
                idsSeenIsEmpty: true
            }, () =>{
                // TODO: Improve.
                this.handleClose()
                this.getData(false)
            });

        }).catch((error) => {
            // @ts-ignore
            if (error.response !== undefined) {
                alert("Couldn't connect to the api server or an unknown error occurred");
            }
            else {
                // @ts-ignore
                alert(error.response.data.description)
                console.error(error)
                this.setState({
                    isLoading: false,
                });
            }
        });
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


    render() {
        return (
            <div>
                <Backdrop
                    sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
                    open={this.state.isLoading}
                >
                    <CircularProgress color="inherit" />
                </Backdrop>
                <Button  onClick={() => {
                    this.handleOpen()
                }}>Add Ledger</Button>
                <Modal
                    open={this.state.modalOpen}

                    onClose={() => this.handleClose()}
                    aria-labelledby="modal-modal-title"
                    aria-describedby="modal-modal-description"
                >
                    <Box sx={{...this.style, width: 200 }}>
                        <TextField id="outlined-basic" label="Ledger name" variant="outlined" onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                            this.setName(event.target.value);
                        }}/>
                        <Button onClick={() => {
                            this.handleClose()
                        }}>Cancel</Button>
                        <Button onClick={() => {
                            this.addLedger()
                        }}>Add Ledger</Button>
                    </Box>
                </Modal>
                <TableContainer component={Paper}>
                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell>id</TableCell>
                                <TableCell align="left">name</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {this.state.ledgers.map((row, index) => (
                                <TableRow
                                    key={index}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                >
                                    <TableCell component="th" scope="row">
                                        <Link href={`/ledger/${row.id}/details`}>{row.id}</Link>
                                    </TableCell>
                                    <TableCell align="left">{row.name}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                <Button variant="text"  disabled={this.state.idsSeenIsEmpty} onClick={() => {
                    this.getData(true)
                }}>Previous</Button>
                <Button variant="text"  disabled={!this.state.hasMore} onClick={() => {
                    this.getData(false)
                }}>Next</Button>
            </div>
        );
    }
}