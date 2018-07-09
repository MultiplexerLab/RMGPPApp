package ipa.rmgppapp.model;

public class ProcessItem {

    private String processName;
    private String machineType;
    private String hourlyTarget;
    private String assignedWorkerId;

    public ProcessItem(String processName, String machineType, String hourlyTarget, String assignedWorkerId) {
        this.processName = processName;
        this.machineType = machineType;
        this.hourlyTarget = hourlyTarget;
        this.assignedWorkerId = assignedWorkerId;
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

    public String getHourlyTarget() {
        return hourlyTarget;
    }

    public void setHourlyTarget(String hourlyTarget) {
        this.hourlyTarget = hourlyTarget;
    }

    public String getAssignedWorkerId() {
        return assignedWorkerId;
    }

    public void setAssignedWorkerId(String assignedWorkerId) {
        this.assignedWorkerId = assignedWorkerId;
    }
}
