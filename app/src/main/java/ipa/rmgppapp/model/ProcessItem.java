package ipa.rmgppapp.model;

public class ProcessItem {

    private int id;
    private String processName;
    private String machineType;
    private Double hourlyTarget;
    private String assignedWorkerId;
    private String assignedWorkerName;

    /*public ProcessItem(int id, String processName, String machineType, Double hourlyTarget, String assignedWorkerId) {
        this.id = id;
        this.processName = processName;
        this.machineType = machineType;
        this.hourlyTarget = hourlyTarget;
        this.assignedWorkerId = assignedWorkerId;
    }*/

    public ProcessItem(int id, String processName, String machineType, Double hourlyTarget, String assignedWorkerId, String assignedWorkerName) {
        this.id = id;
        this.processName = processName;
        this.machineType = machineType;
        this.hourlyTarget = hourlyTarget;
        this.assignedWorkerId = assignedWorkerId;
        this.assignedWorkerName = assignedWorkerName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getProcessName() {
        return processName;
    }
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public Double getHourlyTarget() {
        return Double.valueOf(Math.round(hourlyTarget));
    }

    public void setHourlyTarget(Double hourlyTarget) {
        this.hourlyTarget = hourlyTarget;
    }

    public String getAssignedWorkerId() {
        return assignedWorkerId;
    }

    public void setAssignedWorkerId(String assignedWorkerId) {
        this.assignedWorkerId = assignedWorkerId;
    }

    public String getAssignedWorkerName() {
        return assignedWorkerName;
    }

    public void setAssignedWorkerName(String assignedWorkerName) {
        this.assignedWorkerName = assignedWorkerName;
    }
}
