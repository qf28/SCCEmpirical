package cn.edu.njust.scc.eval;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.edu.njust.scc.analysis.ProjectNames;
import edu.drexel.cs.issuespace.dataStructure.IssueDsm;
import edu.drexel.cs.issuespace.parser.dsm.ParseStructureDSM;

public class Step9_CompareClassCycleWithPackgeCycle {

	static int countPkgCycleBefore = 0;
	static int countNonPkgCycleBefore = 0;

	static int countPkgCycleAfter = 0;
	static int countNonPkgCycleAfter = 0;

	public static void main(String[] args) throws Exception {

		Step9_CompareClassCycleWithPackgeCycle prog = new Step9_CompareClassCycleWithPackgeCycle();
		prog.detectPkgCycle();
	}

	public void detectPkgCycle() throws Exception {
		String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";
		String[] types = { "aggregate", "brand-new", "disappeared", "break", "same" };
		// String[] types = { "aggregate" };
		for (String type : types) {
			countPkgCycleBefore = 0;
			countNonPkgCycleBefore = 0;

			countPkgCycleAfter = 0;
			countNonPkgCycleAfter = 0;

			Map<String, Integer> map = new HashMap<>();

			String dsmName = "";
			if (type.equalsIgnoreCase("aggregate") || type.equalsIgnoreCase("brand-new")) {
				dsmName = "after.dsm";
			} else if (type.equalsIgnoreCase("break") || type.equalsIgnoreCase("disappeared")) {
				dsmName = "before.dsm";
			} else {
				dsmName = "before-after.dsm";
			}

			for (String proj : ProjectNames.projs) {

				String outFolder = folder + proj + "/scc-sdsm/" + type + File.separator;
				if (new File(outFolder).exists()) {
					for (File f : new File(outFolder).listFiles()) {
						if (f.isDirectory()) {
							String dsm = f.getPath() + File.separator + dsmName;
							if (new File(dsm).exists()) {
								IssueDsm dsmModel = ParseStructureDSM.parse(dsm);
								Set<String> pkgs = new HashSet<>();
								for (String s : dsmModel.files) {
									// System.out.println(s);
									String[] tokens = s.split("\\.");
									StringBuilder sb = new StringBuilder();
									for (int i = 0; i < tokens.length - 1; i++) {
										sb.append("." + tokens[i]);
									}
									// System.out.println(tokens.length);
									if (sb.length() > 0) {
										pkgs.add(sb.substring(1));
										// System.out.println(sb.substring(1));
									}
								}
								if (pkgs.size() > 1) {
									countPkgCycleBefore++;
								} else {
									countNonPkgCycleBefore++;
								}
							} else {
								countNonPkgCycleBefore++;
							}
						}
					}
				}
			}
			System.out.println(countPkgCycleBefore + "," + countNonPkgCycleBefore);
		}
		// String dsmNamebefore = "";
		// String dsmNameAfter = "";
		// if (type.equalsIgnoreCase("aggregate")) {
		// dsmNameAfter = "after.dsm";
		// dsmNamebefore = "before-max.dsm";
		// } else if (type.equalsIgnoreCase("break")) {
		// dsmNamebefore = "before.dsm";
		// dsmNameAfter = "after-max.dsm";
		// }

		// for (String proj : ProjectNames.projs) {
		//
		// String outFolder = folder + proj + "/scc-sdsm/" + type + File.separator;
		// if (new File(outFolder).exists()) {
		// for (File f : new File(outFolder).listFiles()) {
		// if (f.isDirectory()) {
		// String dsmBefore = f.getPath() + File.separator + dsmNamebefore;
		// if (new File(dsmBefore).exists()) {
		// IssueDsm dsmBeforeModel = ParseStructureDSM.parse(dsmBefore);
		// Set<String> pkgs = new HashSet<>();
		// for (String s : dsmBeforeModel.files) {
		// // System.out.println(s);
		// String[] tokens = s.split("\\.");
		// StringBuilder sb = new StringBuilder();
		// for (int i = 0; i < tokens.length - 1; i++) {
		// sb.append("." + tokens[i]);
		// }
		// // System.out.println(tokens.length);
		// if (sb.length() > 0) {
		// pkgs.add(sb.substring(1));
		// // System.out.println(sb.substring(1));
		// }
		// }
		// if (pkgs.size() > 1) {
		// countPkgCycleBefore++;
		// } else {
		// countNonPkgCycleBefore++;
		// }
		// } else {
		// countNonPkgCycleBefore++;
		// }
		//
		// String dsmAfter = f.getPath() + File.separator + dsmNameAfter;
		// if (new File(dsmAfter).exists()) {
		// IssueDsm dsmAfterModel = ParseStructureDSM.parse(dsmAfter);
		// Set<String> pkgs = new HashSet<>();
		// for (String s : dsmAfterModel.files) {
		// // System.out.println(s);
		// String[] tokens = s.split("\\.");
		// StringBuilder sb = new StringBuilder();
		// for (int i = 0; i < tokens.length - 1; i++) {
		// sb.append("." + tokens[i]);
		// }
		// // System.out.println(tokens.length);
		// if (sb.length() > 0) {
		// pkgs.add(sb.substring(1));
		// // System.out.println(sb.substring(1));
		// }
		// }
		// if (pkgs.size() > 1) {
		// countPkgCycleAfter++;
		// } else {
		// countNonPkgCycleAfter++;
		// }
		// } else {
		// countNonPkgCycleAfter++;
		// }
		// }
		// }
		//
		// }
		// }
		//
		// System.out.println(countPkgCycleBefore + "," + countNonPkgCycleBefore + "," +
		// countPkgCycleAfter + ","
		// + countNonPkgCycleAfter);
		// }
	}

}
