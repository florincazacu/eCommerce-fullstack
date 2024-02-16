package com.example.springbootlibrary.controller;

import com.example.springbootlibrary.requestmodels.AddBookRequest;
import com.example.springbootlibrary.service.AdminService;
import com.example.springbootlibrary.utils.ExtractJwt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping("/secure/add/book")
	public void postBook(@RequestHeader(value = "Authorization") String token,
	@RequestBody AddBookRequest addBookRequest) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);
		String admin = ExtractJwt.payloadJwtExtraction(token, Util.ADMIN);

		if (admin == null || !admin.equals("admin")) {
			throw new Exception("Administration page only.");
		}

		adminService.postBook(addBookRequest);
	}
}
