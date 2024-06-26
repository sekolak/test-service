@Library('cloudservices-lib') _

import io.cloudservices.Service

pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-west-2'
    }

    stages {
        stage('Deploy Service') {
            steps {
                script {
                    def serviceArgs = [
                        environment: 'prod',
                        name: 'my-service',
                        resourcesTruePrefix: 'tf-',
                        cluster: 'my-cluster',
                        cleanName: 'my-service',
                        regions: ['us-west-2'],
                        template: '''{
                            "containerDefinitions": [
                                {
                                    "name": "_TASK_",
                                    "image": "_REGISTRY_/_REPOSITORY_:_TAG_",
                                    "cpu": 256,
                                    "memory": 512,
                                    "portMappings": _PORT_MAPPINGS_,
                                    "environment": _ENVIRONMENT_,
                                    "secrets": _SECRETS_,
                                    "mountPoints": _MOUNT_POINTS_,
                                    "logConfiguration": {
                                        "logDriver": "awslogs",
                                        "options": {
                                            "awslogs-group": "/ecs/_TASK_",
                                            "awslogs-region": "us-west-2",
                                            "awslogs-stream-prefix": "ecs"
                                        }
                                    }
                                }
                            ]
                        }''',
                        templateParameter: [],
                        portMappings: [[containerPort: 80, hostPort: 0]],
                        volumeMappings: [],
                        taskAttributes: [:],
                        min: 1,
                        max: 5,
                        schedulingStrategy: 'REPLICA',
                        networkMode: 'awsvpc',
                        deployMin: 100,
                        deployMax: 200,
                        ecrPolicyTemplate: '',
                        ecrLifecyclePolicyTemplate: '',
                        healthGracePeriod: 60,
                        volumesTemplate: '',
                        sumoUrl: '',
                        taskSecrets: [:],
                        telemetryEnabled: true,
                        telemetryCustomMetricsEnabled: true,
                        snykPath: ''
                    ]

                    def service = new Service(serviceArgs)

                }
            }
        }
    }
}
