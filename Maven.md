# Examples

`mvn help:describe -Dplugin=archetype` - To get help about a plugin which is `archetype` in this case

`mvn help:effective-pom -Doutput=effective-pom.xml` - Show the merge of real final effective POM for your project. Look at the default configurations of the super POM. You will find default phase and plugin bindings. `output` option to write the output in file instead of the console.

`mvn install` - Here `install` is a phase in a life cycle which will invoke several plugins

`mvn dependency:tree -Dincludes=antlr` - Search for dependency using dependency plugin

`mvn dependency:resolve` - Download dependency. Use -U for forcing the update

`mvn clean install` - Resolve and organize updating references

`mvn dependency:sources` - Download sources jars javadoc

`mvn dependency:resolve -Dclassifier=javadoc` - Download javadoc for all dependencies

`mvn dependency:resolve` - If you want to only download dependencies without doing anything else

`mvn dependency:get -Dartifact=groupId:artifactId:version` - to download a single dependency

`mvn package --projects schedule-job -am` - Package sub-module and make other modules as well. To exclude projects use `--projects !schedule-job,!beans`

`mvn compile dependency:tree` - Instead of looking at local repository for submodule, update  the current one

`mvn archetype:generate -Dfilter=maven-archetype=quickstart` - Use [archetype](https://maven.apache.org/archetype/maven-archetype-plugin/) plugin to create new project filtering specific archetype

`mvn archetype:generate -DgroupId=com.example -DartifactId=sampleApp -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false` - Notice the parameter [`-DarchetypeArtifactId=maven-archetype-quickstart`](https://maven.apache.org/archetypes/maven-archetype-quickstart/dependency-info.html) which identifies already existing archetype so we create a sample built on it.

## Repository

- search.maven.org
- Local repository in ~/.m2

## Archiva

Archiva could be used for deployment directly in the folder and scan the jars with its POM. This also could be done from command line like this

```powershell
mvn org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1:deploy-file -Durl=http://10.0.10.107:8088/repository/arrow/ -DrepositoryId=arrow -Dfile=dfc.jar -Dfiles=dfc.jar -Dclassifiers=debug -Dtypes=jar -DgroupId=com.documentum -DartifactId=dfc -Dversion=16.4.0000.0185 -Dpackaging=jar
```

### References

[Plugins](https://maven.apache.org/plugins/index.html)  
[Lifecycles Reference](https://maven.apache.org/maven-core/lifecycles.html) & [Introduction to Lifecycle Reference](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference)  
[POM.xml Reference](https://maven.apache.org/pom.html)  
[Settings Reference](https://maven.apache.org/settings.html)  
[Configure Plugins](https://maven.apache.org/guides/mini/guide-configuring-plugins.html)  
[Dependency Management](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)  
[Full Guides & References Links](https://maven.apache.org/guides/index.html)
