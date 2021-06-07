package com.xoxo.logistic.web;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.xoxo.logistic.dto.AddressByExampleDto;
import com.xoxo.logistic.dto.AddressDto;
import com.xoxo.logistic.mapper.AddressMapper;
import com.xoxo.logistic.model.Address;
import com.xoxo.logistic.repository.AddressRepository;
import com.xoxo.logistic.service.AddressService;

import javassist.tools.web.BadHttpRequest;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	AddressMapper addressMapper;
	
	@Autowired
	AddressRepository addressRepository;
	
	private List<Address> addresses = new ArrayList<>();
			
		@GetMapping
		public List<AddressDto> getAll(){
			return addressMapper.addressesToDtos(addressService.getAllAddresses());
		}	
		
		@GetMapping("/{id}")
		public AddressDto getById(@PathVariable long id) {
			Address address = findByIdOrThrow(id);
			return addressMapper.addressToDto(address);
		}

		private Address findByIdOrThrow(long id) {
			return addressService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));				
		}
		
		@PostMapping
		public AddressDto createAddress(@RequestBody @Valid AddressDto addressDto) {
			return addressMapper.addressToDto(addressService.save(addressMapper.dtoToAddress(addressDto)));
		}
		
		@PutMapping("/{id}")
		public ResponseEntity<AddressDto> modifyAddress(@PathVariable long id, @RequestBody @Valid AddressDto addressDto) {
			addressDto.setId(id);
			Address updatedAddress = addressService.update(addressMapper.dtoToAddress(addressDto));
			if(updatedAddress == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(addressMapper.addressToDto(updatedAddress));
			}
			
		}
		
		@DeleteMapping("/{id}")
		public void deleteAddress(@PathVariable long id) {
			try {
				addressService.deleteAddress(id);
			} catch (BadHttpRequest e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		
		@PostMapping("/search")
		public List<AddressDto> getAddresses(@RequestParam(required = false) @RequestBody AddressByExampleDto example, Pageable pageable){
			String city = null;
			String countryCode = null;
			long postalCode = 0;
			String streetAddress = null;
			if(city == null && streetAddress == null && countryCode == null && postalCode == 0) {
			} else {
				AddressByExampleDto addressByExampleDto = null;
				Page<Address> page = addressRepository.findAddressesByExample(addressByExampleDto, pageable);
				addresses = page.getContent();
				System.out.println(page.getNumber());
				System.out.println(page.getSize());
				System.out.println(page.getSort());	
			}
			return addressMapper.addressesToDtos(addresses);
		}
	}