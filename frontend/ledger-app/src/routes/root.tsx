import * as React from 'react';
import Button from '@mui/material/Button';
import ButtonGroup from '@mui/material/ButtonGroup';
export default function Root() {
    return(
        <div>
            <p>Hi, this is the front page. If you would like to see only request 1-3, click see proxy view.</p>
            <ButtonGroup variant="contained" aria-label="outlined primary button group">
                <Button href={'/proxy-view'}>Proxy-view</Button>
                <Button href={'/ledgers'}>Request 4 - Ledger views</Button>
            </ButtonGroup>
        </div>
    )
}