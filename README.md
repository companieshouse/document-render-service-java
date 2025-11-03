# document-render-service-java
> A backend processing service for rendering templates into documents

### Service Overview
- Provides two endpoints for rendering (and/or storing) documents.
- Uses pre-defined templates from the `document-render-assets-registry` service.
- Renders HTML -> PDF (using CH GitHub repository) `document-render-from-html5`.
- (Optionally) stores documents in AWS S3 via the `document-storage-service`.

### Incoming Requests
- `POST` request to endpoint: `/document-render/?is_public=true`
    - Authorization: `Basic base64encode(username:password)`
    - Headers:
        - `templateName`: `dissolution`
        - `assetId`: `fonts`
        - `Accept`: `TransportNewLight-Regular.ttf`
        - `Content-Type`: ''
        - `Location`: ''
    - Body:
      ```json
      ```
- `POST` request to endpoint: `/document-render/store?is_public=false`
    - Authorization: `Basic base64encode(username:password)`
    - Headers:
        - `templateName`: `dissolution`
        - `assetId`: `fonts`
        - `Accept`: `TransportNewLight-Regular.ttf`
        - `Content-Type`: ''
        - `Location`: ''
    - Body:
      ```json
      ```

## Sonar Analysis
The code is regularly run through the quality gate, which can be found here: [Sonar Analysis](https://code-analysis.platform.aws.chdev.org/dashboard?id=uk.gov.companieshouse%3Adocument-render-service-java)

## Terraform ECS

### What does this code do?
The code present in this repository is used to define and deploy a dockerised container in AWS ECS.
This is done by calling a [module](https://github.com/companieshouse/terraform-modules/tree/main/aws/ecs) from terraform-modules. Application specific attributes are injected and the service is then deployed using Terraform via the CICD platform 'Concourse'.


Application specific attributes | Value                                                                                                                                                                                                                                                                        | Description
:---------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------
**ECS Cluster**        | follow                                                                                                                                                                                                                                                                       | ECS cluster (stack) the service belongs to
**Load balancer**      | N/A <br> consumer                                                                                                                                                                                                                                                            | The load balancer that sits in front of the service
**Concourse pipeline**     | [Pipeline link](https://ci-platform.companieshouse.gov.uk/teams/team-development/pipelines/document-render-service-java) <br> [Pipeline code](https://github.com/companieshouse/ci-pipelines/blob/master/pipelines/ssplatform/team-development/document-render-service-java) | Concourse pipeline link in shared services


### Contributing
- Please refer to the [ECS Development and Infrastructure Documentation](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/4390649858/Copy+of+ECS+Development+and+Infrastructure+Documentation+Updated) for detailed information on the infrastructure being deployed.

### Testing
- Ensure the terraform runner local plan executes without issues. For information on terraform runners please see the [Terraform Runner Quickstart guide](https://companieshouse.atlassian.net/wiki/spaces/DEVOPS/pages/1694236886/Terraform+Runner+Quickstart).
- If you encounter any issues or have questions, reach out to the team on the **#platform** slack channel.

### Vault Configuration Updates
- Any secrets required for this service will be stored in Vault. For any updates to the Vault configuration, please consult with the **#platform** team and submit a workflow request.

### Useful Links
- [ECS service config dev repository](https://github.com/companieshouse/ecs-service-configs-dev)
- [ECS service config production repository](https://github.com/companieshouse/ecs-service-configs-production)