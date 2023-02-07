package sellerapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sellerapp.dao.DBHelper;

@RestController
public class RestServiceController {

	@Autowired
	DBHelper dbHelper;

	@RequestMapping(value = "/rest-api/v1/generate-short-url", method = RequestMethod.POST)
	public String getShortUrl(@RequestParam("longUrl") String longUrl)
			throws Exception {

		return dbHelper.getShortUrl(longUrl, 24*60*60 , false);
	}
}
