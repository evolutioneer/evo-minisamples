package view;

import oracle.adf.controller.TaskFlowId;

public class IndexDynamicRegionBean {
    private String taskFlowId = "/deptFragTaskFlow.xml#deptFragTaskFlow";

    public IndexDynamicRegionBean() {
    }

    public TaskFlowId getDynamicTaskFlowId() {
        return TaskFlowId.parse(taskFlowId);
    }

    public String empFragTaskFlow() {
        taskFlowId = "/empFragTaskFlow.xml#empFragTaskFlow";
        return null;
    }

    public String deptFragTaskFlow() {
        taskFlowId = "/deptFragTaskFlow.xml#deptFragTaskFlow";
        return null;
    }
}
