@ECHO OFF
mvn sonar:sonar -Dsonar.projectKey=zeeslag -Dsonar.host.url=http://localhost:9000 -Dsonar.login=0cd3467703138465dd3db79d3e0a64ab49e2b14c

  PAUSE