package com.myorg;

import software.amazon.awscdk.*;
import software.constructs.Construct;
import software.amazon.awscdk.services.iot.*;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Permission;
import software.amazon.awscdk.services.iam.ServicePrincipal;

import java.util.List;

public class IotCdkJavaStack extends Stack {
    public IotCdkJavaStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public IotCdkJavaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // 1. Create IoT Thing
        CfnThing iotThing = CfnThing.Builder.create(this, "MyIoTThing")
            .thingName("my-cdk-iot-thing")
            .build();

        // 2. Lambda Function
        Function iotLambda = Function.Builder.create(this, "IoTMessageHandler")
            .runtime(Runtime.NODEJS_18_X)
            .handler("index.handler")
            .code(Code.fromInline("exports.handler = async (event) => { console.log(event); return {}; };"))
            .build();

        // 3. IoT Topic Rule (must be before permissions if you want to use topicRule.getAttrArn)
        CfnTopicRule topicRule = CfnTopicRule.Builder.create(this, "IoTTopicRule")
            .topicRulePayload(CfnTopicRule.TopicRulePayloadProperty.builder()
                .sql("SELECT * FROM 'telemetry/data'")
                .ruleDisabled(false)
                .actions(List.of(
                    CfnTopicRule.ActionProperty.builder()
                        .lambda(CfnTopicRule.LambdaActionProperty.builder()
                            .functionArn(iotLambda.getFunctionArn())
                            .build())
                        .build()))
                .build())
            .build();

        // 4. Permission for IoT to invoke Lambda
        iotLambda.addPermission("AllowIoTInvoke", Permission.builder()
            .action("lambda:InvokeFunction")
            .principal(new ServicePrincipal("iot.amazonaws.com"))
            .sourceArn(topicRule.getAttrArn())
            .build());
    }
}
