package cn.edu.njust.scc.datacollect;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import cn.edu.njust.scc.analysis.ProjectNames;
import cn.edu.njust.scc.datastructure.SCCResult;
import cn.edu.njust.scc.datastructure.SingleSCCResult;

public class Step2_DetectChangedSCC {
	static String resFolder = "/Users/qiongfeng/Documents/cyclic-dependency/summary/";
	static String resFolderDetails = "/Users/qiongfeng/Documents/projects/apache-commit-space/";
	static String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";

	public static void main(String[] args) throws Exception {

		Step2_DetectChangedSCC prog = new Step2_DetectChangedSCC();

		for (String proj : ProjectNames.projs) {

			String projFolder = folder + proj + "/scc/";
			// String projTimeStampFolder = folder + proj + "/startTime/";

			for (File issueDir : new File(projFolder).listFiles()) {
				// System.out.println(issueDir.getPath());
				if (issueDir.isDirectory()) {
					String commitId = issueDir.getName();

					// String timePath = projTimeStampFolder + commitId + "-startTime.txt";
					// LocalDate localDate = LocalDate
					// .parse(FileUtils.readFileToString(new File(timePath)).subSequence(0, 10));

					String beforePath = issueDir.getPath() + "/" + "before.txt";
					String afterPath = issueDir.getPath() + "/" + "after.txt";

					String out = "/Users/qiongfeng/Documents/projects/apache-commit-space/" + proj + "/scc-change/";

					prog.detectChangedSCC(beforePath, afterPath, commitId, out);

				}
			}

		}

	}

	public static void main3(String[] args) throws Exception {

		Step2_DetectChangedSCC prog = new Step2_DetectChangedSCC();
		String tagPath = "/Users/qiongfeng/Downloads/yasson-release/tags/yasson-tags.txt";
		String sccFolder = "/Users/qiongfeng/Downloads/yasson-release/scc/";
		String out = "/Users/qiongfeng/Downloads/yasson-release/changed-scc/";
		List<String> tags = FileUtils.readLines(new File(tagPath));
		for (int i = 0; i < tags.size() - 1; i++) {

			// String[] tokens = tags.get(i).split(" ");
			// for (String s : tokens) {
			// System.out.println(s);
			// }
			String tagi = tags.get(i).split(" ")[0];
			String tagj = tags.get(i + 1).split(" ")[0];

			System.out.println(tagi);
			System.out.println(tagj);

			int start1 = tagi.lastIndexOf('/');
			int start2 = tagj.lastIndexOf('/');

			String beforePath = sccFolder + tagi.substring(start1 + 1) + "-scc.txt";
			String afterPath = sccFolder + tagj.substring(start2 + 1) + "-scc.txt";

			prog.detectChangedSCC(beforePath, afterPath, tagj.substring(start2 + 1), out);
		}

	}

	public void detectChangedSCC(String beforePath, String afterPath, String name, String out) throws Exception {
		List<Set<String>> before = new ArrayList<>();
		List<String> linesBefore = FileUtils.readLines(new File(beforePath));

		for (int i = 0; i < linesBefore.size(); i++) {
			String[] temp = linesBefore.get(i).replace("[", "").replace("]", "").split(",");
			Set<String> setTmp = new HashSet<>();
			for (String s : temp) {
				setTmp.add(s.strip());
			}
			before.add(setTmp);
		}

		List<Set<String>> after = new ArrayList<>();
		List<String> linesAfter = FileUtils.readLines(new File(afterPath));

		for (int i = 0; i < linesAfter.size(); i++) {
			String[] temp = linesAfter.get(i).replace("[", "").replace("]", "").split(",");
			Set<String> setTmp = new HashSet<>();
			for (String s : temp) {
				setTmp.add(s.strip());
			}
			after.add(setTmp);
		}

		detectAggregateSCC(before, after, name, out + "aggregate/");
		detectBrandNewSCC(before, after, name, out + "brand-new/");
		detectBreakingSCC(before, after, name, out + "break/");
		detectDisappearSCC(before, after, name, out + "disappeared/");
		detectSameSCC(before, after, name, out + "same/");
	}

	public void detectAggregateSCC(List<Set<String>> before, List<Set<String>> after, String name, String out)
			throws Exception {
		List<SingleSCCResult> growSCC = new ArrayList<>();

		for (int j = 0; j < after.size(); j++) {
			Set<String> p2 = after.get(j);
			if (p2.size() < 2) {
				continue;
			}

			for (int i = 0; i < before.size(); i++) {
				Set<String> p1 = before.get(i);

				Set<String> tmp = new HashSet<>(p1);
				tmp.retainAll(p2);
				if (tmp.size() == p1.size() && p1.size() < p2.size()) {
					// System.out.println("growing");
					SingleSCCResult res = new SingleSCCResult(name);
					res.isGrowing(p1, p2);
					growSCC.add(res);

				}
			}

		}

		List<SCCResult> merged = new ArrayList<>();

		for (int i = 0; i < growSCC.size(); i++) {
			SingleSCCResult one = growSCC.get(i);
			SCCResult merge = new SCCResult(one.commitId);
			merge.isGrowing(one.after);
			merge.date = one.date;

			boolean contains = false;
			for (int j = merged.size() - 1; j >= 0; j--) {
				SCCResult tmp = merged.get(j);
				if (tmp.after.get(0).equals(one.after)) {
					contains = true;
					continue;
				}
			}
			if (!contains) {
				merged.add(merge);
			}
		}

		for (int j = merged.size() - 1; j >= 0; j--) {
			SCCResult tmp = merged.get(j);
			for (int i = 0; i < growSCC.size(); i++) {
				SingleSCCResult one = growSCC.get(i);
				if (tmp.after.get(0).equals(one.after)) {
					tmp.addGrowingBefore(one.before);
					// System.out.println(one.before);
				}
			}

		}

		// System.out.println(merged.size() + "," + out);
		if (merged.size() > 0) {
			new File(out).mkdirs();

			for (int i = 0; i < merged.size(); i++) {
				SCCResult one = merged.get(i);
				String localName = (i + 1) + "-" + name + ".txt";

				PrintWriter temp = new PrintWriter(out + localName);

				// StringBuilder sb = new StringBuilder();

				temp.print(one.after.get(0).size() + " ");
				// sb.append((i + 1) + "-" + name + "," + one.after.get(0).size());

				for (Set<String> scc : one.before) {
					temp.print(scc.size() + " ");
					// sb.append("," + scc.size());

				}

				// temp.println(one.date);
				temp.println();
				// temp.println(one.commitId);

				temp.println(one.after.get(0));
				for (Set<String> scc : one.before) {
					temp.println(scc);
				}
				temp.close();
			}

		}

	}

	public void detectBrandNewSCC(List<Set<String>> before, List<Set<String>> after, String name, String out)
			throws Exception {
		List<SingleSCCResult> newSCC = new ArrayList<>();
		for (int j = 0; j < after.size(); j++) {
			Set<String> p2 = after.get(j);
			if (p2.size() < 2) {
				continue;
			}
			boolean contains = false;

			for (int i = 0; i < before.size(); i++) {
				Set<String> p1 = before.get(i);

				Set<String> tmp = new HashSet<>(p1);
				tmp.retainAll(p2);
				if (tmp.size() > 0) {
					// System.out.println("breaking");
					// SingleSCCResult res = new SingleSCCResult(issueId);
					// res.isGrowing(p1, p2);
					// res.date = localDate;
					// growSCC.add(res);
					contains = true;

				}
			}
			if (!contains) {
				SingleSCCResult res = new SingleSCCResult(name);
				res.isBrandNew(p2);
				// res.date = localDate;

				newSCC.add(res);
			}
		}

		new File(out).mkdirs();

		for (int i = 0; i < newSCC.size(); i++) {
			SingleSCCResult one = newSCC.get(i);

			String localName = (i + 1) + "-" + name + ".txt";
			PrintWriter temp = new PrintWriter(out + localName);
			// StringBuilder sb = new StringBuilder();

			temp.print(one.after.size() + " ");
			// sb.append(one.commitId + "," + one.after.size());

			// temp.println(one.date);
			temp.println();
			// temp.println(one.commitId);
			temp.println(one.after);

			temp.close();
		}
	}

	public void detectDisappearSCC(List<Set<String>> before, List<Set<String>> after, String name, String out)
			throws Exception {
		List<SingleSCCResult> breakSCC = new ArrayList<>();
		for (int i = 0; i < before.size(); i++) {
			Set<String> p1 = before.get(i);
			if (p1.size() < 2) {
				continue;
			}
			boolean disappeared = true;
			for (int j = 0; j < after.size(); j++) {
				Set<String> p2 = after.get(j);

				Set<String> tmp = new HashSet<>(p2);
				tmp.retainAll(p1);
				if (tmp.size() > 0) {

					disappeared = false;
				}
			}

			if (disappeared) {
				SingleSCCResult res = new SingleSCCResult(name);
				res.isDisappearing(p1);
				// res.date = localDate;
				breakSCC.add(res);
			}
		}

		new File(out).mkdirs();

		for (int i = 0; i < breakSCC.size(); i++) {
			SingleSCCResult one = breakSCC.get(i);

			String localName = (i + 1) + "-" + name + ".txt";
			PrintWriter temp = new PrintWriter(out + localName);

			temp.println(one.before.size());

			// temp.println(one.date);
			// temp.println(one.commitId);
			temp.println(one.before);

			temp.close();
		}

	}

	public void detectBreakingSCC(List<Set<String>> before, List<Set<String>> after, String name, String out)
			throws Exception {
		List<SingleSCCResult> breakSCC = new ArrayList<>();
		for (int i = 0; i < before.size(); i++) {
			Set<String> p1 = before.get(i);
			if (p1.size() < 2) {
				continue;
			}
			for (int j = 0; j < after.size(); j++) {
				Set<String> p2 = after.get(j);

				Set<String> tmp = new HashSet<>(p2);
				tmp.retainAll(p1);
				if (tmp.size() < p1.size() && tmp.size() > 0) {
					// System.out.println("breaking");
					SingleSCCResult res = new SingleSCCResult(name);
					res.isDecreasing(p1, p2);
					// res.date = localDate;
					breakSCC.add(res);
					// break;

				}
			}
		}

		List<SCCResult> merged = new ArrayList<>();

		for (int i = 0; i < breakSCC.size(); i++) {
			SingleSCCResult one = breakSCC.get(i);
			SCCResult merge = new SCCResult(one.commitId);
			merge.isDecreasing(one.before);
			merge.date = one.date;

			boolean contains = false;
			for (int j = merged.size() - 1; j >= 0; j--) {
				SCCResult tmp = merged.get(j);
				// if (tmp.commitId == one.commitId) {
				if (tmp.commitId == one.commitId && tmp.before.get(0).equals(one.before)) {
					// System.out.println(one.before.size() + "," + tmp.before.get(0).size());

					contains = true;
					continue;
				}
			}

			if (!contains) {
				merged.add(merge);
			}
		}

		for (int i = 0; i < breakSCC.size(); i++) {
			SingleSCCResult one = breakSCC.get(i);

			for (int j = merged.size() - 1; j >= 0; j--) {
				SCCResult tmp = merged.get(j);
				// if (tmp.commitId == one.commitId) {
				if (tmp.commitId == one.commitId && tmp.before.get(0).equals(one.before)) {
					// System.out.println(one.before.size() + "," + tmp.before.get(0).size());
					tmp.addDecreasingAfter(one.after);
				}
			}

		}

		new File(out).mkdirs();

		for (int i = 0; i < merged.size(); i++) {
			SCCResult one = merged.get(i);
			String localName = (i + 1) + "-" + name + ".txt";

			PrintWriter temp = new PrintWriter(out + localName);

			temp.print(one.before.get(0).size() + " ");

			// temp.println(one.date);
			// temp.println(one.commitId);
			for (Set<String> scc : one.after) {
				temp.print(scc.size() + " ");

			}
			temp.println();

			temp.println(one.before.get(0));
			for (Set<String> scc : one.after) {
				temp.println(scc);
			}
			temp.close();

		}

	}

	public void detectSameSCC(List<Set<String>> before, List<Set<String>> after, String name, String out)
			throws Exception {
		List<SingleSCCResult> sameSCC = new ArrayList<>();
		for (int i = 0; i < before.size(); i++) {
			Set<String> p1 = before.get(i);
			if (p1.size() < 2) {
				continue;
			}

			for (int j = 0; j < after.size(); j++) {
				Set<String> p2 = after.get(j);

				if (p1.equals(p2)) {
					SingleSCCResult res = new SingleSCCResult(name);
					res.isSame(p1, p2);
					sameSCC.add(res);
					break;
				}
			}
		}

		new File(out).mkdirs();

		for (int i = 0; i < sameSCC.size(); i++) {
			SingleSCCResult one = sameSCC.get(i);
			String localName = (i + 1) + "-" + name + ".txt";

			PrintWriter temp = new PrintWriter(out + localName);
			temp.print(one.after.size() + " ");
			temp.println();
			temp.println(one.after);
			temp.close();
		}

	}

}
