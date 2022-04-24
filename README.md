# forgetful-map app
- author: Joe Chaplin
- languages/technologies: Java17/Maven
- created: 24th Apr 2022

---

## Installation

### Packaging and running as a JAR file
From text-processor app's top level directory in a command line environment:
1. you must have maven and the Java Runtime Environment installed - check that you are at least version 8 with:
`java -version` from command line. Check maven is installed with `maven -version` from command line.
2. `mvn package` to create the jar
3. `java -jar target/forgetful-map-1.0-SNAPSHOT.jar` to run.


## Design choices
- The entire concept of a forgetting-map sounds like a CDN (Content Delivery Network); and given more time would make a good model for this solution.
- No mention of the type of data to be associated - for simplicity we have assumed string data input via command line - but we have left ForgetfulMap itself as an interface capable of being implemented with other data types later on if need be
- For simplicity we have left this app as a command line application
- There is an implicit assumption that there is 1 key to 1 content but the hypothetical user may be unfamiliar with software/collection/programming conventions and expect the option to use multiple keys to single or multiple contents (for instance if by 'key' they mean 'news topic')
- We have added complexity due to the 'tiebreaker' solving requirement.