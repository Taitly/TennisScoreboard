package com.taitly.tennisscoreboard.mapper;

import com.taitly.tennisscoreboard.dto.TennisSetDto;
import com.taitly.tennisscoreboard.model.TennisSet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PlayerMapper.class, TennisTieBreakMapper.class})
public interface TennisSetMapper {
    TennisSetMapper INSTANCE = Mappers.getMapper(TennisSetMapper.class);

    TennisSetDto toDto(TennisSet set);

    TennisSet toEntity(TennisSetDto dto);
}