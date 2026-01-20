package com.example.awsaccess.service;

import com.example.awsaccess.model.AccessRequest;
import org.springframework.stereotype.Service;

@Service
public class PolicyGenerationService {

    public String generateIamPolicy(AccessRequest request) {

        return """
        {
          "Version": "2012-10-17",
          "Statement": [
            {
              "Effect": "Allow",
              "Action": %s,
              "Resource": %s
            }
          ]
        }
        """.formatted(
                request.getServices(),
                request.getResourceArns()
        );
    }

    public String generateAwsCliCommand(Long requestId) {
        return "aws iam create-policy --policy-name TempAccess_" + requestId +
                " --policy-document file://policy.json";
    }
}
