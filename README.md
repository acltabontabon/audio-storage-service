<!-- TOC -->

# Audio Storage Service

## Development

### Prerequisites
- Java 17
- Running Docker

### Running the Application

1. Open a terminal and navigate to the project root directory.
2. Run the following command to start the application:
   ```bash
   ./gradlew clean bootRun
   ```
   >**NOTE**: Running the command above will automatically start a MySQL server from 
   > `src/main/docker/compose.yml` before the application starts and stop it when the application 
   > shuts down.

---

## Use Case
![Alt text for image](./diagrams/d_use-case.png)

## Data Model
![Alt text for image](./diagrams/d_data-Model.png)

## Deployment Landscape
![Alt text for image](./diagrams/d_deployment-landscape.png)