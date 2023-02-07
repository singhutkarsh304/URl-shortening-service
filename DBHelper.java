package sellerapp.dao;

import org.springframework.stereotype.Component;

@Component
public interface DBHelper {

	String getShortUrl(String longUrl, int expiry, boolean skipRetry) throws Exception;

}
