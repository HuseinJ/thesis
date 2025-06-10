Identify operations that don't naturally fit within a single Aggregate:

1. List operations that involve multiple aggregates
2. For each operation, determine if it's:
   - A Domain Service (business logic involving multiple aggregates)
   - An Application Service (orchestration/workflow)
   - Actually belonging inside an aggregate (refactor needed)
3. Define clear interfaces for domain services
4. Ensure domain services contain only domain logic, no infrastructure concerns

Question whether each service truly needs to exist or if the model needs restructuring.