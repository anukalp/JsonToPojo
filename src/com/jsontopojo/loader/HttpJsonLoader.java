package com.jsontopojo.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONObject;

import com.jsontopojo.constants.JsonToPojoConstants;

public class HttpJsonLoader extends JsonLoader {
	private URL JSONUrl = null;

	public HttpJsonLoader(String jSONUrl){
		try {
			JSONUrl = new URL(jSONUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void loadJsonData() {
		try {
			String response = performRequest();
			System.out.println("Respose :" + response);
			BuildJsonObject(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the jSONUrl
	 */
	public URL getJSONUrl() {
		return JSONUrl;
	}


	public String performRequest() throws IOException {

		HttpURLConnection connection = (HttpURLConnection) JSONUrl
				.openConnection();

		ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
		int responseCode = connection.getResponseCode();
		if (responseCode == -1) {
			throw new IOException(
					"Could not retrieve response code from HttpUrlConnection.");
		}
		StatusLine responseStatus = new BasicStatusLine(protocolVersion,
				connection.getResponseCode(), connection.getResponseMessage());
		BasicHttpResponse response = new BasicHttpResponse(responseStatus);
		response.setEntity(entityFromConnection(connection));
		for (Entry<String, List<String>> header : connection.getHeaderFields()
				.entrySet()) {
			if (header.getKey() != null) {
				Header h = new BasicHeader(header.getKey(), header.getValue()
						.get(0));
				response.addHeader(h);
			}
		}
		String result = null;
		if (response.getEntity().getContentLength() > 0) {
			StringWriter writer = new StringWriter();
			IOUtils.copy(response.getEntity().getContent(), writer);
			result = writer.toString();
		}

		return result;
	}

	private static HttpEntity entityFromConnection(HttpURLConnection connection) {

		BasicHttpEntity entity = new BasicHttpEntity();
		InputStream inputStream;
		try {
			inputStream = connection.getInputStream();
		} catch (IOException ioe) {
			inputStream = connection.getErrorStream();
		}
		entity.setContent(inputStream);
		entity.setContentLength(connection.getContentLength());
		entity.setContentEncoding(connection.getContentEncoding());
		entity.setContentType(connection.getContentType());
		return entity;
	}

	public static void main(String[] args) {
		HttpJsonLoader httpJsonLoader = new HttpJsonLoader(JsonToPojoConstants.SERVICE_URL);
		httpJsonLoader.loadJsonData();
	}

	@Override
	public void BuildJsonObject(String data) {
		JSONObject mJsonObject = new JSONObject(data);
		System.out.println("mJsonObject  :" + mJsonObject.length());
	}
}
