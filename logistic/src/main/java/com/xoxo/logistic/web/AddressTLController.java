package com.xoxo.logistic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.xoxo.logistic.model.Address;


@Controller
public class AddressTLController {
	
private List<Address> allAddresss = new ArrayList<>();
	
	{
		allAddresss.add(new Address(1L,"SE","Malmö","Södra Förstadsgatan", 21428L,124L, 0.0, 0.0));
		allAddresss.add(new Address(2L,"SE","Jönköping","Ekhagsringen", 55456L,2L, 0.0, 0.0));
		allAddresss.add(new Address(3L,"SE","Borås"," Sjumilagatan", 50742L,1L, 0.0, 0.0));
		allAddresss.add(new Address(4L,"SE","Götebörg","Vasagatan",41137,45L, 0.0, 0.0));
	}

	@GetMapping("/addresses")
	public String listAddresss(Map<String, Object> model) {
		model.put("addresses", allAddresss);
		model.put("newAddress", new Address());
		return "addresses";
	}
	
	@PostMapping("/addresses")
	public String addAddress(Address address) {
		allAddresss.add(address);
		return "redirect:addresses";
	}
	
	@PostMapping("/updateAddress")
	public String updateAddress(Address address) {
		for(int i=0; i < allAddresss.size(); i++) {
			if(allAddresss.get(i).getId() == address.getId()) {
				allAddresss.set(i, address);
				break;
			}
		}
		return "redirect:addresses";
	}
	
	@GetMapping("/deleteAddress/{id}")
	public String deleteAddress(@PathVariable long id) {
		allAddresss.removeIf(emp -> emp.getId() == id);		
		return "redirect:/addresses";
	}
	
	@GetMapping("/addresses/{id}")
	public String editAddress(@PathVariable long id, Map<String, Object> model) {
		model.put("address", allAddresss.stream().filter(e -> e.getId() == id).findFirst().get());
		return "editAddress";
	}

}

