package org.fasterjson.json.gurobi_demo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;


public class ParameterStoreRetriever {
    private AWSSimpleSystemsManagement ssm;


    public ParameterStoreRetriever() {
        this(AWSSimpleSystemsManagementClientBuilder.defaultClient());
    }

    public ParameterStoreRetriever(AWSSimpleSystemsManagement ssm) {
        this.ssm = ssm;
    }

    public String getParameter(String parameterName) throws NoSuchFieldException {
        GetParameterRequest request = new GetParameterRequest();
        request.setName(parameterName);
        request.setWithDecryption(true);
        try {
            Parameter parameter = ssm.getParameter(request).getParameter();
            return parameter.getValue();
        } catch (AmazonServiceException e) {
            if (e.getErrorCode() != null && e.getErrorCode().equals("UnrecognizedClientException")) {
                throw new NoSuchFieldException(parameterName + " could not be accessed due to invalid credentials.");
            } else {
                throw new NoSuchFieldException(parameterName + " does not exist in parameter store.");
            }
        }
    }
}