# AWS Access Request – Phase 1 POC

## Objective
Phase-1 demonstrates a backend system that converts natural-language AWS access requests into structured, approval-ready requests using an LLM-driven follow-up loop.

## Scope (Phase-1 Only)
- Accept natural language access requests
- Use LLM to identify missing details
- Ask one follow-up question at a time
- Persist all requests and follow-ups
- No approvals
- No AWS execution

## Flow
1. User submits access request
2. Backend calls LLM
3. If information missing → follow-up question is generated
4. User answers follow-up
5. LLM re-evaluates until satisfied
6. Request is marked CREATED

## Key Design Decisions
- LLM is used only to structure intent
- No permission guessing
- No automatic AWS execution
- Deterministic and auditable flow

## APIs
### Create Request
POST /api/access-requests

### Submit Follow-up
POST /api/access-requests/{requestId}/followups/{followupId}

## Tech Stack
- Java 17
- Spring Boot
- Gradle (Groovy)
- H2 Database
- Mock LLM

## Run
./gradlew bootRun

### Phase 2 – Approval Workflow (In Progress)

Phase 2 introduces a persistent, multi-step approval workflow for AWS access requests.

Once an access request has collected all mandatory information (for example, after follow-up questions are answered), the system transitions the request into an approval phase instead of immediately resolving it.

---

### What Phase 2 Implements

#### 1. Approval Domain Model
A new `Approval` entity represents approval decisions.

Each access request can have multiple approvals, such as:
- MANAGER
- DEVOPS

Each approval stores:
- Approver role (MANAGER / DEVOPS)
- Status (PENDING, APPROVED, REJECTED)
- Linked access request ID

---

#### 2. Persistent Approval Storage
- New `approvals` table managed via Spring Data JPA
- `ApprovalRepository` added to query approvals by request ID
- Approvals are stored independently of access requests

---

#### 3. Approval Service Layer
A dedicated `ApprovalService` is responsible for:
- Creating initial approval records for a request
- Handling manager approval actions
- Handling DevOps approval actions

Approval logic is decoupled from request creation and LLM processing.

---

#### 4. Approval REST APIs
New REST endpoints are introduced to support approvals:
- Manager approval endpoint
- DevOps approval endpoint

These endpoints update approval status in the database and are designed to be extended in later phases.

---

#### 5. Access Request Status Transitions
The access request lifecycle now supports approval-related states such as:
- PENDING_MANAGER_APPROVAL
- PENDING_DEVOPS_APPROVAL (planned)
- APPROVED (planned)

This enables a controlled and auditable approval flow.

---

### What Is Deferred to Phase 3

The following items are intentionally not completed in Phase 2:
- Automatic creation of approvals after follow-up resolution
- Enforcement of approval ordering (manager before DevOps)
- IAM policy generation after final approval
- Automatic AWS CLI or IAM execution

Phase 2 focuses only on introducing approval workflow primitives.

---

### Design Rationale

Phase 2 establishes a stable approval foundation without tightly coupling it to IAM execution logic. This allows Phase 3 to focus purely on policy generation and execution.

