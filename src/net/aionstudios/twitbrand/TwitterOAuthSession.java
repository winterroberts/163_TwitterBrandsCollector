package net.aionstudios.twitbrand;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.twitbrand.util.ConfigUtils;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

/**
 * @author Winter Roberts
 * Handles the OAuth Session and API request to any Twitter endpoint
 * that uses OAuth only.
 */
@SuppressWarnings("deprecation")
public class TwitterOAuthSession {
	
	private static TwitterOAuthSession self; //singleton
	
	private static final String AUTH_FILE = "./auth.json";
	
	//Twitter OAuth credentials, loaded from a file.
	private static String consumerKeyStr;
	private static String consumerSecretStr;
	private static String accessTokenStr;
	private static String accessTokenSecretStr;
	
	//Third-party OAuth manager.
	private static OAuthConsumer oAuthConsumer;
	
	private static JSONObject authconfig;

	/**
	 * Creates a new TwitterOAuthSession with the given
	 * login credentials or stops the application if none exist.
	 */
	private TwitterOAuthSession() {
		try {
			File aucf = new File(AUTH_FILE);
			if(!aucf.exists()) {
				aucf.getParentFile().mkdirs();
				aucf.createNewFile();
				authconfig = ConfigUtils.getLinkedJsonObject();
				System.err.println("No config, application will close!");
				authconfig.put("consumer_key", "XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				authconfig.put("consumer_secret", "XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				authconfig.put("access_token", "XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				authconfig.put("access_token_secret", "XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				ConfigUtils.writeConfig(authconfig, aucf);
				System.exit(0);
			} else {
				authconfig = ConfigUtils.readConfig(aucf);
				consumerKeyStr = authconfig.getString("consumer_key");
				consumerSecretStr = authconfig.getString("consumer_secret");
				accessTokenStr = authconfig.getString("access_token");
				accessTokenSecretStr = authconfig.getString("access_token_secret");
				oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKeyStr,
						consumerSecretStr);
				oAuthConsumer.setTokenWithSecret(accessTokenStr, accessTokenSecretStr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return A singleton object of this class.
	 */
	public static TwitterOAuthSession getInstance() {
		if(self == null) {
			self = new TwitterOAuthSession();
		}
		return self;
	}
	
	/**
	 * Makes a request to an API endpoint at the given URL.
	 * While this can make non-twitter calls they will be signed with
	 * the OAuth signature of the Twitter application used to create
	 * this object.
	 * 
	 * @param requestQuery The URL to which this request should be made.
	 * @return An instance of {@link TwitterAPIRequest} used to retrieve the
	 * server response.
	 */
	public TwitterAPIRequest makeAPIRequest(String requestQuery) {
		return new TwitterAPIRequest(requestQuery, oAuthConsumer);
	}
	
	/**
	 * @author Winter Roberts
	 * Makes a request to an endpoint, signed with the Twitter OAuth credentials
	 * loaded by this application, and stores the response.
	 */
	public class TwitterAPIRequest {
		
		private String response;
		
		/**
		 * Creates this TwitterAPIRequest and makes the request.
		 * @param requestQuery The URL to which this request should be made.
		 * @param oauth An OAuthConsumer, used to sign this request.
		 */
		@SuppressWarnings("resource")
		public TwitterAPIRequest(String requestQuery, OAuthConsumer oauth) {
			try {
				HttpGet httpGet = new HttpGet(requestQuery);
				oauth.sign(httpGet);
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpGet);
				response = IOUtils.toString(httpResponse.getEntity().getContent());
			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * @return The response from this web request.
		 */
		public String getResponse() {
			return response;
		}

	}
	
}
