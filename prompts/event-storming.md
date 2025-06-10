Based on the ubiquitous language we've established, let's conduct an Event Storming session:

1. Identify all Domain Events (things that happen) in chronological order
2. For each event, identify:
   - The Command that triggers it
   - The Actor/Role who initiates the command
   - Any Policies/Rules that apply
   - The Aggregate that handles it
3. Look for temporal boundaries and parallel processes
4. Create a visual flow showing the event stream

Format as:
Actor -> Command -> Aggregate -> Event(s) -> Policy/Reaction -> Next Command

Highlight any areas where the flow seems unclear or where multiple interpretations exist.