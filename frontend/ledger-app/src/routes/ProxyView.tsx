
import * as React from "react";
import {LedgerDetailView} from "../components/LedgerDetailView";

export default function ProxyView() {
    return(
        <div className="App">
            <header className="App-header">
                <LedgerDetailView></LedgerDetailView>
            </header>
        </div>
    )
}