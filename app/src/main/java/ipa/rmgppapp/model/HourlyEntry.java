package ipa.rmgppapp.model;

public class HourlyEntry {
    private String timeStamp;
    private String workerId;
    private String workerName;
    private String processName;
    private String problemType;
    private String problem;
    /*private String cuttingSlStart;
    private String cuttingSlEnd;*/
    private int quantity;
    private String entryTime;

    public HourlyEntry(String timeStamp, String workerId, String workerName, String processName, int quantity, String entryTime, String problemType, String problem) {
        this.timeStamp = timeStamp;
        this.workerId = workerId;
        this.workerName = workerName;
        this.processName = processName;
        this.quantity = quantity;
        this.entryTime = entryTime;
        this.problemType = problemType;
        this.problem = problem;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
