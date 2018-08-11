package ipa.rmgppapp.model;

public class LineEntry {

    private String hour;
    private String lineInput;
    private String lineOutput;
    private String problemType;
    private String status;

    public LineEntry(String hour, String lineInput, String lineOutput, String problemType, String status) {
        this.hour = hour;
        this.lineInput = lineInput;
        this.lineOutput = lineOutput;
        this.problemType = problemType;
        this.status = status;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getLineInput() {
        return lineInput;
    }

    public void setLineInput(String lineInput) {
        this.lineInput = lineInput;
    }

    public String getLineOutput() {
        return lineOutput;
    }

    public void setLineOutput(String lineOutput) {
        this.lineOutput = lineOutput;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
