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
