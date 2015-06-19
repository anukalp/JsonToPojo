package com.jsontopojo.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class FileJsonLoader extends JsonLoader {
	private File JsonFileName = null;

	public FileJsonLoader(String jsonFileName) {
		JsonFileName = new File(jsonFileName);
	}

	@Override
	public void loadJsonData() {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(JsonFileName);
			String content = IOUtils.toString(inputStream);
			System.out.println("content :" + content);
			inputStream.close();
			BuildJsonObject(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the jsonFileName
	 */
	public File getJsonFileName() {
		return JsonFileName;
	}

	public static void main(String[] args) {
		FileJsonLoader fileJsonLoader = new FileJsonLoader(
				System.getProperty("user.dir") + "/jsondata/task_data.txt");
		System.out.println(System.getProperty("user.dir"));
		fileJsonLoader.loadJsonData();
	}

	@Override
	public void BuildJsonObject(String data) {
		JSONObject mJsonObject = new JSONObject(data);
		System.out.println("mJsonObject  :" + mJsonObject.length());
	}

}
