package cn.edu.njust.scc.datacollect;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import cn.edu.njust.scc.analysis.ProjectNames;

public class Step3_ExtractSCCDsm {
	static String resFolder = "/Users/qiongfeng/Documents/cyclic-dependency/summary/";
	static String resFolderDetails = "/Users/qiongfeng/Documents/projects/apache-commit-space/";

	public static void main(String[] args) throws Exception {

		Step3_ExtractSCCDsm prog = new Step3_ExtractSCCDsm();

		String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";

		for (String proj : ProjectNames.projs) {
			// String[] types = { "aggregate" };
			String[] types = { "aggregate", "brand-new", "disappeared", "break", "same" };
			// String[] types = { "brand-new", "disappeared" };

			for (String type : types) {
				String sccChangeFolder = folder + proj + "/scc-change/" + type + File.separator;
				String projDsmFolder = folder + proj + "/sdsm/";
				String outFolder = folder + proj + "/scc-sdsm/" + type + File.separator;

				for (File sccChange : new File(sccChangeFolder).listFiles()) {

					if (sccChange.getName().endsWith(".txt")) {
						// System.out.println(sccChange.getPath());

						// String index = sccChange.getName().replace(".txt", "");
						List<String> lines = FileUtils.readLines(sccChange);
						String name = sccChange.getName().replace(".txt", "");
						String commitId = name.substring(name.indexOf("-") + 1);

						String[] temp;

						List<String> prev = new ArrayList<>();
						// System.out.println(lines);

						temp = lines.get(1).replace("[", "").replace("]", "").split(",");
						for (String s : temp) {
							prev.add(s.strip());
						}

						List<String> later = new ArrayList<>();
						for (int i = 2; i < lines.size(); i++) {
							temp = lines.get(i).replace("[", "").replace("]", "").split(",");
							for (String s : temp) {
								String s2 = s.strip();
								later.add(s2);
							}
						}

						String sdsmDir = projDsmFolder + commitId;

						if (new File(sdsmDir).exists()) {
							String dsmFile = folder + proj + "/sdsm/" + commitId + File.separator;
							// System.out.println(dsmFile);

							String beforeDsm = dsmFile + "t0-" + commitId + "~1.dsm";
							String afterDsm = dsmFile + "t1-" + commitId + ".dsm";

							// System.out.println(beforeDsm);
							// System.out.println(afterDsm);

							String outDsm = outFolder + name + File.separator;
							// System.out.println(outDsm);
							new File(outDsm).mkdirs();

							if (type.equalsIgnoreCase("aggregate")) {
								prog.parse(beforeDsm, outDsm + "before.dsm", later);
								prog.parse(afterDsm, outDsm + "after.dsm", prev);
							} else if (type.equalsIgnoreCase("brand-new")) {
								prog.parse(afterDsm, outDsm + "after.dsm", prev);
							} else if (type.equalsIgnoreCase("same")) {
								prog.parse(afterDsm, outDsm + "before-after.dsm", prev);
							} else if (type.equalsIgnoreCase("break")) {
								prog.parse(beforeDsm, outDsm + "before.dsm", prev);
								prog.parse(afterDsm, outDsm + "after.dsm", later);
							} else if (type.equalsIgnoreCase("disappeared")) {
								prog.parse(beforeDsm, outDsm + "before.dsm", prev);
							}
						}
					}
				}
			}
		}
	}

	public void parse(String sdsm, String out, List<String> files) throws Exception {

		PrintWriter pw = new PrintWriter(out);
		List<String> lines = FileUtils.readLines(new File(sdsm));

		String deps = lines.get(0);
		pw.println(deps);
		String line = lines.get(1);
		int size = Integer.valueOf(line);
		pw.println(files.size());

		Map<String, Integer> filemap = new HashMap<>();
		for (int index = 2 + size; index < 2 + size + size; index++) {
			filemap.put(lines.get(index), index - size - 2);

		}

		List<Integer> indexes = new ArrayList<>();
		for (String file : files) {
			// System.out.println(file);
			indexes.add(filemap.get(file));
		}

		// System.out.println(indexes);
		for (int index : indexes) {
			line = lines.get(2 + index);
			// System.out.println("line: " + line);
			String[] tmp = line.split("\\s+");

			// for (int i = 0; i < tmp.length; i++) {
			// System.out.println("index " + i + " " + tmp[i]);
			// }
			StringBuilder sb = new StringBuilder();
			for (int index2 : indexes) {
				sb.append(" " + tmp[index2]);
			}
			pw.println(sb.substring(1));
		}
		for (String file : files) {
			pw.println(file);
		}
		pw.close();
	}

}
