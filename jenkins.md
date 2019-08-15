# Introduction to CI / CD

##  Traditional approach to integrate work between different teams 

![Traditional Integration](https://github.com/hub-kubernetes/jenkins-cicd/blob/master/model%20before.png)


##  What is CI and how is it different? 

> Continuous integration is the practice in which team members of different teams integrate their code as frequently as possible, making integration possible multiple times daily. 


![Traditional Integration](https://github.com/hub-kubernetes/jenkins-cicd/blob/master/ContinuousIntegration.png)


Lets see how multiple teams work in a CI environment - 

> Multiple teams will checkout code from base repository, perform manual coding and test. The integration cycle (jenkins) will be common for all teams.

![Traditional Integration](https://github.com/hub-kubernetes/jenkins-cicd/blob/master/MultipleTeamIntegration.png)

##  Important terms in Jenkins 

* Jobs / Projects

> Jobs are the most basic runnable task in Jenkins. The most simplest job can be running a single build. 

* Pipeline

> This creates an entire workflow. It can span multiple nodes 

* MultiConfiguration Project

> Project that requires multiple configuration like Ubuntu, Centos, Kali Linux, Windows, Android etc 

* Repository

> Location where code is kept that jenkins can pull the code from. Ex, github.com, bitbucket.com, etc

* Folder

> Conceptual group of multiple projects/Jobs 

* Builds

> Builds are created when a project is run. 

* Build Steps

> Build steps are individual tasks within the project. The tasks can be as below - 

Check Github Changes -> Pull Changes -> Build Code -> Automated Tests -> Check pass/fail -> Feedback. 

> Each step in the above phase is called as a build task .


* Artifacts

> Artifacts are immutable files created as a part of the entire build process. These can be the compiled code, jar, war, or container images of your code or even plain files like xml, yaml, json, js that are generated as a part of your build process. 

* Build Tools 

> Tools required to create a build. Ant, maven, gradle, shell scripts, powershell, python scripts are few examples

* Testing

> Testing verifies that the code is working as designed 























