package cn.edu.njust.scc.analysis;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class Step8_CalDepChangePctInShapeTransition {

	public static void main(String[] args) throws Exception {
		Step8_CalDepChangePctInShapeTransition prog = new Step8_CalDepChangePctInShapeTransition();
		// prog.countBreakingDepChange("circle", "/Users/qiongfeng/Downloads/out.csv");
		prog.countAggregatingDepChange("circle", "/Users/qiongfeng/Downloads/aggregate-dep-change.csv");
	}

	public void countBreakingDepChange(String shapeBefore, String path) throws Exception {

		Map<Integer, Integer> mapCircle = new HashMap<>();
		Map<Integer, Integer> mapOther = new HashMap<>();
		Map<Integer, Integer> mapClique = new HashMap<>();
		Map<Integer, Integer> mapChain = new HashMap<>();
		Map<Integer, Integer> mapStar = new HashMap<>();
		Map<Integer, Integer> mapTiny = new HashMap<>();
		Map<Integer, Integer> mapNoSCCAfter = new HashMap<>();

		int countTiny = 0;
		int countStar = 0;
		int countChain = 0;
		int countClique = 0;
		int countOther = 0;
		int countNoSccAfter = 0;
		int countCircle = 0;

		List<String> lines = FileUtils.readLines(new File(path));
		for (String line : lines) {
			String[] tokens = line.split(",");
			String before = tokens[0];
			String after = tokens[1];
			if (before.equalsIgnoreCase(shapeBefore)) {
				if (after.equalsIgnoreCase("tiny")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapTiny.put(i - 1, mapTiny.getOrDefault(i - 1, 0) + 1);
						}

					}
					countTiny++;
				} else if (after.equalsIgnoreCase("star")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapStar.put(i - 1, mapStar.getOrDefault(i - 1, 0) + 1);
						}

					}
					countStar++;
				} else if (after.equalsIgnoreCase("clique")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapClique.put(i - 1, mapClique.getOrDefault(i - 1, 0) + 1);
						}

					}
					countClique++;
				} else if (after.equalsIgnoreCase("chain")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapChain.put(i - 1, mapChain.getOrDefault(i - 1, 0) + 1);
						}

					}
					countChain++;
				} else if (after.equalsIgnoreCase("circle")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapCircle.put(i - 1, mapCircle.getOrDefault(i - 1, 0) + 1);
						}

					}
					countCircle++;
				} else if (after.equalsIgnoreCase("other")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapOther.put(i - 1, mapOther.getOrDefault(i - 1, 0) + 1);
						}

					}
					countOther++;
				} else if (after.equalsIgnoreCase("nosccafter")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapNoSCCAfter.put(i - 1, mapNoSCCAfter.getOrDefault(i - 1, 0) + 1);
						}

					}
					countNoSccAfter++;
				}
			}
		}

		System.out.print(shapeBefore + ",Tiny," + countTiny);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapTiny.get(i));
		}
		System.out.println();

		System.out.print(shapeBefore + ",Circle," + countCircle);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapCircle.get(i));
		}
		System.out.println();

		System.out.print(shapeBefore + ",Clique," + countClique);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapClique.get(i));
		}
		System.out.println();

		System.out.print(shapeBefore + ",Chain," + countChain);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapChain.get(i));
		}
		System.out.println();

		System.out.print(shapeBefore + ",Star," + countStar);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapStar.get(i));
		}
		System.out.println();

		System.out.print(shapeBefore + ",Other," + countOther);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapOther.get(i));
		}
		System.out.println();

		System.out.print(shapeBefore + ",NoSccAfter," + countNoSccAfter);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapNoSCCAfter.get(i));
		}
		System.out.println();

	}

	public void countAggregatingDepChange(String shapeAfter, String path) throws Exception {

		Map<Integer, Integer> mapCircle = new HashMap<>();
		Map<Integer, Integer> mapOther = new HashMap<>();
		Map<Integer, Integer> mapClique = new HashMap<>();
		Map<Integer, Integer> mapChain = new HashMap<>();
		Map<Integer, Integer> mapStar = new HashMap<>();
		Map<Integer, Integer> mapTiny = new HashMap<>();
		Map<Integer, Integer> mapNoSCCAfter = new HashMap<>();

		int countTiny = 0;
		int countStar = 0;
		int countChain = 0;
		int countClique = 0;
		int countOther = 0;
		int countNoSccAfter = 0;
		int countCircle = 0;

		List<String> lines = FileUtils.readLines(new File(path));
		for (String line : lines) {
			String[] tokens = line.split(",");
			String before = tokens[0];
			String after = tokens[1];
			if (after.equalsIgnoreCase(shapeAfter)) {
				if (before.equalsIgnoreCase("tiny")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapTiny.put(i - 1, mapTiny.getOrDefault(i - 1, 0) + 1);
						}

					}
					countTiny++;
				} else if (before.equalsIgnoreCase("star")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapStar.put(i - 1, mapStar.getOrDefault(i - 1, 0) + 1);
						}

					}
					countStar++;
				} else if (before.equalsIgnoreCase("clique")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapClique.put(i - 1, mapClique.getOrDefault(i - 1, 0) + 1);
						}

					}
					countClique++;
				} else if (before.equalsIgnoreCase("chain")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapChain.put(i - 1, mapChain.getOrDefault(i - 1, 0) + 1);
						}

					}
					countChain++;
				} else if (before.equalsIgnoreCase("circle")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapCircle.put(i - 1, mapCircle.getOrDefault(i - 1, 0) + 1);
						}

					}
					countCircle++;
				} else if (before.equalsIgnoreCase("other")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapOther.put(i - 1, mapOther.getOrDefault(i - 1, 0) + 1);
						}

					}
					countOther++;
				} else if (before.equalsIgnoreCase("nosccbefore")) {
					for (int i = 2; i < 15; i++) {
						if (Integer.parseInt(tokens[i]) == 1) {
							mapNoSCCAfter.put(i - 1, mapNoSCCAfter.getOrDefault(i - 1, 0) + 1);
						}

					}
					countNoSccAfter++;
				}
			}
		}

		System.out.print(shapeAfter + ",Tiny," + countTiny);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapTiny.get(i));
		}
		System.out.println();

		System.out.print(shapeAfter + ",Circle," + countCircle);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapCircle.get(i));
		}
		System.out.println();

		System.out.print(shapeAfter + ",Clique," + countClique);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapClique.get(i));
		}
		System.out.println();

		System.out.print(shapeAfter + ",Chain," + countChain);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapChain.get(i));
		}
		System.out.println();

		System.out.print(shapeAfter + ",Star," + countStar);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapStar.get(i));
		}
		System.out.println();

		System.out.print(shapeAfter + ",Other," + countOther);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapOther.get(i));
		}
		System.out.println();

		System.out.print(shapeAfter + ",NoSccBefore," + countNoSccAfter);
		for (int i = 1; i < 14; i++) {
			System.out.print("," + mapNoSCCAfter.get(i));
		}
		System.out.println();

	}
}
