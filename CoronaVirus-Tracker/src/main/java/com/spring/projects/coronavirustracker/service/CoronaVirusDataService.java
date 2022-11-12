package com.spring.projects.coronavirustracker.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spring.projects.coronavirustracker.data.LocationStatData;

@Service
public class CoronaVirusDataService {

	// Make a Http call to the belwo url containing the Corona Virus data
	private static String CORONA_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/archived_data/archived_time_series/time_series_19-covid-Confirmed_archived_0325.csv";
	
	private List<LocationStatData> allStats= new ArrayList<>();
	
	public List<LocationStatData> getAllStats() {
		return allStats;
	}

	/*
	 * 1. Call the below method as this class is instantiated, use the below annotation
	 * 2. Run the below method at a particular frequency like every second, minute or every hour of the day
	 */
	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchData() throws IOException, InterruptedException {
		
		List<LocationStatData> newStats= new ArrayList<>();
		
		HttpClient client = HttpClient.newHttpClient();

		// Create a httpRequest and corresponding httpResponse
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CORONA_DATA_URL)).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		// System.out.println(response.body());

		// Parse the above csv file in a readable format
		StringReader reader = new StringReader(response.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
		for (CSVRecord record : records) {
			
			//System.out.println(record.get("Province/State"));
			  LocationStatData location = new LocationStatData();
			  
			  location.setState(record.get("Province/State"));
			  location.setCountry(record.get("Country/Region"));
			 // location.setTotalCases(Integer.parseInt(record.get("3/23/20")));
			 // location.setTotalCases(record.get("3/23/20"));
		//	  System.out.println(location);
			  newStats.add(location);
			 
		}
		
			this.allStats = newStats;
	}
}
