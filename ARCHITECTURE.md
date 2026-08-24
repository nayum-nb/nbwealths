# Architecture — nbwealths

Version: 0.1
Branch: scaffold/maven-java17-keycloak-kafka

Overview

nbwealths is a small microservice monorepo intended as a learning / interview scaffold. It consists of three Spring Boot services, an Angular frontend placeholder, and local development infra (Keycloak, Kafka/Zookeeper, Postgres). The system uses event-driven communication (Kafka) between services and Keycloak for authentication/authorization.

Goals
- Provide a runnable local development environment (docker-compose) with auth, message bus, and databases.
- Demonstrate service separation (db-per-service), eventing, and a path to production via Terraform and CI/CD.

Core components

- account-service
  - Responsibilities: account lifecycle, balances, transaction API. Produces transaction events to Kafka.
  - Persistence: Postgres database (postgres-account) + Flyway migrations.
  - APIs: REST (examples: POST /api/v1/accounts, GET /api/v1/accounts/{id}).

- portfolio-service
  - Responsibilities: portfolios and positions; consumes transaction events from Kafka and updates holdings.
  - Persistence: Postgres database (postgres-portfolio) + Flyway migrations.
  - APIs: REST (example: GET /api/v1/portfolios).

- admin-service
  - Responsibilities: administrative endpoints, roles management, audit logs.
  - Persistence: Postgres database (postgres-admin) + Flyway migrations.
  - APIs: REST (example: GET /api/v1/admin/ping).

Infrastructure (local)

- Keycloak (realm import) for OIDC / JWT issuance. Realm file in infra/keycloak/realm.json.
- Kafka + Zookeeper (Confluent images) for pub/sub messaging.
- PostgreSQL ×3 (one per service) for data isolation.
- docker-compose (infra/docker-compose.yml) for local orchestration.

Security model

- Keycloak provides authentication and issues JWTs to the frontend or machine clients.
- Service tokens: backend services are confidential clients; frontend is a public client.
- Services validate JWTs and enforce role-based access (ROLE_USER, ROLE_ADMIN).
- Secrets are not committed to the repo; .env.example documents local variables.

Dataflow

1. User (Angular) authenticates via Keycloak and obtains an access token.
2. User calls account-service to create an account or post a transaction.
3. account-service persists the write to its Postgres DB and publishes a "transaction" event to Kafka.
4. portfolio-service consumes the transaction event and adjusts portfolio positions accordingly.
5. admin-service records audit events and provides management APIs.

Design decisions

- db-per-service: each service has its own Postgres instance to reduce coupling and allow independent evolution.
- Event-driven integration: using Kafka for eventual consistency between account and portfolio boundaries.
- Flyway migrations in each service ensure schema is versioned and applied automatically.
- Docker-compose local stack mirrors the main components of a cloud deployment (but simplified).

Phases & Roadmap

Phase 0 — Scaffold (done)
- Deliverables: repository scaffold with services skeleton, docker-compose, Keycloak realm, Dockerfiles, Flyway migrations, CI placeholder.
- Acceptance: branch scaffold/maven-java17-keycloak-kafka with baseline files; services compile locally.

Phase 1 — Core local dev (MVP)
- Tasks:
  - Ensure Flyway migrations run on startup.
  - Finalize REST endpoints for accounts (create/get) and simple portfolio ping endpoints.
  - Add OAuth2 resource server config for services (validate JWTs).
  - Start full stack via docker-compose and verify health endpoints.
- Acceptance:
  - Keycloak reachable, services respond at 8081/8082/8083, create account persists to postgres-account.
- Est. effort: 1–2 days.

Phase 2 — Events & integration
- Tasks:
  - Implement Kafka producers in account-service for transactions.
  - Implement Kafka consumers in portfolio-service with idempotency safeguards.
  - Add integration tests using Testcontainers (Postgres + Kafka).
- Acceptance:
  - Posting a transaction results in portfolio position update; integration tests in CI pass.
- Est. effort: 2–4 days.

Phase 3 — Frontend & OIDC
- Tasks:
  - Scaffold Angular app and integrate Keycloak OIDC.
  - Implement pages to list/create accounts and show portfolios.
  - Protect routes and apply role checks.
- Acceptance: Users can log in, create accounts, and view portfolios.
- Est. effort: 2–3 days.

Phase 4 — CI, tests, and container images
- Tasks:
  - Expand GitHub Actions to run unit and integration tests, build images, and optionally push to GHCR.
  - Add branch protection that requires CI.
- Acceptance: PRs trigger CI; images can be produced for CD.
- Est. effort: 2–4 days.

Phase 5 — Terraform & cloud deployment
- Tasks:
  - Write Terraform modules to provision managed DBs, messaging (or use Confluent Cloud), Keycloak (or managed OIDC), and compute resources.
  - Add secrets management and monitoring.
- Acceptance: Services deploy to a staging environment and pass smoke tests.
- Est. effort: 1–2 weeks.

Phase 6 — Observability & hardening
- Tasks:
  - Add metrics (Micrometer), tracing (OpenTelemetry), logging, dashboards and alerts.
  - Harden security (TLS, secrets rotation, rate limiting).
- Acceptance: Monitoring dashboards and runbooks exist; security review passed.
- Est. effort: 1–2 weeks.

Operational runbook (local)

1. Clone and check out scaffold branch:
   - git clone https://github.com/nayum-nb/nbwealths.git
   - cd nbwealths
   - git fetch origin
   - git checkout scaffold/maven-java17-keycloak-kafka

2. Prepare environment:
   - cp .env.example .env
   - Edit .env if you change ports or credentials.

3. Start infra:
   - cd infra
   - docker compose up --build

4. Start services (either via docker compose or locally):
   - mvn -f services/account-service spring-boot:run
   - mvn -f services/portfolio-service spring-boot:run
   - mvn -f services/admin-service spring-boot:run

5. Smoke tests:
   - Keycloak: http://localhost:8080
   - Account service: GET http://localhost:8081/actuator/health
   - Portfolio ping: GET http://localhost:8082/api/v1/portfolios
   - Create account: POST http://localhost:8081/api/v1/accounts with JSON {"owner":"alice","type":"checking","currency":"USD","balance":100}

Risks & mitigations

- Eventual consistency: implement reconciliation jobs and surface pending state in UI.
- Duplicate event processing: include event id and idempotency keys, or use transactional outbox when moving to production.
- Secrets & credentials: use Secret Manager (cloud) or Vault in production; never store secrets in repo.

Next actions
- Commit this document to the scaffold branch as ARCHITECTURE.md, create issues for Phase 1 tasks, or start implementing Phase 1 items.
