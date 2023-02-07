package sellerapp.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import sellerapp.dao.DBHelper;

@Repository
public class DBHelperImpl extends NamedParameterJdbcDaoSupport implements DBHelper {

	
	private static final String BASE_URL = "sellerapp.com/";
	
	SimpleDateFormat DB_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String getShortUrl(String longUrl, int expiry, boolean skipRetry) throws Exception {
		String sql = "";

		try {
			String token = RandomStringUtils.random(12, true, true);

			String expiredAt = null;
			if(expiry > 0) {
				Date date = new Date();
				date = DateUtils.addSeconds(date, expiry);
				expiredAt = DB_FORMAT.format(date);
			}

			String query = "select count(*) from short_url";
			Integer count = getJdbcTemplate().queryForObject(query, Integer.class);

			if(count>=20000) {
				throw new Exception("Cannot create short url due to over limit in the DB.");
			}
			
			sql = "insert into short_url (token, long_url, expired_at) values (?, ?, ?)";
			getJdbcTemplate().update(sql, new Object[] {token, longUrl, expiredAt});

			return BASE_URL + token;

		}catch(Exception e) {
			e.printStackTrace();
			if(!skipRetry) {
				return getShortUrl(longUrl, expiry, true);
			}
			sql = "select token from short_url where long_url = ?";
			try {
				String token = getJdbcTemplate().queryForObject(sql, new Object[] {longUrl}, String.class);
				return BASE_URL + token;
			}catch(Exception e1) {
				if(!skipRetry) {
					return getShortUrl(longUrl, expiry, true);
				}
			}
		}
		throw new Exception("Cannot create short url.");
	}
}
