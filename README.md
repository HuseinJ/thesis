# thesis

## PlantUML Server Setup

This setup uses Docker Compose to run a PlantUML server based on the `plantuml/plantuml-server:tomcat` image.

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Starting the PlantUML Server

To start the PlantUML server container, run the following command from the root of the project:

```bash
docker compose -f docker/docker-compose-plantuml.yml up -d