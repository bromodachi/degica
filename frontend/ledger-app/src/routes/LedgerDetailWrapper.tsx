import {useParams} from "react-router-dom";
import {LedgerDetailView} from "./LedgerDetail";
import * as React from "react";

export default function LedgerDetailWrapper(props: any)  {
    return(
        <LedgerDetailView {...props} params={useParams()}></LedgerDetailView>
    )
}