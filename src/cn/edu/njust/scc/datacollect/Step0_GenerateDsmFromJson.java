package cn.edu.njust.scc.datacollect;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.stream.JsonReader;

import cn.edu.njust.scc.analysis.ProjectNames;
import javafx.util.Pair;

public class Step0_GenerateDsmFromJson {

	public static void main2(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Step0_GenerateDsmFromJson prog = new Step0_GenerateDsmFromJson();

		for (String proj : ProjectNames.projs) {

			String projFolder = ProjectNames.folder + proj + "/src/";
			String jsonFolder = ProjectNames.folder + proj + "/json/";
			String sdsmFolder = ProjectNames.folder + proj + "/sdsm/";
			// projFolder = "/Users/qiongfeng/Downloads/test/";
			for (File issueDir : new File(projFolder).listFiles()) {
				// System.out.println(issueDir.getPath());
				if (issueDir.isDirectory()) {

					String commitId = issueDir.getName();

					String before = jsonFolder + "t0-" + commitId + "~1.json";
					String after = jsonFolder + "t1-" + commitId + ".json";

					new File(sdsmFolder + commitId + File.separator).mkdirs();
					String sdsmoutbefore = sdsmFolder + commitId + File.separator + "t0-" + commitId + "~1.dsm";
					String sdsmoutafter = sdsmFolder + commitId + File.separator + "t1-" + commitId + ".dsm";
					// prog.process(before, after, commitId, sccout);
					Set<String> types = new HashSet<>();
					List<String> fileNames = new ArrayList<>();
					Map<Pair<Integer, Integer>, Set<String>> map = new HashMap<>();

					prog.parseJson(before, fileNames, types, map);
					if (fileNames.size() > 0) {
						prog.generateDsm(sdsmoutbefore, fileNames, types, map);
					}
					types = new HashSet<>();
					fileNames = new ArrayList<>();
					map = new HashMap<>();

					prog.parseJson(after, fileNames, types, map);
					if (fileNames.size() > 0) {
						prog.generateDsm(sdsmoutafter, fileNames, types, map);
					}

				}

			}

		}

	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Step0_GenerateDsmFromJson prog = new Step0_GenerateDsmFromJson();
		File dir = new File("/Users/qiongfeng/Downloads/yasson-release/json/");
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".json")) {
				String json = f.getPath();
				String dsm = json.replace("json", "dsm");
				System.out.println(dsm);
				if (new File(f.getPath().replace("json", "dsm")).exists()) {
					System.out.println("already existing:" + f.getName());
				} else {
					prog.getDsm(json, dsm);
				}
				//
			}
		}
	}

	public void getDsm(String json, String outDsm) throws Exception {
		Set<String> types = new HashSet<>();
		List<String> fileNames = new ArrayList<>();
		Map<Pair<Integer, Integer>, Set<String>> map = new HashMap<>();

		parseJson(json, fileNames, types, map);
		if (fileNames.size() > 0) {
			generateDsm(outDsm, fileNames, types, map);
		}
	}

	public void generateDsm(String out, List<String> fileNames, Set<String> types,
			Map<Pair<Integer, Integer>, Set<String>> map) throws Exception {
		PrintWriter pw = new PrintWriter(out);
		List<String> typeList = new ArrayList<>(types);
		int size = fileNames.size();

		StringBuilder sb = new StringBuilder();
		for (String s : typeList) {
			sb.append("," + s);
			// System.out.println(s);
		}
		if (types.size() == 0) {
			pw.println("[depend]");
		} else {
			pw.println("[" + sb.substring(1) + "]");
		}
		pw.println(size);

		for (int i = 0; i < size; i++) {
			StringBuilder sbLine = new StringBuilder();
			for (int j = 0; j < size; j++) {

				if (i != j) {
					StringBuilder sbp = new StringBuilder();
					Pair p = new Pair(i, j);

					if (map.containsKey(p)) {
						Set<String> deps = map.get(p);

						for (int k = 0; k < typeList.size(); k++) {
							String dp = typeList.get(k);

							if (deps.contains(dp)) {
								sbp.append(1);
							} else {
								sbp.append(0);
							}
						}
					} else {
						sbp.append(0);
					}
					sbLine.append(" " + sbp);
				} else {
					sbLine.append(" 0");
				}

			}
			pw.println(sbLine.substring(1));
		}

		for (String s : fileNames) {
			pw.println(s);
		}
		pw.close();
	}

	public void parseJson(String json, List<String> fileNames, Set<String> types,
			Map<Pair<Integer, Integer>, Set<String>> map) throws Exception {

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
								types.add(type);
								deps.add(type);
								// System.out.println("type:" + type);
								// System.out.println("weight:" + weight);
							}
							jsonReader.endObject();

						}
					}
					Pair<Integer, Integer> p = new Pair<>(src, dest);
					map.put(p, deps);
					jsonReader.endObject();

				}
				jsonReader.endArray();
			}

		}

		jsonReader.endObject();
		jsonReader.close();

		// System.out.println(types);

	}
}
