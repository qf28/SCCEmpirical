package cn.edu.njust.scc.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import edu.drexel.cs.issuespace.dataStructure.IssueDsm;
import edu.drexel.cs.issuespace.parser.dsm.ParseStructureDSM;

public class Step6_CheckGeneralShape {
	public enum Shape {
		TINY, CLIQUE, STAR, CHAIN, CIRCLE, OTHER
	}

	static int count = 0;

	public static void main(String[] args) throws Exception {
		Step6_CheckGeneralShape prog = new Step6_CheckGeneralShape();
		// prog.detectShapes();
		// prog.getShape("/Users/qiongfeng/Downloads/sample.dsm");
		prog.detectShapeTransition();
		// System.out.println(count);
		// prog.detectCircleBreaking();

	}

	public void detectShapes() throws Exception {
		String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";
		// String[] types = { "aggregate", "brand-new", "disappeared", "break", "same"
		// };

		String[] types = { "same" };

		for (String type : types) {
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
							String shape = "NoScc";
							// System.out.println(dsm);
							if (new File(dsm).exists()) {

								shape = getShape(dsm).toString();
								if (shape.equalsIgnoreCase("clique")) {
									System.out.println(dsm);
								}
							}

							map.put(shape, map.getOrDefault(shape, 0) + 1);

						}
					}

				}
			}

			// System.out.println(type);
			// System.out.println(map);

		}
	}

	public void detectShapeTransition() throws Exception {

		Map<String, Integer> mapCircle = new HashMap<>();
		Map<String, Integer> mapOther = new HashMap<>();
		Map<String, Integer> mapClique = new HashMap<>();
		Map<String, Integer> mapChain = new HashMap<>();
		Map<String, Integer> mapStar = new HashMap<>();
		Map<String, Integer> mapTiny = new HashMap<>();

		String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";
		// String[] types = { "aggregate", "brand-new", "disappeared", "break", "same"
		// };

		List<Integer> counts = new ArrayList<>();

		counts.add(0);
		counts.add(0);
		counts.add(0);
		String[] types = { "aggregate" };

		for (String type : types) {
			Map<String, Integer> map = new HashMap<>();

			String dsmNamebefore = "";
			String dsmNameAfter = "";
			if (type.equalsIgnoreCase("aggregate")) {
				dsmNameAfter = "after.dsm";
				dsmNamebefore = "before-max.dsm";
			} else if (type.equalsIgnoreCase("break")) {
				dsmNamebefore = "before.dsm";
				dsmNameAfter = "after-max.dsm";
			}

			for (String proj : ProjectNames.projs) {

				String outFolder = folder + proj + "/scc-sdsm/" + type + File.separator;
				// System.out.println(outFolder);
				if (new File(outFolder).exists()) {
					for (File f : new File(outFolder).listFiles()) {
						if (f.isDirectory()) {
							String dsmBefore = f.getPath() + File.separator + dsmNamebefore;
							String dsmAfter = f.getPath() + File.separator + dsmNameAfter;

							String dsmBeforeForDep = f.getPath() + File.separator + "before.dsm";
							String dsmAfterForDep = f.getPath() + File.separator + "after.dsm";

							String shapeAfter = "OTHER";
							String shapeBefore = "OTHER";

							// System.out.println(dsmBefore);
							// System.out.println(dsmAfter);

							if (type.equalsIgnoreCase("aggregate")) {
								if (new File(dsmBefore).exists()) {
									shapeBefore = getShape(dsmBefore).toString();
								} else {
									shapeBefore = "NoSCCBefore";
								}

								shapeAfter = getShape(dsmAfter).toString();

								if (shapeAfter.equalsIgnoreCase("tiny")) {
									mapTiny.put(shapeBefore.toLowerCase(),
											mapTiny.getOrDefault(shapeBefore.toLowerCase(), 0) + 1);
								}

								if (shapeAfter.equalsIgnoreCase("circle")) {
									mapCircle.put(shapeBefore.toLowerCase(),
											mapCircle.getOrDefault(shapeBefore.toLowerCase(), 0) + 1);

								}
								if (shapeAfter.equalsIgnoreCase("clique")) {
									mapClique.put(shapeBefore.toLowerCase(),
											mapClique.getOrDefault(shapeBefore.toLowerCase(), 0) + 1);
								}
								if (shapeAfter.equalsIgnoreCase("chain")) {
									mapChain.put(shapeBefore.toLowerCase(),
											mapChain.getOrDefault(shapeBefore.toLowerCase(), 0) + 1);
								}
								if (shapeAfter.equalsIgnoreCase("star")) {
									mapStar.put(shapeBefore.toLowerCase(),
											mapStar.getOrDefault(shapeBefore.toLowerCase(), 0) + 1);

								}
								if (shapeAfter.equalsIgnoreCase("other")) {
									mapOther.put(shapeBefore.toLowerCase(),
											mapOther.getOrDefault(shapeBefore.toLowerCase(), 0) + 1);

								}

								// if (shapeBefore.equalsIgnoreCase("star") &&
								// shapeAfter.equalsIgnoreCase("star")) {
								// System.out.println(dsmBefore);
								// System.out.println(dsmAfter);
								// }

							} else {
								if (new File(dsmAfter).exists()) {
									shapeAfter = getShape(dsmAfter).toString();
								} else {
									shapeAfter = "NoSCCAfter";
								}

								shapeBefore = getShape(dsmBefore).toString();

								if (shapeBefore.equalsIgnoreCase("tiny")) {
									mapTiny.put(shapeAfter.toLowerCase(),
											mapTiny.getOrDefault(shapeAfter.toLowerCase(), 0) + 1);
								}

								if (shapeBefore.equalsIgnoreCase("circle")) {
									mapCircle.put(shapeAfter.toLowerCase(),
											mapCircle.getOrDefault(shapeAfter.toLowerCase(), 0) + 1);

								}
								if (shapeBefore.equalsIgnoreCase("clique")) {
									mapClique.put(shapeAfter.toLowerCase(),
											mapClique.getOrDefault(shapeAfter.toLowerCase(), 0) + 1);
								}
								if (shapeBefore.equalsIgnoreCase("chain")) {
									mapChain.put(shapeAfter.toLowerCase(),
											mapChain.getOrDefault(shapeAfter.toLowerCase(), 0) + 1);
								}
								if (shapeBefore.equalsIgnoreCase("star")) {
									mapStar.put(shapeAfter.toLowerCase(),
											mapStar.getOrDefault(shapeAfter.toLowerCase(), 0) + 1);

								}
								if (shapeBefore.equalsIgnoreCase("other")) {
									mapOther.put(shapeAfter.toLowerCase(),
											mapOther.getOrDefault(shapeAfter.toLowerCase(), 0) + 1);

								}
							}

							count = count + Step7_CheckDependencyChange.checkFiles(dsmBeforeForDep, dsmAfterForDep,
									shapeBefore, shapeAfter, counts);
						}
					}

				}
			}

			// printShapeTrans(type, mapCircle, mapOther, mapClique, mapChain, mapStar,
			// mapTiny);
		}

		System.out.println(counts);

	}

	public void detectCircleBreaking() throws Exception {

		Map<String, Integer> mapCircle = new HashMap<>();
		Map<String, Integer> mapOther = new HashMap<>();
		Map<String, Integer> mapClique = new HashMap<>();
		Map<String, Integer> mapChain = new HashMap<>();
		Map<String, Integer> mapStar = new HashMap<>();
		Map<String, Integer> mapTiny = new HashMap<>();

		String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";
		// String[] types = { "aggregate", "brand-new", "disappeared", "break", "same"
		// };

		String[] types = { "break" };

		for (String type : types) {
			Map<String, Integer> map = new HashMap<>();

			String dsmNamebefore = "";
			String dsmNameAfter = "";
			if (type.equalsIgnoreCase("aggregate")) {
				dsmNameAfter = "after.dsm";
				dsmNamebefore = "before-max.dsm";
			} else if (type.equalsIgnoreCase("break")) {
				dsmNamebefore = "before.dsm";
				dsmNameAfter = "after-max.dsm";
			}

			for (String proj : ProjectNames.projs) {

				String outFolder = folder + proj + "/scc-sdsm/" + type + File.separator;
				// System.out.println(outFolder);
				if (new File(outFolder).exists()) {
					for (File f : new File(outFolder).listFiles()) {
						if (f.isDirectory()) {
							String dsmBefore = f.getPath() + File.separator + dsmNamebefore;
							String dsmAfter = f.getPath() + File.separator + dsmNameAfter;

							String dsmBeforeForDep = f.getPath() + File.separator + "before.dsm";
							String dsmAfterForDep = f.getPath() + File.separator + "after.dsm";

							String shapeAfter = "OTHER";
							String shapeBefore = "OTHER";

							if (new File(dsmAfter).exists()) {
								shapeAfter = getShape(dsmAfter).toString();
							} else {
								shapeAfter = "NoSCCAfter";
							}

							shapeBefore = getShape(dsmBefore).toString();

							if (shapeBefore.equalsIgnoreCase("star") && shapeAfter.equalsIgnoreCase("nosccafter")) {
								// System.out.println(f.getPath());

							}

						}
					}

				}
			}

			// printShapeTrans(type, mapCircle, mapOther, mapClique, mapChain, mapStar,
			// mapTiny);
		}

	}

	public void printShapeTrans(String type, Map<String, Integer> mapCircle, Map<String, Integer> mapOther,
			Map<String, Integer> mapClique, Map<String, Integer> mapChain, Map<String, Integer> mapStar,
			Map<String, Integer> mapTiny) {
		if (type.equalsIgnoreCase("aggregate")) {
			String[] shapes = { "tiny", "clique", "star", "chain", "circle", "other", "nosccbefore" };
			System.out.println("Aggreated shape was from ");
			System.out.println("Tiny was to :" + mapTiny);
			System.out.println("Clique was to :" + mapClique);
			System.out.println("Star was to :" + mapStar);
			System.out.println("Chain was to :" + mapChain);
			System.out.println("Circle was to :" + mapCircle);
			System.out.println("Other was to :" + mapOther);

			System.out.print("Tiny was from ");
			for (String shape : shapes) {
				System.out.print("," + mapTiny.get(shape));
			}
			System.out.println();
			System.out.print("Clique was from ");
			for (String shape : shapes) {
				System.out.print("," + mapClique.get(shape));
			}
			System.out.println();
			System.out.print("Star was from ");
			for (String shape : shapes) {
				System.out.print("," + mapStar.get(shape));
			}
			System.out.println();
			System.out.print("Chain was from ");
			for (String shape : shapes) {
				System.out.print("," + mapChain.get(shape));
			}
			System.out.println();
			System.out.print("Circle was from ");
			for (String shape : shapes) {
				System.out.print("," + mapCircle.get(shape));
			}
			System.out.println();
			System.out.print("Other was from ");
			for (String shape : shapes) {
				System.out.print("," + mapOther.get(shape));
			}

		} else {
			String[] shapes = { "tiny", "clique", "star", "chain", "circle", "other", "nosccafter" };
			System.out.println("Untangling shape was broken to ");
			System.out.println("Tiny was to :" + mapTiny);
			System.out.println("Clique was to :" + mapClique);
			System.out.println("Star was to :" + mapStar);
			System.out.println("Chain was to :" + mapChain);
			System.out.println("Circle was to :" + mapCircle);
			System.out.println("Other was to :" + mapOther);

			System.out.print("Tiny was to ");
			for (String shape : shapes) {
				System.out.print("," + mapTiny.get(shape));
			}
			System.out.println();
			System.out.print("Clique was to ");
			for (String shape : shapes) {
				System.out.print("," + mapClique.get(shape));
			}
			System.out.println();
			System.out.print("Star was to ");
			for (String shape : shapes) {
				System.out.print("," + mapStar.get(shape));
			}
			System.out.println();
			System.out.print("Chain was to ");
			for (String shape : shapes) {
				System.out.print("," + mapChain.get(shape));
			}
			System.out.println();
			System.out.print("Circle was to ");
			for (String shape : shapes) {
				System.out.print("," + mapCircle.get(shape));
			}
			System.out.println();
			System.out.print("Other was to ");
			for (String shape : shapes) {
				System.out.print("," + mapOther.get(shape));
			}
		}
	}

	public Shape getShape(String f) throws Exception {
		if (isTiny(f)) {
			return Shape.TINY;
		}
		// System.out.println("1");
		if (isClique(f)) {
			return Shape.CLIQUE;
		}
		// System.out.println("2");
		if (isStar(f)) {
			return Shape.STAR;
		}
		// System.out.println("3");
		if (isChain(f)) {
			return Shape.CHAIN;
		}
		// System.out.println("4");
		if (isCircle(f)) {
			return Shape.CIRCLE;
		}
		// System.out.println("5");
		return Shape.OTHER;

	}

	public boolean isTiny(String f) throws Exception {
		IssueDsm dsm = ParseStructureDSM.parse(f);
		int size = dsm.size;
		if (size == 2) {
			return true;
		}
		return false;
	}

	public boolean isClique(String f) throws Exception {
		// IssueDsm dsm = ParseStructureDSM.parse(f);
		boolean isClique = true;

		// open input stream test.txt for reading purpose.
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = br.readLine();
		int size = Integer.parseInt(br.readLine());

		for (int i = 0; i < size; i++) {
			line = br.readLine();
			String[] tokens = line.split("\\s+");
			for (int j = 0; j < size; j++) {
				if (i == j)
					continue;
				if (tokens[j].equalsIgnoreCase("0")) {
					isClique = false;
					break;
				}
			}
		}
		return isClique;
	}

	public boolean isChain(String f) throws Exception {
		IssueDsm dsm = ParseStructureDSM.parse(f);
		int size = dsm.size;

		Map<String, Set<String>> mapRelyOn = dsm.relyOnOthers;
		Map<String, Set<String>> mapRelyBy = dsm.relyByOthers;

		if (mapRelyOn.size() < size || mapRelyBy.size() < size) {
			return false;
		}
		String s = dsm.files.get(0);

		int dep = depth(mapRelyOn, mapRelyBy, s, new HashSet<>());

		if (dep == size - 1) {
			return true;
		}
		return false;
	}

	public int depth(Map<String, Set<String>> mapRelyOn, Map<String, Set<String>> mapRelyBy, String s,
			Set<String> visited) {
		visited.add(s);
		Set<String> set1 = mapRelyOn.get(s);
		Set<String> set2 = mapRelyBy.get(s);
		set2.retainAll(set1);
		int max = 0;
		if (set2.size() > 0) {

			for (String tmp : set2) {
				if (visited.contains(tmp)) {
					continue;
				}
				max = Math.max(1, 1 + depth(mapRelyOn, mapRelyBy, tmp, visited));
			}
		}

		return max;
	}

	public boolean isCircle(String f) throws Exception {

		List<String> lines = FileUtils.readLines(new File(f));
		String line = lines.get(1);
		int size = Integer.valueOf(line);

		boolean adjMatrix[][] = new boolean[size][size];

		int total = 0;
		for (int index = 2; index < 2 + size; index++) {
			line = lines.get(index);
			String[] tmp = line.split("\\s+");
			int count = 0;

			for (int col = 0; col < size; col++) {

				// if (!(to.toLowerCase().contains("test"))) {
				if (!tmp[col].equalsIgnoreCase("0")) {
					adjMatrix[index - 2][col] = true;
					count++;
					total++;
				}

			}
			if (count < 1) {
				return false;
			}
		}

		for (int col = 0; col < size; col++) {
			int count = 0;
			for (int index = 2; index < 2 + size; index++) {
				line = lines.get(index);
				String[] tmp = line.split("\\s+");
				if (!tmp[col].equalsIgnoreCase("0")) {
					count++;
				}
			}
			if (count < 1) {
				return false;
			}
		}

		IssueDsm dsm = ParseStructureDSM.parse(f);
		Map<String, Set<String>> mapRelyOn = dsm.relyOnOthers;
		Map<String, Set<String>> mapRelyBy = dsm.relyByOthers;
		if (mapRelyOn.size() < size || mapRelyBy.size() < size) {
			return false;
		}
		String s = dsm.files.get(0);

		int dep1 = depCircle(mapRelyOn, s, new HashSet<>());
		int dep2 = depCircle(mapRelyBy, s, new HashSet<>());

		if (dep1 == size - 1 || dep2 == size - 1) {
			return true;
		} else {
			return false;
		}

	}

	public int depCircle(Map<String, Set<String>> mapRelyOn, String s, Set<String> visited) {
		visited.add(s);
		Set<String> set1 = mapRelyOn.get(s);

		int max = 0;
		if (set1.size() > 0) {

			for (String tmp : set1) {
				if (visited.contains(tmp)) {
					continue;
				}
				max = Math.max(1, 1 + depCircle(mapRelyOn, tmp, visited));
			}
		}

		return max;
	}

	public boolean isStar(String f) throws Exception {
		IssueDsm dsm = ParseStructureDSM.parse(f);

		int size = dsm.size;

		Map<String, Set<String>> mapRelyOn = dsm.relyOnOthers;
		Map<String, Set<String>> mapRelyBy = dsm.relyByOthers;

		for (String key : mapRelyOn.keySet()) {
			if (mapRelyOn.get(key).size() == (size - 1) && mapRelyBy.get(key).size() == (size - 1)) {

				return true;
			}
		}
		return false;
	}
}
