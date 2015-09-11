UJI Final Year Project
======================

This repository contains the code developed for my bachelor thesis.
The complete thesis (Spanish language) can be found <a href="http://andreacalia.github.io/static/document/bachelor-thesis.pdf" alt="Bachelor thesis">HERE</a>. Also, the video demonstrating the features of the project can be found on <a href="http://youtu.be/HdGypS9rvb8" alt="Project's video">Youtube</a>.

How to build the project
======================
To build the project you need to use Maven. The following commands will generate the WAR packages that you can deploy on, for example, Apache Tomcat.

```
cd path/to/git
cd commons/
mvn clean compile install
cd ../BicicasWebService/
mvn clean compile package
cd ../RenfeWebService/
mvn clean compile package
cd ../TripPlanner/
mvn clean compile package
```


License
===================
<a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/">Creative Commons Attribution-ShareAlike 4.0 International License</a>.
