package jp.co.fluxengine.apptest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestResult {

	@JsonProperty("ケース")
	public String testFileName;
	
	@JsonProperty("TestNo")
	public String testNo;
	
	@JsonProperty("結果")
	public boolean succeeded;

	public String getTestFileName() {
		return testFileName;
	}

	public String getTestNo() {
		return testNo;
	}

	public boolean isSucceeded() {
		return succeeded;
	}

	@Override
	public String toString() {
		return "TestResult [testFileName=" + testFileName + ", testNo=" + testNo + ", succeeded=" + succeeded + "]";
	}
	
}
