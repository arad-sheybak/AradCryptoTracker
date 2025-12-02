package com.aradsheybak.aradcrypto.data.mapper

import com.aradsheybak.aradcrypto.core.domain.entity.Currency
import com.aradsheybak.aradcrypto.data.remote.dto.response.CurrencyDto

class CurrencyMapper {
    fun dtoToEntity(symbol: String,dto: CurrencyDto): Currency {
        return Currency(
            symbol = symbol,
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


    fun dtoListToEntityList(symbolDtoMap: Map<String, CurrencyDto>): List<Currency> {
        return symbolDtoMap.map { (symbol, dto) ->
            dtoToEntity(symbol, dto)
        }
    }
}