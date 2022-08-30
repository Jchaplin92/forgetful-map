# forgetful-map app
- author: Joe Chaplin
- languages/technologies: Java17/Maven
- created: version 1 on 24th Apr 2022, version 2 on 27th August 2022

---

## Installation

### Packaging and running as a JAR file
From text-processor app's top level directory in a command line environment:
1. you must have maven and the Java Runtime Environment installed - check that you are at least version 8 with:
`java -version` from command line. Check maven is installed with `maven -version` from command line.
2. `mvn package` to create the jar
3. `java -jar target/forgetful-map-2.0-SNAPSHOT.jar` to run.


## Design choices
- I opted for a Spring application for easier HTTP input (localhost:8080 calls) and automatic thread safety
- Desired separate data structure ordering keys by lookup count (thus avoiding dual sources of truth) but as lookup count is constantly mutating it was simpler albeit less efficient to simply stream the minimum element
  - With longer time would have implemented a min heap - elements on mutation would compare to parents else children (the transitive property of comparison would make this sufficient). However the amount of complexity this would introduce was undesirable as none of the Java standard library's collections are a suitable substitute.
  - Created a TreeSet with comparator drawing on the hashMap delegate's lookup counts. Consequently this design is quite efficient but had problems adding elements probably relating to the comparator implementation
  - Considered PriorityQueue but ordering takes place on adding items only and heapify as a private method cannot be done on poll. This is a problem as the lookup count changes continuously. 
