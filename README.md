## **What is Spring-JDBC-ROMA?**

**Spring-JDBC-ROMA** is a rowmapper extension for **Spring-JDBC module**. There is already a rowmapper named **"org.springframework.jdbc.core.BeanPropertyRowMapper"** for binding resultset attributes to object. But it is reflection based and can cause performance problems as Spring developers said. However **Spring-JDBC-ROMA** is not reflection based and it is byte code generation (with **CGLib** and **Javassist**) based rowmapper. It generates rowmapper on the fly like implementing as manual so it has no performance overhead. It also supports object relations as lazy and eager. There are other lots of interesting features and these features can be customized with developer's extended classes. 

## **What features does Spring-JDBC-ROMA have?**

* All primitive types, enums, dates, collections, blob, clob and complex objects are supported.  

* Lazy or eager field accessing is supported.   

* Writing your custom field based **RowMapperFieldGenerator** implementations is supported.   

* Writing your custom class (or type) based **RowMapperFieldGeneratorFactory** implementations is supported. 
  
* Writing field access definitions as compilable Java code in annotation or configuration file (XML file, properties file, ...) is supported.    


## **Install**

In your **pom.xml**, you must add repository and dependency for Spring-JDBC-ROMA. 
You can change **spring.jdbc.roma.version** to any existing **spring-jdbc-roma** library version.

~~~~~ xml
...
<properties>
    ...
    <spring.jdbc.roma.version>1.0.0-RELEASE</spring.jdbc.roma.version>
    ...
</properties>
...
<dependencies>
    ...
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-jdbc-roma</artifactId>
		<version>${spring.jdbc.roma.version}</version>
	</dependency>
	...
</dependencies>
...
<repositories>
	...
	<repository>
		<id>serkanozal-maven-repository</id>
		<url>https://github.com/serkan-ozal/maven-repository/raw/master/</url>
	</repository>
	...
</repositories>
...
~~~~~

And finally, in your **Spring context xml** file add following configuration to make your Spring context automatically aware of Spring-JDBC-ROMA.

~~~~~ xml
...
<import resource="classpath*:roma-context.xml"/>
...
~~~~~

## **Spring-JDBC-ROMA with a simple example**
  
Here is **User** class:  

~~~~~ java
public class User {

    @RowMapperField(columnName = "id")
    private Long id;
    @RowMapperField(columnName = "username")
    private String username;
    @RowMapperField(columnName = "password")
    private String password;
    @RowMapperField(columnName = "firstname")
    private String firstname;
    @RowMapperField(columnName = "lastname")
    private String lastname;
    @RowMapperField(columnName = "enabled")
    private boolean enabled = true;
    @RowMapperField(columnName = "gender")
    private Gender gender;
    @RowMapperObjectField(
        provideViaSpringProvider = 
            @RowMapperSpringProvider(
                provideCode = "@{roleDAO}.getUserRoleList(${id})"),
        lazy = true)
    private List<Role> roles = new ArrayList<Role>();
    
    ...
    
} 
~~~~~

Here is **Role** class:    

~~~~~ java
public class Role {

    @RowMapperField(columnName = "id")
    private Long id;
    @RowMapperField(columnName = "name")
    private String name;
    @RowMapperObjectField(
        provideViaSpringProvider = 
            @RowMapperSpringProvider(
                provideCode = "@{permissionDAO}.getRolePermissionList(${id})"),
        lazy = true)
    private List<Permission> permissions = new ArrayList<Permission>();

    ...
    
}
~~~~~
	
You can get **User** entity rowmapper as follows:

~~~~~ java
@Autowired
RowMapperService rowMapperService;
    
...

RowMapper<User> userRowMapper = rowMapperService.getRowMapper(User.class);
~~~~~

In this example, we can get related **Role** entites of **User** entity with **@RowMapperObjectField** annotion automatically. We use **RowMapperObjectField** for accessing related **Role** entites of **User** entity with **id** attribute of User. We have **"lazy=true"** configuration, since **roles** field are initialized while we are accessing it first time. If we don't access it, it will not be set. 

In addition, we can define **User** entity with compilable pure Java code as follows:

 
~~~~~ java 
public class User {

    ...
    
    @RowMapperObjectField(
        provideViaImplementationProvider = 
            @RowMapperImplementationProvider(
                provideCode = "RoleDAO.getUserRoleListAsStaticMethod(${id})", 
                usedClasses = {RoleDAO.class}), 
        lazy = true)
    private List<Role> roles = new ArrayList<Role>();
    
    ...
    
} 
~~~~~


Also, we can user our custom data provider classes these implement **"RowMapperObjectFieldDataProvider"** interface with its **"public Object provideData(T ownerObj)"** method. Here is sample usage:

 
~~~~~ java   
public class User {

    ...
    
    @RowMapperObjectField(
        provideViaCustomProvider = 
            @RowMapperCustomProvider(
                dataProvider = MyCustomDataProvider.class), 
        lazy = true)
    private List<Role> roles = new ArrayList<Role>();
    
    ...
    
} 
~~~~~

You can find all demo codes (including these samples above) at [https://github.com/serkan-ozal/spring-jdbc-roma-demo](https://github.com/serkan-ozal/spring-jdbc-roma-demo)
 
