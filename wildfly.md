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

## Config files in wildfly-15.0.1.Final\modules\arrow\main

<https://docs.jboss.org/author/display/WFLY/Class+Loading+in+WildFly#ClassLoadinginWildFly-GlobalModules>
