package com.gayatry.rest;

import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class JsonParser {

	private final String BaseUrl = "http://wcfmobile.gayatrihomes.com/Service1.svc/";

	public String postResponse(String mUrl, JSONObject object) throws Exception {
		HttpURLConnection conexao = null;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
		try {
			Log.e("url", ""+BaseUrl+""+mUrl);
			URL url = new URL(BaseUrl+""+mUrl);
			conexao = (HttpURLConnection) url.openConnection();
			conexao.setConnectTimeout(20000);
			conexao.setReadTimeout(15000);
			conexao.setRequestMethod("POST");
			conexao.setRequestProperty("content-type", "application/json");
			conexao.setDoInput(true);
			conexao.setDoOutput(true);

			Uri.Builder builder = new Uri.Builder()
					.appendQueryParameter("parametros", object.toString());
			String query = builder.build().getEncodedQuery();
			conexao.setFixedLengthStreamingMode(query.getBytes().length);
			OutputStream os = conexao.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(query);
			writer.flush();
			writer.close();
			os.close();
			conexao.connect();
			int responseCode = conexao.getResponseCode();
			Log.v( " reponseCode", String.valueOf(responseCode));
			if(responseCode == HttpURLConnection.HTTP_OK){
				StringBuilder sb = new StringBuilder();
				try{
					BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
					String linha;
					while ((linha = br.readLine())!= null){
						sb.append(linha);
					}
					return sb.toString();
				}catch (Exception e){
					e.printStackTrace();
				}
			}else{
				if(responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
					throw new Exception("Tempo maximo na comunição atingido: "+ conexao.getErrorStream());
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			throw new Exception("Falha de comunicação, verifique sua conexão com a internet");
		}finally {

			conexao.disconnect();
		}

		return "";
	}

	public String postToResponse(String mUrl, String object) throws IOException, JSONException {
		URL url = new URL(BaseUrl+""+mUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");

		OutputStream os = conn.getOutputStream();
		os.write(object.getBytes("UTF-8"));
		os.close();

		// read the response
		InputStream in = new BufferedInputStream(conn.getInputStream());
		Log.e("url", ""+BaseUrl+""+mUrl);
		Log.e("req code", ""+conn.getResponseCode());
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder result = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null) {
			result.append(line);
		}
		Log.e("response", result.toString());
		in.close();
		conn.disconnect();
		return result.toString();
	}

	public String getResponse(String mUrl){
		URL url;
		StringBuffer response = null;
		HttpURLConnection urlConnection = null;
		try {
			Log.e("Url",""+BaseUrl+""+mUrl);
			url = new URL(BaseUrl+""+mUrl);

			urlConnection = (HttpURLConnection) url
					.openConnection();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			Log.e("response", "" + response);
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return response.toString();
	}

}
