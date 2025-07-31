package com.taitly.tennisscoreboard.mapper;

import com.taitly.tennisscoreboard.dto.TennisTieBreakDto;
import com.taitly.tennisscoreboard.model.TennisTieBreakPoint;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TennisTieBreakMapper {
    TennisTieBreakMapper INSTANCE = Mappers.getMapper(TennisTieBreakMapper.class);

    TennisTieBreakDto toDto(TennisTieBreakPoint tieBreakPoint);

    TennisTieBreakPoint toEntity(TennisTieBreakDto dto);
}