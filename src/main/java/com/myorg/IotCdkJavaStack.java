// package com.myorg;

// import software.constructs.Construct;
// import software.amazon.awscdk.Stack;
// import software.amazon.awscdk.StackProps;
// // import software.amazon.awscdk.Duration;
// // import software.amazon.awscdk.services.sqs.Queue;

// public class IotCdkJavaStack extends Stack {
//     public IotCdkJavaStack(final Construct scope, final String id) {
//         this(scope, id, null);
//     }

//     public IotCdkJavaStack(final Construct scope, final String id, final StackProps props) {
//         super(scope, id, props);

//         // The code that defines your stack goes here

//         // example resource
//         // final Queue queue = Queue.Builder.create(this, "IotCdkJavaQueue")
//         //         .visibilityTimeout(Duration.seconds(300))
//         //         .build();
//     }
// }

package com.myorg;

import software.amazon.awscdk.*;
import software.constructs.Construct;
import software.amazon.awscdk.services.iot.*;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.iam.*;

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

        // 3. Permissions for IoT to invoke Lambda
        iotLambda.addPermission("AllowIoTInvoke", Permission.builder()
            .principal(new ServicePrincipal("iot.amazonaws.com"))
            .action("lambda:InvokeFunction")
            .build());

        // 4. IoT Topic Rule
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
    }
}

