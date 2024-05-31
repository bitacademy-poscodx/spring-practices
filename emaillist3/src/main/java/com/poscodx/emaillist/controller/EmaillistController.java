package com.poscodx.emaillist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poscodx.emaillist.repository.EmaillistRepository;
import com.poscodx.emaillist.vo.EmaillistVo;

@Controller
public class EmaillistController {
	@Autowired
	private EmaillistRepository emaillistRepository;
	
	@ResponseBody
	@RequestMapping("/")
	public String index(Model model) {
		List<EmaillistVo> list = emaillistRepository.findAll();
		model.addAttribute("list", list);
		
		return "/WEB-INF/views/index.jsp";
	}
}
