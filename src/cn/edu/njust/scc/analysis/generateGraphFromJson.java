package cn.edu.njust.scc.analysis;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.stream.JsonReader;

import edu.drexel.cs.issuespace.util.output.Graph;

public class generateGraphFromJson {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

	}

	public static Graph parseJson(String json) throws Exception {
		Graph res = null;
		List<String> fileNames = new ArrayList<>();
		JsonReader jsonReader = new JsonReader(new FileReader(json));

		jsonReader.beginObject();
		// System.out.println("here");
		// System.out.println(jsonReader.nextName());
		while (jsonReader.hasNext()) {

			String name = jsonReader.nextName();
			// System.out.println(name);
			if (name.equals("schemaVersion")) {
				jsonReader.nextString();
			} else if (name.equals("name")) {
				jsonReader.nextString();
			} else if (name.equals("variables")) {
				jsonReader.beginArray();
				while (jsonReader.hasNext()) {
					fileNames.add(jsonReader.nextString().strip());
				}
				jsonReader.endArray();
				res = new Graph(fileNames.size(), fileNames);
			} else if (name.equals("cells")) {
				jsonReader.beginArray();
				while (jsonReader.hasNext()) {
					jsonReader.beginObject();
					int src = 0;
					int dest = 0;
					Set<String> deps = new HashSet<>();
					while (jsonReader.hasNext()) {
						String nameInCell = jsonReader.nextName();

						if (nameInCell.equals("src")) {
							src = Integer.parseInt(jsonReader.nextString());
							// System.out.println(src);
						} else if (nameInCell.equals("dest")) {
							dest = Integer.parseInt(jsonReader.nextString());
							// System.out.println(dest);
						} else if (nameInCell.equals("values")) {
							jsonReader.beginObject();
							while (jsonReader.hasNext()) {
								String type = jsonReader.nextName();
								String weight = jsonReader.nextString();
								// types.add(type);
								// deps.add(type);
								// System.out.println("type:" + type);
								// System.out.println("weight:" + weight);
								res.addEdge(src, dest);
							}
							jsonReader.endObject();

						}
					}
					// Pair<Integer, Integer> p = new Pair<>(src, dest);
					// map.put(p, deps);
					jsonReader.endObject();

				}
				jsonReader.endArray();
			}

		}

		jsonReader.endObject();
		jsonReader.close();

		return res;

	}
}
