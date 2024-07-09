package nicerloop.asciidoctorj.pptx;

import org.approvaltests.Approvals;
import org.approvaltests.namer.ApprovalNamer;
import org.approvaltests.namer.GetApprovalName;
import org.approvaltests.namer.GetSourceFilePath;
import org.approvaltests.namer.NamerWrapper;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class CucumberApprovalNamer implements GetApprovalName, GetSourceFilePath {

    @Before
    public void setupApprovalsNamer(Scenario scenario) {
        sourceFilePath = scenario.getUri().getPath();
        approvalName = "." + scenario.getName();
        if (approvalName == null || approvalName.isEmpty()) {
            approvalName = scenario.getLine().toString();
        }
        approvalName = sourceFilePath.substring(sourceFilePath.lastIndexOf('/') + 1) + approvalName;
        sourceFilePath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf('/'));
        Approvals.namerCreater = () -> new NamerWrapper(this, this) {
            @Override
            public ApprovalNamer addAdditionalInformation(String info) {
                return this;
            }
        };
    }

    private String sourceFilePath = null;
    private String approvalName = null;

    @Override
    public String getSourceFilePath() {
        return sourceFilePath;
    }

    @Override
    public String getApprovalName() {
        return approvalName;
    }

}