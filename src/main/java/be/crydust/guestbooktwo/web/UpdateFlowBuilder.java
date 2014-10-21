package be.crydust.guestbooktwo.web;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;

/**
 *
 * @author kristof
 */
public class UpdateFlowBuilder implements Serializable {

    private static final long serialVersionUID = 42L;

    @Produces
    @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {

        String flowId = "updateFlow";
        flowBuilder.id("", flowId);
        flowBuilder.viewNode(flowId, "jsf/" + flowId + "/step_1.xhtml").markAsStartNode();
        flowBuilder.viewNode("step_2", "jsf/" + flowId + "/step_2.xhtml");
        flowBuilder.viewNode("step_3", "jsf/" + flowId + "/step_3.xhtml");
        return flowBuilder.getFlow();
    }
}
