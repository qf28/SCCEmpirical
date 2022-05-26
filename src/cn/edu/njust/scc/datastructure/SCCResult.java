package cn.edu.njust.scc.datastructure;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SCCResult {

	public List<Set<String>> before;
	public List<Set<String>> after;
	public String commitId;
	public LocalDate date;

	Result res;

	enum Result {
		REMAINSAME, GROW, DECREASE, DISAPPEAR, UNDECIDED

	}

	public SCCResult(String id) {
		commitId = id;
		before = new ArrayList<>();
		after = new ArrayList<>();

	}

	public void isSame(Set<String> p1, Set<String> p2) {
		before.add(p1);
		after.add(p2);
		res = Result.REMAINSAME;
	}

	public void addGrowingBefore(Set<String> p1) {
		before.add(p1);
	}

	public void isGrowing(Set<String> p2) {
		after.add(p2);
		res = Result.GROW;
	}

	public void addDecreasingAfter(Set<String> p2) {
		after.add(p2);
	}

	public void isDecreasing(Set<String> p1) {
		before.add(p1);
		res = Result.DECREASE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (res == Result.GROW) {
			result = prime * result + ((after == null) ? 0 : after.hashCode());
			result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
			// System.out.println(result);
		} else if (res == Result.DECREASE) {
			result = prime * result + ((before == null) ? 0 : before.hashCode());
			result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
		}
		return result;
	}

	@Override
	public String toString() {
		return "SCCResult [commitId=" + commitId + ", res=" + res + ", before=" + before + ", after=" + after + "]";
	}

}
