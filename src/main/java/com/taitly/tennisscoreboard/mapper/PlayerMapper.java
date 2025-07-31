package com.taitly.tennisscoreboard.mapper;

import com.taitly.tennisscoreboard.dto.PlayerDto;
import com.taitly.tennisscoreboard.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerDto toDto(Player player);

    Player toEntity(PlayerDto playerDto);
}