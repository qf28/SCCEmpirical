package cn.edu.njust.scc.eval;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Step9_CompareBreakCycleBetweenCommitWithRelease {

	public static void main(String[] args) throws Exception {

		Step9_CompareBreakCycleBetweenCommitWithRelease prog = new Step9_CompareBreakCycleBetweenCommitWithRelease();

		String SCCDirCommit = "/Users/qiongfeng/Documents/projects/apache-commit-space/yasson/scc-change/";
		String SCCDirRelease = "/Users/qiongfeng/Downloads/yasson-release/changed-scc/";
		String commitTimeStampFolder = "/Users/qiongfeng/Documents/projects/apache-commit-space/yasson/startTime/";
		String releaseFilePath = "/Users/qiongfeng/Downloads/yasson-release/tags/yasson-tags.txt";

		prog.compareFilesBetweenReleaseAndCommit(SCCDirCommit, SCCDirRelease, commitTimeStampFolder, releaseFilePath);
	}

	public void compareFilesBetweenReleaseAndCommit(String SCCDirCommit, String SCCDirRelease,
			String commitTimeStampFolder, String releaseFilePath) throws Exception {

		// Set<String> linesRelease = new HashSet<>();
		// Set<String> linesCommit = new HashSet<>();
		Map<String, LocalDate> linesRelease = new HashMap<>();
		Map<String, LocalDate> linesCommit = new HashMap<>();
		// String[] types = { "aggregate", "brand-new", "disappeared", "break" ,"same"
		// };
		String[] types = { "break" };

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

		// String test = "IDeserializerProvider_java";
		// System.out.println(linesRelease.contains(test));
		// System.out.println(linesCommit.contains(test));

		Set<String> tmp = new HashSet<>(linesRelease.keySet());
		tmp.retainAll(linesCommit.keySet());
		System.out.println(linesRelease.size() + "," + linesCommit.size() + "," + tmp.size());

		// tmp = new HashSet<>(linesRelease);
		// tmp.removeAll(linesCommit);
		// System.out.println(linesRelease.size() + "," + linesCommit.size() + "," +
		// tmp.size());

		for (String s : tmp) {
			System.out.println(s);

			System.out.println("Release found time :" + linesRelease.get(s));
			System.out.println("Commit found time :" + linesCommit.get(s));
			System.out.println();
		}

		// for (String s : tmp) {
		// System.out.println(s);
		//
		// System.out.println("Release contains:" + linesRelease.contains(s));
		// System.out.println("Commit contains:" + linesCommit.contains(s));
		// System.out.println();
		// }

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
							// lines.put(, localDate);
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
			String[] tokens = tag.split("\\s+");
			releaseTimes.put(tokens[0].substring(tokens[0].lastIndexOf("/") + 1), LocalDate.parse(tokens[1]));
		}

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
}
