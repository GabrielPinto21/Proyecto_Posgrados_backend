Dockerizing Proyecto_Posgrados_backend

Local build & run

1) Build the Docker image (from repo root):

```bash
docker build -t posgrados-backend:latest .
```

2) Run the container (bind port 8080):

```bash
docker run --rm -p 8080:8080 \
  -e PORT=8080 \
  -e JAVA_OPTS="-Xmx512m" \
  posgrados-backend:latest
```

3) Alternatively use docker-compose:

```bash
docker-compose up --build
```

Notes for Railway

- You can deploy using Railway's Docker deployment by setting the project to use a Dockerfile. Railway will build the image using the provided `Dockerfile`.
- If you prefer to keep the current Railpack build, no change is required; the current commands build and run the jar directly.
- Railway start command (if using image that runs the jar) should be compatible with:

```
java $JAVA_OPTS -Dserver.port=$PORT -jar platform/application/target/*.jar
```

or, when deploying via Dockerfile, Railway will run the container's `ENTRYPOINT`.

Environment file (`.env.properties`)

- The application module uses `application.properties` which imports `optional:file:.env.properties`.
- The application module uses `application.properties` which imports `optional:file:.env.properties`.
- IMPORTANT: The Docker image does NOT include or bake `.env.properties`. You must provide the file
  at runtime (mount) or pass the required variables via environment variables. This prevents
  secrets from being embedded in the image.
- For local Docker runs you can either:
  - Keep a `.env.properties` file at the repo root and the provided `docker-compose.yml` will mount it into the container at `/app/.env.properties`.
  - Or pass the required environment variables via `docker run -e ...` (recommended for secrets).

Example using the mounted file (compose):

```bash
docker-compose up --build
```

Or with `docker run` and explicit env vars (avoids committing secrets):

```bash
docker build -t posgrados-backend:latest .
docker run --rm -p 8080:8080 \
  -e PORT=8080 \
  -e DB_URL=jdbc:mysql://host:3306/posgrados \
  -e DB_USER=... \
  -e DB_PASSWORD=... \
  -e JAVA_OPTS="-Xmx512m" \
  posgrados-backend:latest
```

Railway specific notes

- Do NOT bake secrets into the image. Preferably set secrets and DB variables in Railway's Project > Variables (Environment) panel.
- If you want Railway to build from this repo using the `Dockerfile`, set the service to use Docker deployment. Railway will run `docker build` and use the image's `ENTRYPOINT`.
- If you keep the Railpack Maven build (current setup), continue using the existing `buildCommand` and `startCommand` and set the environment variables in Railway.

Pushing to a registry (optional)

1) Tag and push to Docker Hub:

```bash
docker tag posgrados-backend:latest yourdockerhubuser/posgrados-backend:latest
docker push yourdockerhubuser/posgrados-backend:latest
```

2) Configure Railway to deploy from the container registry, or use a Git-based deployment that builds the Dockerfile.

Environment & DB

- This Dockerfile builds the full project and runs the `platform/application` jar. The app may require external services (DB, S3, etc.). When running locally you can set environment variables or add services in `docker-compose.yml` for postgres, redis, etc.

Troubleshooting

- If the build fails due to memory, increase Docker build memory or use a remote builder.
- If the resultant image is large, consider using a slimmer runtime base or optimizing Maven layers by copying only `pom.xml` first and running `mvn dependency:go-offline` (advanced optimization).
