# Wildfly

## Deployment and module adding in WildFly

1- Add

```xml
<global-modules>
<module name="arrow" />
</global-modules>
```

To `standalone.xml`

Inside the tag `<subsystem xmlns="urn:jboss:domain:ee:4.0">`

2- Create the following path & file, you can change arrow folder name if you want

`wildfly-15.0.1.Final\modules\arrow\main\module.xml`

add the following to created file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ This is NOT free software;
  ~
  ~ This software is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ Lesser General Public License for more details.
  -->
<module name="arrow" xmlns="urn:jboss:module:1.5">

    <resources>
        <resource-root path="."/>
    </resources>
</module>
```

## Add user / Connect using CLI

Use `add-user.bat` then `jboss-cli.bat --connect`

## Global directory

Simply make folder in wildfly folder or anywhere and connect using CLI then apply

`/subsystem=ee/global-directory=common-libs:add(path=ex, relative-to=jboss.home.dir)`

while `ex` is the created folder and since it's inside wildfly folder then it's relative to `jboss.home.dir`. `common-libs` is then name of the module/global directory to be added in standalone.xml

## Deployment logging

To use each deployment logging API then add the following

```xml
<add-logging-api-dependencies value="false" />
<use-deployment-logging-config value="true" />
```

## Config files in wildfly-15.0.1.Final\modules\arrow\main

<https://docs.jboss.org/author/display/WFLY/Class+Loading+in+WildFly#ClassLoadinginWildFly-GlobalModules>
