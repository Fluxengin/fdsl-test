package jp.co.fluxengine.apptest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TestResult {

    @JsonProperty("ケース")
    public String testFileName;

    @JsonProperty("TestNo")
    public String testNo;

    @JsonProperty("結果")
    public boolean succeeded;

    public TestResult() {
    }

    public TestResult(String testFileName, String testNo, boolean succeeded) {
        this.testFileName = testFileName;
        this.testNo = testNo;
        this.succeeded = succeeded;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return succeeded == that.succeeded &&
                Objects.equals(testFileName, that.testFileName) &&
                Objects.equals(testNo, that.testNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testFileName, testNo, succeeded);
    }

    @Override
    public String toString() {
        return "TestResult [testFileName=" + testFileName + ", testNo=" + testNo + ", succeeded=" + succeeded + "]";
    }

}
