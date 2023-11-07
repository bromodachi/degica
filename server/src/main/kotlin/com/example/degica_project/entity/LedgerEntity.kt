package com.example.degica_project.entity

import com.example.degica_project.annotations.NoArgsConstructor

@NoArgsConstructor
data class LedgerEntity(
    val id: Long,
    val name: String
) {
    companion object {
        fun create(name: String) = LedgerEntity(0, name)
    }
}
