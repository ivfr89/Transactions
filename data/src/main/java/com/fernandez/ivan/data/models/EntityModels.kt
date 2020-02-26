package com.fernandez.ivan.data.models

import com.fernandez.ivan.domain.Transaction


interface ModelEntity

data class EntityTransaction(
    val id: Int?,
    val date: String?,
    val amount: Float?,
    val fee: Float?,
    val description: String?
    ): ModelEntity
{
    fun toDomain() = Transaction(id,date,amount,fee,description)
}