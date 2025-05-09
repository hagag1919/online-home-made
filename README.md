---

# 📦 Orders Microservice

This is the **Orders Microservice**, a component of a larger microservices-based application. It is developed using Jakarta EE and deployed on the WildFly application server.([JBoss Documentation][1])

---

## 🚀 Prerequisites

Ensure the following are installed and configured on your system:

* **Java Development Kit (JDK)**: Version 11 or higher.
* **Apache Maven**: For building and managing the project.
* **WildFly Application Server**: Version 26 or higher.
* **Visual Studio Code (VS Code)**: With Java extensions installed.

---

## 🛠️ Project Setup

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/yourusername/orders-service.git
   cd orders-service
   ```



2. **Build the Project**:

   Use Maven to compile and package the application:

   ```bash
   mvn clean package
   ```



---

## 🧪 Running the Application

### 1. **Start WildFly Server**

* Navigate to the WildFly `bin` directory:

```bash
  cd $WILDFLY_HOME/bin
```



* Start the server:([WildFly Documentation][2])

```bash
  ./standalone.sh
```



For Windows:

```bash
  standalone.bat
```



### 2. **Deploy the Application**

* Deploy the packaged WAR file to WildFly using Maven:([Camunda Forum][3])

```bash
  mvn wildfly:deploy
```



Ensure the `wildfly-maven-plugin` is configured in your `pom.xml`:

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>org.wildfly.plugins</groupId>
        <artifactId>wildfly-maven-plugin</artifactId>
        <version>5.0.1.Final</version>
      </plugin>
    </plugins>
  </build>
```



### 3. **Access the Application**

* Open a web browser and navigate to:

```
  http://localhost:8080/orders-service
```



Replace `orders-service` with your actual context path if different.

---

## 🔄 Redeploying After Code Changes

After making changes to the codebase, follow these steps to redeploy the application:

1. **Rebuild the Application**:

   ```bash
   mvn clean package
   ```



2. **Redeploy to WildFly**:

   ```bash
   mvn wildfly:redeploy
   ```



This command will redeploy the updated WAR file to the running WildFly server.&#x20;

Alternatively, you can use the WildFly CLI to redeploy:

```bash
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="deployment redeploy orders-service.war"
```



For Windows:

```bash
%WILDFLY_HOME%\bin\jboss-cli.bat --connect --command="deployment redeploy orders-service.war"
```



Replace `orders-service.war` with the actual name of your WAR file.&#x20;

---

## 🧰 Additional Tips

* **Hot Deployment**: For development purposes, consider using the `wildfly:dev` goal, which monitors for code changes and redeploys automatically:

```bash
  mvn wildfly:dev
```



This can streamline the development process by reducing manual redeployment steps.&#x20;

* **Undeploying the Application**: To remove the application from WildFly:([JBoss Developer][4])

```bash
  mvn wildfly:undeploy
```



---

