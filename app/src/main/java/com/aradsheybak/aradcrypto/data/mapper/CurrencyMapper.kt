package com.aradsheybak.aradcrypto.data.mapper

import com.aradsheybak.aradcrypto.core.domain.entity.Currency
import com.aradsheybak.aradcrypto.data.remote.dto.response.CurrencyDto

class CurrencyMapper {
    fun dtoToEntity(dto: CurrencyDto): Currency {
        return Currency(
            symbol = dto.symbol,
            last = dto.last,
            open = dto.open,
            close = dto.close,
            high = dto.high,
            low = dto.low,
            volume = dto.volume,
            sellTotal = dto.sellTotal,
            buyTotal = dto.buyTotal,
            period = dto.period,
            deal = dto.deal
        )
    }

    fun dtoListToEntityList(dtos: List<CurrencyDto>): List<Currency> {
        return dtos.map { dtoToEntity(it) }
    }
}