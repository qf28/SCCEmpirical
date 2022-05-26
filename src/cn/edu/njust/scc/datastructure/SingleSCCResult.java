package cn.edu.njust.scc.datastructure;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class SingleSCCResult {

	public Set<String> before;
	public Set<String> after;
	public String commitId;
	public LocalDate date;

	Result res;

	enum Result {
		REMAINSAME, GROW, DECREASE, DISAPPEAR, BRANDNEW

	}

	public SingleSCCResult(String id) {
		commitId = id;
		before = new HashSet<>();
		after = new HashSet<>();

	}

	public void isSame(Set<String> p1, Set<String> p2) {
		before = new HashSet<>(p1);
		after = new HashSet<>(p2);
		res = Result.REMAINSAME;
	}

	public void isGrowing(Set<String> p1, Set<String> p2) {
		before = new HashSet<>(p1);
		after = new HashSet<>(p2);
		res = Result.GROW;
	}

	public void isDecreasing(Set<String> p1, Set<String> p2) {
		before = new HashSet<>(p1);
		after = new HashSet<>(p2);
		res = Result.DECREASE;
	}

	public void isBrandNew(Set<String> p2) {
		before = new HashSet<>();
		after = new HashSet<>(p2);
		res = Result.BRANDNEW;
	}

	public void isDisappearing(Set<String> p1) {
		before = new HashSet<>(p1);
		after = new HashSet<>();
		res = Result.DISAPPEAR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (res == Result.GROW) {
			result = prime * result + ((after == null) ? 0 : after.hashCode());
			result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
			result = prime * result + ((res == null) ? 0 : res.hashCode());
			// System.out.println(result);
		} else if (res == Result.DECREASE) {
			result = prime * result + ((before == null) ? 0 : before.hashCode());
			result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
			result = prime * result + ((res == null) ? 0 : res.hashCode());
		}
		return result;
	}

	@Override
	public String toString() {
		return "SCCResult [commitId=" + commitId + ", res=" + res + ", before=" + before + ", after=" + after + "]";
	}

}
