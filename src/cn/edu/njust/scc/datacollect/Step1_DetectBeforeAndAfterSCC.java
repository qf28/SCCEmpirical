package cn.edu.njust.scc.datacollect;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cn.edu.njust.scc.analysis.ProjectNames;
import edu.drexel.cs.issuespace.dataStructure.IssueDsm;
import edu.drexel.cs.issuespace.parser.dsm.ParseStructureDSM;
import edu.drexel.cs.issuespace.util.output.Graph;
import edu.drexel.cs.issuespace.util.output.GraphUtil;

public class Step1_DetectBeforeAndAfterSCC {
	static String resFolder = "/Users/qiongfeng/Documents/cyclic-dependency/";
	static String resFolderDetails = "/Users/qiongfeng/Documents/cyclic-dependency/details/";

	public static void main(String[] args) throws Exception {

		Step1_DetectBeforeAndAfterSCC prog = new Step1_DetectBeforeAndAfterSCC();

		String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";

		for (String proj : ProjectNames.projs) {

			String projFolder = folder + proj + "/sdsm/";
			String projSCCFolder = folder + proj + "/scc/";

			// projFolder = "/Users/qiongfeng/Downloads/test/";
			for (File issueDir : new File(projFolder).listFiles()) {
				// System.out.println(issueDir.getPath());
				if (issueDir.isDirectory()) {

					String commitId = issueDir.getName();
					// System.out.println(commitId);

					File[] dsms = new File(issueDir.getPath() + File.separator).listFiles();

					if (dsms.length < 2) {
						System.out.println(commitId);
						continue;
					}
					Arrays.sort(dsms, new Comparator<File>() {

						@Override
						public int compare(File o1, File o2) {
							// System.out.println(o1.getName().replaceAll("-.+", "").replace("t", ""));
							// System.out.println(o2.getName().replaceAll("-.+", "").replace("t", ""));
							int f1 = Integer.parseInt(o1.getName().replaceAll("-.+", "").replace("t", ""));
							int f2 = Integer.parseInt(o2.getName().replaceAll("-.+", "").replace("t", ""));
							return f1 - f2;
						}
					});

					String before = dsms[0].getPath();
					String after = dsms[dsms.length - 1].getPath();
					// System.out.println("before" + before);
					// System.out.println("after" + after);

					String sccout = projSCCFolder + commitId + File.separator;
					prog.process(before, after, commitId, sccout);

				}

			}

		}
	}

	public static void main5(String[] args) throws Exception {
		Step1_DetectBeforeAndAfterSCC prog = new Step1_DetectBeforeAndAfterSCC();
		File dir = new File("/Users/qiongfeng/Downloads/yasson-release/dsm/");
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".dsm")) {
				if (new File(f.getPath().replace(".dsm", "-scc.txt").replace("dsm", "scc")).exists()) {
					System.out.println("already existing:" + f.getName());
				} else {
					prog.detectSCC(f.getPath(), f.getPath().replace(".dsm", "-scc.txt").replace("dsm", "scc"));
				}

			}
		}

	}

	public static void main3(String[] args) throws Exception {
		Step1_DetectBeforeAndAfterSCC prog = new Step1_DetectBeforeAndAfterSCC();
		File dir = new File(
				"/Users/qiongfeng/Downloads/yasson-dsm/yasson-one-commit/b74033d37ea6f1cb297922540131eb4a483dc24e/");
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".dsm")) {
				prog.detectSCC(f.getPath(), f.getPath().replace(".dsm", "-scc.txt"));
			}
		}

	}

	public static void main4(String[] args) throws Exception {
		Step1_DetectBeforeAndAfterSCC prog = new Step1_DetectBeforeAndAfterSCC();
		File dir = new File("/Users/qiongfeng/Downloads/yasson-dsm");
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".dsm")) {
				prog.detectSCC(f.getPath(), f.getPath().replace(".dsm", "-scc.txt"));
			}
		}

	}

	public void detectSCC(String dsm, String out) throws Exception {
		Graph graph = GraphUtil.convertDsmToGraph(ParseStructureDSM.parse(dsm));
		List<Set<String>> scc = graph.SCC();

		PrintWriter pw = new PrintWriter(out);
		for (Set<String> one : scc) {
			pw.println(one);
		}
		pw.close();
	}

	public void process(String before, String after, String issueId, String sccout) throws Exception {
		IssueDsm beforedsm = ParseStructureDSM.parse(before);
		IssueDsm afterdsm = ParseStructureDSM.parse(after);

		new File(sccout).mkdirs();
		PrintWriter sccbefore = new PrintWriter(sccout + "before.txt");

		PrintWriter sccafter = new PrintWriter(sccout + "after.txt");

		// if ((beforedsm.files.size() >= 2 || afterdsm.files.size() >= 2)
		// && (beforedsm.files.size() <= 100 && afterdsm.files.size() <= 100)) {

		if ((beforedsm.files.size() >= 2 || afterdsm.files.size() >= 2)) {

			Graph graphBefore = GraphUtil.convertDsmToGraph(beforedsm);
			Graph graphAfter = GraphUtil.convertDsmToGraph(afterdsm);

			List<Set<String>> sccBefore = graphBefore.SCC();
			List<Set<String>> sccAfter = graphAfter.SCC();

			for (Set<String> one : sccBefore) {
				sccbefore.println(one);
			}

			for (Set<String> one : sccAfter) {
				sccafter.println(one);
			}

		}

		sccbefore.close();
		sccafter.close();

	}

}
