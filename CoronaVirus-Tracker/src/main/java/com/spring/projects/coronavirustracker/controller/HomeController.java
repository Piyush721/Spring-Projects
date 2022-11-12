package com.spring.projects.coronavirustracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.projects.coronavirustracker.data.LocationStatData;
import com.spring.projects.coronavirustracker.service.CoronaVirusDataService;

@Controller
public class HomeController {

	@Autowired
	CoronaVirusDataService cservice;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model m) {
		
		List<LocationStatData> allStats=cservice.getAllStats();
		int sum= allStats.stream().mapToInt(stat -> stat.getTotalCases()).sum();
		
		m.addAttribute("stats", allStats);
		m.addAttribute("totalCases", sum);
		return "home";
	}

}
