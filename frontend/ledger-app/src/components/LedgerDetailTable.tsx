import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {LedgerEntry} from "../model/LedgerEntry";
import dayjs from "dayjs";
import utc from "dayjs/plugin/utc";

interface LedgerDetailsProps {
    ledgerDetails: LedgerEntry[];
}

export default function LedgerDetailTable({ ledgerDetails }: LedgerDetailsProps) {
    function formatToUtc(date: any) {
        let isNotNumber = isNaN(Number(date));
        if (isNotNumber) {
            return date;
        }
        else {
            let toNumber = Number(date);
            dayjs.extend(utc)
            return dayjs(toNumber).utc().format('YYYY-MM-DD HH:mm:ss [UTC]');
        }
    }
    return (
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Amount</TableCell>
                        <TableCell align="left">Description</TableCell>
                        <TableCell align="left">Datetime</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {ledgerDetails.map((row, index) => (
                        <TableRow
                            key={index}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell component="th" scope="row">
                                {row.currency}{row.symbol}{row.isRefund}{row.amount}
                            </TableCell>
                            <TableCell align="left">{row.description}</TableCell>
                            <TableCell align="left">{formatToUtc(row.datetime)}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}