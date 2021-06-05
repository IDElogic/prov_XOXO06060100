package com.xoxo.logistic.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.xoxo.logistic.dto.AddressDto;
import com.xoxo.logistic.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
		
	AddressDto addressToDto(Address address);

	List<Address> dtosToAddresses(List<AddressDto> addressDtos);
	
	Address dtoToAddress (AddressDto addressDto);

	List<AddressDto> addressesToDtos(List<Address> addresses);
	
}
