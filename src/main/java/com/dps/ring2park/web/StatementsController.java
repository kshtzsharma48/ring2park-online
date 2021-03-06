package com.dps.ring2park.web;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dps.ring2park.domain.Booking;
import com.dps.ring2park.service.BookingService;

@Controller
@RequestMapping("/statements/*")
public class StatementsController {

	@Autowired
	private BookingService bookingService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Principal currentUser, Model model) {
		List<Booking> bookings;
		boolean isAdmin = false;
		Collection<GrantedAuthority> authorities =  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) { 
				isAdmin = true;
			}
		}      
		if (isAdmin)
			bookings = bookingService.findBookings();
		else
			bookings = bookingService.findBookings(currentUser.getName());
		model.addAttribute("bookingList", bookings);
		return "statements/list";
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String details(@PathVariable Long id, Model model) {
		model.addAttribute(bookingService.findBookingById(id));
		return "statements/details";
	}

}