# Introduction to CI / CD

##  Traditional approach to integrate work between different teams 

![Traditional Integration](https://github.com/hub-kubernetes/jenkins-cicd/blob/master/model%20before.png)

***

##  What is CI and how is it different? 

> Continuous integration is the practice in which team members of different teams integrate their code as frequently as possible, making integration possible multiple times daily. 


![Traditional Integration](https://github.com/hub-kubernetes/jenkins-cicd/blob/master/ContinuousIntegration.png)


Lets see how multiple teams work in a CI environment - 

> Multiple teams will checkout code from base repository, perform manual coding and test. The integration cycle (jenkins) will be common for all teams.

![Traditional Integration](https://github.com/hub-kubernetes/jenkins-cicd/blob/master/MultipleTeamIntegration.png)


***

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


***

# Jenkins Installation with Master & Slave configuration

### Pre-requisite

Below are the VM requirements for installing Jenkins master and slave 

* master VM - 2cpu x 2GB x 30 GB storage. This will be the master. Ubuntu - 18.04LTS

* slave VM - 1 cpu x 1GB x 30 GB storage. This will be the slave. Ubuntu - 18.04LTS


***

### Install & Configure Jenkins Master

Lets start installing jenking - 

> We will first install the master. The below steps are to be performed only on the master node. 

* Install OS related common packages as prerequisite 

` apt-get update` 

` sudo apt install software-properties-common apt-transport-https -y`

` sudo add-apt-repository ppa:openjdk-r/ppa -y`


* Install Java 8 

` sudo apt install openjdk-8-jdk -y`

` java -version`


* Add the Jenkins stable repository key to apt-key for md5 verification

` wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -`

* Add the debian repository to sources

` echo 'deb https://pkg.jenkins.io/debian-stable binary/' | tee -a /etc/apt/sources.list`

* Install Jenkins 

` sudo apt update`

` sudo apt install jenkins -y`

> By default jenkins will run on port 8080 of your system. This can be customized later by changing the jenkins configuration. 

* Start Jenkins

` systemctl start jenkins`

` systemctl enable jenkins`

* Verify Jenkins is running 

` ps -ef | grep -i jenkins` 

` netstat -anp | grep 8080` 


> You can now access jenkins by logging in to your VM external IP address at port 8080. On your Browser - **http://EXTERNAL_IP:8080**

***

## Install APACHE2 as Reverse PROXY (OPTIONAL STEP)

> In **Production** systems you will not expose any external port (8080) and will need access only via port 80 or 443. To achieve this we will now install a webserver and perform a reverse proxy from the webserver port 80 to Jenkins port 8080


* Install Apache2 

` sudo apt install apache2 -y`

* Enable Apache proxy modules 

` a2enmod proxy`

` a2enmod proxy_http`


* Create a virtualhost configuration for jenkins master. This virtualhost configuration will contain details of the reverse proxy 

`cd /etc/apache2/sites-available/`

` vi jenkins.conf` 

> The below configuration contains the virtualhost configuration for jenkins along with the reverse proxy details. Please change the **ServerName** field and replace **EXTERNAL_IP_ADDRESS_OF_YOUR_VM** with the **EXTERNAL IP ADDRESS** of your own VM. 

~~~
<Virtualhost *:80>
    ServerName        EXTERNAL_IP_ADDRESS_OF_YOUR_VM
    ProxyRequests     Off
    ProxyPreserveHost On
    AllowEncodedSlashes NoDecode
 
    <Proxy http://localhost:8080/*>
      Order deny,allow
      Allow from all
    </Proxy>
 
    ProxyPass         /  http://localhost:8080/ nocanon
    ProxyPassReverse  /  http://localhost:8080/
    ProxyPassReverse  /  http://jenkins.hakase-labs.io/
</Virtualhost>
~~~

> Add the above entry to the jenkins.conf file and make the changes to **EXTERNAL_IP_ADDRESS_OF_YOUR_VM** field. Save the file 


* Activate the virtualhost configuration 

` a2ensite jenkins`

* Restart Apache and Jenkins 

` systemctl restart apache2`

`systemctl restart jenkins`

> You can now log in to jenkins by accessing only the IP address of your VM **without** the **8080** port. On your browser access **http://EXTERNAL_IP_OF_YOUR_VM**

***

## Configure Jenkins for the first time 

* Get the initial password for Jenkins 

` cat /var/lib/jenkins/secrets/initialAdminPassword`

> Copy and paste the password from this file to the Administrator Password field. Click continue. 


* Select Install selected Plugins and wait for plugins to get installed. 

* Create a username and password for your user. Click Save and Continue and then Save and Finish. You can now start using jenkins 


* **NOTE**

> In case your jenkins webpage is stuck at a blank screen after finishing configuration, go to your terminal and run `systemctl restart jenkins` and `systemctl restart apache2` to refresh configuration changes. This will fix the issue. 


***

## Install Jenkins Slave 

At this point we have created the Jenkins master. Its now time to create the slave node and add it to the jenkins master. The below steps will guide you to create a slave node using the SSH method. 

* Log in to the **master** node and create ssh keys for jenkins user. 

` sudo -iu jenkins` 

Execute : `ssh-keygen` to generate ssh keys

> Press enter for all configurations till you get the prompt back

` cd .ssh`

` cat id_rsa.pub >> authorized_keys` 

` chmod 600 *` 

` cat id_rsa` 

> Copy the private key

* Log in to **Jenkins Dashboard**

* Click **Credentials** on the left panel

* Click **Global** link

* Click **Add Credentials** in the dropdown

* Select **kind** as **SSH Username with private key**

* Retain **Scope** as **Global(Jenkins, nodes, items, all child items, etc)

* On **Private Key** field, select **Enter Directly** and click **Add**

* Paste the content of id_rsa to in the textbox

* Click **OK**

> We have now setup SSH private key successfully on the jenkins dashboard. Now we will distribute our public key to the slave node and authorize the jenkins user on the slave node to connect to master

***

### Install and configure Jenkins Slave node

* Log in to the **master**

` cd .ssh` 

` cat id_rsa.pub` 

> Copy the output of id_rsa.pub for later use

* Log in to the **slave** node

* Log in as root

` sudo -i` 

* Install OS dependency of the software

```
sudo apt-get update
sudo apt install software-properties-common apt-transport-https -y
sudo add-apt-repository ppa:openjdk-r/ppa -y
```

* Install java 8 

` sudo apt-get update`

` sudo apt install openjdk-8-jdk -y`

` java -version` 

* Create a jenkins user and copy the public key from **master**

` useradd -m -s /bin/bash jenkins`

` sudo -iu jenkins` 

` mkdir .ssh`

` chmod 700 .ssh` 

` cd .ssh` 

` vi authorized_keys` 

> Copy the content of id_rsa.pub from master to this file and save 

` chmod 600 *` 

* Log in to **jenkins** user in **master** and run **ssh** to **jenkins@slave**

* Log in to **master**

` sudo -i jenkins`

` ssh jenkins@slave hostname` 

> For the first prompt type : **yes** and you show get the hostname of the slave node. 

> Now you have successfully set up ssh between master and slave 


* Log in to **Jenkins DashBoard** to setup slave 

* On the Left Panel click **Manage Jenkins**

* Click **Manage Nodes**

* On the left Panel select **New Node**

* On the Node Name field provide the name **slave1**

* Click on **Permanent Agent**

* Click **OK**

> This will navigate you to the details page for the new node 

* In the **remote root directory** give the **HOME PATH OF JENKINS USER** on the **slave** node. 

* To get the home path of **jenkins** user - log in to slave node 

` sudo -iu jenkins`

` pwd`

You will get an output as below 

```
jenkins@slave:~$ pwd
/home/jenkins
```

* Enter the above output `/home/jenkins` in the **remote root directory** field. 

* Enter the label as **slave1**

* In the **Launch Method** use **Launch Slave Agent via SSH**

* In the **Host** field provide the **INTERNAL_IP** of the slave node

* In **Credentials** select **jenkins**

* Keep all other configurations as default 

* Click **Save** and wait for master to initialize the slave. This might take a minute or two. 





























