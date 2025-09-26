# thesis

AI-Assisted Domain Modeling: Enhanced Bounded Context Extraction with LLMs

## Abstract

This thesis explores the application of Large Language Models (LLMs) to enhance
domain modeling in software architecture, specifically focusing on the extraction of
bounded contexts as defined by Domain-Driven Design (DDD). Traditional domain
modeling is a time-consuming and expertise-driven process, often leading to suboptimal
architectures under tight deadlines. This research investigates how AI can assist in
decomposing complex monolithic systems into modular, maintainable components.
A case study was conducted with FTAPI Software GmbH, a company facing the chal-
lenge of modernizing its legacy monolithic architecture. A five-phase, prompt-driven
workflow was developed to guide LLMs through a structured analysis, including ubiq-
uitous language extraction, event storming, bounded context identification, aggregate
design, and technical architecture mapping. This methodology was applied to two
distinct domains: SecuRooms, a well-defined and previously modularized system
serving as a benchmark, and SecuMails, a complex monolithic system targeted for
modernization.
The results show that LLMs can effectively generate viable bounded contexts and
domain models that closely align with those created by experienced human architects,
especially for domains with clear requirements like SecuRooms. For the more entangled
SecuMails monolith, the LLM proposals provided a valuable starting point but struggled
to capture implicit business rules and historical technical debt. Expert interviews
confirmed the value of the LLM as an "architectural sparring partner" that accelerates
initial design, enforces systematic analysis, and offers unbiased perspectives.
The thesis concludes that a semi-automated approach, combining the analytical speed
of LLMs with the contextual judgment of human experts, offers a highly effective
strategy for software architecture design. This collaborative model enables a more
thorough exploration of architectural candidates, ultimately leading to more robust
and maintainable systems.

Author: Husein Jusic

Supervisor: Tobias Eisenreich

## Disclaimer

The software requirements, domain models, and related artifacts included in this project were generated using an AI-assisted workflow and edited manually for research purposes. They are based on publicly available information and may not accurately reflect the real-world systems they represent.  

These materials are intended solely for scientific research and demonstration in the context of this thesis. They **should not be used as authoritative or production-ready requirements**, and the authors make no guarantees regarding their correctness, completeness, or applicability.

## License
This project is licensed under the [CC BY-NC 4.0 License](https://creativecommons.org/licenses/by-nc/4.0/).
