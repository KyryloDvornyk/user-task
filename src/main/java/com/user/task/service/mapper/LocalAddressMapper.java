package com.user.task.service.mapper;

import com.user.task.dto.request.LocalAddressRequestDto;
import com.user.task.dto.response.LocalAddressResponseDto;
import com.user.task.model.LocalAddress;
import org.springframework.stereotype.Component;

@Component
public class LocalAddressMapper {
    public LocalAddressResponseDto mapToDto(LocalAddress address) {
        LocalAddressResponseDto addressDto = new LocalAddressResponseDto();
        addressDto.setId(address.getId());
        addressDto.setStreet(address.getStreet());
        addressDto.setHouseNumber(address.getHouseNumber());
        return addressDto;
    }

    public LocalAddress mapToEntity(LocalAddressRequestDto requestDto) {
        LocalAddress address = new LocalAddress();
        address.setHouseNumber(requestDto.getHouseNumber());
        address.setStreet(requestDto.getStreet());
        return address;
    }
}
