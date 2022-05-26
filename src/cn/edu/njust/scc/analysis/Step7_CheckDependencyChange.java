package cn.edu.njust.scc.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.drexel.cs.issuespace.dataStructure.IssueDsm;
import edu.drexel.cs.issuespace.parser.dsm.ParseStructureDSM;
import edu.drexel.cs.issuespace.parser.dsm.compareStructureDSM;

public class Step7_CheckDependencyChange {

	public static int checkFiles(String before, String after, String shapeBefore, String shapeAfter,
			List<Integer> counts) throws Exception {
		ParseStructureDSM parser = new ParseStructureDSM();
		IssueDsm dsm1 = parser.parse(before);
		IssueDsm dsm2 = parser.parse(after);

		int sum = 0;

		List<String> files1 = dsm1.files;
		List<String> files2 = dsm2.files;
		Set<String> file1Set = new HashSet<>(files1);
		Set<String> file2Set = new HashSet<>(files2);

		Set<String> addFiles = new HashSet<>(files2);
		addFiles.removeAll(file1Set);

		Set<String> deleteFiles = new HashSet<>(files1);
		deleteFiles.removeAll(file2Set);

		Set<String> sameFiles = new HashSet<>(files1);
		sameFiles.retainAll(file2Set);

		// System.out.print(issueId + "," + dsm1.size + "," + dsm2.size);
		StringBuilder sb = new StringBuilder();
		sb.append(shapeBefore + "," + shapeAfter + "," + before.substring(0, before.lastIndexOf("/")));
		System.out.println(shapeBefore + "," + shapeAfter + "," + before.substring(0, before.lastIndexOf("/")));
		int tmp1 = detectNewFileImpact(dsm1, dsm2, addFiles, sameFiles, sb);
		int tmp2 = detectDeleteFileChange(dsm1, dsm2, sameFiles, deleteFiles, sb);
		int tmp3 = detectExistingFilesChange(dsm1, dsm2, sameFiles, sb);
		sum += tmp1 + tmp2 + tmp3;
		int newfilecount = 0;
		int deletefilecount = 0;
		int existingfilechangecount = 0;
		if (tmp1 > 0) {
			newfilecount++;
		}
		if (tmp2 > 0) {
			deletefilecount++;
		}

		if (tmp3 > 0) {
			existingfilechangecount++;
		}

		counts.set(0, counts.get(0) + newfilecount);
		counts.set(1, counts.get(1) + deletefilecount);
		counts.set(2, counts.get(2) + existingfilechangecount);
		// System.out.println(sb);

		return sum;
	}

	public static int detectNewFileImpact(IssueDsm dsm1, IssueDsm dsm2, Set<String> addFiles, Set<String> sameFiles,
			StringBuilder sb) {
		compareStructureDSM comparer = new compareStructureDSM();
		List<String> sameFileList = new ArrayList<>(sameFiles);

		int countExistDependNew = 0;
		int countNewDependExist = 0;
		int countCycle = 0;

		for (String addFile : addFiles) {

			for (int i = 0; i < sameFileList.size(); i++) {
				String file1 = sameFileList.get(i);
				// System.out.println(file1);
				int res = comparer.checkNewFileExistingPairDsm(dsm1, dsm2, file1, addFile);
				if (res == 0) {
					// countNoDepend++;
				} else if (res == 1) {
					countExistDependNew++;
					// System.out.print(",1");
				} else if (res == 2) {
					countNewDependExist++;
					// System.out.print(",2");
				} else if (res == 3) {
					countCycle++;
					// System.out.print(",3");
				}
			}
		}

		if (countExistDependNew >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}
		if (countNewDependExist >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}
		if (countCycle >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		return (countExistDependNew + countNewDependExist + countCycle);
	}

	public static int detectDeleteFileChange(IssueDsm dsm1, IssueDsm dsm2, Set<String> sameFiles,
			Set<String> deleteFiles, StringBuilder sb) {
		compareStructureDSM comparer = new compareStructureDSM();
		List<String> sameFileList = new ArrayList<>(sameFiles);

		int countExistDependDelete = 0;
		int countDeleteDependExist = 0;
		int countDeleteExistCycle = 0;

		for (String filed : deleteFiles) {
			// System.out.println(filed);
			for (int i = 0; i < sameFileList.size(); i++) {
				String file1 = sameFileList.get(i);
				int res = comparer.checkDeleteFileExistingPairDsm(dsm1, dsm2, file1, filed);
				if (res == 1) {
					countExistDependDelete++;
				} else if (res == 2) {
					countDeleteDependExist++;

				} else if (res == 3) {
					countDeleteExistCycle++;
				}
			}
		}
		if (countDeleteDependExist >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		if (countExistDependDelete >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		if (countDeleteExistCycle >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		return (countExistDependDelete + countDeleteDependExist + countDeleteExistCycle);
	}

	public static int detectExistingFilesChange(IssueDsm dsm1, IssueDsm dsm2, Set<String> sameFiles, StringBuilder sb) {
		compareStructureDSM comparer = new compareStructureDSM();
		List<String> sameFileList = new ArrayList<>(sameFiles);

		int countNochange = 0;
		int count0to1 = 0;
		int count0to2 = 0;
		int count1to0 = 0;
		int count1toRevert1 = 0;
		int count1to2 = 0;
		int count2to1 = 0;
		int count2to0 = 0;

		for (int i = 0; i < sameFileList.size(); i++) {
			String file1 = sameFileList.get(i);
			for (int j = i + 1; j < sameFileList.size(); j++) {
				String file2 = sameFileList.get(j);
				int res = comparer.checkExistingPairDsm(dsm1, dsm2, file1, file2);
				if (res == 0) {
					countNochange++;
				} else if (res == 1) {
					count0to1++;
				} else if (res == 2) {
					count0to2++;
				} else if (res == 3) {
					count1to0++;
					// System.out.print(",9");
				} else if (res == 4) {
					count1toRevert1++;
					// System.out.println(file1);
					// System.out.println(file2);
				} else if (res == 5) {
					count1to2++;
					// System.out.print(",11");
				} else if (res == 6) {
					count2to1++;
					// System.out.print(",12");
				} else if (res == 7) {
					count2to0++;
					// System.out.print(",13");
				}
			}
		}
		if (count0to1 >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		if (count0to2 >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		if (count1to0 >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		if (count1toRevert1 >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		if (count1to2 >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		if (count2to1 >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		if (count2to0 >= 1) {
			sb.append(",1");
		} else {
			sb.append(",0");
		}

		return (count0to1 + count0to2 + count1to0 + count1toRevert1 + count1to2 + count2to1 + count2to0);
	}

}
