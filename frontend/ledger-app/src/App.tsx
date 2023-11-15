import React from 'react';
import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Root from "./routes/root";
import ProxyView from "./routes/ProxyView";
import NoPage from "./routes/NoPage";
import {LedgersList} from "./routes/LedgerList";
import LedgerDetailWrapper from "./routes/LedgerDetailWrapper";

function App() {

  return (
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<Root />}/>
              <Route path="proxy-view" element={<ProxyView />} />
              <Route path="/ledgers" element={<LedgersList />} />
              <Route path="/ledger/:id/details" element={<LedgerDetailWrapper />} />
              <Route path="*" element={<NoPage />} />
          </Routes>
      </BrowserRouter>
  );
}

export default App;