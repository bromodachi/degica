package com.example.degica_project.service

import com.example.degica_project.dto.Ledger

interface ProxyService {
    fun getLedger(): Ledger
}
