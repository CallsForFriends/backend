package ru.itmo.calls.adapter.transaction

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.calls.port.TransactionProvider

@Service
class SpringTransactionService: TransactionProvider {
    @Transactional
    override fun <R> executeInTransaction(action: () -> R): R {
        return action()
    }
}