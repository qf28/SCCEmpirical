package cn.edu.njust.scc.analysis;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Step5_AnalyzeEvovledSCC {
	static String resFolder = "/Users/qiongfeng/Documents/cyclic-dependency/summary/";
	static String resFolderDetails = "/Users/qiongfeng/Documents/projects/apache-commit-space/";
	static String folder = "/Users/qiongfeng/Documents/projects/apache-commit-space/";

	public static void main(String[] args) throws Exception {

		Step5_AnalyzeEvovledSCC prog = new Step5_AnalyzeEvovledSCC();
		String[] types = { "aggregate", "brand-new", "disappeared", "break", "same" };
		// for (String type : types) {
		// System.out.print("," + type);
		// }
		// System.out.println();

		String title = "Project,Diameter Same,BrandNew,Disappear,AggregateBefore,AggregateAfter,BreakBefore,"
				+ "BreakAfter,Size Same,BrandNew,Disappear,AggregateBefore,AggregateAfter,BreakBefore,BreakAfter";
		System.out.println(title);
		for (String proj : ProjectNames.projs) {

			prog.calDiameterSizeChange(proj, types);

		}

	}

	public void calDiameterSizeChange(String proj, String[] types) throws Exception {
		DescriptiveStatistics statSameDiameter = new DescriptiveStatistics();
		DescriptiveStatistics statBrandnewDiameter = new DescriptiveStatistics();
		DescriptiveStatistics statDisappearDiameter = new DescriptiveStatistics();

		DescriptiveStatistics statAggregateDiameterBefore = new DescriptiveStatistics();
		DescriptiveStatistics statAggregateDiameterAfter = new DescriptiveStatistics();
		DescriptiveStatistics statBreakDiameterBefore = new DescriptiveStatistics();
		DescriptiveStatistics statBreakDiameterAfter = new DescriptiveStatistics();

		DescriptiveStatistics statSameSize = new DescriptiveStatistics();
		DescriptiveStatistics statBrandnewSize = new DescriptiveStatistics();
		DescriptiveStatistics statDisappearSize = new DescriptiveStatistics();

		DescriptiveStatistics statAggregateSizeBefore = new DescriptiveStatistics();
		DescriptiveStatistics statAggregateSizeAfter = new DescriptiveStatistics();
		DescriptiveStatistics statBreakSizeBefore = new DescriptiveStatistics();
		DescriptiveStatistics statBreakSizeAfter = new DescriptiveStatistics();

		Frequency fSameDiameter = new Frequency();
		Frequency fBrandnewDiameter = new Frequency();
		Frequency fDisappearDiameter = new Frequency();

		Frequency fAggregateDiameterBefore = new Frequency();
		Frequency fBreakDiameterBefore = new Frequency();
		Frequency fAggregateDiameterAfter = new Frequency();
		Frequency fBreakDiameterAfter = new Frequency();

		Frequency fSameSize = new Frequency();
		Frequency fBrandnewSize = new Frequency();
		Frequency fDisappearSize = new Frequency();

		Frequency fAggregateSizeBefore = new Frequency();
		Frequency fBreakSizeBefore = new Frequency();
		Frequency fAggregateSizeAfter = new Frequency();
		Frequency fBreakSizeAfter = new Frequency();

		for (String type : types) {

			String out = folder + proj + "/scc-size-diameter/" + type + ".csv";

			if (type.equalsIgnoreCase("aggregate")) {
				List<String> lines = FileUtils.readLines(new File(out));

				for (int i = 1; i < lines.size(); i++) {
					String line = lines.get(i);
					String[] tokens = line.split(",");
					fAggregateDiameterBefore.addValue(Integer.parseInt(tokens[2]));
					fAggregateSizeBefore.addValue(Integer.parseInt(tokens[1]));
					statAggregateDiameterBefore.addValue(Integer.parseInt(tokens[2]));
					statAggregateSizeBefore.addValue(Integer.parseInt(tokens[1]));

					fAggregateDiameterAfter.addValue(Integer.parseInt(tokens[4]));
					fAggregateSizeAfter.addValue(Integer.parseInt(tokens[3]));
					statAggregateDiameterAfter.addValue(Integer.parseInt(tokens[4]));
					statAggregateSizeAfter.addValue(Integer.parseInt(tokens[3]));
				}
			} else if (type.equalsIgnoreCase("brand-new")) {
				List<String> lines = FileUtils.readLines(new File(out));

				for (int i = 1; i < lines.size(); i++) {
					String line = lines.get(i);
					String[] tokens = line.split(",");
					fBrandnewDiameter.addValue(Integer.parseInt(tokens[2]));
					fBrandnewSize.addValue(Integer.parseInt(tokens[1]));
					statBrandnewDiameter.addValue(Integer.parseInt(tokens[2]));
					statBrandnewSize.addValue(Integer.parseInt(tokens[1]));
				}
			} else if (type.equalsIgnoreCase("disappeared")) {
				List<String> lines = FileUtils.readLines(new File(out));

				for (int i = 1; i < lines.size(); i++) {
					String line = lines.get(i);
					String[] tokens = line.split(",");
					fDisappearDiameter.addValue(Integer.parseInt(tokens[2]));
					fDisappearSize.addValue(Integer.parseInt(tokens[1]));
					statDisappearDiameter.addValue(Integer.parseInt(tokens[2]));
					statDisappearSize.addValue(Integer.parseInt(tokens[1]));
				}
			} else if (type.equalsIgnoreCase("break")) {
				List<String> lines = FileUtils.readLines(new File(out));

				for (int i = 1; i < lines.size(); i++) {
					String line = lines.get(i);
					String[] tokens = line.split(",");
					fBreakDiameterBefore.addValue(Integer.parseInt(tokens[2]));
					fBreakSizeBefore.addValue(Integer.parseInt(tokens[1]));
					statBreakDiameterBefore.addValue(Integer.parseInt(tokens[2]));
					statBreakSizeBefore.addValue(Integer.parseInt(tokens[1]));

					fBreakDiameterAfter.addValue(Integer.parseInt(tokens[4]));
					fBreakSizeAfter.addValue(Integer.parseInt(tokens[3]));
					statBreakDiameterAfter.addValue(Integer.parseInt(tokens[4]));
					statBreakSizeAfter.addValue(Integer.parseInt(tokens[3]));
				}
			} else if (type.equalsIgnoreCase("same")) {
				List<String> lines = FileUtils.readLines(new File(out));

				for (int i = 1; i < lines.size(); i++) {
					String line = lines.get(i);
					String[] tokens = line.split(",");
					fSameDiameter.addValue(Integer.parseInt(tokens[2]));
					fSameSize.addValue(Integer.parseInt(tokens[1]));
					statSameDiameter.addValue(Integer.parseInt(tokens[2]));
					statSameSize.addValue(Integer.parseInt(tokens[1]));
				}
			}

		}
		System.out.print(proj + "," + statSameDiameter.getMean());
		System.out.print("," + statBrandnewDiameter.getMean());
		System.out.print("," + statDisappearDiameter.getMean());
		System.out.print("," + statAggregateDiameterBefore.getMean());
		System.out.print("," + statAggregateDiameterAfter.getMean());
		System.out.print("," + statBreakDiameterBefore.getMean());
		System.out.print("," + statBreakDiameterAfter.getMean());

		System.out.print("," + statSameSize.getMean());
		System.out.print("," + statBrandnewSize.getMean());
		System.out.print("," + statDisappearSize.getMean());
		System.out.print("," + statAggregateSizeBefore.getMean());
		System.out.print("," + statAggregateSizeAfter.getMean());
		System.out.print("," + statBreakSizeBefore.getMean());
		System.out.println("," + statBreakSizeAfter.getMean());
	}

	public void countSccAndNonScc(String proj) throws Exception {
		String dir = "/Users/qiongfeng/Documents/projects/apache-commit-space/" + proj + "/scc/";
		int countScc = 0;
		int countNonScc = 0;
		if (new File(dir).exists()) {

			for (File txtF : new File(dir).listFiles()) {
				boolean containsScc = false;
				if (txtF.isDirectory()) {
					String beforePath = txtF.getPath() + "/before.txt";
					String afterPath = txtF.getPath() + "/after.txt";
					// System.out.println(beforePath);
					// System.out.println(afterPath);

					List<String> linesBefore = FileUtils.readLines(new File(beforePath));
					List<String> linesAfter = FileUtils.readLines(new File(afterPath));

					for (String line : linesBefore) {
						String[] tokens = line.replace("[", "").replace("]", "").split(",");
						if (tokens.length > 1) {
							containsScc = true;
						}

					}
					for (String line : linesAfter) {
						String[] tokens = line.replace("[", "").replace("]", "").split(",");
						if (tokens.length > 1) {
							containsScc = true;
						}

					}

				}
				if (containsScc) {
					countScc++;
				} else {
					countNonScc++;
				}
			}

		}
		System.out.println(proj + "," + countScc + "," + countNonScc);
	}

	public void countEvolvedScc(String proj, String[] types) {
		String out = "/Users/qiongfeng/Documents/projects/apache-commit-space/" + proj + "/scc-change/";

		System.out.print(proj);

		for (String type : types) {
			String sumFile = out + type + "/";
			int count = 0;
			if (new File(sumFile).exists()) {

				for (File txtF : new File(sumFile).listFiles()) {
					if (txtF.getName().endsWith(".txt")) {
						count++;
					}
				}

			}
			System.out.print("," + count);
		}

		System.out.println();
	}

}
