package ru.itmo.calls.port

interface TransactionProvider {
    fun <R> executeInTransaction(action: () -> R): R
}