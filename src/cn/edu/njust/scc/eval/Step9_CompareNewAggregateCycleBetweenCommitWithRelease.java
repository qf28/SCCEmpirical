package cn.edu.njust.scc.eval;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Step9_CompareNewAggregateCycleBetweenCommitWithRelease {

	public static void main(String[] args) throws Exception {

		Step9_CompareNewAggregateCycleBetweenCommitWithRelease prog = new Step9_CompareNewAggregateCycleBetweenCommitWithRelease();

		String SCCDirCommit = "/Users/qiongfeng/Documents/projects/apache-commit-space/yasson/scc-change/";
		String SCCDirRelease = "/Users/qiongfeng/Downloads/releases/yasson-release/changed-scc/";
		String commitTimeStampFolder = "/Users/qiongfeng/Documents/projects/apache-commit-space/yasson/startTime/";
		String releaseFilePath = "/Users/qiongfeng/Downloads/releases/yasson-release/tags/yasson-tags.txt";
		// prog.compareSizeBetweenReleaseAndCommit(SCCDirCommit, SCCDirRelease,
		// commitTimeStampFolder, releaseFilePath);
		prog.compareFilesBetweenReleaseAndCommit(SCCDirCommit, SCCDirRelease, commitTimeStampFolder, releaseFilePath);
	}

	public void compareSizeBetweenReleaseAndCommit(String SCCDirCommit, String SCCDirRelease,
			String commitTimeStampFolder, String releaseFilePath) throws Exception {

		Map<String, LocalDate> linesRelease = new HashMap<>();
		Map<String, LocalDate> linesCommit = new HashMap<>();
		// ,"brand-new" "aggregate",
		String[] types = { "aggregate" };

		for (String type : types) {
			String commitDir = SCCDirCommit + type + File.separator;
			String releaseDir = SCCDirRelease + type + File.separator;
			// getAvgSizeFromScc(releaseDir, releaseFilePath);
			// System.out.println("\n\n\n");
			getAvgSizeFromCommitScc(commitDir, commitTimeStampFolder);
			System.out.println("\n\n\n");
		}
	}

	public void compareFilesBetweenReleaseAndCommit(String SCCDirCommit, String SCCDirRelease,
			String commitTimeStampFolder, String releaseFilePath) throws Exception {

		Map<String, LocalDate> linesRelease = new HashMap<>();
		Map<String, LocalDate> linesCommit = new HashMap<>();
		// ,"brand-new" "aggregate",
		String[] types = { "aggregate", "brand-new" };

		for (String type : types) {
			String commitDir = SCCDirCommit + type + File.separator;
			String releaseDir = SCCDirRelease + type + File.separator;
			// System.out.println(type);
			Map<String, LocalDate> rmap = getLinesFromReleaseDir(releaseDir, releaseFilePath);
			for (String key : rmap.keySet()) {
				linesRelease.put(key, rmap.get(key));
			}

			Map<String, LocalDate> cmap = getLinesFromCommitDir(commitDir, commitTimeStampFolder);
			for (String key : cmap.keySet()) {
				linesCommit.put(key, cmap.get(key));
			}

		}

		// Set<String> tmp = new HashSet<>(linesRelease.keySet());
		// tmp.retainAll(linesCommit.keySet());
		// System.out.println(linesRelease.size() + "," + linesCommit.size() + "," +
		// tmp.size());
		//
		// long total = 0;
		// for (String s : tmp) {
		// System.out.println(s);
		// LocalDate cTime = linesCommit.get(s);
		// LocalDate rTime = linesRelease.get(s);
		// // DAYS.between(cTime, rTime);
		// // Duration.between(cTime, rTime).toDays();
		// System.out.println("Release found time :" + linesRelease.get(s));
		// System.out.println("Commit found time :" + linesCommit.get(s));
		// total += cTime.until(rTime, ChronoUnit.DAYS);
		// System.out.println();
		// }
		// System.out.println(total / tmp.size());

		Set<String> tmp = new HashSet<>(linesCommit.keySet());
		tmp.removeAll(linesRelease.keySet());
		for (String s : tmp) {
			System.out.println(s);
		}

	}

	public Map<String, LocalDate> getLinesFromCommitDir(String dir, String commitTimeStampFolder) throws Exception {

		Map<String, LocalDate> lines = new HashMap<>();
		File dirFile = new File(dir);

		for (File f : dirFile.listFiles()) {

			String commitId = f.getName().substring(f.getName().indexOf("-") + 1).replace(".txt", "");
			// System.out.println(commitId);
			String timePath = commitTimeStampFolder + commitId + "-startTime.txt";
			LocalDate localDate = LocalDate.parse(FileUtils.readFileToString(new File(timePath)).subSequence(0, 10));
			LocalDate lastReleaseTime = LocalDate.of(2022, 1, 13);

			if (localDate.isBefore(lastReleaseTime)) {
				List<String> tmp = FileUtils.readLines(f);
				for (String line : tmp) {
					String[] tokens = line.replace("[", "").replace("]", "").split(",");
					for (String token : tokens) {
						if (token.endsWith("_java")) {
							int index = token.lastIndexOf(".");
							String key = token.substring(index + 1).strip();

							if (lines.containsKey(key)) {
								int v = lines.get(key).compareTo(localDate);
								if (v > 0) {
									lines.put(key, localDate);
								}
							} else {
								lines.put(key, localDate);
							}

						}
					}
				}
			}
		}
		return lines;
	}

	public Map<String, LocalDate> getLinesFromReleaseDir(String dir, String releaseFilePath) throws Exception {

		List<String> tags = FileUtils.readLines(new File(releaseFilePath));
		Map<String, LocalDate> releaseTimes = new HashMap<>();
		for (String tag : tags) {
			String[] tokens = tag.split(" ");
			releaseTimes.put(tokens[0].substring(tokens[0].lastIndexOf("/") + 1), LocalDate.parse(tokens[1]));
		}
		// System.out.println(releaseTimes);

		Map<String, LocalDate> lines = new HashMap<>();
		File dirFile = new File(dir);

		for (File f : dirFile.listFiles()) {

			String releaseId = f.getName().substring(f.getName().indexOf("-") + 1).replace(".txt", "");

			List<String> tmp = FileUtils.readLines(f);
			for (String line : tmp) {
				String[] tokens = line.replace("[", "").replace("]", "").split(",");
				for (String token : tokens) {
					if (token.endsWith("_java")) {
						int index = token.lastIndexOf(".");
						lines.put(token.substring(index + 1).strip(), releaseTimes.get(releaseId));
						// lines.add(token.strip());
					}
				}
			}

		}
		return lines;
	}

	public void getAvgSizeFromScc(String dir, String releaseFilePath) throws Exception {

		List<String> tags = FileUtils.readLines(new File(releaseFilePath));
		Map<String, LocalDate> releaseTimes = new HashMap<>();
		for (String tag : tags) {
			String[] tokens = tag.split(" ");
			releaseTimes.put(tokens[0].substring(tokens[0].lastIndexOf("/") + 1), LocalDate.parse(tokens[1]));
		}
		// System.out.println(releaseTimes);

		Map<String, LocalDate> lines = new HashMap<>();
		File dirFile = new File(dir);

		int total = 0;
		int count = 0;
		for (File f : dirFile.listFiles()) {
			List<String> tmp = FileUtils.readLines(f);
			String line = tmp.get(1);
			String[] tokens = line.replace("[", "").replace("]", "").split(",");
			System.out.println(tokens.length);
			total += tokens.length;
			count++;

		}

		System.out.println(total / count);
	}

	public void getAvgSizeFromCommitScc(String dir, String commitTimeStampFolder) throws Exception {

		Map<String, LocalDate> lines = new HashMap<>();
		File dirFile = new File(dir);
		int total = 0;
		int count = 0;
		for (File f : dirFile.listFiles()) {

			String commitId = f.getName().substring(f.getName().indexOf("-") + 1).replace(".txt", "");
			// System.out.println(commitId);
			String timePath = commitTimeStampFolder + commitId + "-startTime.txt";
			LocalDate localDate = LocalDate.parse(FileUtils.readFileToString(new File(timePath)).subSequence(0, 10));
			LocalDate lastReleaseTime = LocalDate.of(2022, 1, 13);

			if (localDate.isBefore(lastReleaseTime)) {
				List<String> tmp = FileUtils.readLines(f);
				String line = tmp.get(1);
				String[] tokens = line.replace("[", "").replace("]", "").split(",");
				System.out.println(tokens.length);
				total += tokens.length;
				count++;
			}
		}
		System.out.println(total / count);
	}

}
