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

@SuppressWarnings("deprecation")
public class TwitterOAuthSession {
	
	private static TwitterOAuthSession self;
	
	private static final String AUTH_FILE = "./auth.json";
	
	private static String consumerKeyStr;
	private static String consumerSecretStr;
	private static String accessTokenStr;
	private static String accessTokenSecretStr;
	
	private static OAuthConsumer oAuthConsumer;
	
	private static JSONObject authconfig;

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
	
	public static TwitterOAuthSession getInstance() {
		if(self == null) {
			self = new TwitterOAuthSession();
		}
		return self;
	}
	
	public TwitterAPIRequest makeAPIRequest(String requestQuery) {
		return new TwitterAPIRequest(requestQuery, oAuthConsumer);
	}
	
	public class TwitterAPIRequest {
		
		private String response;
		
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
		
		public String getResponse() {
			return response;
		}

	}
	
}
