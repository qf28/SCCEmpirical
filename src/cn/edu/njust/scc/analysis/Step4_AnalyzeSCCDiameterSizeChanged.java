package cn.edu.njust.scc.analysis;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import edu.drexel.cs.issuespace.dataStructure.IssueDsm;
import edu.drexel.cs.issuespace.parser.dsm.ParseStructureDSM;
import edu.drexel.cs.issuespace.util.output.Graph;

public class Step4_AnalyzeSCCDiameterSizeChanged {

	public static void main(String[] args) throws Exception {

		String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";

		Step4_AnalyzeSCCDiameterSizeChanged prog = new Step4_AnalyzeSCCDiameterSizeChanged();

		for (String proj : ProjectNames.projs) {

			String[] types = { "aggregate", "brand-new", "disappeared", "break", "same" };
			// String[] types = { "break" };
			for (String type : types) {

				String out = folder + proj + "/scc-size-diameter/" + type + ".csv";
				String outFolder = folder + proj + "/scc-sdsm/" + type + File.separator;

				new File(folder + proj + "/scc-size-diameter/").mkdirs();
				PrintWriter pw = new PrintWriter(out);
				pw.println("Index,SizeBefore,DiameterBefore,SizeAfter,DiameterAfter");
				if (!new File(outFolder).exists()) {
					pw.close();
					continue;
				}

				for (File sdsmFolder : new File(outFolder).listFiles()) {
					if (sdsmFolder.isDirectory()) {

						// System.out.println(proj + "," + type + "," + sdsmFolder.getName());
						if (type.equalsIgnoreCase("aggregate")) {
							File before = new File(sdsmFolder + File.separator + "before-max.dsm");
							String sizeBefore = "1";
							int diameterBefore = 0;
							List<String> lines;
							if (before.exists()) {
								lines = FileUtils.readLines(before);
								sizeBefore = lines.get(1);
								diameterBefore = prog.calDiameter(before.getPath());
							}

							File after = new File(sdsmFolder + File.separator + "after.dsm");
							lines = FileUtils.readLines(after);
							String sizeAfter = lines.get(1);
							int diameterAfter = prog.calDiameter(after.getPath());

							// System.out.println(sdsm.getPath());
							pw.println(sdsmFolder.getName() + "," + sizeBefore + "," + diameterBefore + "," + sizeAfter
									+ "," + diameterAfter);
						} else if (type.equalsIgnoreCase("break")) {

							File before = new File(sdsmFolder + File.separator + "before.dsm");
							String sizeBefore = "1";
							int diameterBefore = 0;
							List<String> lines;
							if (before.exists()) {
								lines = FileUtils.readLines(before);
								sizeBefore = lines.get(1);
								diameterBefore = prog.calDiameter(before.getPath());
							}

							File after = new File(sdsmFolder + File.separator + "after-max.dsm");
							String sizeAfter = "1";
							int diameterAfter = 0;
							if (after.exists()) {
								lines = FileUtils.readLines(after);
								sizeAfter = lines.get(1);
								diameterAfter = prog.calDiameter(after.getPath());
							}

							// System.out.println(sdsm.getPath());
							pw.println(sdsmFolder.getName() + "," + sizeBefore + "," + diameterBefore + "," + sizeAfter
									+ "," + diameterAfter);
						}

						else if ((type.equalsIgnoreCase("brand-new") || type.equalsIgnoreCase("disappeared")
								|| type.equalsIgnoreCase("same"))) {
							for (File sdsm : sdsmFolder.listFiles()) {
								if (sdsm.getName().endsWith(".dsm")) {
									// System.out.println(sdsm.getPath());
									List<String> lines = FileUtils.readLines(sdsm);
									String size = lines.get(1);
									int diameter = prog.calDiameter(sdsm.getPath());

									// System.out.println(proj + "," + type + "," + sdsmFolder.getName() + "," +
									// diameter);

									// System.out.println(sdsm.getPath());
									pw.println(sdsmFolder.getName() + "," + size + "," + diameter);
								}
							}

						}

					}

				}

				pw.close();
			}
		}

		// String f = "/Users/qiongfeng/Downloads/test.dsm";
		// prog.calDiameter(f);

	}

	public int calJsonDiameter(String fname) throws Exception {
		Graph graph = generateGraphFromJson.parseJson(fname);
		return graph.getDiameter();
	}

	public int calDiameter(String fname) throws Exception {
		IssueDsm dsm = ParseStructureDSM.parse(fname);
		int size = dsm.size;
		Map<String, Set<String>> map = dsm.relyOnOthers;
		Map<String, Set<String>> map2 = dsm.relyByOthers;

		List<String> files = dsm.files;

		int diameter = 0;

		int d1 = 0, d2 = 0;
		for (String file : files) {
			Queue<String> q = new LinkedList<>();
			Set<String> visited = new HashSet<>();
			q.offer(file);
			int dep = -1;
			// System.out.println(file);
			while (!q.isEmpty()) {
				int qsize = q.size();
				dep++;
				for (int i = 0; i < qsize; i++) {
					String head = q.poll();
					visited.add(head);
					Set<String> nb = map.getOrDefault(head, new HashSet<>());
					// System.out.println(head + "," + nb);
					q.addAll(nb);
				}
				if (visited.size() == size) {
					break;
				}
			}
			diameter = (diameter > dep) ? diameter : dep;
			d1 = diameter;
			// System.out.println(file + "," + diameter);

		}
		return diameter;
	}

}
